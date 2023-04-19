package vbn.instrument;

import java.util.HashMap;
import java.util.Map;

/**
 * Map from the variable name from the program
 * to a unique identifier
 */
public class SymbolTable {
    public Map<String, String> map;
    public Map<String, String> reverseMap;

    public SymbolTable() {
        map = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public String getIdentifier(String variable) {
        var value = map.get(variable);
        if (value != null) return value;
        int i = map.size() + 1;
        String id = String.format("v_%d", i);
        map.put(variable, id);
        reverseMap.put(id, variable);
        return id;
    }

    public String getVariable(String key) {
        return reverseMap.get(key);
    }
}
