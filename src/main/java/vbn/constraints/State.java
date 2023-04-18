package vbn.constraints;

import java.io.Serializable;
import java.util.*;

class Symbol implements Serializable {
    String id;
};

abstract class ConstraintItem {
//    void push(PushConstaintsVisitor visitor);
};

enum BoolOperand {
    AND,
    OR,
    NOT,
    EQ,
};

final class ConstraintItemBool extends ConstraintItem {
    Symbol left;
    BoolOperand op;
    Optional<Symbol> right;

//    @Override
//    void push(PushConstaintsVisitor visitor) {
//        visitor.push(this);
//    }
};

enum IntOperand {
    LT,
    LE,
    EQ,
    GT,
    GE,
};

final class ConstraintItemInt extends ConstraintItem {
    Symbol left;
    IntOperand op;
    Symbol right;

//    @Override
//    void push(PushConstaintsVisitor visitor) {
//        visitor.push(this);
//    }
};

//class PushConstaintsVisitor {
//
//    public ConstraintItem newConstraintItem;
//
//    public ArrayList<Symbol> newSymbols = new ArrayList<>(2);
//
//    public void push(ConstraintItemBool constraint) {
//        newSymbols.clear();
//        newConstraintItem = constraint;
//        newSymbols.add(constraint.left, constraint.right);
//    }
//
//    public void push(ConstraintItemInt constraint) {
//        newSymbols.clear();
//        newConstraintItem = constraint;
//        newSymbols.add(constraint.left, constraint.right);
//    }
//}

/**
 * This class handles all state necessary to solve an equation at a certain point
 */
public class State {
    State() {
        super();
    }

    private Set<Symbol> symbols = new HashSet<>();

    private Stack<ConstraintItem> constraints = new Stack<>();

    public void pushConstaints(ConstraintItem constraint) {
        constraints.push(constraint);
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }

    public Stack<ConstraintItem> getConstraints() {
        return constraints;
    }

    public Set<Symbol> getSymbols() {
        return symbols;
    }
}