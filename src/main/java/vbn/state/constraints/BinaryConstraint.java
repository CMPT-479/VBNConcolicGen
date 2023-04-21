package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Objects;

public class BinaryConstraint extends AbstractConstraint {

    @Nullable
    public AbstractSymbol assigned;

    @NonNull
    public Value left;

    @NonNull
    public BinaryOperand op;

    @NonNull
    public Value right;

    public BinaryConstraint(@NonNull Value left, @NonNull BinaryOperand op, @NonNull Value right) {
        this.assigned = null;
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public BinaryConstraint(@NonNull Value left, @NonNull BinaryOperand op, @NonNull Value right, @Nullable AbstractSymbol assigned) {
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