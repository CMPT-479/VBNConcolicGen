package vbn.examples;

public class Test_08_Function {
    public static int f(int x, int y) {
        return x + y;
    }

    public static void main(String[] args) {
        int a = f(2 ,3);
        int b = f(a, 4);
        int c = f(f(2, a), b);
        System.out.println(c);
    }
}
