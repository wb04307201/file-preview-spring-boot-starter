package cn.wubo.file.preview.function;

@FunctionalInterface
public interface PreviewFunction<T, U, O, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param o the thirty function argument
     * @return the function result
     */
    R apply(T t, U u, O o);
}
