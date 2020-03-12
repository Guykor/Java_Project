package filesprocessing.filters.utils.Name;

import java.io.File;
import java.util.function.Predicate;

public class Prefix extends NameFilter {
    public Prefix(String val) {
        super(val);
    }

    @Override
    public Predicate<File> getFilter() {
        return x->x.getName().startsWith(value);
    }
}
