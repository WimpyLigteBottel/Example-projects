package org.example.combine.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapMergeTest {

    Yaml YAML = new Yaml();


    @Test
    @DisplayName("very basic merge between a and b test")
    void deepMerge_2() {

        var mapA = YAML.loadAs("""
                a: "value1"
                """, HashMap.class);
        var mapB = YAML.loadAs("""
                b: "value2"
                """, HashMap.class);

        var actual = MapMerge.deepMerge(mapA, mapB);


        assertEquals("value1", actual.get("a"));
        assertEquals("value2", actual.get("b"));
    }


    @Test
    @DisplayName("more complex merge between a and b")
    void moreComplexDeepMerge() {

        HashMap<String, Object> a = YAML.loadAs("""
                a:
                    1A:
                        2A:
                            3A: ""
                """, HashMap.class);


        HashMap<String, Object> b = YAML.loadAs("""
                a:
                    1B:
                        2B: ""
                """, HashMap.class);


        var actual = MapMerge.deepMerge(a, b);


        var expected = new HashMap<>();
        expected.put("a",
                Map.of(
                        "1A", Map.of("2A", Map.of("3A", "")),
                        "1B", Map.of("2B", "")
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