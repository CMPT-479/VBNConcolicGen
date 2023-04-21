package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.value.ISymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Objects;

public class BinaryConstraint implements IConstraint {

    @Nullable
    public ISymbol assigned;

    @NonNull
    public Value left;

    @NonNull
    public BinaryOperand op;

    @NonNull
    public Value right;

    @NonNull
    public boolean evaluatedResult;

    private int lineNumber = -1;

    public BinaryConstraint(@NonNull Value left, @NonNull BinaryOperand op, @NonNull Value right, boolean evaluatedResult) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
        this.evaluatedResult = evaluatedResult;
    }

    public BinaryConstraint(@NonNull Value left, @NonNull BinaryOperand op, @NonNull Value right, boolean evaluatedResult, @Nullable ISymbol assigned) {
        this.assigned = assigned;
        this.left = left;
        this.op = op;
        this.right = right;
        this.evaluatedResult = evaluatedResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryConstraint)) {
            return false;
        }

        BinaryConstraint otherBinaryConstraint = (BinaryConstraint) obj;
        if (this.assigned == null || otherBinaryConstraint.assigned == null) {
            if (this.assigned != otherBinaryConstraint.assigned) {
                return false;
            }
            return Objects.equals(this.left, otherBinaryConstraint.left)
                    && Objects.equals(this.right, otherBinaryConstraint.right)
                    && this.op == otherBinaryConstraint.op;
        } else {
            return Objects.equals(this.left, otherBinaryConstraint.left)
                    && Objects.equals(this.assigned, otherBinaryConstraint.assigned)
                    && Objects.equals(this.right, otherBinaryConstraint.right)
                    && this.op == otherBinaryConstraint.op;
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
            System.out.println("Binary Constraint line " + lineNumber + " { " + left + " " + op + " " + right + " }");
        } else {
            System.out.println("Binary Constraint line " + lineNumber + " { " + assigned + " = " + left + " " + op + " " + right + " }");
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