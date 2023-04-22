package vbn;

import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.CustomOperand;
import vbn.state.constraints.IConstraint;
import vbn.state.constraints.IOperand;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.ComputeValueType;
import vbn.state.value.*;

import java.util.Stack;

import static vbn.solver.VBNRunner.insertStateIntoIO;
import static vbn.state.helpers.ComputeOperand.*;

/**
 *
 */
public class Call {

    static ComputeConstraints computeConstraints;

    static boolean TESTING_MODE = false;
    static LatestState latestState;

    static final String finalizeIndentStr = "\t";
    static final String pushIndentStr = "\t\t";

    /**
     * When the program begins
     */
    @SuppressWarnings("unused")
    public static void init() {
        // Create new to avoid carried over state
        latestState = new LatestState(new GlobalState());
        computeConstraints = new ComputeConstraints();

        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    @SuppressWarnings("unused")
    public static void pushSym(@NonNull String varName, Object value) {
        ISymbol result = latestState.getLatestSymbolAndCreateIfDoesntExist(varName, value);

        computeConstraints.pushSymbol(result);

        if (TESTING_MODE) {
            System.out.println(finalizeIndentStr + "pushSym " + latestState.getSymbolName(varName));
            System.out.print(pushIndentStr);
            System.out.println(result);
        }
    }

    /**
     *
     * @param value the concrete value to store
     */
    @SuppressWarnings("unused")
    public static void pushConstant(Object value) {
        var type = ComputeValueType.getType(value);
        IConstant constant;

        switch (type) {
            case INT_TYPE:
                constant = new IntConstant((int) value);
                break;
            case REAL_TYPE:
                constant = new RealConstant((double) value);
                break;
            case BOOL_TYPE:
                constant = new BooleanConstant((boolean) value);
                break;
            case UNKNOWN:
            default:
                throw new VBNLibraryRuntimeException("A type was not handled");
        }

        computeConstraints.pushConstant(constant);

        if (TESTING_MODE) {
            System.out.println(finalizeIndentStr + "pushConstant ");
            System.out.print(pushIndentStr);
            System.out.println(constant);
        }
    }

    /**
     * Applies an Operand expressed as a string
     * @param op the untrimmed string
     */
    @SuppressWarnings("unused")
    public static void apply(@NonNull String op){
        var opTrimmed = op.trim();

        var binOp = getBinaryOperand(opTrimmed);
        if (binOp != null) {
            applyOperand(binOp);
            return;
        }

        var unaOp = getUnaryOperand(opTrimmed);
        if (unaOp != null) {
            applyOperand(unaOp);
            return;
        }

        throw new VBNLibraryRuntimeException("Tried processing an op that does not exist: '" + opTrimmed + "'");
    }

    /**
     * Select the operand used for computation
     * @param operand the operand (e.g. + or -) to be applied to the symbols
     */
    private static void applyOperand(IOperand operand) {
        computeConstraints.setOperand(operand);
    }


    /**
     * To cast an object to a type
     * @param typeToCast the type to cast the symbol into
     */
    @SuppressWarnings("unused")
    public static void applyCast(String typeToCast) {
        // TODO: Do I need to do something with `typeToCast`
        applyOperand(CustomOperand.CAST);
    }

    /**
     * Set the operand to "reassign"
     * Handle $r1 = $r2
     */
    public static void applyReassignment() {
        applyOperand(CustomOperand.REASSIGN);
    }
    /**
     * Store the result of this operand in the constraints
     * We decided not to update the concreteValue of varName, using a strategy of "Update on Use"
     * @param varName the name of the symbol to store the expression
     */
    @SuppressWarnings("unused")
    public static void finalizeStore(String varName, Object concreteValue, int lineNumber) {
        @NonNull ISymbol assignmentSymIfExists;

        // FIXME: This should be fixed in instrumentation
        if (computeConstraints.isCasting()) {
            return;
        }
        // Note: this should ideally be in the instrumented code
        if (computeConstraints.isReassignment()) {
            applyReassignment();
        }
//        else {
//            assignmentSymIfExists = latestState.getLatestSymbol(varName);
//            if (TESTING_MODE) {
//                System.out.println(finalizeIndentStr + " getLatestSymbol=" + assignmentSymIfExists);
//            }
//        }

        latestState.generateNewSymbolForVariable(varName);

        assignmentSymIfExists = latestState.getLatestSymbolAndCreateIfDoesntExist(varName, concreteValue);
        if (TESTING_MODE) {
            System.out.println(finalizeIndentStr + " getLatestSymbolAndCreateIfDoesntExist=" + assignmentSymIfExists);
        }

        var constraint = computeConstraints.generateAssignmentFromPushes(lineNumber, assignmentSymIfExists);
        latestState.pushConstraint(constraint);

        if (TESTING_MODE) {
            System.out.println("Finalize Store of: " + latestState.getSymbolName(varName));
            System.out.print(finalizeIndentStr);
            System.out.println(constraint);
            System.out.println();
        }
    }

    /**
     * Store the result of this operand in the constraints
     */
    @SuppressWarnings("unused")
    public static void finalizeIf(int lineNumber) {
        var constraint = computeConstraints.generateBranchFromPushes(lineNumber);
        latestState.pushConstraint(constraint);

        if (TESTING_MODE) {
            System.out.println("Finalize If");
            System.out.print("\t");
            System.out.println(constraint);
        }
    }

    /**
     * Run when the if condition evaluated to TRUE
     * @param lineNumber the line number of the if condition
     */
    @SuppressWarnings("unused")
    public static void pushTrueBranch(int lineNumber) {
        computeConstraints.setEvaluatedToTrue();
    }

    /**
     * Run when the if condition evaluated to FALSE
     * @param lineNumber the line number of the if condition
     */
    @SuppressWarnings("unused")
    public static void pushFalseBranch(int lineNumber) {
        computeConstraints.setEvaluatedToFalse();
    }

    @SuppressWarnings("unused")
    public static void finalizeReturn(String symbol, Object value, int lineNumber) {}

    /**
     * When the DFS search hits an error, return, etc.
     */
    @SuppressWarnings("unused")
    public static void terminatePath(int lineNumber) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);

        onAllTerminates();
    }

    /**
     * Trigger before involving a function
     */
    @SuppressWarnings("unused")
    public static void beforeInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger after involving a function
     */
    @SuppressWarnings("unused")
    public static void afterInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    @SuppressWarnings("unused")
    public static void pushArg(String symbol, Object value) {

    }

    @SuppressWarnings("unused")
    public static void pushArgConst(Object value) {

    }

    @SuppressWarnings("unused")
    public static void popArg(String symbol, Object value) {

    }

    /**
     * After the program is completed
     */
    @SuppressWarnings("unused")
    public static void terminatedWithError(Throwable theError) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);

        latestState.setError(theError);

        if (latestState.hasVBNError()) {
            System.out.println("VBN's Runtime Library for the instrumentation failed with an error:");
            throw (RuntimeException) theError;
        }
        else {
            System.out.println("System failed with an error:");
        }
        System.out.println("----------------------------------------------------------------------");
        theError.printStackTrace();
        System.out.println("----------------------------------------------------------------------");

        // Give VBN.run() the state object in order to know the constraints and values
        onAllTerminates();
    }

    /**
     * To run on all terminate functions
     */
    private static void onAllTerminates() {
        if (!TESTING_MODE) {
            pushStateToIO();
        }

        TESTING_MODE = false;
    }

    /**
     * Store the state in an external datastore, so it can be accessed by VBN
     */
    private static void pushStateToIO() {
        insertStateIntoIO(latestState.getSerializeState());
    }

    public static void initTestingMode() {
        TESTING_MODE = true;
    }

    public static boolean constraintsEqual(Stack<IConstraint> constraints) {
        if (!TESTING_MODE) {
            throw new VBNLibraryRuntimeException("Only use this function for testing");
        }

        return latestState.getConstraints() == constraints;
    }
}

