package vbn;

import vbn.constraints.State;

/**
 *
 */

public class Call {
    /**
     * When the program begins
     *
     * @param globalState The global state passed around
     */
    public static void init(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Handle any assignment that we don't control the value of
     * e.g. user input, external functions, etc.
     *
     * @param globalState The global state passed around
     */
    public static void initNewInput(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger before involving a function
     *
     * @param globalState The global state passed around
     */
    public static void beforeInvokeFunc(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger after involving a function
     *
     * @param globalState The global state passed around
     */
    public static void afterInvokeFunc(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Handle a new assignment (may contain new symbols)
     *
     * @param globalState The global state passed around
     */
    public static void handleAssignment(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }


    /**
     * Handle new conditionals
     *
     * @param globalState The global state passed around
     */
    public static void handleConditional(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * When the DFS search hits an error, return, etc.
     *
     * @param globalState The global state passed around
     */
    public static void terminatePath(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * After the program is completed
     *
     * @param globalState The global state passed around
     */
    public static void error(State globalState) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }
}
