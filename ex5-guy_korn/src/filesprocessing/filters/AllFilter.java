package filesprocessing.filters;


import java.io.File;
import java.util.function.Predicate;

public class AllFilter implements FilterStrategy {
    @Override
    public Predicate<File> getFilter() {
        return x -> true;
    }
}
