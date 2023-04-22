package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * This class handles all state necessary to solve an equation at a certain point
 */
public class State implements Serializable {
    private Throwable error;

    public State() {
        super();
        symbols = new HashMap<>();
        constraints = new Stack<>();
    }

    public State(Map<String, ISymbol> symbols, Stack<IConstraint> constraints) {
        this.symbols = symbols;
        this.constraints = constraints;
    }

    private final Map<String, ISymbol> symbols;

    private final Stack<IConstraint> constraints;

    /**
     * Push a general constraint
     * @param constraint the constraint to add the constraint stack
     */
    public void pushConstraint(IConstraint constraint) {
        constraints.push(constraint);
    }

    /**
     * Add a general symbol
     * @param symbol the symbol object to add
     */
    public void addSymbol(ISymbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    /**
     * Getters
     * @return the constraint stack
     */
    @NonNull
    public Stack<IConstraint> getConstraints() {
        return constraints;
    }

    /**
     * Get the symbol object given a name. The symbol MUST exist or error thrown.
     * @param symbolName The name of the symbol
     * @return the symbol object
      */
    public ISymbol getSymbol(String symbolName) {
        @NonNull
        var result = symbols.get(symbolName);

        return result;
    }

    @Nullable
    public ISymbol getSymbolCanBeNull(String symbolName) {
        return symbols.get(symbolName);
    }

    @NonNull
    public ArrayList<ISymbol> getSymbols() {
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

    /**
     * Creates a Serializable copy of the state, removing the Unknown Symbols
     * @return the state is Serializable
     */
    public State getSerializeState() {
        Stack<IConstraint> finalConstraints = new Stack<>();
        Map<String, ISymbol> finalSymbols = new HashMap<>();

        this.symbols.forEach((key, val) -> {
            if (!(val instanceof UnknownSymbol)) {
                finalSymbols.put(key, val);
            }
        });

        for (IConstraint constraint : this.constraints) {
            if (constraint instanceof BinaryConstraint) {
                BinaryConstraint binConst = (BinaryConstraint) constraint;
                if (!(binConst.left instanceof UnknownSymbol) && !(binConst.right instanceof UnknownSymbol)) {
                    finalConstraints.push(binConst);
                }
            }
            else if (constraint instanceof UnaryConstraint) {
                UnaryConstraint unaryConstraint = (UnaryConstraint) constraint;
                if (!(unaryConstraint.symbol instanceof UnknownSymbol)) {
                    finalConstraints.push(unaryConstraint);
                }
            }
        }

        return new State(finalSymbols, finalConstraints);

    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean hasVBNError() {
        return error instanceof IVBNException;
    }
}