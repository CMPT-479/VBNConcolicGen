package vbn;

import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.CustomOperand;
import vbn.state.constraints.IOperand;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.ComputeValueType;
import vbn.state.value.*;

import javax.annotation.Nullable;

import static vbn.state.helpers.ComputeOperand.*;

/**
 *
 */
public class Call {
    static State globalState;

    static ComputeConstraints computeConstraints;

    /**
     * When the program begins
     */
    @SuppressWarnings("unused")
    public static void init() {
        // Create new to avoid carried over state
        globalState = new State();
        computeConstraints = new ComputeConstraints();

        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    @SuppressWarnings("unused")
    public static void pushSym(@NonNull String symName, Object value) {
        Symbol result = updateSymbolValueAndInitializeIfNecessary(symName, value);

        computeConstraints.pushSymbol(result);
    }

    /**
     *
     * @param value the concrete value to store
     */
    @SuppressWarnings("unused")
    public static void pushConstant(Object value) {
        var type = ComputeValueType.getType(value);
        AbstractConstant constant;

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
                throw new RuntimeException("A type was not handled");
        }

        computeConstraints.pushConstant(constant);
    }

    /**
     * Applies an Operand expressed as a string
     * @param op the untrimmed string
     */
    @SuppressWarnings("unused")
    public static void apply(String op){
        var opTrimmed = op.trim();

        var binOp = getBinaryOperand(opTrimmed);
        if (binOp != null) {
            applyOperand(binOp);
        }

        var unaOp = getUnaryOperand(opTrimmed);
        if (unaOp != null) {
            applyOperand(unaOp);
        }

        throw new RuntimeException("Tried processing an op that does not exist");
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
     * We decided not to update the value of symName, using a strategy of "Update on Use"
     * @param symName the name of the symbol to store the expression
     */
    @SuppressWarnings("unused")
    public static void finalizeStore(String symName, Object value) {
        var symbol = updateSymbolValueAndInitializeIfNecessary(symName, value);

        // Note: this should ideally be in the instrumented code
        if (computeConstraints.isReassignment()) {
            applyReassignment();
        }

        var constraint = computeConstraints.generateFromPushes(globalState.getSymbol(symName));
        globalState.pushConstraint(constraint);
    }

    /**
     * Store the result of this operand in the constraints
     */
    @SuppressWarnings("unused")
    public static void finalizeIf(int lineNumber) {
        var constraint = computeConstraints.generateFromPushes();
    }

    /**
     * When the DFS search hits an error, return, etc.
     */
    @SuppressWarnings("unused")
    public static void terminatePath(int lineNumber) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger before involving a function
     */
    public static void beforeInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger after involving a function
     */
    public static void afterInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * After a return statement
     * @param lineNumber Jimple's line number
     */
    public static void finalizeReturn(int lineNumber) {

    }

    /**
     * After the program is completed
     */
    // FIXME: GitHub Issue #33
    public static void terminatedWithError() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }


    /**
     * Update the symbol value and initialize it if necessary
     * @param symName the name of the symbol (used as a unique id)
     * @param value the value of the symbol
     * @return the updated or newly create symbol
     */
    private static Symbol updateSymbolValueAndInitializeIfNecessary(@NonNull String symName, Object value) {
        @Nullable
        var result = globalState.getSymbolCanBeNull(symName);

        if (result == null) {
            Value.Type type = ComputeValueType.getType(value);
            result = globalState.addSymbol(symName, type, value);
        }
        else {
            globalState.updateSymbolConcreteValue(symName, value);
        }
        return result;
    }
}

