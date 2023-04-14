package org.cmpt479.instrument;

import soot.Body;
import soot.BodyTransformer;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Map;

public class CuteTransformer extends BodyTransformer {
    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        UnitGraph graph = new ExceptionalUnitGraph(body);
        for (var unit: graph) {
            System.out.println(unit);
        }
    }
}
