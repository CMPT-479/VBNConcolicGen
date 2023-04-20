package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.Symbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
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

    private final Stack<AbstractConstraint> constraints = new Stack<>();

    /**
     * Push a general constraint
     * @param constraint the constraint to add the constraint stack
     */
    public void pushConstraint(AbstractConstraint constraint) {
        constraints.push(constraint);
    }

    /**
     * Add a general symbol
     * @param symbol the symbol object to add
     */
    public void addSymbol(Symbol symbol) {
        symbols.put(symbol.varName, symbol);
    }

    /**
     * Add a symbol shortcut
     *
     * @param symName       the name of the symbol
     * @param valueType     the Z3 type of the symbol
     * @param concreteValue the current value of the symbol
     * @return the symbol that was just created
     */
    public Symbol addSymbol(String symName, Value.Type valueType, Object concreteValue) {
        var newSymbol = new Symbol(symName, valueType, concreteValue);
        symbols.put(symName, newSymbol);
        return newSymbol;
    }

    /**
     * Getters
     * @return the constraint stack
     */
    @NonNull
    public Stack<AbstractConstraint> getConstraints() {
        return constraints;
    }

    /**
     * Get the symbol object given a name. The symbol MUST exist or error thrown.
     * @param symbolName The name of the symbol
     * @return the symbol object
      */
    public Symbol getSymbol(String symbolName) {
        @NonNull
        var result = symbols.get(symbolName);

        return result;
    }

    @Nullable
    public Symbol getSymbolCanBeNull(String symbolName) {
        return symbols.get(symbolName);
    }

    @NonNull
    public ArrayList<Symbol> getSymbols() {
        return new ArrayList<>(symbols.values());
    }

    /**
     * Update the concrete concreteValue of the symbol
     * @param symName The name of the symbol
     * @param concreteValue the value to update the concrete value to
     */
    public void updateSymbolConcreteValue(String symName, Object concreteValue) {
        getSymbol(symName).value = concreteValue;
    }

    public void initSymbol(String symName, Object value) {

    }
}