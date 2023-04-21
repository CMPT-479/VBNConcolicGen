package vbn.examples;

public class Test_08_If_Value {
    static int x;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        if (x < 0) {
            System.out.println("Less than 0");
        } else {
            System.out.println("Greater than 0");
        }
    }
}
