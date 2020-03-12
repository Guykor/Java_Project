import oop.ex3.spaceship.Item;
import oop.ex3.spaceship.ItemFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This is test class for the Long Term Storage Class.
 */
public class LongTermTest {
    private static final int FAIL = -1;
    private static final int LTS_SUCCESS = 1;
    private static Item baseballBat;
    private static Item bigHelmet;
    /**
     * test LongTermStorage object.
     */
    private LongTermStorage testLts;
    private Map<String, Integer> expectedMap;

    @BeforeClass
    public static void testItems() {
        baseballBat = ItemFactory.createSingleItem("baseball bat"); // 2 units
        bigHelmet = ItemFactory.createSingleItem("helmet, size 3"); // 5 units
    }

    @Before
    public void createLocker() {
        this.testLts = new LongTermStorage();
        expectedMap = new HashMap<String, Integer>();
        Locker.longTermStorage.resetInventory();
    }


    /**
     * GENERAL AND CONSTRUCTOR TESTS
     */
    @Test()
    public void doResetWorks() {
        this.testLts.addItem(baseballBat, 100);
        this.testLts = new LongTermStorage();
        assertTrue(testLts.getAvailableCapacity() == 1000);
        this.testLts.addItem(baseballBat, 100);
        this.testLts.resetInventory();
        assertTrue(testLts.getAvailableCapacity() == 1000);
    }

    @Test
    public void checkNullItemInput() {
        assertEquals(FAIL, this.testLts.addItem(null, 0));
        assertEquals(FAIL, this.testLts.addItem(null, 1));
        assertEquals(FAIL, this.testLts.addItem(null, -1));
        assertEquals(1000, this.testLts.getAvailableCapacity());
    }


    /**
     * INTEGRATION
     */
    @Test
    public void checkIf80PercentWereTransferred() {
        Locker testLocker = new Locker(10);
        testLocker.addItem(baseballBat, 3); //2 storage unit per bat, so 2 bats should move to lts.
        expectedMap.put(baseballBat.getType(), 2);
        assertEquals(expectedMap, Locker.longTermStorage.getInventory());
        assertEquals(996, Locker.longTermStorage.getAvailableCapacity());
        assertEquals(2, Locker.longTermStorage.getItemCount(baseballBat.getType()));
    }

    /**
     * ADD ITEM TESTS.
     */
    @Test
    public void addSingleItem() {
        assertEquals("basic case, adding item when there's room", LTS_SUCCESS,
                this.testLts.addItem(baseballBat, 1));
    }

    @Test
    public void addZeroItemsToFullLocker() {
        this.testLts.addItem(bigHelmet, 200);
        assertEquals("add 0 items to full inventory", LTS_SUCCESS,
                this.testLts.addItem(baseballBat, 0));
    }

    @Test
    public void addBeyondCapacity() {
        assertEquals("try to add at once when too over capacity", FAIL,
                this.testLts.addItem(bigHelmet, 250));
    }

    @Test
    public void addBeyondCapacityEffect() {
        this.testLts.addItem(bigHelmet, 200);
        this.testLts.addItem(baseballBat, 5);
        assertEquals("make sure that the inventory havn't changed", 200,
                this.testLts.getItemCount(bigHelmet.getType()));
        assertEquals("make sure that the inventory havn't changed", 0,
                this.testLts.getItemCount(baseballBat.getType()));
    }

    @Test
    public void addToExistingTypeBeyondCapacity() {
        this.testLts.addItem(bigHelmet, 200);
        assertEquals("add to already existing entry in inventory such that the addition exceed " +
                "capacity", FAIL, this.testLts.addItem(bigHelmet, 1));
    }

    @Test
    public void addingEffectOnInventory() {
        this.testLts.addItem(baseballBat, 2);
        assertEquals("check if adding took place the amount of items in inventory", 2,
                this.testLts.getItemCount(baseballBat.getType()));
        assertEquals("check if adding took place on available place", 996,
                this.testLts.getAvailableCapacity());
    }

    @Test
    public void addNonValidNumber() {
        assertEquals("inserting negative number of items to store", FAIL,
                this.testLts.addItem(baseballBat, -1));
        assertEquals("inserting zero items", LTS_SUCCESS,
                this.testLts.addItem(baseballBat, 0));
        assertEquals("validate that nothing has changed", 0,
                this.testLts.getItemCount(baseballBat.getType()));
    }


    /**
     * GET ITEM COUNT TESTS
     */
    @Test
    public void countEmptyLocker() {
        assertEquals("check for empty LTS", 0,
                this.testLts.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countUnvalidInput() {
        assertEquals("non valid input-string", 0,
                this.testLts.getItemCount("53jk2-"));
    }

    @Test
    public void countAfterAddingItems() {
        this.testLts.addItem(baseballBat, 1);
        assertEquals("check if an item was added to count", 1,
                this.testLts.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countAfterFailedAddition() {
        this.testLts.addItem(bigHelmet, 200);
        this.testLts.addItem(bigHelmet, 1);
        assertEquals("check if the addition did nothing", 200,
                this.testLts.getItemCount(bigHelmet.getType()));
    }

    /**
     * GET INVENTORY TESTS
     */
    @Test
    public void emptyInventory() {
        assertEquals("map an empty locker", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkWhenLockerIsFull() {
        this.testLts.addItem(bigHelmet, 200);
        expectedMap.put(bigHelmet.getType(), 200);
        assertEquals("basic check for a full locker", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkAfterModifing() {
        this.testLts.addItem(bigHelmet, 155);
        this.testLts.addItem(bigHelmet, 5);
        expectedMap.put(bigHelmet.getType(), 160);
        assertEquals("after adding to an existing item", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkAfterZeroAdditionEmptyLocker() {
        this.testLts.addItem(baseballBat, 0);
        assertEquals("zero addition to empty locker", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkZeroAdditionToExisting() {
        this.testLts.addItem(baseballBat, 1);
        this.testLts.addItem(baseballBat, 0);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("zero addition to an existing item", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkAfterNegativeAdditionEmptyLocker() {
        this.testLts.addItem(baseballBat, -1);
        assertEquals("negative addition to empty locker", expectedMap,
                this.testLts.getInventory());
    }

    @Test
    public void checkNegativeAdditionToExisting() {
        this.testLts.addItem(baseballBat, 1);
        this.testLts.addItem(baseballBat, -1);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("negative addition to an existing item", expectedMap,
                this.testLts.getInventory());
    }


    /**
     * GET CAPACITY TESTS
     */

    @Test
    public void nothingChangesCapacity() {
        this.testLts.addItem(baseballBat, 1);//basic addition
        assertEquals(1000, this.testLts.getCapacity());
        this.testLts.addItem(baseballBat, 2); //LTS
        assertEquals(1000, this.testLts.getCapacity());
        this.testLts.addItem(baseballBat, 7); //failed addition
        assertEquals(1000, this.testLts.getCapacity());
        this.testLts.getAvailableCapacity();// check using the capacity field
        assertEquals(1000, this.testLts.getCapacity());
        this.testLts.getItemCount(baseballBat.getType());
        assertEquals(1000, this.testLts.getCapacity());
    }

    /**
     * GET AVAILABLE CAPACITY TESTS
     * notice that testing the method when LTS is full appears in another test imn this class.
     */
    @Test
    public void availbleCapacityTrivialTests() {
        assertEquals(1000, this.testLts.getAvailableCapacity());
        this.testLts.addItem(baseballBat, 1);//basic addition, 2 unit storage
        assertEquals(998, this.testLts.getAvailableCapacity());
        this.testLts.resetInventory();
        this.testLts.addItem(bigHelmet, 200);//fill locker
        assertEquals(0, this.testLts.getAvailableCapacity());
        this.testLts.addItem(bigHelmet, 1);//failed addition
        assertEquals(0, this.testLts.getAvailableCapacity());
    }
}


