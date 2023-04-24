package vbn.eval_test_files.vbn_custom_tests;

public class Test_09_If_Double {
    static double x;

    public static void main(String[] args) {
        x = Double.parseDouble(args[0]);
        if (x > 0.1) {
            System.out.println("Less than 0.1");
        } else {
            System.out.println("Greater than 0.1");
        }
    }
}
