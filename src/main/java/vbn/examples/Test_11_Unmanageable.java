package vbn.examples;

public class Test_11_Unmanageable {
    static int x;
    static int y;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
//        int z = x*x*x + 3*x*x + 9;
        if (x*x*x + 3*x*x + 9 == y) {
            System.out.println("Good branch");
        } else {
            System.out.println("Bad branch");
        }

    }
}
