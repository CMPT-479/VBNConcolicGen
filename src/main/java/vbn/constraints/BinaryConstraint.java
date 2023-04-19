package vbn.constraints;

public class BinaryConstraint<Left, Right> extends Constraint {

    public Symbol assigned;
    public Left left;
    public BinaryOperand op;
    public Right right;

    public BinaryConstraint(Left left, BinaryOperand op, Right right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public BinaryConstraint(Left left, BinaryOperand op, Right right, Symbol assigned) {
        this.assigned = assigned;
        this.left = left;
        this.op = op;
        this.right = right;
    }

}