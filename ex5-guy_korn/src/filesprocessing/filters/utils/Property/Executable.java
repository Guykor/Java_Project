package filesprocessing.filters.utils.Property;

import java.io.File;
import java.util.function.Predicate;

public class Executable extends PropertyFilter {
    public Executable(boolean bool) {
        super(bool);
    }

    @Override
    public Predicate<File> getFilter() {
        return File::canExecute;
    }
}
