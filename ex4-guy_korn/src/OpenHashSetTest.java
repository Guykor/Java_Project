import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class OpenHashSetTest {
    private OpenHashSet testSet;

    @Before
    public void createTestObjects() {
        testSet = new OpenHashSet();
    }

    //    TEST CONSTRUCTORS
    @Ignore //HEAVY!
    @Test
    public void testDataConstructor() {
        String[] stringsToAdd = Ex4Utils.file2array("data1.txt");
        testSet = new OpenHashSet(stringsToAdd);
    }
//TEST BASIC OPERATION AND EDGE CASES - empty, full, maximal and minimal bounds, resizing recursively.

    /**
     * simple test for the initial data members.
     */
    @Test
    public void testInitialDataMembers() {
        assertEquals(16, testSet.capacity());
        assertEquals(0, testSet.size());
        assertEquals(0.25f, testSet.getLowerLoadFactor(), 0.01);
        assertEquals(0.75f, testSet.getUpperLoadFactor(), 0.01);
    }

    /**
     * add twelve elements (with no assumptions or relying on their hash codes) to the test obj.
     */
    private void fillSetTwelveStrings() {
        String[] testStrings = new String[]{"a", "b", "c", "d", "f", "e", "g", "h", "i", "j", "k", "l"};
        for (String s : testStrings) {//add 3/4, should not cause resizing.
            testSet.add(s);
        }
    }

    /**
     * test that the set do not resize if upper bound has reached.
     */
    @Test
    public void testAddReachedThreshold() {
        fillSetTwelveStrings();
        assertEquals(12, testSet.size());
        assertEquals(16, testSet.capacity());
    }

    /**
     * adding one element above threshold, and checks the resize outcome.
     */
    @Test
    public void testAddSingleViolatesUpperThreshold() {
        fillSetTwelveStrings();
        testSet.add("m");//single violation
        assertEquals(13, testSet.size());
        assertEquals(32, testSet.capacity());
    }

    /**
     * adding four element in a loop above threshold to check if the resize functionality works.
     */
    @Test
    public void testAddMultipleViolatesUpperBound() {
        fillSetTwelveStrings();
        String[] testStrings = new String[]{"M", "N", "O", "P"};
        for (String s : testStrings) {
            testSet.add(s);
        }
        assertEquals(16, testSet.size());
        assertEquals(32, testSet.capacity());
    }

    /**
     * check basic deletion functionality.
     */
    @Test
    public void testDeleteMethod() {
        fillSetTwelveStrings();
        testSet.delete("a");
        assertFalse(testSet.contains("a"));
    }

    /**
     * helper method, delete 8 of the 12 strings added in the second helper function.
     */
    private void deleteEightStrings() {
        String[] stringsInSet = new String[]{"a", "b", "c", "d", "f", "e", "g", "h"};
        for (String s : stringsInSet) {
            testSet.delete(s);
        }
    }

    /**
     * test the resize functionality after deletion, within the load factor bound (equals to it,
     * specifically - 4/16 strings will remain).
     */
    @Test
    public void testDeleteOnLowerBound() {
        fillSetTwelveStrings(); //12 out of 16;
        deleteEightStrings(); //4 out of 16. now load factor equals to lower bound (1/4).
        assertEquals(4, testSet.size());
        assertEquals(16, testSet.capacity());
    }

    /**
     * tests an edge case, where multiple objects are requested to be deleted but none of them is in the
     * set. make sure your size method (counter) works properly.
     */
    @Test
    public void testDeletionOfElementsNotInSet() {
        deleteEightStrings();
        assertEquals(0, testSet.size());
        assertEquals(16, testSet.capacity());
    }

    /**
     * tests deletion that violates the lower bound of the load factor.
     */
    @Test
    public void testDeleteBelowLowerBound() {
        fillSetTwelveStrings();
        deleteEightStrings();
        testSet.delete("l"); // goes below threshold - 3 out of 16.
        assertEquals(3, testSet.size());
        assertEquals(8, testSet.capacity());
    }

    /**
     * test for basic operation on the set works and interact with each other.
     */
    @Test
    public void testThatBasicOperationsWorkd() {
        assertTrue(testSet.add("a"));
        assertTrue(testSet.contains("a"));
        assertFalse(testSet.add("a"));

        assertTrue(testSet.delete("a"));
        assertFalse(testSet.contains("a"));
        assertFalse(testSet.delete("a"));
    }

    /**
     * this test assume that resize won't take place if the elements to add are'nt really needed to be
     * added because their already in the array. Namely, the function will return before the resize call
     * will be executed. The same with deletion.
     */
    @Test
    public void violateThresholdWithElementsThatWontBeAdded() {
        //ADDITION CHECK
        fillSetTwelveStrings();
        assertEquals(12, testSet.size());
        assertEquals(16, testSet.capacity());
        fillSetTwelveStrings();
        assertEquals(12, testSet.size());
        assertEquals(16, testSet.capacity());

        //DELETION CHECK
        deleteEightStrings();
        assertEquals(4, testSet.size());
        assertEquals(16, testSet.capacity());
        deleteEightStrings();
        assertEquals(4, testSet.size());
        assertEquals(16, testSet.capacity());
    }

    /**
     * resizing test for a set with maximal load factor bound [0,1]. Basically I assume here that the
     * resizing part should take place before the element that surpass the upper bound has been added. it's
     * only matter in closed set, when adding beyond the set capacity (meaning that even when the load
     * factor hits 1 the addition still works and returns True).
     */
    @Test
    public void testResizingForMaximalBounds() {
        this.testSet = new OpenHashSet(1, 0);
        fillSetTwelveStrings();//12 elements, we'll add 4 more.
        String[] fourMore = new String[]{"m", "n", "o", "p"};//assuming the resizing works immediately after
        // deletion and adding of an item, and only if the bound has passed (strong inequality)
        for (String s : fourMore) {
            testSet.add(s);
        }
        assertEquals(16, testSet.capacity());
        assertEquals(16, testSet.size());

        assertTrue(testSet.add("violationString!"));
        assertEquals(32, testSet.capacity());
        assertEquals(17, testSet.size());

        //Now we delete enough (17 elements) to return below the lower bound, but table should'nt shrink
        // because the lower bound is zero (no way to violate it).
        for (String s : new String[]{"a", "b", "c", "d", "f", "e", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "violationString!"}) {
            testSet.delete(s);
        }
        assertEquals(32, testSet.capacity());
        assertEquals(0, testSet.size());
    }

    /**
     * checks the resize functionality for minimal bounds.
     * Attention - might cause massive runtime due to a bug.
     */
    @Test
    public void testResizingForMinimalBounds() {
        testSet = new OpenHashSet(0, 0);
        fillSetTwelveStrings();
        assertEquals(12, testSet.size());
        assertEquals(65536, testSet.capacity());
    }

    /**
     * test various input edge cases.
     */
    @Test
    public void testEdgeCaseInput() {
        assertTrue(testSet.add(""));
        assertTrue(testSet.add("KKKKKKKKKKKKKKKKKKK"));
        assertTrue(testSet.add("---1-1--11-1-13424"));
        assertTrue(testSet.add(null));


        assertTrue(testSet.delete(""));
        assertTrue(testSet.delete("KKKKKKKKKKKKKKKKKKK"));
        assertTrue(testSet.delete("---1-1--11-1-13424"));
        assertFalse(testSet.delete(null));
    }

    /**
     * runs some few failed operations and make sure that the set size and capacity has not changed.
     */
    @Test
    public void isolateResizingFromFailingOperations() {
        testSet.add(null);
        assertEquals(16, testSet.capacity());
        assertEquals(0, testSet.size());
        testSet.delete("5");
        assertEquals(16, testSet.capacity()); //ATTENTION! deleting 0 elements doe's not suppose  to
        // cause resizing!
        assertEquals(0, testSet.size());

        testSet.contains("hello");
        assertEquals(16, testSet.capacity());
        assertEquals(0, testSet.size());
    }
}
