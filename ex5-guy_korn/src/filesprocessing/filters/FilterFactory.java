package filesprocessing.filters;


//QUSTION - Singleton vs static.
//holds the exhaustive list of all filters supported. im using enumns to maintain the single choice principal
//the function will return a filter method, that than can be activated within the file collection.

//TODO: refactor general methods.

import filesprocessing.filters.utils.AllFilter;
import filesprocessing.filters.utils.Size.Between;
import filesprocessing.filters.utils.Size.GreaterThan;
import filesprocessing.filters.utils.Name.Contains;
import filesprocessing.filters.utils.Name.FileName;
import filesprocessing.filters.utils.Name.Prefix;
import filesprocessing.filters.utils.Name.Suffix;
import filesprocessing.filters.utils.Property.Executable;
import filesprocessing.filters.utils.Property.Hidden;
import filesprocessing.filters.utils.Property.Writable;
import filesprocessing.filters.utils.Size.SmallerThan;
import filesprocessing.orders.OrderFactory;

import java.io.File;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FilterFactory {

    private static FilterFactory ourInstance = new FilterFactory();
    public static FilterFactory getInstance() {
        return ourInstance;
    }
    private FilterFactory() {
    }


    private final String DELIMITER = "#";
    private final String NEGATION_SYMBOL = "NOT";
    private final String TRUE_SYMBOL = "YES";
    private final String FALSE_SYMBOL = "NO";
    boolean toNegate;

    public enum SupportedFilters {
        ALL(0), BETWEEN(2), CONTAINS, EXECUTABLE, FILE, GREATER_THAN, HIDDEN,
        PREFIX, SMALLER_THAN, SUFFIX, WRITABLE;
        private int argsRequired;
        SupportedFilters(){
            this.argsRequired = 1;
        }
        SupportedFilters (int numArgs){
            this.argsRequired = numArgs;
        }

        public int getArgsRequired() {
            return this.argsRequired;
        }
    }


    private SupportedFilters isSupported(String[] args) throws IllegalFilterArgument {
        String type = args[0];
        for (SupportedFilters supportedOperator : SupportedFilters.values()) {
            if (supportedOperator.name().toLowerCase().equals(type)) {
                if (args.length - 1 == supportedOperator.getArgsRequired()){
                return supportedOperator;
                }
            }
        }
        throw new IllegalFilterArgument();
    }

    //its here to parse every thing in the same place.
    private boolean strToBool(String s) throws IllegalFilterArgument {
        if (!TRUE_SYMBOL.equals(s) && !FALSE_SYMBOL.equals(s)) {
            throw new IllegalFilterArgument();
        }
        return TRUE_SYMBOL.equals(s);
    }


    private String handleSpecialCommands(String args) {
        toNegate = (args.endsWith(NEGATION_SYMBOL));
        if (toNegate) {
            return args.replace(NEGATION_SYMBOL, "");
        } else {
            return args;
        }
    }

    public Predicate<File> select(String command) {
        Predicate<File> output = null;
        String[] args = handleSpecialCommands(command).split(DELIMITER);
        SupportedFilters filterName = isSupported(args);
        switch (filterName) {
            case ALL:
                output = new AllFilter().getFilter();
                break;
            case GREATER_THAN:
                output = new GreaterThan(Double.parseDouble(args[1])).getFilter();
                break;
            case SMALLER_THAN:
                output = new SmallerThan(Double.parseDouble(args[1])).getFilter();
                break;
            case BETWEEN:
                output = new Between(Double.parseDouble(args[1]), Double.parseDouble(args[2])).getFilter();
                break;
            case FILE:
                output = new FileName(args[1]).getFilter();
                break;
            case CONTAINS:
                output = new Contains(args[1]).getFilter();
                break;
            case PREFIX:
                output = new Prefix(args[1]).getFilter();
                break;
            case SUFFIX:
                output = new Suffix(args[1]).getFilter();
                break;
            case HIDDEN:
                output = new Hidden(strToBool(args[1])).getFilter();
                break;
            case WRITABLE:
                output = new Writable(strToBool(args[1])).getFilter();
                break;
            case EXECUTABLE:
                output = new Executable(strToBool(args[1])).getFilter();
                break;
        }
            if (toNegate) {
                return output.negate();
            } else {
                return output;
            }
    }
}
