package org.ps.streammapper.mapping;

import org.ps.streammapper.function.ThrowingFunction;

import java.util.function.Function;

/**
 * @author Puneet Sharma
 */
public class Mapping<E, R> {

    private final E failure;
    private final R success;

    public Mapping(
            E failure,
            R success
    ) {
        this.failure = failure;
        this.success = success;
    }

    public static <T, R, E extends Exception> Function<T, Mapping<E, R>> of(ThrowingFunction<T, R, E> function) {
        return t -> {
            try {
                return new Mapping<>(null, function.apply(t));
            } catch (Exception e) {
                return new Mapping<>((E) e, null);
            }
        };
    }

    public E getFailure() {
        return failure;
    }

    public R getSuccess() {
        return success;
    }

    public boolean isFailure() {
        return failure != null;
    }

    public boolean isSuccess() {
        return success != null;
    }

    public String toString() {
        if (isFailure()) {
            return "Failure(" + failure + ")";
        }
        return "Success(" + success + ")";
    }
}
