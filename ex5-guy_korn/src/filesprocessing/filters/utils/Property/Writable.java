package filesprocessing.filters.utils.Property;
import java.io.File;
import java.util.function.Predicate;

public class Writable extends PropertyFilter {

    public Writable(boolean bool) {
        super(bool);
    }

    @Override
    public Predicate<File> getFilter() {
        return File::canWrite;
    }
}
