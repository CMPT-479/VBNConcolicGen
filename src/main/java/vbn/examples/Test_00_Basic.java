package vbn.examples;

public class Test_00_Basic {
    public static void f(Object o) {
        System.out.println(o);
    }

    public static void main(String[] args) {
        int length = args.length;
        var x = Integer.parseInt(args[0]);
        var y = Integer.parseInt(args[1]);
        var z = 3;

        var p = (x + y) * z;

        var q = p;

        q = q + 1;

        f(p);
        f(q);
    }
}
