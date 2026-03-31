package autoservice.repair.functional;

@FunctionalInterface
public interface ObjectFormatter<T> {
    String format(T obj);
}
