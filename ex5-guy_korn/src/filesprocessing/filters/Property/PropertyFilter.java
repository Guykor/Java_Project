package filesprocessing.filters.Property;

import filesprocessing.filters.FilterStrategy;

public abstract class PropertyFilter implements FilterStrategy {
    private boolean flag;
    public PropertyFilter(boolean bool){
        flag = bool;
    }

}
