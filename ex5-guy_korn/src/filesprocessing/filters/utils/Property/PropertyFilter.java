package filesprocessing.filters.utils.Property;

import filesprocessing.filters.utils.FilterStrategy;

public abstract class PropertyFilter implements FilterStrategy {
    private boolean flag;
    public PropertyFilter(boolean bool){
        flag = bool;
    }

}
