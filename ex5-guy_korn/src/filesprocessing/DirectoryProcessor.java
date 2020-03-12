package filesprocessing;


import filesprocessing.inputhandler.InputExceptions.CommandsFileError;
import filesprocessing.inputhandler.InputExceptions.FilterSubSectionException;
import filesprocessing.inputhandler.InputExceptions.OrderSubSectionException;
import filesprocessing.inputhandler.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DirectoryProcessor {
    private static final String TYPE_2_ERROR_FORMAT = "ERROR: %s\n";

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException();
            }
            File[] sourceDir = Parser.getSourceDir(args[0]);
            ArrayList<Section> operatingSections = Parser.getSections(args[1]);
            for (Section section : operatingSections) {
                section.operate(sourceDir);
            }
        } catch (CommandsFileError e) {
            if (e instanceof FilterSubSectionException){
            System.err.println(String.format(TYPE_2_ERROR_FORMAT, "FILTER sub-section missing."));}
            else if (e instanceof OrderSubSectionException){
                System.err.println(String.format(TYPE_2_ERROR_FORMAT, "ORDER sub-section missing"));
            }

        } catch (IOException e) {
            System.err.println(String.format(TYPE_2_ERROR_FORMAT, "I/O problem occurred while accessing " +
                    "supplied paths."));
        } catch (IllegalArgumentException e) {
            System.err.println(String.format(TYPE_2_ERROR_FORMAT, "Invalid argument"));
    }
}
}
