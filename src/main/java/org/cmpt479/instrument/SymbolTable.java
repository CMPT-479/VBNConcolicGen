package org.cmpt479.instrument;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    public Map<String, Integer> map;
    public Map<Integer, String> reverseMap;

    public SymbolTable() {
        map = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public int get(String key) {
        var value = map.get(key);
        if (value != null) return value;
        int i = map.size() + 1;
        map.put(key, i);
        reverseMap.put(i, key);
        return i;
    }

    public String getReverse(int key) {
        return reverseMap.get(key);
    }
}
