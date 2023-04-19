package vbn.solver;

import com.microsoft.z3.*;
import vbn.constraints.Constraint;
//import vbn.constraints.ConstraintIntComp;
import vbn.constraints.State;
import vbn.constraints.Symbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Z3Solver {


    public static BoolExpr handleBoolConstraints(ConstraintItemBool constraintItemBool,
                                                 Map<String, Expr> z3ExprMap, Context ctx) {
        BoolExpr expr = null;
        if (constraintItemBool == null ||
                constraintItemBool.left == null || constraintItemBool.left.id == null ||
                constraintItemBool.right == null || constraintItemBool.right.id == null) {
            return null;
        }

        Expr leftExpr = z3ExprMap.get(constraintItemBool.left.id);
        Expr rightExpr = z3ExprMap.get(constraintItemBool.right.id);
        if (leftExpr == null || rightExpr == null) {
            return null;
        }

        switch (constraintItemBool.op) {
            case OR:
                expr = ctx.mkOr(leftExpr, rightExpr);
                break;
            case AND:
                expr = ctx.mkAnd(leftExpr, rightExpr);
                break;
            case EQ:
                expr = ctx.mkEq(leftExpr, rightExpr);
                break;
        }
        return expr;
    }

    public static IntExpr handleIntConstraints(ConstraintItemInt constraintItemInt,
                                               Map<String, Expr> z3ExprMap, Context ctx) {
        IntExpr expr = null;
        if (constraintItemInt == null ||
                constraintItemInt.left == null || constraintItemInt.left.id == null ||
                constraintItemInt.right == null || constraintItemInt.right.id == null) {
            return null;
        }

        Expr leftExpr = z3ExprMap.get(constraintItemInt.left.id);
        Expr rightExpr = z3ExprMap.get(constraintItemInt.right.id);

        if (leftExpr == null || rightExpr == null) {
            return null;
        }

        switch (constraintItemInt.op) {
            case EQ:
                break;
            case GE:
                break;
            case GT:
                break;
            case LE:
                break;
            case LT:
                break;
        }
        return expr;
    }

    public static void solve(State state) {
        System.out.println("================ TESTING Z3 SOLVER ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        Map<String, vbn.constraints.Symbol> symbolMap = state.getSymbolsMap();
        Set<String> keys = state.getSymbolsMap().keySet();
        Map<String, Expr> z3ExprMap = new HashMap<>();
        for (String k : keys) {
            switch (symbolMap.get(k).symbolType) {
                case INT_TYPE:
                    z3ExprMap.put(k, ctx.mkIntConst(k));
                    break;
                case BOOL_TYPE:
                    z3ExprMap.put(k, ctx.mkBoolConst(k));
                    break;
                default:
                    break;
            }
        }
        Set<Expr> constraints = new HashSet<>();
        for (ConstraintItem constraintItem : state.getConstraints()) {
            System.out.println(constraintItem.getClass());
            if (constraintItem.getClass().equals(ConstraintItemBool.class)) {
                constraints.add(handleBoolConstraints((ConstraintItemBool) constraintItem, z3ExprMap, ctx));
            } else if (constraintItem.getClass().equals(ConstraintItemInt.class)) {
                constraints.add(handleIntConstraints((ConstraintItemInt) constraintItem, z3ExprMap, ctx));
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
        for (Expr expr : constraints) {
            solver.add(expr);
        }
//        solver.add(pathConstraint);

        // Check for satisfying assignment
        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            // Get satisfying assignment
            Model model = solver.getModel();
            for (String k : keys) {
                System.out.println(k + " = " + model.eval(z3ExprMap.get(k), true));
            }
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
