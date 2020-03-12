package filesprocessing.filters.utils.Name;

import java.io.File;
import java.util.function.Predicate;

public class Contains extends NameFilter {

    public Contains(String val) {
        super(val);
    }

    @Override
    public Predicate<File> getFilter() {
        return x->x.getName().contains(this.value);
    }
}
