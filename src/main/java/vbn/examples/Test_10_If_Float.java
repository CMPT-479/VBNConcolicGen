package vbn.examples;

public class Test_10_If_Float {
    static float x;

    public static void main(String[] args) {
        x = Float.parseFloat(args[0]);
        if (x > 0.1) {
            System.out.println("Less than 0.1");
        } else {
            System.out.println("Greater than 0.1");
        }
    }
}
