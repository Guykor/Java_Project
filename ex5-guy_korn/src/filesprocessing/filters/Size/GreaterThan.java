package filesprocessing.filters.Size;


import java.io.File;
import java.util.function.Predicate;


public class GreaterThan extends SizeFilters{
    public GreaterThan(double size) {
        super(size);
    }
    public Predicate<File> getFilter(){
        return x -> x.length()/BYTE_TO_KB_RATIO > size;
    }

    }
