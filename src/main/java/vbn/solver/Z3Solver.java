package vbn.solver;

import com.microsoft.z3.*;
import lombok.NonNull;
import vbn.constraints.*;
import vbn.constraints.Symbol;

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
            case INT_LT:
                exprToReturn = ctx.mkLt(leftExpr, rightExpr);
                break;
            case INT_LTE:
                exprToReturn = ctx.mkLe(leftExpr, rightExpr);
                break;
            case INT_EQ:
                exprToReturn = ctx.mkEq(leftExpr, rightExpr);
                break;
            case INT_NEQ:
                exprToReturn = ctx.mkNot(ctx.mkEq(leftExpr, rightExpr));
                break;
            case INT_GT:
                exprToReturn = ctx.mkGt(leftExpr, rightExpr);
                break;
            case INT_GTE:
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

    public static List<Symbol> solve(@NonNull State state) {
        System.out.println("================ TESTING Z3 SOLVER ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        Collection<Symbol> symbols = state.getSymbols();
        Map<String, Expr> z3ExprMap = new HashMap<>();
        for (var sym : symbols) {
            switch (sym.symbolType) {
                case INT_TYPE:
                    z3ExprMap.put(sym.id, ctx.mkIntConst(sym.id));
                    break;
                case BOOL_TYPE:
                    z3ExprMap.put(sym.id, ctx.mkBoolConst(sym.id));
                    break;
                default:
                    throw new RuntimeException("Error, symbol " + sym.symbolType + " wasn't of expected type");
            }
        }

        Stack<Constraint> constraintStack = state.getConstraints();
        // will need to keep negating the top of the stack and then removing it while going down
        for (Constraint constraint : constraintStack) {
            System.out.println(constraint.getClass());
            if (constraint instanceof UnaryConstraint) {
                solver.add(handleUnaryConstraints(ctx, z3ExprMap, (UnaryConstraint) constraint));
            } else if (constraint instanceof BinaryConstraint) {
                solver.add(handleBinaryConstraints(ctx, z3ExprMap, (BinaryConstraint) constraint));
            } else {
                throw new RuntimeException("Error, constraint type does not exist");
            }
        }

        // Check for satisfying assignment
        Status status = solver.check();
        ArrayList<Symbol> returnList = new ArrayList<>();
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
                            returnList.add(new BooleanConstant(k, false));
                            break;
                        case "Z3_L_TRUE":
                            returnList.add(new BooleanConstant(k, true));
                            break;
                        default:
                            throw new RuntimeException("Error, BoolExpr return value " + evaluatedBookExprEnumIntVal + " does not exist");
                    }
                } else if (evaluatedValue instanceof IntNum) {
                    IntNum evaluatedValueIntNum = (IntNum) evaluatedValue;
                    int val = evaluatedValueIntNum.getInt();
                    returnList.add(new IntConstant(k, val));
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

    public static void printSolvedValuesBasedOnList(List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof BooleanConstant) {
                System.out.println(symbol.id + " = " + ((BooleanConstant) symbol).value);
            } else if (symbol instanceof IntConstant) {
                System.out.println(symbol.id + " = " + ((IntConstant) symbol).value);
            } else {
                System.out.println(symbol.id + " = unknown value");
            }
        }
    }
}
