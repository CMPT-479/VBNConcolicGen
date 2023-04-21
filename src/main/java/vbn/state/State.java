package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.*;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.*;

/**
 * This class handles all state necessary to solve an equation at a certain point
 */
public class State {
    public State() {
        super();
        symbols = new HashMap<>();
        constraints = new Stack<>();
    }

    public State(Map<String, AbstractSymbol> symbols, Stack<AbstractConstraint> constraints) {
        this.symbols = symbols;
        this.constraints = constraints;
    }

    private final Map<String, AbstractSymbol> symbols;

    private final Stack<AbstractConstraint> constraints;

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

//    public void initSymbol(String symName, Object value) {
//
//    }
}