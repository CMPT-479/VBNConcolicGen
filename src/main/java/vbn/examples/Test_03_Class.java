package vbn.examples;

public class Test_03_Class {
    public int x;
    public int y;

    public static void main(String[] args) {
        Test_03_Class object = new Test_03_Class();
        object.x = 2;
        object.y = 4;
        System.out.println(object.x + 2 * object.y);
    }
}
