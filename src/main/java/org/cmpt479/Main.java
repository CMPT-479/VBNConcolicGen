package org.cmpt479;

import org.cmpt479.instrument.CuteTransformer;
import soot.PackManager;
import soot.G;
import soot.Transform;
import soot.options.Options;

import java.io.File;

public class Main {
    public static void setupSoot() {
        G.reset();
        Options.v().set_soot_classpath(".:build/classes:VIRTUAL_FS_FOR_JDK");
    }

    public static void main(String[] args) {
        setupSoot();
        final var instrument = new CuteTransformer();
        final var transform = new Transform("jtp.CuteTransformer", instrument);
        PackManager.v().getPack("jtp").add(transform);
        soot.Main.main(new String[] {"org.cmpt479.examples.Test_00_Basic"});
    }
}
