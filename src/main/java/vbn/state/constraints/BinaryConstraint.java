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

    private final int lineNumber;

    private Boolean isBranch = null;

    private Integer constraintNumber = null;

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
    public @NonNull Boolean isBranch() {
        return isBranch;
    }

    @Override
    public void setIsBranch(boolean isBranch) {
        this.isBranch = isBranch;
    }

    @Override
    public long getUniqueId() {
        if (constraintNumber == null) {
            throw new VBNLibraryRuntimeException("The constraint number is not set. This is required to generate a unique id.");
        }

        long result = 0;
        result = result | ((long) lineNumber << 32);
        result = result | (long) constraintNumber;
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        if (isBranch()) {
            result += "Br#";
        }
        else {
            result += "id#";
        }

        result += constraintNumber + "/L#" + lineNumber;
        result += " BinConstr{ ";

        if (assignedSymbol == null) {
            result += "\t\t ";
        } else {
            result += assignedSymbol + " = ";
        }

        result += left + " " + op + " " + right;
        if (isBranch) {
            result += getOriginalEvaluation() ? ", IS_TRUE" : ", IS_FALSE";
        }
        result += " }";
        
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

    @Override
    public void setConstraintNumber(int constraintNumber) {
        this.constraintNumber = constraintNumber;
    }
}