package vbn.constraints;

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

    private Map<String, Symbol> symbols = new HashMap<>();

    private Stack<Constraint> constraints = new Stack<>();

    public void pushConstraints(Constraint constraint) {
        constraints.push(constraint);
    }

    public void addSymbol(Symbol symbol) {
        symbols.put(symbol.id, symbol);
    }
    public void addSymbol(String stringSymbol, Symbol.SymbolType symbolType) {
        symbols.put(stringSymbol, new Symbol(stringSymbol, symbolType));
    }

    public Stack<Constraint> getConstraints() {
        return constraints;
    }

    public Map<String, Symbol> getSymbolsMap() {
        return symbols;
    }

    public Symbol getSymbol(String symbolName) {
        return symbols.get(symbolName);
    }

}