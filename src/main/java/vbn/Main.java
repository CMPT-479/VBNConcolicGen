package vbn;

import vbn.instrument.Instrument;
import soot.G;
import soot.PackManager;
import soot.Transform;

public class Main {
    public static void main(String[] args) {
        G.reset();
        final var instrument = new Instrument();
        final var transform = new Transform("jtp.CuteTransformer", instrument);
        PackManager.v().getPack("jtp").add(transform);
        soot.Main.main(new String[] {"org.cmpt479.examples.Test_00_Basic"});
    }
}
