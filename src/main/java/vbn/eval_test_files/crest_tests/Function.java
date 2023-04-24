package vbn.eval_test_files.crest_tests;

public class Function {
    public static int f(int x) {
        return 2 * x;
    }

    static int x0;

    public static void main(String[] args) {
        x0 = Integer.parseInt(args[0]);
        if (f(x0) + 3 == 9) {
            System.out.println("OK");
        }
    }
}
