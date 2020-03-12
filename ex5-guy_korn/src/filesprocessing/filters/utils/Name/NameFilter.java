package filesprocessing.filters.utils.Name;

import filesprocessing.filters.utils.FilterStrategy;


abstract class NameFilter implements FilterStrategy{
    final String value;

    NameFilter(String val) {
        this.value = val;
    }
}