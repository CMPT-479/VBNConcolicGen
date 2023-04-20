package vbn.constraints;

import lombok.NonNull;
import vbn.constraints.Value.Symbol;

import javax.annotation.Nullable;
import java.util.Objects;

public class UnaryConstraint extends Constraint {

    @Nullable public Symbol assigned;
    @NonNull public UnaryOperand op;
    @NonNull public Symbol symbol;

    public UnaryConstraint(@NonNull UnaryOperand op, @NonNull Symbol symbol) {
        this.assigned = null;
        this.symbol = symbol;
        this.op = op;
    }
    public UnaryConstraint(@NonNull UnaryOperand op, @NonNull Symbol symbol, Symbol assigned) {
        this.assigned = assigned;
        this.symbol = symbol;
        this.op = op;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnaryConstraint)) {
            return false;
        }

        UnaryConstraint otherUnaryConstraint = (UnaryConstraint) obj;
        if (this.assigned == null || otherUnaryConstraint.assigned == null) {
            if (this.assigned != otherUnaryConstraint.assigned) {
                return false;
            }
            return Objects.equals(this.symbol, otherUnaryConstraint.symbol)
                    && this.op == otherUnaryConstraint.op;
        } else {
            return Objects.equals(this.symbol, otherUnaryConstraint.symbol)
                    && Objects.equals(this.assigned, otherUnaryConstraint.assigned)
                    && this.op == otherUnaryConstraint.op;
        }
    }
}
