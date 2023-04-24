package vbn.eval_test_files.vbn_custom_tests;

public class Test_02_NEG_vs_MINUS {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        boolean z = Boolean.parseBoolean(args[3]);

        // Negation is handled through a if statement
        boolean not_z = !z;

        int i = z ? x - y : y - x;
        int k = not_z ? -x : -y;
        int j = i - k;

        System.out.println(j);
    }
}
