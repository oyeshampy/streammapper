package org.ps.streammapper.mapping;

import org.ps.streammapper.function.ThrowingFunction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    @SuppressWarnings("unchecked")
    public static <T, R, E extends Exception> Function<T, Mapping<E, R>> of(ThrowingFunction<T, R, E> function) {
        return t -> {
            try {
                return new Mapping<>(null, function.apply(t));
            } catch (Exception e) {
                return new Mapping<>((E) e, null);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T, R, E extends Exception, F> Function<T, Mapping<F, R>> of(
            ThrowingFunction<T, R, E> function,
            Function<? super E, ? extends F> failureMapper
    ) {
        return t -> {
            try {
                return new Mapping<>(null, function.apply(t));
            } catch (Exception e) {
                return new Mapping<>(failureMapper.apply((E) e), null);
            }
        };
    }

    public static <E, R> Mapping<E, R> success(R value) {
        return new Mapping<>(null, value);
    }

    public static <E, R> Mapping<E, R> failure(E error) {
        return new Mapping<>(error, null);
    }

    public static <E, R> Predicate<Mapping<E, R>> onlySuccess() {
        return Mapping::isSuccess;
    }

    public static <E, R> Predicate<Mapping<E, R>> onlyFailure() {
        return Mapping::isFailure;
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

    public <U> Mapping<E, U> map(Function<? super R, ? extends U> mapper) {
        if (isFailure()) {
            return new Mapping<>(failure, null);
        }
        return new Mapping<>(null, mapper.apply(success));
    }

    public <U> Mapping<E, U> flatMap(Function<? super R, Mapping<E, U>> mapper) {
        if (isFailure()) {
            return new Mapping<>(failure, null);
        }
        return mapper.apply(success);
    }

    public Mapping<E, R> recover(Function<? super E, ? extends R> recoverer) {
        if (isFailure()) {
            return new Mapping<>(null, recoverer.apply(failure));
        }
        return this;
    }

    public Mapping<E, R> recoverWith(Function<? super E, Mapping<E, R>> recoverer) {
        if (isFailure()) {
            return recoverer.apply(failure);
        }
        return this;
    }

    public Mapping<E, R> ifSuccess(Consumer<? super R> consumer) {
        if (isSuccess()) {
            consumer.accept(success);
        }
        return this;
    }

    public Mapping<E, R> ifFailure(Consumer<? super E> consumer) {
        if (isFailure()) {
            consumer.accept(failure);
        }
        return this;
    }

    public Optional<R> toOptional() {
        return isSuccess() ? Optional.ofNullable(success) : Optional.empty();
    }

    public R orElse(R other) {
        return isSuccess() ? success : other;
    }

    public R orElseGet(Supplier<? extends R> supplier) {
        return isSuccess() ? success : supplier.get();
    }

    @Override
    public String toString() {
        if (isFailure()) {
            return "Failure(" + failure + ")";
        }
        return "Success(" + success + ")";
    }
}
