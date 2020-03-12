package filesprocessing.filters.utils.Name;

import java.io.File;
import java.util.function.Predicate;

public class FileName extends NameFilter {

    public FileName(String val) {
        super(val);
    }

    public Predicate<File> getFilter() {
        return x-> x.getName().equals(this.value);
    }
}
