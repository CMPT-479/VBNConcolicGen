package vbn.eval_test_files.vbn_custom_tests;

public class Test_00_Basic {
    public static void main(String[] args) {
        int length = args.length;
        var x = Integer.parseInt(args[0]);
        var y = Integer.parseInt(args[1]);
        var z = 3;

        var p = (x + y) * z;

        var q = p;

        q = q + 1;

        System.out.println(q);
    }
}
