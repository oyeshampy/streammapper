# streammapper

streammapper is a tiny Java utility for safely mapping streams when your
`map()` function can throw. It wraps each mapped item in a `Mapping`, capturing
either the successful value or the thrown exception so you can continue
processing without breaking the stream.

This project was created specifically to handle exceptions in stream mapping
while keeping the successes and failures visible and easy to inspect.

## How it works

- Successes are wrapped as `Mapping(null, result)`
- Failures are wrapped as `Mapping(exception, null)`

## Usage

```java
import org.ps.streammapper.mapping.Mapping;
import org.ps.streammapper.function.ThrowingFunction;

List<Mapping<Exception, Integer>> result = inputs.stream()
    .map(Mapping.of((String s) -> Integer.parseInt(s)))
    .toList();
```

## API

- `ThrowingFunction<T, R, E extends Exception>`: a functional interface that
  allows checked exceptions.
- `Mapping.of(ThrowingFunction)`: turns a throwing function into a safe mapper
  for streams.
- `Mapping.getSuccess()`, `Mapping.getFailure()`, `isSuccess()`, `isFailure()`
  for downstream handling.
