package vbn.solver;

import com.microsoft.z3.*;
import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.*;
import vbn.state.value.*;
import vbn.state.value.IntSymbol;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;

import static vbn.solver.VBNRunner.constraintNegatedMap;
import static vbn.solver.VBNRunner.constraintOriginallyNegated;

// IMPORTANT: IGNORE ALL CONCRETE VALUES OF SYMBOLS

public class Z3Solver {

    @NonNull
    private static Expr handleConstant(@NonNull Context ctx, @NonNull IConstant constant) {
        if (constant instanceof IntConstant) {
            return ctx.mkInt(((int) constant.getValue()));
        } else if (constant instanceof RealConstant) {
//            return ctx.mkReal(((RealConstant) constant).value);
            throw new RuntimeException("RealConstant cannot be handled");
        } else if (constant instanceof BooleanConstant) {
            return ctx.mkBool(((BooleanConstant) constant).value);
        } else if (constant instanceof UnknownConstant) {
            throw new RuntimeException("UnknownConstant cannot be handled");
        } else {
            throw new RuntimeException("Did not expect to reach this location in handleConstant");
        }
    }

    public static Expr handleExprBasedOnValue(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                                         @NonNull Value value) {
        if (value instanceof ISymbol) {
            @NonNull ISymbol symbol = (ISymbol) value;
            return z3ExprMap.get(symbol.getName());
        } else if (value instanceof IConstant) {
            return handleConstant(ctx, (IConstant) value);
        } else {
            throw new RuntimeException("Unable to handle value of unknown instance type");
        }
    }

    @NonNull
    public static Expr handleBinaryConstraints(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                               @NonNull BinaryConstraint binaryConstraint) {

        @NonNull Expr leftExpr = handleExprBasedOnValue(ctx, z3ExprMap, binaryConstraint.left);
        @NonNull Expr rightExpr = handleExprBasedOnValue(ctx, z3ExprMap, binaryConstraint.right);
        @NonNull BinaryOperand op = binaryConstraint.op;
        @Nullable ISymbol assigned = binaryConstraint.assigned;  // optional

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
            case NEQ:
                exprToReturn = ctx.mkNot(ctx.mkEq(leftExpr, rightExpr));
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
//            case POWER:
//                exprToReturn = ctx.mkPower(leftExpr, rightExpr);
//                break;
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
                throw new VBNSolverRuntimeError("Error, binary constraint operation not handled " + op);
        }
        if (exprToReturn != null && assigned != null) {
            @NonNull Expr assignedExpr = z3ExprMap.get(assigned.getName());
            exprToReturn = ctx.mkEq(assignedExpr, exprToReturn);
        }

        return exprToReturn;
    }

    @NonNull
    public static Expr handleUnaryConstraints(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                              @NonNull UnaryConstraint unaryConstraint) {
        @NonNull Expr symbolExpr = handleExprBasedOnValue(ctx, z3ExprMap, unaryConstraint.symbol);
        @NonNull UnaryOperand op = unaryConstraint.op;
        @Nullable ISymbol assigned = unaryConstraint.assigned; // optional

        Expr exprToReturn = null;

        switch(op) {
            case NOT:
                exprToReturn = ctx.mkNot(symbolExpr);
                break;
            case NEG:
                exprToReturn = ctx.mkMul(symbolExpr, ctx.mkInt(-1));
                break;
            default:
                throw new VBNSolverRuntimeError("Error, unary constraint operation not handled " + op);
        }
        if (exprToReturn != null && assigned != null) {
            @NonNull Expr assignedExpr = z3ExprMap.get(assigned.getName());
            exprToReturn = ctx.mkEq(assignedExpr, exprToReturn);
        }

        return exprToReturn;
    }

    public static ArrayList<ISymbol> returnSortedSymbols(ArrayList<ISymbol> symbols, Stack<IConstraint> constraintStack) {
        HashSet<String> hsSymbols = new HashSet<>();
        for (ISymbol sym : symbols) {
            hsSymbols.add(sym.getName());
        }

        ArrayList<ISymbol> sortedInputSymbols = new ArrayList<>();
        for (IConstraint constraint : constraintStack) {
            if (constraint instanceof BinaryConstraint) {
                BinaryConstraint bc = (BinaryConstraint) constraint;
                if (bc.assigned != null && hsSymbols.contains(bc.assigned.getName())) {
                    sortedInputSymbols.add(bc.assigned);
                    hsSymbols.remove(bc.assigned.getName());
                }

                if (bc.left instanceof ISymbol && hsSymbols.contains(((ISymbol) bc.left).getName())) {
                    sortedInputSymbols.add((ISymbol) bc.left);
                    hsSymbols.remove(((ISymbol) bc.left).getName());
                }

                if (bc.right instanceof ISymbol && hsSymbols.contains(((ISymbol) bc.right).getName())) {
                    sortedInputSymbols.add((ISymbol) bc.right);
                    hsSymbols.remove(((ISymbol) bc.right).getName());
                }
            } else if (constraint instanceof UnaryConstraint) {
                UnaryConstraint uc = (UnaryConstraint) constraint;
                if (uc.assigned != null && hsSymbols.contains(uc.assigned.getName())) {
                    sortedInputSymbols.add(uc.assigned);
                    hsSymbols.remove(uc.assigned.getName());
                }

                if (uc.symbol instanceof ISymbol && hsSymbols.contains(((ISymbol) uc.symbol).getName())) {
                    sortedInputSymbols.add((ISymbol) uc.symbol);
                    hsSymbols.remove(((ISymbol) uc.symbol).getName());
                }
            } else {
                throw new RuntimeException("Error, constraint not handled");
            }
        }

        return sortedInputSymbols;
    }

    @NonNull
    private static Expr negate(@NonNull Context ctx, @NonNull Expr expr) {
        return ctx.mkNot(expr);
    }

    public static ArrayList<ISymbol> solve(@NonNull State state) {
        System.out.println("================ TESTING Z3 SOLVER ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        ArrayList<ISymbol> symbols = state.getSymbols();
        System.out.println("DEBUGGING INPUT SYMBOLS: " + symbols);
        Map<String, Expr> z3ExprMap = new HashMap<>();
        for (ISymbol sym : symbols) {
            switch (sym.getType()) {
                case INT_TYPE:
                    z3ExprMap.put(sym.getName(), ctx.mkIntConst(sym.getName()));
                    break;
                case BOOL_TYPE:
                    z3ExprMap.put(sym.getName(), ctx.mkBoolConst(sym.getName()));
                    break;
                default:
                    throw new VBNSolverRuntimeError("Error, symbol " + sym.getType() + " wasn't of expected type");
            }
        }

        Stack<IConstraint> constraintStack = state.getConstraints();
        // will need to keep negating the top of the stack and then removing it while going down
        for (IConstraint constraint : constraintStack) {
             System.out.println(constraint);

            Expr constraintExpr;
            if (constraint instanceof UnaryConstraint) {
                constraintExpr = handleUnaryConstraints(ctx, z3ExprMap, (UnaryConstraint) constraint);
            } else if (constraint instanceof BinaryConstraint) {
                constraintExpr = handleBinaryConstraints(ctx, z3ExprMap, (BinaryConstraint) constraint);
            } else {
                throw new VBNSolverRuntimeError("Error, constraint type does not exist");
            }

            if (constraint.hasLineNumber()) {
                int constraintLineNumber = constraint.getLineNumber();
                if (constraintOriginallyNegated.containsKey(constraintLineNumber) && constraintOriginallyNegated.get(constraintLineNumber)) {
                    constraintExpr = negate(ctx, constraintExpr);
                }

                if (!constraintNegatedMap.containsKey(constraintLineNumber)) {
                    throw new RuntimeException("Constraint doesn't have its line number inside the constraint negated map");
                } else if (constraintNegatedMap.get(constraintLineNumber)) {
                    constraintExpr = negate(ctx, constraintExpr);
                }
            }

            solver.add(constraintExpr);
        }
        System.out.println("============== SOLVER CONSTRAINTS ==============");
        System.out.println(solver);
        System.out.println("============== END OF SOLVER CONSTRAINTS ==============");

        // Check for satisfying assignment
        Status status = solver.check();
        ArrayList<ISymbol> returnList = new ArrayList<>();
        if (status == Status.SATISFIABLE) {
            // Get satisfying assignment
            Model model = solver.getModel();

            String k;
            for (ISymbol s : returnSortedSymbols(symbols, constraintStack)) {
                // TODO: Only solve for the input variables:
                k = s.getName();
                if (!k.startsWith("vbn")) {
                    continue;
                }
                Expr evaluatedValue = model.eval(z3ExprMap.get(k), true);
                if (evaluatedValue instanceof BoolExpr) {
                    BoolExpr evaluatedValueBoolExpr = (BoolExpr) evaluatedValue;
                    String evaluatedBookExprEnumIntVal = evaluatedValueBoolExpr.getBoolValue().toString();
                    switch (evaluatedBookExprEnumIntVal) {
                        case "Z3_L_FALSE":
                            returnList.add(new BooleanSymbol(k, false));
                            break;
                        case "Z3_L_TRUE":
                            returnList.add(new BooleanSymbol(k, true));
                            break;
                        default:
                            throw new VBNSolverRuntimeError("Error, BoolExpr return value " + evaluatedBookExprEnumIntVal + " does not exist");
                    }
                } else if (evaluatedValue instanceof IntNum) {
                    IntNum evaluatedValueIntNum = (IntNum) evaluatedValue;
                    long val = evaluatedValueIntNum.getInt64();
                    returnList.add(new IntSymbol(k, val));
                } else {
                    throw new VBNSolverRuntimeError("Error, evaluatedValue instance of an unhandled class");
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

    public static void printSolvedValuesBasedOnList(List<ISymbol> symbols) {
        for (ISymbol symbol : symbols) {
            System.out.println(symbol.getName() + " = " + symbol.getValue());
//                System.out.println(symbol.getName() + " = unknown value (instance type: " + symbol.getClass().getName() + ")");

        }
    }
}
