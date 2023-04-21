import vbn.ObjectIO;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.InstrumentedRunner;
import vbn.solver.VBNRunner;
import vbn.solver.Z3Solver;
import vbn.state.State;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.constraints.AbstractConstraint;
import vbn.state.constraints.UnaryConstraint;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class MainTest {

    @BeforeEach
    final void initializeSoot() {
        G.reset();
        final String[] paths = {
                ".",
                new File("target", "classes").toString(),
                "VIRTUAL_FS_FOR_JDK",
        };

        Options.v().set_soot_classpath(String.join(File.pathSeparator, paths));
        Options.v().set_keep_line_number(true);
        final var cute = new Instrument();
        final var transform = new Transform("jtp.Instrument", cute);
        PackManager.v().getPack("jtp").add(transform);
    }

    @Test
    final void basic() {
        VBNRunner.execute("vbn.examples.Test_00_Basic", new String[]{"0", "1"});
    }

    @Test
    final void testWritingAndReadingSymbolListWithExternalDataStore() {
        State state = new State();
        state.addSymbol("123", Value.Type.BOOL_TYPE, true);
        state.addSymbol("567", Value.Type.INT_TYPE, 1);
        String fileName = "symbols.ser";
        ObjectIO.writeObjectToFile(state.getSymbols(), fileName);

        ArrayList<AbstractSymbol> symbols = (ArrayList<AbstractSymbol>) ObjectIO.readObjectFromFile(fileName);
        Assertions.assertNotNull(symbols);
        System.out.println(symbols);
        Assertions.assertNotEquals(symbols.size(), 0);
        for (AbstractSymbol s : symbols) {
            System.out.println("Found symbol from file: " + s.getName() + " " + s.getType() + " " + s.getValue());
        }
        ObjectIO.deleteFile(fileName);
    }

    @Test
    final void testWritingAndReadingConstraintListWithExternalDataStore() {
        State state = new State();
        BooleanSymbol symbol1 = new BooleanSymbol("1", false); // set with random variable
        BooleanSymbol symbol2 = new BooleanSymbol("2", true);
        state.addSymbol(symbol1);
        state.addSymbol(symbol2);
        state.pushConstraint(new BinaryConstraint(symbol1, BinaryOperand.AND, symbol2));

        String fileName = "constraints.ser";
        ObjectIO.writeObjectToFile(state.getConstraints(), fileName);

        Stack<AbstractConstraint> constraints = (Stack<AbstractConstraint>) ObjectIO.readObjectFromFile(fileName);
        Assertions.assertNotNull(constraints);
        System.out.println(constraints);
        Assertions.assertNotEquals(constraints.size(), 0);
        for (AbstractConstraint c : constraints) {
            if (c instanceof BinaryConstraint) {
                BinaryConstraint bc = (BinaryConstraint) c;
                System.out.println("Found instance of constraint "
                        + ((BooleanSymbol) bc.left).getName()
                        + " " + bc.op + " "
                        + ((BooleanSymbol) bc.right).getName());
            } else if (c instanceof UnaryConstraint) {
                UnaryConstraint uc = (UnaryConstraint) c;
                System.out.println("Found instance of constraint" + ((BooleanSymbol) uc.symbol).getName() + " " + uc.op);
            } else {
                System.out.println("Found ineligible instance of constraint");
            }
        }
        ObjectIO.deleteFile(fileName);
    }

    @Test
    final void testCollectionStore() {
//        List<Serializable> names = new ArrayList<>();
//        names.add("Alice");
//        names.add(1);
//        names.add(true);
//        Serializable insert = (Serializable) names;
//
//        File file = new File("names.dat");
//
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//            oos.writeObject(insert);
//            System.out.println("Names written to file");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
//            List<Serializable> readNames = (List<Serializable>) ois.readObject();
//            System.out.println("Names read from file:");
//            for (Serializable name : readNames) {
//                System.out.println(name.toString());
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        file.delete();
    }

    @Test
    final void basicArray() {
        final String[] args = new String[] {"vbn.examples.Test_01_Array"};
        soot.Main.main(args);
    }

    @Test
    final void basicNegAndMinus() {
        final String[] args = new String[] {"vbn.examples.Test_02_NEG_vs_MINUS"};
        soot.Main.main(args);
    }

    @Test
    final void basicClass() {
        final String[] args = new String[] {"vbn.examples.Test_03_Class"};
        soot.Main.main(args);
    }

//    @Test
//    final void testZ3SimpleOr() {
//        // x or y
//        State state = new State();
//        state.addSymbol("x", Value.ValueType.BOOL_TYPE);
//        state.addSymbol("y", Value.ValueType.BOOL_TYPE);
//        state.pushConstraint(
//                new BinaryConstraint(
//                        state.getSymbol("x"),
//                        BinaryOperand.OR,
//                        state.getSymbol("y")));
//
//        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
//        Z3Solver.printSolvedValuesBasedOnList(solved);
//    }
//
//    @Test
//    final void testZ3SimpleAnd() {
//        // x and y
//        State state = new State();
//        state.addSymbol("x", Value.ValueType.BOOL_TYPE);
//        state.addSymbol("y", Value.ValueType.BOOL_TYPE);
//        state.pushConstraint(
//                new BinaryConstraint(
//                        state.getSymbol("x"),
//                        BinaryOperand.AND,
//                        state.getSymbol("y")));
//
//        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
//        Z3Solver.printSolvedValuesBasedOnList(solved);
//    }
//
//    @Test
//    final void testZ3Gt() {
//        // x > y
//        State state = new State();
//        state.addSymbol("x", Value.ValueType.INT_TYPE);
//        state.addSymbol("y", Value.ValueType.INT_TYPE);
//        state.pushConstraint(
//                new BinaryConstraint(
//                        state.getSymbol("x"),
//                        BinaryOperand.GT,
//                        state.getSymbol("y")));
//
//        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
//        Z3Solver.printSolvedValuesBasedOnList(solved);
//    }
}
