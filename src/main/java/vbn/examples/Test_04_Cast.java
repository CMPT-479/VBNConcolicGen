package vbn.examples;

public class Test_04_Cast {
    public static void main(String[] args) {
        int[] x = {1, 2, 3, 4};

        System.out.println(x[1]);
        System.out.println(x[2]);

        var k = x.clone();

        k[2] = 89898;

        System.out.println(k[2]);

    }
}
