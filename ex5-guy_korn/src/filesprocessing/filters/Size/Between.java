package filesprocessing.filters.Size;

import filesprocessing.Exceptions.IllegalFilterArgument;

import java.io.File;
import java.util.function.Predicate;

public class Between extends SizeFilters {
    private double low;
    private double high;

    public Between(double l, double h) throws IllegalFilterArgument {
        if (l >= 0 && l <= h){
        this.low = l;
        this.high = h;
        } throw new IllegalFilterArgument();
    }

    @Override
    public Predicate<File> getFilter() {
        return x-> (x.length()/BYTE_TO_KB_RATIO >= this.low) && (x.length()/BYTE_TO_KB_RATIO <= this.high);
    }

}
