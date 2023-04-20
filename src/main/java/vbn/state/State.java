package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.Symbol;
import vbn.state.value.Value;

import java.io.Serializable;
import java.util.*;


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

//class PushConstraintsVisitor {
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
public final class State implements Serializable {

    private static final long serialVersionUID = 1L;

    public State() {
        super();
    }

    private final Map<String, Symbol> symbols = new HashMap<>();

    private final Stack<Constraint> constraints = new Stack<>();

    /**
     * Push a general constraint
     * @param constraint the constraint to add the constraint stack
     */
    public void pushConstraint(Constraint constraint) {
        constraints.push(constraint);
    }

    /**
     * Push a Binary Assignment Constraint
     * @param left the name of the left symbol
     * @param operand the operand applied to both
     * @param right the name of the right symbol
     * @param assigned where to store the result
     */
    public void pushConstraint(String left, BinaryOperand operand, String right, String assigned) {
        var constraint = new BinaryConstraint(getSymbol(left), operand, getSymbol(right), getSymbol(assigned));
        pushConstraint(constraint);
    }

    /**
     * Push a Binary Assignment Constraint
     * @param left the name of the left symbol
     * @param operand the operand applied to both
     * @param right the name of the right symbol
     */
    public void pushConstraint(String left, BinaryOperand operand, String right) {
        var constraint = new BinaryConstraint(getSymbol(left), operand, getSymbol(right));
        pushConstraint(constraint);
    }

    /**
     * Push a Unary Assignment Constraint
     * @param symbol the name of the symbol to be operated on
     * @param operand the operand applied to both
     * @param assigned where to store the result
     */
    public void pushConstraint(UnaryOperand operand, String symbol, String assigned) {
        var constraint = new UnaryConstraint(operand, getSymbol(symbol), getSymbol(assigned));
        pushConstraint(constraint);
    }

    /**
     * Push a Unary Assignment Constraint
     * @param symbol the name of the symbol to be operated on
     * @param operand the operand applied to both
     */
    public void pushConstraint(UnaryOperand operand, String symbol) {
        var constraint = new UnaryConstraint(operand, getSymbol(symbol));
        pushConstraint(constraint);
    }

    /**
     * Add a general symbol
     * @param symbol the symbol object to add
     */
    public void addSymbol(Symbol symbol) {
        symbols.put(symbol.id, symbol);
    }

    /**
     * Add a symbol shortcut
     * @param symName the name of the symbol
     * @param valueType the Z3 type of the symbol
     * @param concreteValue the current value of the symbol
     */
    public void addSymbol(String symName, Value.Type valueType, Object concreteValue) {
        symbols.put(symName, new Symbol(symName, valueType, concreteValue));
    }

    /**
     * Getters
     * @return the constraint stack
     */
    public Stack<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * Get the symbol object given a name. The symbol MUST exist or error thrown.
     * @param symbolName The name of the symbol
     * @return the symbol object
      */
    public Symbol getSymbol(String symbolName) {
        @NonNull var result = symbols.get(symbolName);

        return result;
    }

    public ArrayList<Symbol> getSymbols() {
        return new ArrayList<>(symbols.values());
    }

}