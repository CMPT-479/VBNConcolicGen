package vbn.constraints;

import java.util.Optional;

// TODO: Add unary operation handling (e.g. not x)
public final class ConstraintItemBool extends ConstraintItem {
    Symbol left;
    BoolOperand op;
    Symbol right;

    public ConstraintItemBool(Symbol left, BoolOperand op, Symbol right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

//    @Override
//    void push(PushConstaintsVisitor visitor) {
//        visitor.push(this);
//    }
}
