package info.puneetsharma.graceful.trycatch;

import info.puneetsharma.function.ThrowingFunction;

import java.util.function.Function;

public class Try<E, R> {

    private E failure;
    private R success;

    public Try(
            E failure,
            R success
    ) {
        this.failure = failure;
        this.success = success;
    }

    public static <T, R, E extends Exception> Function<T, Try<E, R>> of(ThrowingFunction<T, R, E> function) {
        return t -> {
            try {
                return new Try<>(null, function.apply(t));
            } catch (Exception e) {
                return new Try<>((E) e, null);
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
