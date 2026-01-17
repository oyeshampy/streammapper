package org.ps.safestreammapper.function;

/**
 * Represents a function that accepts one argument. It can produce a result or throw an exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception the function can throw
 *
 * @author Puneet Sharma
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result or throws an exception
     */
    R apply(T t) throws E;
}