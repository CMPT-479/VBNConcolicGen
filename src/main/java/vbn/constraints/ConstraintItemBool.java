package vbn.constraints;

// TODO: Add unary operation handling (e.g. not x)
public final class ConstraintItemBool extends ConstraintItem {
    public Symbol left;
    public BoolBinaryCompare op;
    public Symbol right;

    public ConstraintItemBool(Symbol left, BoolBinaryCompare op, Symbol right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

//    @Override
//    void push(PushConstaintsVisitor visitor) {
//        visitor.push(this);
//    }
}
