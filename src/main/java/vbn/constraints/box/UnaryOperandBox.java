package vbn.constraints.box;

import vbn.constraints.BinaryOperand;
import vbn.constraints.UnaryOperand;

public class UnaryOperandBox extends Box<UnaryOperand> implements IOperandBox {
    public UnaryOperandBox(UnaryOperand value) {
        super(value);
    }
}
