package vbn.constraints;

public class BooleanConstant extends AbstractConstant {
    public boolean value;
    public BooleanConstant(String id, boolean value) {
        super(id, SymbolType.BOOL_TYPE);
        this.value = value;
    }
}
