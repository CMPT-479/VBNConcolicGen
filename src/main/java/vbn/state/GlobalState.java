package vbn.state;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.helpers.ComputeValueType;
import vbn.state.value.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * This class handles all state necessary to solve an equation at a certain point
 */
public class GlobalState implements Serializable {
    private Throwable error;

    private final Map<String, ISymbol> symbols;

    private final Stack<IConstraint> constraints;


    /**
     * When we're casting an int to a bool and don't know what value to give it,
     * give it this.
     * TODO: Should we default to false? Or something else?
     */
    static final boolean DEFAULT_VALUE_REASSIGN_BOOLEAN = false;

    public GlobalState() {
        super();
        symbols = new HashMap<>();
        constraints = new Stack<>();
    }

    public GlobalState(Map<String, ISymbol> symbols, Stack<IConstraint> constraints) {
        this.symbols = symbols;
        this.constraints = constraints;
    }

    /**
     * Push a general constraint
     * @param constraint the constraint to add the constraint stack
     */
    public void pushConstraint(IConstraint constraint) {
        if (constraint instanceof BinaryConstraint) {
            processBoolToInt((BinaryConstraint) constraint);

            if (!((BinaryConstraint) constraint).valuesTypesAreValid()) {
                throw new VBNLibraryRuntimeException("The value types are not equal when creating a constraint");
            }
        }

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
     * Actually handles setting the symbol
     * @param symbol the symbol to set
     */
    private void addSymbolFinal(ISymbol symbol) {
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

    public boolean symbolsContainsKey(String symbolName) {
        return symbols.containsKey(symbolName);
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
    @NonNull
    public GlobalState getSerializeState() {
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

        return new GlobalState(finalSymbols, finalConstraints);

    }

    public ISymbol createNewSymbol(String varName, Object concreteValue) {
        ISymbol result;
        Value.Type valueType = ComputeValueType.getType(concreteValue);

        switch (valueType) {
            case INT_TYPE:
                result = new IntSymbol(varName, ((Number) concreteValue).longValue());
                break;
            case REAL_TYPE:
                result = new RealSymbol(varName, ((Number) concreteValue).doubleValue());
                break;
            case BOOL_TYPE:
                result = new BooleanSymbol(varName, (boolean) concreteValue);
                break;
            case UNKNOWN:
                result = new UnknownSymbol(varName, concreteValue);
                break;
            default:
                throw new VBNLibraryRuntimeException("A type was not handled");
        }

        addSymbol(result);

        return result;
    }

    private void processBoolToInt(BinaryConstraint constraint) {

        // Jimple converts booleans into an int that is one or zero
        // We need to convert it back into a boolean
        constraint.right = handleConvertIntToBoolForComparison(constraint.left, constraint.op, constraint.right);
        constraint.left = handleConvertIntToBoolForComparison(constraint.right, constraint.op, constraint.left);

        constraint.assignedSymbol = handleConvertIntToBoolForAssignment(constraint.assignedSymbol, constraint.op);
    }

    private ISymbol handleConvertIntToBoolForAssignment(ISymbol assignSym, BinaryOperand binOp) {
        if (assignSym == null || assignSym instanceof BooleanSymbol) {
            return assignSym;
        }

        switch (binOp) {
            case EQ:
            case NEQ:

            case AND:
            case OR:

            case LTE:
            case GTE:
            case LT:
            case GT:
                assignSym = new BooleanSymbol(assignSym.getName(), DEFAULT_VALUE_REASSIGN_BOOLEAN);
                addSymbolFinal(assignSym);
                break;

            case ADD:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
                // Does not need to do anything
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + binOp);
        }

        return assignSym;
    }

    /**
     * Jimple converts booleans into an int that is one or zero (e.g. if (BOOL == 0))
     *
     * @param boolSymbol the boolean symbol
     * @param binOp the operation to confirm is equal
     * @param intSymbol the int symbol to convert to a bool symbol
     * @return the int symbol, potentially converted to a bool symbol
     */
    private static Value handleConvertIntToBoolForComparison(@NonNull Value boolSymbol, @NonNull BinaryOperand binOp, @NonNull Value intSymbol) {
        if (boolSymbol instanceof BooleanSymbol && intSymbol instanceof IntConstant) {
            int leftValue = (int) intSymbol.getValue();
            if (!(leftValue == 0 || leftValue == 1)) {
                throw new VBNLibraryRuntimeException("The int value handled must be 0 or 1 when converting to bool");
            }
            if (!(binOp == BinaryOperand.EQ)) {
                throw new VBNLibraryRuntimeException("The operator must be 'equal to' when converting ");
            }
            intSymbol = new BooleanConstant(leftValue == 1);
        }
        return intSymbol;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean hasVBNError() {
        // FIXME: We should somehow detect NullPointersFromVBNOnly
        return error instanceof IVBNException || error instanceof NullPointerException;
    }
}