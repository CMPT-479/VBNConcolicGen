package vbn.instrument;

import java.util.HashMap;
import java.util.Map;

/**
 * Map from the variable name from the program
 * to a unique identifier
 */
public class SymbolTable {
    public Map<String, Integer> map;
    public Map<Integer, String> reverseMap;


    public SymbolTable() {
        map = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public int getId(String variable) {
        var value = map.get(variable);
        if (value != null) return value;
        int i = map.size() + 1;
        map.put(variable, i);
        reverseMap.put(i, variable);
        return i;
    }

    public String getVariable(int key) {
        return reverseMap.get(key);
    }
}
