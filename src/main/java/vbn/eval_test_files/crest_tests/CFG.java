package vbn.eval_test_files.crest_tests;

public class CFG {
    static void f(int x) {
        if (x > 13) {
            System.out.println("X > 13");
        }
    }

    static void g(int x) {
        h(x);
        if (x == 7) {
            System.out.println("X == 7");
        }
        i(x);
    }

    static void h(int x) {
        if (x == -4) {
            System.out.println("X == -4");
        }
    }

    static void i(int x) {
        if (x == 100) {
            System.out.println("X == 100");
        }
    }

    static int p;

    public static void main(String[] args) {
        p = Integer.parseInt(args[0]);
        if (p == 19) {
            System.out.println("Is 19");
        }

        f(p);
        g(p);

        if (p != -1) {
            System.out.println("Not -1");
        }

    }
}
