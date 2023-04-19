package vbn.constraints;

public class UnaryConstraint extends Constraint {

    public Symbol assigned;
    public UnaryOperand op;
    public Symbol symbol;

    public UnaryConstraint(UnaryOperand op, Symbol symbol) {
        this.assigned = null;
        this.symbol = symbol;
        this.op = op;
    }
    public UnaryConstraint(UnaryOperand op, Symbol symbol, Symbol assigned) {
        this.assigned = assigned;
        this.symbol = symbol;
        this.op = op;
    }

}
