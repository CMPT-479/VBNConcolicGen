package vbn.crest_tests;

public class Simple {
    static int a;

    public static void main(String[] args) {
        a = Integer.parseInt(args[0]);
        int b;
        b = 3 * a + 2;
        if (b == 8) {
            System.out.println("8");
        } else {
            System.out.println("8\n");
        }
    }
}
