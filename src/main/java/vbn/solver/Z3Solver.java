package vbn.solver;

import com.microsoft.z3.*;
import lombok.NonNull;
import soot.util.Cons;
import vbn.constraints.*;
import vbn.constraints.Symbol;
//import vbn.constraints.ConstraintIntComp;

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
        }
        if (exprToReturn != null && assigned != null) {
            @NonNull Expr assignedExpr = z3ExprMap.get(assigned.id);
            exprToReturn = ctx.mkEq(assignedExpr, exprToReturn);
        }

        return exprToReturn;
    }

//
//    public static IntExpr handleIntConstraints(ConstraintItemInt constraintItemInt,
//                                               Map<String, Expr> z3ExprMap, Context ctx) {
//        IntExpr expr = null;
//        if (constraintItemInt == null ||
//                constraintItemInt.left == null || constraintItemInt.left.id == null ||
//                constraintItemInt.right == null || constraintItemInt.right.id == null) {
//            return null;
//        }
//
//        Expr leftExpr = z3ExprMap.get(constraintItemInt.left.id);
//        Expr rightExpr = z3ExprMap.get(constraintItemInt.right.id);
//
//        if (leftExpr == null || rightExpr == null) {
//            return null;
//        }
//
//        switch (constraintItemInt.op) {
//            case EQ:
//                break;
//            case GE:
//                break;
//            case GT:
//                break;
//            case LE:
//                break;
//            case LT:
//                break;
//        }
//        return expr;
//    }

    public static void solve(@NonNull State state) {
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
                    System.out.println("ERROR, Symbol type wasn't of expected type");
                    break;
            }
        }

        Stack<Constraint> constraintStack = state.getConstraints();
        // will need to keep negating the top of the stack and then removing it while going down
        for (Constraint constraint : constraintStack) {
            System.out.println(constraint.getClass());
            if (constraint.getClass().equals(UnaryConstraint.class)) {
                solver.add();
//                constraints.add(handleBoolConstraints((ConstraintItemBool) constraintItem, z3ExprMap, ctx));
            } else if (constraint.getClass().equals(BinaryConstraint.class)) {
                solver.add(handleBinaryConstraints(ctx, z3ExprMap, (BinaryConstraint) constraint));
//                constraints.add(handleIntConstraints((ConstraintItemInt) constraintItem, z3ExprMap, ctx));
            } else {
                System.out.println("Error");
            }
        }

        // Create logical expression
//        BoolExpr x = ctx.mkBoolConst("x");
//        BoolExpr y = ctx.mkBoolConst("y");
//        BoolExpr p1 = ctx.mkOr(x, y);
//        BoolExpr p2 = ctx.mkOr(ctx.mkNot(x), y);
//        BoolExpr pathConstraint = ctx.mkAnd(p1, p2);

//        BoolExpr pathConstraint =;

        // Add path constraint to solver
//        for (Expr expr : constraints) {
//            solver.add(expr);
//        }
//        solver.add(pathConstraint);

        // Check for satisfying assignment
        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            // Get satisfying assignment
            Model model = solver.getModel();
//            for (String k : keys) {
//                System.out.println(k + " = " + model.eval(z3ExprMap.get(k), true));
//            }
//            System.out.println("x = " + model.eval(x, true));
//            System.out.println("y = " + model.eval(y, true));
        } else if (status == Status.UNSATISFIABLE) {
            System.out.println("Path constraint is unsatisfiable");
        } else {
            System.out.println("Solver did not return a satisfying assignment");
        }

        // Dispose of solver instance
        System.gc();
        ctx.close();
    }
}
