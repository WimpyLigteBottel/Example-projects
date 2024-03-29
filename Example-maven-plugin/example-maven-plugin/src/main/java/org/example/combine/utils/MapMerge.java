package org.example.combine.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapMerge {

    /**
     * Deep merge two maps. This method performs a recursive merge of the two maps.
     *
     * @param inputA The first map.
     * @param inputB The second map.
     * @return A new map containing the merged result.
     */
    public static HashMap<String, Object> deepMerge(Map<String, Object> inputA, Map<String, Object> inputB) {
        var newMap = new HashMap<>(inputA);

        for (String key : inputB.keySet()) {
            var value2 = inputB.get(key);

            if (!newMap.containsKey(key)) {
                newMap.put(key, value2);
                continue;
            }

            Object value1 = newMap.get(key);
            if (value1 instanceof Map && value2 instanceof Map) {
                Map<String, Object> inputATemp = (Map<String, Object>) value1;
                Map<String, Object> inputBTemp = (Map<String, Object>) value2;

                newMap.put(key, deepMerge(inputATemp, inputBTemp));
            } else if (value1 instanceof List a && value2 instanceof List b) {
                newMap.put(key, mergeList(a, b));
            } else if (value1 != null && value2 != null) {
                newMap.put(key, value2);
            }

        }

        return newMap;
    }

    static <T> List<T> mergeList(List<T> list1, List<T> list2) {
        list2.removeAll(list1);
        list1.addAll(list2);
        return list1;
    }
}
