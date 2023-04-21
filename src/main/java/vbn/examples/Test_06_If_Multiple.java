package vbn.examples;

public class Test_06_If_Multiple {
    static int x;
    static int y;
    static int z;
    static boolean t;
    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
        z = Integer.parseInt(args[2]);
        if (x > y) {
            System.out.println("Path 1");
            if (y > z) {
                System.out.println("Path 1.1");
            } else {
                System.out.println("Path 1.2");
            }
        } else {
            System.out.println("Path 2");
            if (x > z) {
                System.out.println("Path 2.1");
            } else {
                System.out.println("Path 2.2");
            }
        }
    }
}
