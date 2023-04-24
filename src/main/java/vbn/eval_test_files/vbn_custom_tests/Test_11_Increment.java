package vbn.eval_test_files.vbn_custom_tests;

public class Test_11_Increment {
    static int x;
    static int y;

    public static void main(String[] args) {
        y = 5;
        x = Integer.parseInt(args[0]);
        x = x + 1;
        x = x + 2;
        x = x + 3;

        if (x < y) {
            System.out.println("x < y");
        }
        else {
            System.out.println("x >= y");
        }
    }
}
