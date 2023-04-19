package vbn.constraints;

public class BinaryConstraint extends Constraint {

    public Symbol assigned;
    public Symbol left;
    public BinaryOperand op;
    public Symbol right;

    public BinaryConstraint(Symbol left, BinaryOperand op, Symbol right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public BinaryConstraint(Symbol left, BinaryOperand op, Symbol right, Symbol assigned) {
        this.assigned = assigned;
        this.left = left;
        this.op = op;
        this.right = right;
    }

}