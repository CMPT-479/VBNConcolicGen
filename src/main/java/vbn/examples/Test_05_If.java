package vbn.examples;

public class Test_05_If {
    static int x;
    static int y;
    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
        if (x > y) {
            System.out.println("COOL");
        } else {
            System.out.println("NICE");
        }
    }
}
