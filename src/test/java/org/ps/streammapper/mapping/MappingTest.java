package org.ps.streammapper.mapping;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappingTest {

    @Test
    void of_success_creates_success_mapping() {
        Function<String, Mapping<Exception, Integer>> mapper = Mapping.of(Integer::parseInt);

        Mapping<Exception, Integer> result = mapper.apply("42");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals(42, result.getSuccess());
    }

    @Test
    void of_failure_captures_exception() {
        Function<String, Mapping<Exception, Integer>> mapper = Mapping.of(Integer::parseInt);

        Mapping<Exception, Integer> result = mapper.apply("not-a-number");

        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
        assertNotNull(result.getFailure());
    }

    @Test
    void map_and_flatMap_chain_success() {
        Mapping<Exception, Integer> result = Mapping.<Exception, Integer>success(2)
                .map(value -> value + 1)
                .flatMap(value -> Mapping.success(value * 2));

        assertTrue(result.isSuccess());
        assertEquals(6, result.getSuccess());
    }

    @Test
    void recover_and_recoverWith_handle_failure() {
        Mapping<Exception, Integer> recovered = Mapping.<Exception, Integer>failure(new Exception("boom"))
                .recover(exception -> 10);

        assertTrue(recovered.isSuccess());
        assertEquals(10, recovered.getSuccess());

        Mapping<Exception, Integer> recoveredWith = Mapping.<Exception, Integer>failure(new Exception("boom"))
                .recoverWith(exception -> Mapping.success(5));

        assertTrue(recoveredWith.isSuccess());
        assertEquals(5, recoveredWith.getSuccess());
    }

    @Test
    void ifSuccess_and_ifFailure_execute_side_effects() {
        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicBoolean failureCalled = new AtomicBoolean(false);

        Mapping.<Exception, Integer>success(1)
                .ifSuccess(value -> successCalled.set(true))
                .ifFailure(error -> failureCalled.set(true));

        assertTrue(successCalled.get());
        assertFalse(failureCalled.get());
    }

    @Test
    void optional_and_orElse_helpers_work() {
        Mapping<Exception, Integer> success = Mapping.success(7);
        Mapping<Exception, Integer> failure = Mapping.failure(new Exception("x"));

        Optional<Integer> present = success.toOptional();
        Optional<Integer> empty = failure.toOptional();

        assertTrue(present.isPresent());
        assertFalse(empty.isPresent());
        assertEquals(7, success.orElse(0));
        assertEquals(0, failure.orElse(0));
    }

    @Test
    void collectors_extract_successes_and_failures() {
        List<Mapping<Exception, Integer>> mappings = Stream.of(
                Mapping.<Exception, Integer>success(1),
                Mapping.<Exception, Integer>failure(new Exception("x")),
                Mapping.<Exception, Integer>success(2)
        ).collect(Collectors.toList());

        List<Integer> successes = mappings.stream().collect(MappingCollectors.successes());
        List<Exception> failures = mappings.stream().collect(MappingCollectors.failures());

        assertEquals(Arrays.asList(1, 2), successes);
        assertEquals(1, failures.size());
    }
}
