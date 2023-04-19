package vbn.constraints;

public class UnaryExpr<OpEnum extends IUnaryOperand> extends Constraint {

    public Symbol assigned;
    public Symbol left;
    public OpEnum op;
    public Symbol right;

    public UnaryExpr(Symbol left, OpEnum op, Symbol right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public UnaryExpr(Symbol left, OpEnum op, Symbol right, Symbol assigned) {
        this.assigned = assigned;
        this.left = left;
        this.op = op;
        this.right = right;
    }

}

/**
 * UnaryComp
 * BinaryComp
 * UnaryExpr
 * BinaryExpr
 */
