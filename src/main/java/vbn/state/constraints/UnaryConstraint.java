package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.VBNLibraryRuntimeException;
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

    public boolean evaluatedResult;

    private Integer lineNumber = null;

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
    public boolean hasLineNumber() {
        return lineNumber != null;
    }

    @Override
    public int getLineNumber() {
        if (lineNumber == null) {
            throw new VBNLibraryRuntimeException("The line number is not set. Check hasLineNumber before running");
        }
        return lineNumber;
    }

    @Override
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public void print() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String result;
        if (assigned == null) {
            result = "#" + lineNumber + " Binary Constraint{ " + op + " " + symbol + " }";
        } else {
            result = "#" + lineNumber + " Binary Constraint{ " + assigned + " = " + op + " " + symbol + " }";
        }
        return result;
    }

    @Override
    public boolean getOriginalEvaluation() {
        return evaluatedResult;
    }
}
