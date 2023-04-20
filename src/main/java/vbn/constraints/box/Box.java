package vbn.constraints.box;

public class Box<U> extends AbstractBox {
    public U value;

    Box(U value) {
        this.value = value;
    }
}
