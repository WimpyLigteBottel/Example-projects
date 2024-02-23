package org.example.combine.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapMerge {

    /**
     * This is not effiecient merge but should be good enough to merge 2 maps together.
     *
     * @param inputA
     * @param inputB
     * @return
     */
    public static HashMap<String, Object> deepMerge(Map<String, Object> inputA, Map<String, Object> inputB) {
        var newMap = new HashMap<>(inputA);

        for (String key : inputB.keySet()) {
            var value2 = inputB.get(key);
            if (newMap.containsKey(key)) {
                Object value1 = newMap.get(key);
                if (value1 instanceof Map && value2 instanceof Map) {
                    newMap.put(key, deepMerge((Map<String, Object>) value1, (Map<String, Object>) value2));
                } else if (value1 instanceof List && value2 instanceof List) {
                    newMap.put(key, merge((List<Object>) value1, (List<Object>) value2));
                } else {
                    newMap.put(key, value2);
                }
            } else {
                newMap.put(key, value2);
            }
        }

        return newMap;
    }

    static List<Object> merge(List<Object> list1, List<Object> list2) {
        list2.removeAll(list1);
        list1.addAll(list2);
        return list1;
    }


}
