package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.value.ISymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Objects;

public class UnaryConstraint implements IConstraint {
    @Nullable
    public ISymbol assignedSymbol;

    @NonNull
    public UnaryOperand op;

    @NonNull
    public Value symbol;

    public boolean evaluatedResult;

    private int lineNumber;
    private Boolean isBranch;

    public UnaryConstraint(
            @NonNull UnaryOperand op,
            @NonNull Value symbol,
            boolean evaluatedResult,
            int lineNumber) {
        this.symbol = symbol;
        this.op = op;
        this.evaluatedResult = evaluatedResult;
        this.lineNumber = lineNumber;
        this.assignedSymbol = null;
        this.isBranch = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnaryConstraint)) {
            return false;
        }

        UnaryConstraint otherUnaryConstraint = (UnaryConstraint) obj;
        if (this.assignedSymbol == null || otherUnaryConstraint.assignedSymbol == null) {
            if (this.assignedSymbol != otherUnaryConstraint.assignedSymbol) {
                return false;
            }
            return Objects.equals(this.symbol, otherUnaryConstraint.symbol)
                    && this.op == otherUnaryConstraint.op;
        } else {
            return Objects.equals(this.symbol, otherUnaryConstraint.symbol)
                    && Objects.equals(this.assignedSymbol, otherUnaryConstraint.assignedSymbol)
                    && this.op == otherUnaryConstraint.op;
        }
    }

    @Override
    public boolean isBranch() {
        return isBranch;
    }

    @Override
    public void setIsBranch(boolean isBranch) {
        this.isBranch = isBranch;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        String result;
        if (assignedSymbol == null) {
            result = "#" + lineNumber + " Binary Constraint{ " + op + " " + symbol + " }";
        } else {
            result = "#" + lineNumber + " Binary Constraint{ " + assignedSymbol + " = " + op + " " + symbol + " }";
        }
        return result;
    }

    @Override
    public boolean getOriginalEvaluation() {
        return evaluatedResult;
    }

    @Override
    public void setAssignmentSymbol(ISymbol assignedSymbol) {
        this.assignedSymbol = assignedSymbol;
    }
}
