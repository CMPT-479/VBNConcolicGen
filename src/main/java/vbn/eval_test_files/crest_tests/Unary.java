package vbn.eval_test_files.crest_tests;

public class Unary {
    static int a;

    public static void main(String[] args) {
        a = Integer.parseInt(args[0]);
        int x = 2;
        int b;
        b = -a;
        if (-x * a == 8) {
            System.out.format("8 (%d %d)\n", a, b);
        } else {
            System.out.format("not 8 (%d %d)\n", a, b);
        }
        return;
    }
}
