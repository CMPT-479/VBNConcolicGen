package vbn.eval_test_files.crest_tests;

public class Structure {
    public static class Banana {
        int weight;
        int price;
    }

    static int p, w;

    public static void main(String[] args) {
        w = Integer.parseInt(args[0]);
        p = Integer.parseInt(args[1]);
        Banana b = new Banana();
        b.price = p;
        b.weight = w;
        if (b.weight > 136 && b.price < 15) {
            System.out.println("Good");
        }
    }
}
