package filesprocessing.filters.Name;

import filesprocessing.Exceptions.IllegalFilterArgument;
import filesprocessing.filters.FilterStrategy;

abstract class NameFilter implements FilterStrategy {
    final String value;
    NameFilter(String val){
        this.value = val;
    }

    private void validateNameArgument(String arg) throws IllegalFilterArgument {
        if (!arg.matches("[A-Za-z0-9]+| ./_-")){//TODO: add more.
            throw new IllegalFilterArgument();
        }

    }
}
