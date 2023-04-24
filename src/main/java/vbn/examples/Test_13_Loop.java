package vbn.examples;

public class Test_13_Loop {
    static int x;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        int xPlus1 = x + 1;
        int sum = 0;

        for (int i = 0; i < xPlus1; i++) {
            sum = sum + i;
        }
//        for (int i = 0; i < 10; i++) {
//            x = x + 1;
//        }
        System.out.println(x);
    }
}
