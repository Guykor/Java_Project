package filesprocessing.filters.Size;

import filesprocessing.Exceptions.IllegalFilterArgument;
import filesprocessing.filters.FilterStrategy;

abstract class SizeFilters implements FilterStrategy {
    static final int BYTE_TO_KB_RATIO = 1024;
    double size;
    public SizeFilters(){}
    public SizeFilters(double size){
        if (size < 0){
            throw new IllegalFilterArgument();
        }
        this.size = size;
    }
}
