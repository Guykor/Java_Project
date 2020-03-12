package filesprocessing.filters.utils.Property;

import java.io.File;
import java.util.function.Predicate;

public class Hidden extends PropertyFilter {

    public Hidden(boolean bool) {
        super(bool);
    }

    @Override
    public Predicate<File> getFilter() {
        return File::isHidden;
    }
}
