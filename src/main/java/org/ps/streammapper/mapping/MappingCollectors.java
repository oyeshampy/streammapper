package org.ps.streammapper.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collectors for success/failure extraction from Mapping streams.
 */
public final class MappingCollectors {

    private MappingCollectors() {
    }

    public static <E, R> Collector<Mapping<E, R>, ?, List<R>> successes() {
        return Collector.of(
                (Supplier<List<R>>) ArrayList::new,
                (list, mapping) -> {
                    if (mapping.isSuccess()) {
                        list.add(mapping.getSuccess());
                    }
                },
                (left, right) -> {
                    left.addAll(right);
                    return left;
                }
        );
    }

    public static <E, R> Collector<Mapping<E, R>, ?, List<E>> failures() {
        return Collector.of(
                (Supplier<List<E>>) ArrayList::new,
                (list, mapping) -> {
                    if (mapping.isFailure()) {
                        list.add(mapping.getFailure());
                    }
                },
                (left, right) -> {
                    left.addAll(right);
                    return left;
                }
        );
    }
}
