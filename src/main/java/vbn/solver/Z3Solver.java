package vbn.solver;

import com.microsoft.z3.*;
import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.*;
import vbn.state.value.*;
import vbn.state.value.IntSymbol;

import javax.annotation.Nullable;
import java.util.*;

import static vbn.solver.VBNRunner.constraintNegatedMap;
import static vbn.solver.VBNRunner.constraintOriginallyNegated;

// IMPORTANT: IGNORE ALL CONCRETE VALUES OF SYMBOLS

public class Z3Solver {

    /**
     * Source: https://stackoverflow.com/a/1657688
     * @param input
     * @return convert a real number to a fraction, the first value of ret int array is numerator, second is denominator
     */
    private static int[] realToFraction(double input) {
        int p0 = 1;
        int q0 = 0;
        int p1 = (int) Math.floor(input);
        int q1 = 1;
        int p2;
        int q2;

        double r = input - p1;
        double next_cf;
        while(true) {
            r = 1.0 / r;
            next_cf = Math.floor(r);
            p2 = (int) (next_cf * p1 + p0);
            q2 = (int) (next_cf * q1 + q0);

            // Limit the numerator and denominator to be 256 or less
            if(p2 > 256 || q2 > 256)
                break;

            // remember the last two fractions
            p0 = p1;
            p1 = p2;
            q0 = q1;
            q1 = q2;

            r -= next_cf;
        }

        input = (double) p1 / q1;
        // hard upper and lower bounds for ratio
        if(input > 256.0) {
            p1 = 256;
            q1 = 1;
        } else if(input < 1.0 / 256.0) {
            p1 = 1;
            q1 = 256;
        }
        return new int[] {p1, q1};
    }

    @NonNull
    private static Expr handleConstant(@NonNull Context ctx, @NonNull IConstant constant) {
        if (constant instanceof IntConstant) {
            return ctx.mkInt(((int) constant.getValue()));
        } else if (constant instanceof RealConstant) {
            int[] fraction = realToFraction(((Number) constant.getValue()).doubleValue());
            return ctx.mkReal(fraction[0], fraction[1]);
        } else if (constant instanceof BooleanConstant) {
            return ctx.mkBool(((BooleanConstant) constant).value);
        } else if (constant instanceof UnknownConstant) {
            throw new VBNSolverRuntimeError("UnknownConstant cannot be handled");
        } else {
            throw new VBNSolverRuntimeError("Did not expect to reach this location in handleConstant");
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
            throw new VBNSolverRuntimeError("Unable to handle value of unknown instance type");
        }
    }

    @NonNull
    public static Expr handleBinaryConstraints(@NonNull Context ctx, @NonNull Map<String, Expr> z3ExprMap,
                                               @NonNull BinaryConstraint binaryConstraint) {

        @NonNull Expr leftExpr = handleExprBasedOnValue(ctx, z3ExprMap, binaryConstraint.left);
        @NonNull Expr rightExpr = handleExprBasedOnValue(ctx, z3ExprMap, binaryConstraint.right);
        @NonNull BinaryOperand op = binaryConstraint.op;
        @Nullable ISymbol assigned = binaryConstraint.assignedSymbol;  // optional

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
        @Nullable ISymbol assigned = unaryConstraint.assignedSymbol; // optional

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
                if (bc.assignedSymbol != null && hsSymbols.contains(bc.assignedSymbol.getName())) {
                    sortedInputSymbols.add(bc.assignedSymbol);
                    hsSymbols.remove(bc.assignedSymbol.getName());
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
                if (uc.assignedSymbol != null && hsSymbols.contains(uc.assignedSymbol.getName())) {
                    sortedInputSymbols.add(uc.assignedSymbol);
                    hsSymbols.remove(uc.assignedSymbol.getName());
                }

                if (uc.symbol instanceof ISymbol && hsSymbols.contains(((ISymbol) uc.symbol).getName())) {
                    sortedInputSymbols.add((ISymbol) uc.symbol);
                    hsSymbols.remove(((ISymbol) uc.symbol).getName());
                }
            } else {
                throw new VBNSolverRuntimeError("Error, constraint not handled");
            }
        }

        return sortedInputSymbols;
    }

    @NonNull
    private static Expr negate(@NonNull Context ctx, @NonNull Expr expr) {
        return ctx.mkNot(expr);
    }

    public static ArrayList<ISymbol> solve(@NonNull GlobalState globalState) {
        System.out.println("================ TESTING Z3 SOLVER ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        ArrayList<ISymbol> symbols = globalState.getSymbols();
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
                case REAL_TYPE:
                    z3ExprMap.put(sym.getName(), ctx.mkRealConst(sym.getName()));
                    break;
                default:
                    throw new VBNSolverRuntimeError("Error, symbol " + sym.getType() + " wasn't of expected type");
            }
        }

        Stack<IConstraint> constraintStack = globalState.getConstraints();
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

            if (constraint.isBranch()) {
                long constraintLineNumber = constraint.getUniqueId();
                if (constraintOriginallyNegated.containsKey(constraintLineNumber) && constraintOriginallyNegated.get(constraintLineNumber)) {
                    constraintExpr = negate(ctx, constraintExpr);
                }

                if (!constraintNegatedMap.containsKey(constraintLineNumber)) {
                    throw new VBNSolverRuntimeError("Constraint doesn't have its line number inside the constraint negated map");
                } else if (constraintNegatedMap.get(constraintLineNumber)) {
                    constraintExpr = negate(ctx, constraintExpr);
                }
            }

            System.out.println("\t" + constraintExpr);

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
                }else if (evaluatedValue instanceof RatNum) {
                    RatNum evaluatedValueIntNum = (RatNum) evaluatedValue;
                    double val = (double) evaluatedValueIntNum.getNumerator().getInt64() / evaluatedValueIntNum.getDenominator().getInt64();
                    returnList.add(new RealSymbol(k, val));
                } else {
                    throw new VBNSolverRuntimeError("Error, evaluatedValue instance of an unhandled class " + evaluatedValue.getClass());
                }
            }
        } else if (status == Status.UNSATISFIABLE) {
            throw new VBNSolverRuntimeError("Path constraint is unsatisfiable");
        } else {
            throw new VBNSolverRuntimeError("Solver did not return a satisfying assignment");
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
