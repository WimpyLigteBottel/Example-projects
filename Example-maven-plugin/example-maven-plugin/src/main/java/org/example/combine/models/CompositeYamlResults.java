package org.example.combine.models;

import java.util.Map;

public record CompositeYamlResults(
        Map<Object, Object> mapA,
        Map<Object, Object> mapB,
        Map<Object, Object> mapCombined
) {
}