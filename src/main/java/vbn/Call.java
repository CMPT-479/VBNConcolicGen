package vbn;

import vbn.constraints.*;
import vbn.constraints.helpers.ComputeConstraints;
import vbn.constraints.helpers.TooManyOperandsException;

/**
 *
 */

public class Call {

    static State globalState;

    static ComputeConstraints tempComputeConstraints;

    /**
     * Hello World just to test involving a function
     */
    public static void helloWorld() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * When the program begins
     */
    public static void init() {
        // Create new to avoid carried over state
        globalState = new State();
        tempComputeConstraints = new ComputeConstraints();

        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Handle any assignment that we don't control the value of
     * e.g. user input, external functions, etc.
     */
    public static void initNewInput() {
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
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    public static void pushSym(String symName) {
        try {
            tempComputeConstraints.pushSymbol(symName);
        } catch (TooManyOperandsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    public static void pushConstant(Object constant) {
        tempComputeConstraints.pushConstant(constant);
    }

    /**
     * Select the operand used for computation
     * @param operand the operand (e.g. + or -) to be applied to the symbols
     * @param <JEnum> the type of operand
     */
    public static <JEnum extends IOperand> void applyOperand(JEnum operand) {
        tempComputeConstraints.setOperand(operand);
    }

    /**
     * Store the result of this operand in the constraints
     * @param symName the name of the symbol to store the expression
     */
    public static void finalizeStore(String symName) {
        tempComputeConstraints.generateConstraint(globalState, symName);
    }

    /**
     * Store the result of this operand in the constraints
     */
    public static void finalizeIf() {
        tempComputeConstraints.generateConstraint(globalState);
    }

    /**
     * Handle new conditionals
     */
    public static void handleConditional() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * When the DFS search hits an error, return, etc.
     */
    public static void terminatePath() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * After the program is completed
     */
    public static void error() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Ensure that state does not carry over
     */
    public static void destroy() {
        globalState = null;
        tempComputeConstraints = null;
    }
}
