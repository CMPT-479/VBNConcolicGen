package vbn.constraints;

public final class ConstraintItemInt extends ConstraintItem {
    public Symbol left;
    public IntOperand op;
    public Symbol right;

    public ConstraintItemInt(Symbol left, IntOperand op, Symbol right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

//    @Override
//    void push(PushConstaintsVisitor visitor) {
//        visitor.push(this);
//    }
}
