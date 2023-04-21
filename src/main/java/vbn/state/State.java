package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.*;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;


//class PushConstaintsVisitor {
//
//    public ConstraintItem newConstraintItem;
//
//    public ArrayList<AbstractSymbol> newSymbols = new ArrayList<>(2);
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
//    public ArrayList<AbstractSymbol> newSymbols = new ArrayList<>(2);
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

    private final Map<String, AbstractSymbol> symbols = new HashMap<>();

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
    public void addSymbol(AbstractSymbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    /**
     * Add a symbol shortcut
     *
     * @param symName       the name of the symbol
     * @param valueType     the Z3 type of the symbol
     * @param concreteValue the current value of the symbol
     * @return the symbol that was just created
     */
    public AbstractSymbol addSymbol(String symName, Value.Type valueType, Object concreteValue) {
        AbstractSymbol newSymbol;

        switch (valueType) {
            case INT_TYPE:
                newSymbol = new IntSymbol(symName, (int) concreteValue);
                break;
            case REAL_TYPE:
                newSymbol = new RealSymbol(symName, (double) concreteValue);
                break;
            case BOOL_TYPE:
                newSymbol = new BooleanSymbol(symName, (boolean) concreteValue);
                break;
            case UNKNOWN:
                newSymbol = new UnknownSymbol(symName, concreteValue);
                break;
            default:
                throw new RuntimeException("A type was not handled");
        }

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
    public AbstractSymbol getSymbol(String symbolName) {
        @NonNull
        var result = symbols.get(symbolName);

        return result;
    }

    @Nullable
    public AbstractSymbol getSymbolCanBeNull(String symbolName) {
        return symbols.get(symbolName);
    }

    @NonNull
    public ArrayList<AbstractSymbol> getSymbols() {
        return new ArrayList<>(symbols.values());
    }

    /**
     * Update the concrete concreteValue of the symbol
     * @param symName The name of the symbol
     * @param concreteValue the value to update the concrete value to
     */
    public void updateSymbolConcreteValue(String symName, Object concreteValue) {
        var sym = getSymbol(symName);
        sym.setValue(concreteValue);
    }

    public void initSymbol(String symName, Object value) {

    }
}