package vbn.constraints;

public class IntConstant extends AbstractConstant {
    public int value;
    public IntConstant(String id, int value) {
        super(id, SymbolType.INT_TYPE);
        this.value = value;
    }
}