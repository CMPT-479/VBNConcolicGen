package vbn.examples;

import java.util.Map;

public class Test_00_Basic {
    public static void main(String[] args) {
        int[] x = {1, 2, 3, 4};

        System.out.println(x[1]);
        System.out.println(x[2]);

        var k = x.clone();

        k[2] = 89898;

        System.out.println(k[2]);


    }
}
