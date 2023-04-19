package vbn.constraints;

public class BooleanConstant extends Symbol {
    public boolean value;
    public BooleanConstant(String id, boolean value) {
        super(id, SymbolType.BOOL_TYPE);
        this.value = value;
    }
}
