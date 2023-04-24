package vbn.ksen_tests;

public class InterfaceTest {
    static int x;

    public static void main(String[] args) {
        x = Integer.parseInt(args[0]);
        if(x==100){
            System.out.println("Error");
        }
    }
}
