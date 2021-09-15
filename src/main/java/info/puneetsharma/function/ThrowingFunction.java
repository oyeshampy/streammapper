package info.puneetsharma.function;

public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}