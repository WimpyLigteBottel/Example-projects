package org.example.combine.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapMergeTest {

    @Test
    @DisplayName("very basic merge between a and b")
    void deepMerge() {
        var a = new HashMap<String, Object>();
        a.put("a", "A");
        var b = new HashMap<String, Object>();
        b.put("b", "B");


        var actual = MapMerge.deepMerge(a, b);


        var expected = new HashMap<>();
        expected.putAll(a);
        expected.putAll(b);


        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("more complex merge between a and b")
    void moreComplexDeepMerge() {
        var a = new HashMap<String, Object>();
        a.put("0",
                Map.of("1A",
                        Map.of("2A",
                                Map.of("3A", Map.of())
                        )
                )
        );

        var b = new HashMap<String, Object>();
        b.put("0",
                Map.of("1B",
                        Map.of("2B", Map.of())
                )
        );


        var actual = MapMerge.deepMerge(a, b);


        var expected = new HashMap<>();
        expected.put("0",
                Map.of(
                        "1A", Map.of("2A", Map.of("3A", Map.of())),
                        "1B", Map.of("2B", Map.of())
                )
        );


        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("end keys ends with value Strings")
    void moreComplexDeepMergeStringAtEnd() {
        var a = new HashMap<String, Object>();
        a.put("0",
                Map.of("1A",
                        Map.of("2A",
                                Map.of(
                                        "a", "random 1",
                                        "b", "random 2",
                                        "c", "random 3"
                                )
                        )
                )
        );

        var b = new HashMap<String, Object>();
        b.put("0",
                Map.of("1B",
                        Map.of("2B", Map.of(
                                        "a", "random 1",
                                        "b", "random 2",
                                        "c", "random 3"
                                )
                        )
                )
        );

        var actual = MapMerge.deepMerge(a, b);


        var expected = new HashMap<>();
        expected.put("0",
                Map.of("1A", Map.of("2A", Map.of(
                                "a", "random 1",
                                "b", "random 2",
                                "c", "random 3"
                        )),
                        "1B", Map.of("2B", Map.of(
                                        "a", "random 1",
                                        "b", "random 2",
                                        "c", "random 3"
                                )
                        )
                )
        );


        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("newMap should update oldMap")
    void complexMergeWhereLaterMapOverwriteEalierValues() {
        var oldMap = new HashMap<String, Object>();
        oldMap.put("0",
                Map.of("1A",
                        Map.of("2A",
                                Map.of(
                                        "a", "random 1",
                                        "b", "random 2",
                                        "c", "random 3"
                                )
                        )
                )
        );

        var newMap = new HashMap<String, Object>();
        newMap.put("0",
                Map.of("1A",
                        Map.of("2A", Map.of(
                                        "a", "1",
                                        "b", "random 2",

                                        "c", "3"
                                )
                        )
                )
        );

        var actual = MapMerge.deepMerge(oldMap, newMap);


        var expected = new HashMap<>();
        expected.put("0",
                Map.of("1A", Map.of("2A", Map.of(
                                "a", "1",
                                "b", "random 2",
                                "c", "3"
                        ))
                )
        );


        assertEquals(expected, actual);
    }


}