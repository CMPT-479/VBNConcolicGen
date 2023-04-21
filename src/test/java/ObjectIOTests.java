import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vbn.ObjectIO;
import vbn.solver.VBNRunner;
import vbn.state.State;
import vbn.state.constraints.IConstraint;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.constraints.UnaryConstraint;
import vbn.state.value.ISymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.IntSymbol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ObjectIOTests {

    @Test
    final void testWritingAndReadingSymbolListWithExternalDataStore() {
        State state = new State();
        state.addSymbol(new BooleanSymbol("123", true));
        state.addSymbol(new IntSymbol("567", 1));
        String fileName = "symbols.ser";
        ObjectIO.writeObjectToFile(state.getSymbols(), fileName);

        ArrayList<ISymbol> symbols = (ArrayList<ISymbol>) ObjectIO.readObjectFromFile(fileName);
        Assertions.assertNotNull(symbols);
        System.out.println(symbols);
        Assertions.assertNotEquals(symbols.size(), 0);
        for (ISymbol s : symbols) {
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

        Stack<IConstraint> constraints = (Stack<IConstraint>) ObjectIO.readObjectFromFile(fileName);
        Assertions.assertNotNull(constraints);
        System.out.println(constraints);
        Assertions.assertNotEquals(constraints.size(), 0);
        for (IConstraint c : constraints) {
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
        List<Serializable> names = new ArrayList<>();
        names.add("Alice");
        names.add(1);
        names.add(true);
        Serializable insert = (Serializable) names;

        File file = new File("names.dat");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(insert);
            System.out.println("Names written to file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Serializable> readNames = (List<Serializable>) ois.readObject();
            System.out.println("Names read from file:");
            for (Serializable name : readNames) {
                System.out.println(name.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        file.delete();
    }

    @Test
    final void testWritingAndReadingStateWithExternalDataStore() {
        State state = new State();
        BooleanSymbol symbol1 = new BooleanSymbol("1", false); // set with random variable
        BooleanSymbol symbol2 = new BooleanSymbol("2", true);
        state.addSymbol(symbol1);
        state.addSymbol(symbol2);
        state.pushConstraint(new BinaryConstraint(symbol1, BinaryOperand.AND, symbol2));

        VBNRunner.insertStateIntoIO(state);
        State returnedState = VBNRunner.returnStateFromIO();
        ArrayList<ISymbol> symbols = returnedState.getSymbols();

        Assertions.assertNotNull(symbols);
        System.out.println(symbols);
        Assertions.assertNotEquals(symbols.size(), 0);
        for (ISymbol s : symbols) {
            System.out.println("Found symbol from file: " + s.getName() + " " + s.getType() + " " + s.getValue());
        }

        Stack<IConstraint> constraints = returnedState.getConstraints();
        Assertions.assertNotNull(constraints);
        System.out.println(constraints);
        for (IConstraint c : constraints) {
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
    }
}
