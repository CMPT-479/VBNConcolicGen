package vbn.constraints;

import lombok.NonNull;
import vbn.constraints.Value.Symbol;

import javax.annotation.Nullable;
import java.util.Objects;

public class BinaryConstraint extends Constraint {

    @Nullable public Symbol assigned;
    @NonNull public Symbol left;
    @NonNull public BinaryOperand op;
    @NonNull public Symbol right;

    public BinaryConstraint(@NonNull Symbol left, @NonNull BinaryOperand op, @NonNull Symbol right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public BinaryConstraint(@NonNull Symbol left, @NonNull BinaryOperand op, @NonNull Symbol right, Symbol assigned) {
        this.assigned = assigned;
        this.left = left;
        this.op = op;
        this.right = right;
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
}