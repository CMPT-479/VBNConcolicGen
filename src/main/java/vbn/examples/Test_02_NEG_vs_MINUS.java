package vbn.examples;

public class Test_02_NEG_vs_MINUS {

    static int x;
    static int y;
    static boolean z;
    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
        z = Boolean.parseBoolean(args[2]);

        // Negation is handled through a if statement
        boolean not_z = !z;

        int i = z ? x - y : y - x;
        int k = not_z ? -x : -y;
        int j = i - k;

        System.out.println(j);
    }
}
