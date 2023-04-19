package vbn.constraints;

public class BinaryExpr<OpEnum extends IBinaryOperand> extends Constraint {

    public Symbol assigned;
    public Symbol left;
    public OpEnum op;
    public Symbol right;

    public BinaryExpr(Symbol left, OpEnum op, Symbol right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public BinaryExpr(Symbol left, OpEnum op, Symbol right, Symbol assigned) {
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
