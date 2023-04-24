package vbn.eval_test_files.crest_tests;

public class Math {
    static int a, b, c, d, e;

    public static void main(String[] args) {
        a = Integer.parseInt(args[0]);
        b = Integer.parseInt(args[1]);
        c = Integer.parseInt(args[2]);
        d = Integer.parseInt(args[3]);
        e = Integer.parseInt(args[4]);
        if (3*a + 3*(b - 5*c) + (b+c) - a == d - 17*e) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }
}
