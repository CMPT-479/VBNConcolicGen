package vbn.crest_tests;

public class UniformTest {
    static int a, b, c, d;
    public static void main(String[] args) {
        a = Integer.parseInt(args[0]);
        b = Integer.parseInt(args[1]);
        c = Integer.parseInt(args[2]);
        d = Integer.parseInt(args[3]);

        if (a == 5) {
            if (b == 19) {
                if (c == 7) {
                    if (d == 4) {
                        System.err.println("GOAL!");
                    }
                }
            }
        }
    }
}
