package filesprocessing.filters.Name;

import java.io.File;
import java.util.function.Predicate;

public class Suffix extends NameFilter {
    public Suffix(String val) {
        super(val);
    }

    @Override
    public Predicate<File> getFilter() {
        return x->x.getName().endsWith(this.value);
    }
}
