package vbn.crest_tests;

public class StructureTest {
    private static class Bar {
        int x;
        int y;
    }

    private static class Foo {
        int x;
        Bar bar;
        int y;
    }

    public static void main(String[] args) {
        Foo f1 = new Foo();
        Foo f2 = new Foo();
        Bar b = new Bar();
        f1.x = Integer.parseInt(args[0]);
        f1.bar = b;
        f1.y = Integer.parseInt(args[1]);
    }
}
