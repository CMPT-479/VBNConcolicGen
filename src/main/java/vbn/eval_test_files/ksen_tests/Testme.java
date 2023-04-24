package vbn.eval_test_files.ksen_tests;

public class Testme {

    static int x;
    static int y;

    public static void main(String[] args){
        x = Integer.parseInt(args[0]);
        y = Integer.parseInt(args[1]);
        int z = 2*y;
        if(z==x){
            if(x>y+10){
                return; // ERROR
            }
        }
    }
}
