package vbn.examples;

public class Test_13_Function {

    public static int f(int x, int y) {
        if (x < y) {
            return x + y;
        }
        else {
            return x - y;
        }
    }
    static int x;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        int a = f(x ,3);        // 2^1
        int b = f(a, 4);        // 2^2
        int c = f(f(2, a), b);  // 2^4
        System.out.println(c);
    }
}
