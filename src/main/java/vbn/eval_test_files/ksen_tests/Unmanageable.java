package vbn.eval_test_files.ksen_tests;

public class Unmanageable {
    static int x;
    static int y;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
        int z = x*x*x + 3*x*x + 9;
        if (z == y) {
            System.out.println("Good branch");
        } else {
            System.out.println("Bad branch");
        }

    }
}
