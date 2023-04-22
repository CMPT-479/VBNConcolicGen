package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.VBNLibraryRuntimeException;
import vbn.state.value.ISymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Objects;

public class BinaryConstraint implements IConstraint {

    @Nullable
    public ISymbol assignedSymbol;

    @NonNull
    public Value left;

    @NonNull
    public BinaryOperand op;

    @NonNull
    public Value right;

    public boolean evaluatedResult;

    private Integer lineNumber = null;

    private Boolean isBranch = false;

    public BinaryConstraint(
            @NonNull Value left,
            @NonNull BinaryOperand op,
            @NonNull Value right,
            boolean evaluatedResult,
            int lineNumber) {
        this.left = left;
        this.op = op;
        this.right = right;
        this.evaluatedResult = evaluatedResult;
        this.lineNumber = lineNumber;
        this.assignedSymbol = null;
    }

    /**
     * Used when validating values
     * @return true if the values are valid
     */
    public boolean valuesTypesAreValid() {
        return left.getType() == right.getType();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryConstraint)) {
            return false;
        }

        BinaryConstraint otherBinaryConstraint = (BinaryConstraint) obj;
        if (this.assignedSymbol == null || otherBinaryConstraint.assignedSymbol == null) {
            if (this.assignedSymbol != otherBinaryConstraint.assignedSymbol) {
                return false;
            }
            return Objects.equals(this.left, otherBinaryConstraint.left)
                    && Objects.equals(this.right, otherBinaryConstraint.right)
                    && this.op == otherBinaryConstraint.op;
        } else {
            return Objects.equals(this.left, otherBinaryConstraint.left)
                    && Objects.equals(this.assignedSymbol, otherBinaryConstraint.assignedSymbol)
                    && Objects.equals(this.right, otherBinaryConstraint.right)
                    && this.op == otherBinaryConstraint.op;
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
        if (lineNumber == null) {
            throw new VBNLibraryRuntimeException("The line number is not set. Check hasLineNumber before running");
        }
        return lineNumber;
    }

    @Override
    public String toString() {
        String result;
        if (assignedSymbol == null) {
            result = "#" + lineNumber + " Binary Constraint{ " + left + " " + op + " " + right + " }";
        } else {
            result = "#" + lineNumber + " Binary Constraint{ " + assignedSymbol + " = " + left + " " + op + " " + right + " }";
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