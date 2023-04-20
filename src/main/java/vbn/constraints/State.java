package vbn.constraints;

import vbn.constraints.Value.Symbol;
import vbn.constraints.Value.Value;

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
public class State {
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
     * @throws SymbolMissingException if left, right, or assigned is not valid
     */
    public void pushConstraint(String left, BinaryOperand operand, String right, String assigned) throws SymbolMissingException {
        var constraint = new BinaryConstraint(getSymbol(left), operand, getSymbol(right), getSymbol(assigned));
        pushConstraint(constraint);
    }

    /**
     * Push a Binary Assignment Constraint
     * @param left the name of the left symbol
     * @param operand the operand applied to both
     * @param right the name of the right symbol
     * @throws SymbolMissingException if left, right, or assigned is not valid
     */
    public void pushConstraint(String left, BinaryOperand operand, String right) throws SymbolMissingException {
        var constraint = new BinaryConstraint(getSymbol(left), operand, getSymbol(right));
        pushConstraint(constraint);
    }

    /**
     * Push a Unary Assignment Constraint
     * @param symbol the name of the symbol to be operated on
     * @param operand the operand applied to both
     * @param assigned where to store the result
     * @throws SymbolMissingException if symbol or assigned is not valid
     */
    public void pushConstraint(UnaryOperand operand, String symbol, String assigned) throws SymbolMissingException {
        var constraint = new UnaryConstraint(operand, getSymbol(symbol), getSymbol(assigned));
        pushConstraint(constraint);
    }

    /**
     * Push a Unary Assignment Constraint
     * @param symbol the name of the symbol to be operated on
     * @param operand the operand applied to both
     * @throws SymbolMissingException if symbol or assigned is not valid
     */
    public void pushConstraint(UnaryOperand operand, String symbol) throws SymbolMissingException {
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
     * Add a general symbol
     * @param stringSymbol the name of the symbol
     * @param valueType the Z3 type of the symbol
     */
    public void addSymbol(String stringSymbol, Value.ValueType valueType) {
        symbols.put(stringSymbol, new Symbol(stringSymbol, valueType));
    }

    /**
     * Getters
     * @return the constraint stack
     */
    public Stack<Constraint> getConstraints() {
        return constraints;
    }

//    public Map<String, Symbol> getSymbolsMap() {
//        return symbols;
//    }

    /**
     * Get the symbol object given a name. The symbol MUST exist or error thrown.
     * @param symbolName The name of the symbol
     * @return the symbol object
     * @throws SymbolMissingException If an error is missing
     */
    public Symbol getSymbol(String symbolName) throws SymbolMissingException {
        var result = symbols.get(symbolName);

        if (result == null) {
            throw new SymbolMissingException("Can not find symbol with the requested name");
        }

        return result;
    }

    public Collection<Symbol> getSymbols() {
        return symbols.values();
    }

}