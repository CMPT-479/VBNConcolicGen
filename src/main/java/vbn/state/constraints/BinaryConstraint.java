package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.VBNLibraryRuntimeException;
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

    private Integer lineNumber = null;

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
            result = "#" + lineNumber + " Binary Constraint{ " + left + " " + op + " " + right + " }";
        } else {
            result = "#" + lineNumber + " Binary Constraint{ " + assigned + " = " + left + " " + op + " " + right + " }";
        }
        return result;
    }

    @Override
    public boolean getOriginalEvaluation() {
        return evaluatedResult;
    }
}