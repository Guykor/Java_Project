package filesprocessing.filters;


import java.io.File;
import java.util.function.Predicate;

public interface FilterStrategy{
    Predicate<File> getFilter();
}

