package vbn;

import vbn.constraints.Constraint;
import vbn.constraints.JimpleOperandI;
import vbn.constraints.State;

import java.util.Stack;

/**
 *
 */

class ComputeExpression {
    public Stack<String> symbols = new Stack<>();
    public Object operand = null;

    public void clear() {
        symbols.clear();
        operand = null;
    }
}

public class Call {

    static State globalState;

    static ComputeExpression tempComputeExpr;

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
        globalState = new State();
        tempComputeExpr = new ComputeExpression();

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
        tempComputeExpr.symbols.push(symName);
    }

    /**
     * Select the operand used for computation
     * @param operand the operand (e.g. + or -) to be applied to the symbols
     * @param <JEnum> the type of operand
     */
    public static <JEnum extends JimpleOperandI> void applyOperand(JEnum operand) {
        tempComputeExpr.operand = operand;
    }

    /**
     * Store the result of this operand in the constraints
     * @param symName the name of the symbol to store the expression
     */
    public static void storeSym(String symName) throws Exception {
        var numOfOps = tempComputeExpr.symbols.size();

        Constraint constraint;
        switch (numOfOps) {
            case 2:
//                constraint = new BooleanEx
                break;
            case 1:
                break;
            case 0:
                break;

            default:
                throw new Exception("Too many symbols have been pushed on to the stack.");
        }

//        globalState.pushConstraints(constraint);

//        globalState.pushConstraints();
        tempComputeExpr.clear();
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
        tempComputeExpr = null;
    }
}
