package vbn.constraints;

public final class ConstraintItemInt extends ConstraintItem {
    Symbol left;
    IntOperand op;
    Symbol right;

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
