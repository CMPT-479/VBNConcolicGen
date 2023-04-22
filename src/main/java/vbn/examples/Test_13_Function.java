package vbn.examples;

public class Test_13_Function {

    static int a;
    static int b;
    static int c;

    public static int f(int x, int y) {
        if (x < y) {
            return x + y;
        }
        else {
            return x - y;
        }
    }

    public static void main(String[] args) {
        a = f(2 ,3); // 2^1
        b = f(a, 4);    // 2^2
        c = f(f(2, a), b); // 2^4
        System.out.println(c);
    }
}
