package vbn.constraints;

public class BinaryConstant extends Symbol {
    public boolean value;
    public BinaryConstant(String id, boolean value) {
        super(id, SymbolType.BOOL_TYPE);
        this.value = value;
    }
}
