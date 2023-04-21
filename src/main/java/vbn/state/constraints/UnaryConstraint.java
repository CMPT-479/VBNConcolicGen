package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.value.ISymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Objects;

public class UnaryConstraint implements IConstraint {
    @Nullable
    public ISymbol assigned;

    @NonNull
    public UnaryOperand op;

    @NonNull
    public Value symbol;
    private int lineNumber = -1;

    public UnaryConstraint(@NonNull UnaryOperand op, @NonNull Value symbol, boolean evaluatedResult) {
        this.assigned = null;
        this.symbol = symbol;
        this.op = op;
    }
    public UnaryConstraint(@NonNull UnaryOperand op, @NonNull Value symbol, boolean evaluatedResult, @Nullable ISymbol assigned) {
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

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public void print() {
        if (assigned == null) {
            System.out.println("Unary Constraint line " + lineNumber + " { " + op + " " + symbol + " }");
        } else {
            System.out.println("Unary Constraint line " + lineNumber + " { " + assigned + " = " + op + " " + symbol + " }");
        }
    }

    @Override
    public boolean getOriginalEvaluation() {
        return false;
    }

    @Override
    public boolean hasLineNumber() {
        return lineNumber != -1;
    }
}
