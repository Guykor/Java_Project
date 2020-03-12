import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * This class is a performance analyser that compare the running times of several operations of type
 * SimpleSet.
 * Namely, this class is hardcoded to compare between OpenHashSet, CloseHashSet, and TreeSet,
 * LinkedList, HashSet - three of java collections wrapped by facade class that implements SimpleSet as well.
 * the class allows to pick which test to preform from those implemented and prints it's results.
 */
public class SimpleSetPerformanceAnalyzer {
    /**
     * path for the Data sets used for the analysis.
     */
    private static final String SAME_HASH_DATASET = "data1.txt", DIFFERENT_HASH_DATASET = "data2.txt";
    /**
     * Test Numbers used for the check of user test choice.
     */
    private static final String TEST1 = "1", TEST2 = "2", TEST3 = "3", TEST4 = "4", TEST5 = "5", TEST6 = "6";
    /**
     * Names Array for the data structured used in the analysis, corresponding with the order of appearance
     * in the actual array of data structures. used for printing results.
     */
    private static String[] SET_TYPES = new String[]{"OpenHashSet", "ClosedHashSet", "TreeSet", "LinkedList",
            "HashSet"};
    /**
     * Number of iterations required for low complexity operation. Concretely, this number represent how
     * much times to call contains operations during warm up and the actual test.
     */
    private static int NUMBER_OF_CONTAINS_ITERATION = 70000;
    /**
     * Array of SimpleSet, used to contain initialized data structures with data from the same hash data
     * set (data1.txt)
     */
    private static SimpleSet[] data1HashSets;
    /**
     * Array of SimpleSet, used to contain initialized data structures with data from the different hash data
     * set (data2.txt)
     */
    private static SimpleSet[] data2HashSets;


    /**
     * this method initialized the test arrays with the relevant data structures to test, all of them of
     * type SimpleSet, while the java collections are wrapped with CollectionFacadeSet to allow it.
     * this method will initialize the arrays and copy them into any of the data members test arrays.
     */
    private static SimpleSet[] resetTestArrays() {
        SimpleSet[] testSets = new SimpleSet[5];
        testSets[0] = new OpenHashSet();
        testSets[1] = new ClosedHashSet();
        testSets[2] = new CollectionFacadeSet(new TreeSet<String>());
        testSets[3] = new CollectionFacadeSet(new LinkedList<String>());
        testSets[4] = new CollectionFacadeSet(new HashSet<String>());
        return testSets;
    }


    /**
     * this method uses a helper to read all Strings from a file.
     *
     * @param path Strings file path
     * @return array of words from the file.
     */
    private static String[] getStringArray(String path) {
        return Ex4Utils.file2array(path);
    }

    /**
     * this method will print an updating progress bar of the data insertion process.
     *
     * @param i - number of iteration in the insertion process.
     */
    private static void progressBar(int i) {
        int DATA_SET_LENGTH = 99999;
        int status = (int) ((i / (double) DATA_SET_LENGTH) * 100);
        System.out.print("\rProgress: " + status + "%");
    }

    /**
     * this method takes an array of strings an insert it to a given data structure using it's SimpleSet
     * Interface.
     *
     * @param testSet    - specific data structure.
     * @param dataSource path for the data set file.
     */
    private static void addDataToTestArray(SimpleSet testSet, String dataSource) {
        String[] data = getStringArray(dataSource);
        if (data == null) {
            return;
        }
        int counter = 0;
        for (String entry : data) {
            testSet.add(entry);
            counter++;
            if ((counter % 10000) == 0) {
                progressBar(counter);
            }
        }
    }

    /**
     * TEST METHOD - will measure in milliseconds the longevity of the insertion process given a test array
     * type (data1 - same hash strings, data2 - different hash strings). after this method is called, the
     * relevant data members that holds those array should be filled with data.
     *
     * @param dataSource path for the dataset file.
     */
    private static void measureDataInsertion(String dataSource) {
        int NANOSECOND_TO_MILLI_FACTOR = 1000000;
        SimpleSet[] testArray;
        if (dataSource.equals(SAME_HASH_DATASET)) {
            data1HashSets = resetTestArrays();
            testArray = data1HashSets;
        } else {
            data2HashSets = resetTestArrays();
            testArray = data2HashSets;
        }

        System.out.println("\nTest: measure adding 99,999 elements");
        for (int i = 0; i < 5; i++) {
            long timeBefore = System.nanoTime();
            addDataToTestArray(testArray[i], dataSource);
            long differenceInMilliSec = (System.nanoTime() - timeBefore) / NANOSECOND_TO_MILLI_FACTOR;
            System.out.print("\r** Result : Data insertion from " + dataSource + " to " +
                    SET_TYPES[i] + " took " + differenceInMilliSec +
                    " milliseconds **\n");
        }
    }

    /**
     * loop uses for the contains test method.
     *
     * @param set         a given data structure implementing SimpleSet
     * @param stringInput String to search.
     */
    private static void callContains7kTimes(SimpleSet set, String stringInput) {
        int counter = 0;
        while (counter < NUMBER_OF_CONTAINS_ITERATION) {
            set.contains(stringInput);
            counter++;
        }
    }

    /**
     * TEST METHOD - preform the contains analysis comparison between data structures and prints the results.
     * this method relies on the test arrays data members that should be instantiated in the measure
     * insertion test method or by the main method.
     * Important - the contains method has a low running time, so to be more precise this test will warm up
     * the compiler for 7k iteration (excluding linked list) and only than will preform another measured 7k
     * iterations. the result of this test will be the running time divided by 7k (i.e mean for single
     * iteration) in nanoseconds.
     *
     * @param stringInput    input to search
     * @param setsDataSource - path to the data set file.
     */
    private static void containsAnalysis(String stringInput, String setsDataSource) {
        System.out.println("\nTest: measure Contain for String " + stringInput);
        System.out.println("Result for dataset: " + setsDataSource);
        SimpleSet[] testArray;
        if (setsDataSource.equals(SAME_HASH_DATASET)) {
            testArray = data1HashSets;
        } else {
            testArray = data2HashSets;
        }
        for (int i = 0; i < 5; i++) {
            if (!(testArray[i] instanceof LinkedList)) {
                callContains7kTimes(testArray[i], stringInput);
            }
            long timeBefore = System.nanoTime();
            callContains7kTimes(testArray[i], stringInput);
            long difference = System.nanoTime() - timeBefore;
            long differencePerIteration = difference / NUMBER_OF_CONTAINS_ITERATION;
            System.out.println(" contains " + stringInput + " in " + SET_TYPES[i] + " returned: " +
                    testArray[i].contains(stringInput) + " " + "took " + differencePerIteration + " " +
                    "nanoseconds.");
        }
    }

    /**
     * this helper method will print a menu on the console to help choose the test to preform.
     *
     * @return String provided by the user in console.
     */
    private static String manageTests() {
        System.out.println("\nEnter the test number you'd like to preform: \n");
        System.out.println("1. Measure Data insertion for data1.txt.\n2. Measure Data " +
                "insertion for data2.txt.\n3. Contains hi' data1.txt.\n4. Contains 'hi' data2.txt.\n5" +
                ". Contains '-13170890158' for data1.txt.\n6. Contains '23' for data2.txt\n");
        System.out.println("Enter input:");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    /**
     * builder method for the test arrays data members filled with relevant data according to test
     * requested from user. will be called only if contains test has called and the test array is not ready
     * to be tested.
     */
    private static void buildTestSets() {
        System.out.println("building testing data sets");
        data1HashSets = resetTestArrays();
        data2HashSets = resetTestArrays();
        for (int i = 0; i < 5; i++) {
            addDataToTestArray(data1HashSets[i], SAME_HASH_DATASET);
            addDataToTestArray(data2HashSets[i], DIFFERENT_HASH_DATASET);
        }
    }

    /**
     * runner method, will present a menu to pick test, and will manage the test accordingly.
     */
    public static void main(String[] args) {
        String userChoice = manageTests();
        if (userChoice.contains(TEST1)) {
            measureDataInsertion(SAME_HASH_DATASET);
        }
        if (userChoice.contains(TEST2)) {
            measureDataInsertion(DIFFERENT_HASH_DATASET);
        }
        if (!userChoice.contains(TEST1) && !userChoice.contains(TEST2)) {
            buildTestSets();
        }
        if (userChoice.contains(TEST3)) {
            containsAnalysis("hi", SAME_HASH_DATASET);
        }
        if (userChoice.contains(TEST4)) {
            containsAnalysis("hi", DIFFERENT_HASH_DATASET);
        }
        if (userChoice.contains(TEST5)) {
            containsAnalysis("-13170890158", SAME_HASH_DATASET);
        }
        if (userChoice.contains(TEST6)) {
            containsAnalysis("23", DIFFERENT_HASH_DATASET);
        }
    }
}

