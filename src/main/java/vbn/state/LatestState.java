package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.IConstraint;
import vbn.state.value.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This class handles all state necessary to solve an equation at a certain point
 */
public class LatestState implements Serializable {
    final Map<String, Integer> latestSymbolCounters;
    final GlobalState globalState;

    /**
     * Used to create a unique symbol every time it is necessary
     */
    int symbol_counter = 0;

    public LatestState(GlobalState globalState) {
        this.globalState = globalState;
        this.latestSymbolCounters = new HashMap<>();
    }

    /**
     * Generate a new symbol for the latest symbol
     */
    public void generateNewSymbolForVariable(String varName) {
        var currentCounter = latestSymbolCounters.get(varName);

        if (currentCounter == null) {
            currentCounter = 0;
        }

        int newValue = currentCounter + 1;
        latestSymbolCounters.put(varName, newValue);
    }

    /**
     * The default function to use
     * @param varName the name of the variable
     * @param concreteValue the value to update the variable to
     * @return the symbol that has been updated
     */
    @NonNull
    public ISymbol getLatestSymbolAndUpdateValue(@NonNull String varName, Object concreteValue) {
        updateLatestSymbol(varName, concreteValue);
        return getLatestSymbol(varName);
    }

    public ISymbol getLatestSymbolAndCreateIfDoesntExist(@NonNull String varName, Object concreteValue) {
        if (globalState.symbolsContainsKey(getSymbolName(varName))) {
            return getLatestSymbol(varName);
        }
        return createLatestSymbol(varName, concreteValue);
    }

    private void updateLatestSymbol(String varName, Object concreteValue) {
        var latestKey = getSymbolName(varName);
        globalState.updateSymbolConcreteValue(latestKey, concreteValue);

    }

    @NonNull
    private ISymbol createLatestSymbol(@NonNull String varName, Object concreteValue) {
        if (!latestSymbolCounters.containsKey(varName)) {
            latestSymbolCounters.put(varName, 0);
        }

        return globalState.createNewSymbol(getSymbolName(varName), concreteValue);
    }

    /**
     * Returns the symbol when you know it is not empty
     * @param varName the name of the variable, unappended
     * @return the symbol
     */

    @NonNull
    public ISymbol getLatestSymbol(String varName) {
        return globalState.getSymbol(getSymbolName(varName));
    }


    /**
     * We need non-duplicate names when recasting or visiting different functions
     * @param varName the original name of the variable
     * @return the variable name with the appended strings
     */
    @NonNull
    public String getSymbolName(String varName) {
        return varName + "__" + String.valueOf(latestSymbolCounters.get(varName));
    }

    /*
     * Wrapper functions to ensure everything passes through latest state
     */

    public void pushConstraint(IConstraint constraint) {
        globalState.pushConstraint(constraint);
    }

    public void setError(Throwable theError) {
        globalState.setError(theError);
    }

    public boolean hasVBNError() {
        return globalState.hasVBNError();
    }

    @NonNull
    public GlobalState getSerializeState() {
        return globalState.getSerializeState();
    }

    public @NonNull Stack<IConstraint> getConstraints() {
        return globalState.getConstraints();
    }
}