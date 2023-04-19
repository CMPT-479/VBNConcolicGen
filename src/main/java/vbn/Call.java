package vbn;

import vbn.constraints.State;
import vbn.instrument.JimpleOperand;

import java.util.Stack;

/**
 *
 */

public class Call {

    static State globalState = new State();

    static Stack<String> tempSymbolsToCompute;
    static JimpleOperand tempApplyOperand;

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
     * Handle a new assignment (may contain new symbols)
     */
    public static void pushSym(String symName) {
        tempSymbolsToCompute.push(symName);
    }

    public static void applyOperand(JimpleOperand operand) {
        tempApplyOperand = operand;
    }

    public static void storeSym(String symName) {

//        globalState.pushConstraints();
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
}
