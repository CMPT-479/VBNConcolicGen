package vbn.solver;

import com.microsoft.z3.*;
import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.*;
import vbn.state.value.AbstractSymbolConstant;
import vbn.state.value.Symbol;
import vbn.state.value.SymbolBooleanConstant;
import vbn.state.value.SymbolIntConstant;

import javax.annotation.Nullable;
import java.util.*;

public class Z3Solver {


    public static Expr handleBinaryConstraints(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                               @NonNull BinaryConstraint binaryConstraint) {
        @NonNull Symbol leftSymbol = binaryConstraint.left;
        @NonNull Symbol rightSymbol = binaryConstraint.right;
        @NonNull BinaryOperand op = binaryConstraint.op;
        @Nullable Symbol assigned = binaryConstraint.assigned;  // optional

        @NonNull Expr leftExpr = z3ExprMap.get(leftSymbol.id);
        @NonNull Expr rightExpr = z3ExprMap.get(rightSymbol.id);

        Expr exprToReturn = null;
        switch (op) {
            case AND:
                exprToReturn = ctx.mkAnd(leftExpr, rightExpr);
                break;
            case OR:
                exprToReturn = ctx.mkOr(leftExpr, rightExpr);
                break;
            case EQ:
                exprToReturn = ctx.mkEq(leftExpr, rightExpr);
                break;
            case ADD:
                exprToReturn = ctx.mkAdd(leftExpr, rightExpr);
                break;
            case MINUS:
                exprToReturn = ctx.mkSub(leftExpr, rightExpr);
                break;
            case MULTIPLY:
                exprToReturn = ctx.mkMul(leftExpr, rightExpr);
                break;
            case DIVIDE:
                exprToReturn = ctx.mkDiv(leftExpr, rightExpr);
                break;
            case POWER:
                exprToReturn = ctx.mkPower(leftExpr, rightExpr);
                break;
            case LT:
                exprToReturn = ctx.mkLt(leftExpr, rightExpr);
                break;
            case LTE:
                exprToReturn = ctx.mkLe(leftExpr, rightExpr);
                break;
//            case EQ: // Removed because we previous had EQ and INT_EQ
//                exprToReturn = ctx.mkEq(leftExpr, rightExpr);
//                break;
//            case NEG: // Removed because negation is not binary
//                exprToReturn = ctx.mkNot(ctx.mkEq(leftExpr, rightExpr));
//                break;
            case GT:
                exprToReturn = ctx.mkGt(leftExpr, rightExpr);
                break;
            case GTE:
                exprToReturn = ctx.mkGe(leftExpr, rightExpr);
                break;
            default:
                throw new RuntimeException("Error, binary constraint operation not handled " + op);
        }
        if (exprToReturn != null && assigned != null) {
            @NonNull Expr assignedExpr = z3ExprMap.get(assigned.id);
            exprToReturn = ctx.mkEq(assignedExpr, exprToReturn);
        }

        return exprToReturn;
    }

    public static Expr handleUnaryConstraints(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                              @NonNull UnaryConstraint unaryConstraint) {
        @NonNull Symbol symbol = unaryConstraint.symbol;
        @NonNull UnaryOperand op = unaryConstraint.op;
        @Nullable Symbol assigned = unaryConstraint.assigned; // optional

        @NonNull Expr symbolExpr = z3ExprMap.get(symbol.id);
        Expr exprToReturn = null;

        switch(op) {
            case NOT:
                exprToReturn = ctx.mkNot(symbolExpr);
                break;
            case NEG:
                exprToReturn = ctx.mkMul(symbolExpr, ctx.mkInt(-1));
                break;
            default:
                throw new RuntimeException("Error, unary constraint operation not handled " + op);
        }
        if (exprToReturn != null && assigned != null) {
            @NonNull Expr assignedExpr = z3ExprMap.get(assigned.id);
            exprToReturn = ctx.mkEq(assignedExpr, exprToReturn);
        }

        return exprToReturn;
    }

    public static ArrayList<AbstractSymbolConstant> solve(@NonNull State state) {
        System.out.println("================ TESTING Z3 SOLVER ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        Collection<Symbol> symbols = state.getSymbols();
        Map<String, Expr> z3ExprMap = new HashMap<>();
        for (var sym : symbols) {
            switch (sym.type) {
                case INT_TYPE:
                    z3ExprMap.put(sym.id, ctx.mkIntConst(sym.id));
                    break;
                case BOOL_TYPE:
                    z3ExprMap.put(sym.id, ctx.mkBoolConst(sym.id));
                    break;
                default:
                    throw new RuntimeException("Error, symbol " + sym.type + " wasn't of expected type");
            }
        }

        Stack<Constraint> constraintStack = state.getConstraints();
        // will need to keep negating the top of the stack and then removing it while going down
        for (Constraint constraint : constraintStack) {
            System.out.println(constraint.getClass());
            if (constraint instanceof UnaryConstraint) {
                if (constraint.negated) {
                    solver.add(ctx.mkNot(handleUnaryConstraints(ctx, z3ExprMap, (UnaryConstraint) constraint)))   ;
                } else {
                    solver.add(handleUnaryConstraints(ctx, z3ExprMap, (UnaryConstraint) constraint));
                }

            } else if (constraint instanceof BinaryConstraint) {
                if (constraint.negated) {
                    solver.add(ctx.mkNot(handleBinaryConstraints(ctx, z3ExprMap, (BinaryConstraint) constraint)))   ;
                } else {
                    solver.add(handleBinaryConstraints(ctx, z3ExprMap, (BinaryConstraint) constraint));
                }
            } else {
                throw new RuntimeException("Error, constraint type does not exist");
            }
        }

        // Check for satisfying assignment
        Status status = solver.check();
        ArrayList<AbstractSymbolConstant> returnList = new ArrayList<>();
        if (status == Status.SATISFIABLE) {
            // Get satisfying assignment
            Model model = solver.getModel();
            for (String k : z3ExprMap.keySet()) {
                Expr evaluatedValue = model.eval(z3ExprMap.get(k), true);
                if (evaluatedValue instanceof BoolExpr) {
                    BoolExpr evaluatedValueBoolExpr = (BoolExpr) evaluatedValue;
                    String evaluatedBookExprEnumIntVal = evaluatedValueBoolExpr.getBoolValue().toString();
                    switch (evaluatedBookExprEnumIntVal) {
                        case "Z3_L_FALSE":
                            returnList.add(new SymbolBooleanConstant(k, false));
                            break;
                        case "Z3_L_TRUE":
                            returnList.add(new SymbolBooleanConstant(k, true));
                            break;
                        default:
                            throw new RuntimeException("Error, BoolExpr return value " + evaluatedBookExprEnumIntVal + " does not exist");
                    }
                } else if (evaluatedValue instanceof IntNum) {
                    IntNum evaluatedValueIntNum = (IntNum) evaluatedValue;
                    int val = evaluatedValueIntNum.getInt();
                    returnList.add(new SymbolIntConstant(k, val));
                } else {
                    throw new RuntimeException("Error, evaluatedValue instance of an unhandled class");

                }
            }
        } else if (status == Status.UNSATISFIABLE) {
            System.out.println("Path constraint is unsatisfiable");
        } else {
            System.out.println("Solver did not return a satisfying assignment");
        }

        // Dispose of solver instance
        System.gc();
        ctx.close();

        return returnList;
    }

    public static void printSolvedValuesBasedOnList(List<AbstractSymbolConstant> symbols) {
        for (AbstractSymbolConstant symbol : symbols) {
            if (symbol instanceof SymbolBooleanConstant) {
                System.out.println(symbol.id + " = " + ((SymbolBooleanConstant) symbol).value);
            } else if (symbol instanceof SymbolIntConstant) {
                System.out.println(symbol.id + " = " + ((SymbolIntConstant) symbol).value);
            } else {
                System.out.println(symbol.id + " = unknown value");
            }
        }
    }
}
