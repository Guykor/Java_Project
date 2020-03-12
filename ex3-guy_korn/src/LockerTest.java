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
 * This is test class for the Locker Class.
 * TESTING ASSUMPTIONS:
 * * fixed threshold to initiate LTS transfer - at most 0.5 of the same type occupies the Locker inventory.
 * * Transfer to Lts is made in order to keep at most 0.2 of the item type in the Locker.
 */
public class LockerTest {
    private static final int FAIL = -1;
    private static final int SUCCESS = 0;
    private static final int LTS_TRANSFER = 1;
    private static final int CONTRADICTION_FAILURE = -2;
    private static Item baseballBat;
    private static Item bigHelmet;
    private static Item smallHelmet;
    private static Item footBall;
    /**
     * test Locker with an even capacity number
     */
    private Locker testLocker1;
    /**
     * test Locker with an odd capacity number
     */
    private Locker testLocker2;
    private Map<String, Integer> expectedMap;

    @BeforeClass
    public static void testItems() {
        baseballBat = ItemFactory.createSingleItem("baseball bat"); // 2 units
        bigHelmet = ItemFactory.createSingleItem("helmet, size 3"); // 5 units
        smallHelmet = ItemFactory.createSingleItem("helmet, size 1"); // 3 units
        footBall = ItemFactory.createSingleItem("football"); // 4 units.
    }

    @Before
    public void createLocker() {
        this.testLocker1 = new Locker(10);
        this.testLocker2 = new Locker(11);
        expectedMap = new HashMap<String, Integer>();
        Locker.longTermStorage.resetInventory();

    }


    /**
     * CONSTURCTOR TESTS
     */
    @Test()
    public void negativeCapacity() {
        Locker unvalidLocker = new Locker(-1);
        assertTrue(unvalidLocker.getCapacity() == Locker.DEFAULT_CAPACITY);
    }

    /**
     * TEST ALL METHODS FOR NULL ITEMS
     */
    @Test
    public void checkNullItemInput() {
        assertEquals(FAIL, this.testLocker1.addItem(null, 0));
        assertEquals(FAIL, this.testLocker1.addItem(null, 1));
        assertEquals(FAIL, this.testLocker1.addItem(null, -1));
        assertEquals(10, this.testLocker1.getAvailableCapacity());

        assertEquals(FAIL, this.testLocker1.removeItem(null, 0));
        assertEquals(FAIL, this.testLocker1.removeItem(null, 1));
        assertEquals(FAIL, this.testLocker1.removeItem(null, -1));
        assertEquals(10, this.testLocker1.getAvailableCapacity());
    }


    /**
     * ADD ITEM TESTS.
     */
    @Test
    public void addSingleItem() {
        assertEquals("basic case, adding item when there's room", SUCCESS,
                this.testLocker1.addItem(baseballBat, 1));
    }

    @Test
    public void fillLockerBelowOccupationThreshold() {
        this.testLocker1.addItem(bigHelmet, 1);
        this.testLocker1.addItem(smallHelmet, 1);
        assertEquals("filling up the locker such that every type do not exceed over the " +
                "50% occupation level", SUCCESS, this.testLocker1.addItem(baseballBat, 1));
    }

    @Test
    public void addZeroItemsToFullLocker() {
        this.testLocker1.addItem(bigHelmet, 1);
        this.testLocker1.addItem(smallHelmet, 1);
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("add 0 items to full inventory", SUCCESS,
                this.testLocker1.addItem(baseballBat, 0)); // while still below 50%
        assertEquals("add 0 items to full inventory", SUCCESS, //and not LTS success.
                this.testLocker1.addItem(bigHelmet, 0)); // allegedly makes it above 50%
    }

    @Test
    public void violateThresholdWhenLockerFull() {
        this.testLocker1.addItem(bigHelmet, 1);
        this.testLocker1.addItem(smallHelmet, 1);
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("violate 50% when locker is full, should fail anyway", FAIL,
                this.testLocker1.addItem(bigHelmet, 1));

    }

    @Test
    public void violateThresholdWhenLtsFull() {
        int ltsSuccess = LTS_TRANSFER;
        this.testLocker1.addItem(baseballBat, 1);
        while (ltsSuccess == LTS_TRANSFER) {
            ltsSuccess = this.testLocker1.addItem(baseballBat, 2);
        } //when LTS is Full
        assertEquals("when there is room in Locker but no room in LTS", FAIL,
                this.testLocker1.addItem(baseballBat, 2));
        assertEquals("available room not suppose to change", 8,
                this.testLocker1.getAvailableCapacity());
        assertEquals("leftovers in Locker remained the same", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }


    @Test
    public void addUpToThreshold() {
        this.testLocker1.addItem(bigHelmet, 1);
        assertEquals("fill up locker to be precise 50% occupation", 1,
                this.testLocker1.getItemCount(bigHelmet.getType()));
    }

    @Test
    public void addBeyondCapacity() {
        assertEquals("try to add at once when too over capacity", FAIL,
                this.testLocker1.addItem(baseballBat, 6));
    }

    @Test
    public void addBeyondCapacityEffect() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(baseballBat, 5);
        assertEquals("make sure that the inventory havn't changed", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void addToExistingTypeBeyondCapacity() {
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("add to already existing entry in inventory such that the addition exceed " +
                        "capacity",
                FAIL, this.testLocker1.addItem(baseballBat, 5));
    }

    @Test
    public void addingEffectOnInventory() {
        this.testLocker1.addItem(baseballBat, 2);
        assertEquals("check if adding took place the amount of items in inventory", 2,
                this.testLocker1.getItemCount(baseballBat.getType()));
        assertEquals("check if adding took place on available place", 6,
                this.testLocker1.getAvailableCapacity());
    }

    @Test
    public void addNonValidNumber() {
        assertEquals("inserting negative number of items to store", FAIL,
                this.testLocker1.addItem(baseballBat, -1));
        assertEquals("inserting zero items", SUCCESS,
                this.testLocker1.addItem(baseballBat, 0));
        assertEquals("validate that nothing has changed", 0,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }


    /**
     * Test the relationships between lockers, for baseball bat (even storage volume) in the even and odd
     * capacity lockers
     */
    @Test
    public void transferLtsBaseballBatFromEvenLocker() {
        assertEquals("baseball bat - violate Locker threshold in a single insertion to an empty " +
                "inventory", LTS_TRANSFER, this.testLocker1.addItem(baseballBat, 3));
    }

    @Test
    public void checkBaseBallBatRemainderAfterLtsEvenLocker() {
        this.testLocker1.addItem(baseballBat, 3);
        assertEquals("baseball bat - check for remainder in the Locker after LTS TRANSFER", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void transferLtsBaseballBatFromOddLocker() {
        assertEquals("baseball bat - violate Locker threshold in a single insertion to an empty inventory",
                LTS_TRANSFER, this.testLocker2.addItem(baseballBat, 3));
    }

    @Test
    public void checkBaseballBatRemainderAfterLtsOddLocker() {
        this.testLocker1.addItem(baseballBat, 3);
        assertEquals("baseball bat - check for remainder in the Locker after LTS TRANSFER", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void transferBaseBallBatLtsAfterAddToExistingItem() {
        this.testLocker1.addItem(baseballBat, 2);
        assertEquals("baseball bat - violate Locker threshold by adding to an existing item", LTS_TRANSFER,
                this.testLocker1.addItem(baseballBat, 1));

    }

    /**
     * Test the relationships between lockers, for Big Helmet (odd storage volume) in the even and odd
     * capacity lockers.
     * Notice that the helmet 5 unit storage should check a non trivial case when all
     * items of this type should be moved to LTS in order to maintain the 20% lower bound.
     */
    @Test
    public void transferLtsBigHelmetFromEvenLocker() {
        assertEquals("helmet size 3 - violate Locker threshold in a single insertion to an empty inventory",
                LTS_TRANSFER, this.testLocker1.addItem(bigHelmet, 2));
    }

    @Test
    public void checkBigHelmetRemainderAfterLtsEvenLocker() {
        this.testLocker1.addItem(bigHelmet, 2);
        assertEquals("helmet size 3 - check for remainder in the Locker after LTS TRANSFER", 0,
                this.testLocker1.getItemCount(bigHelmet.getType()));
    }

    @Test
    public void transferLtsBigHelmetFromOddLocker() {
        assertEquals("helmet size 3 - violate Locker threshold in a single insertion to an empty inventory",
                LTS_TRANSFER, this.testLocker2.addItem(bigHelmet, 2));
    }


    @Test
    public void checkBigHelmetRemainderAfterLtsOddLocker1() {
        this.testLocker2.addItem(baseballBat, 3);
        assertEquals("helmet size 3 - check for remainder in the Locker after LTS TRANSFER", 0,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void checkBigHelmetRemainderAfterLtsOddLocker2() {
        Locker thisTestLocker = new Locker(25);
        thisTestLocker.addItem(bigHelmet, 3); // 15 is above half storage, s.t one item is 20% of storage
        assertEquals("helmet size 3 - check for remainder in the Locker after LTS TRANSFER", 1,
                thisTestLocker.getItemCount(bigHelmet.getType()));
    }

    @Test
    public void transferBigHelmetLtsAfterAddToExistingItem() {
        this.testLocker1.addItem(bigHelmet, 1);
        assertEquals("helmet size 3 - violate Locker threshold by adding to an existing item",
                LTS_TRANSFER, this.testLocker1.addItem(bigHelmet, 1));
    }


    /**
     * REMOVE ITEM TESTS
     */
    @Test
    public void removeSingleItem() {
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("basic removal of item", SUCCESS,
                this.testLocker1.removeItem(baseballBat, 1));
    }

    @Test
    public void removeItemNotInLocker() {
        assertEquals("remove item not in inventory", FAIL,
                this.testLocker1.removeItem(baseballBat, 1));
    }

    @Test
    public void removalEffectOnInventory() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.removeItem(baseballBat, 1);
        assertEquals("checks if removal preformed correctly", 0,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void tryToRemoveMoreThanPossible() {
        this.testLocker1.addItem(baseballBat, 2);
        assertEquals("remove more than possible", FAIL,
                this.testLocker1.removeItem(baseballBat, 3));
    }

    @Test
    public void tryRemovingNegativeAmount() {
        this.testLocker1.addItem(baseballBat, 2);
        assertEquals("remove negative amount of items", FAIL,
                this.testLocker1.removeItem(baseballBat, -1));
    }

    @Test
    public void removingNegativeAmountItemNotInLocker() {
        assertEquals("remove negative amount of items when no item is in locker", FAIL,
                this.testLocker1.removeItem(baseballBat, -1));
    }

    @Test
    public void tryRemovingZeroItems() {
        this.testLocker1.addItem(baseballBat, 2);
        assertEquals("remove zero items", SUCCESS,
                this.testLocker1.removeItem(baseballBat, 0));
    }

    @Test
    public void removeZeroItemNotInLocker() {
        assertEquals("remove zero items when the when no item is in locker in the first place",
                FAIL, this.testLocker1.removeItem(baseballBat, 0));
    }


    /**
     * GET ITEM COUNT TESTS
     */
    @Test
    public void countEmptyLocker() {
        assertEquals("check for empty locker", 0,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countUnvalidInput() {
        assertEquals("non valid input-string", 0,
                this.testLocker1.getItemCount("53jk2-"));
    }

    @Test
    public void countAfterAddingItems() {
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("check if an item was added to count", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countAfterRemoval() {
        this.testLocker1.addItem(baseballBat, 2);
        this.testLocker1.removeItem(baseballBat, 1);
        assertEquals("check if an item number has reduced", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
        this.testLocker1.removeItem(baseballBat, 1);
        assertEquals("check if an item number decreased to zero", 0,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countAfterFailedAddition() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(baseballBat, 6);
        assertEquals("check if the addition did nothing", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    @Test
    public void countAfterFailedRemoval() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.removeItem(baseballBat, -2);
        assertEquals("check if the removal did nothing", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
    }

    /**
     * GET INVENTORY TESTS
     */
    @Test
    public void emptyInventory() {
        assertEquals("map an empty locker", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkWhenLockerIsFull() {
        this.testLocker1.addItem(bigHelmet, 1);
        this.testLocker1.addItem(smallHelmet, 1);
        this.testLocker1.addItem(baseballBat, 1);
        expectedMap.put(bigHelmet.getType(), 1);
        expectedMap.put(smallHelmet.getType(), 1);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("basic check for a full locker", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkAfterModifing() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(baseballBat, 1);
        expectedMap.put(baseballBat.getType(), 2);
        assertEquals("after adding to an existing item", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkAfterZeroAdditionEmptyLocker() {
        this.testLocker1.addItem(baseballBat, 0);
        assertEquals("zero addition to empty locker", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkZeroAdditionToExisting() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(baseballBat, 0);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("zero addition to an existing item", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkAfterNegativeAdditionEmptyLocker() {
        this.testLocker1.addItem(baseballBat, -1);
        assertEquals("negative addition to empty locker", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void checkNegativeAdditionToExisting() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(baseballBat, -1);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("negative addition to an existing item", expectedMap,
                this.testLocker1.getInventory());
    }

    @Test
    public void emptyInventoryWhenItemIsTotallyRemoved() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.removeItem(baseballBat, 1);
        assertEquals("the key is removed if it's value is zero.", expectedMap,
                this.testLocker1.getInventory());
    }

    /**
     * GET CAPACITY TESTS
     */
    @Test
    public void testGetCapacity() {
        assertEquals("basic check", 10, this.testLocker1.getCapacity());
        assertEquals("basic check", 11, this.testLocker2.getCapacity());
    }

    @Test
    public void nothingChangesCapacity() {
        this.testLocker1.addItem(baseballBat, 1);//basic addition
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.addItem(baseballBat, 2); //LTS
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.addItem(baseballBat, 7); //failed addition
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.removeItem(baseballBat, 1);//basic removal
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.removeItem(baseballBat, 2); //failed removal
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.getAvailableCapacity();// check using the capacity field
        assertEquals(10, this.testLocker1.getCapacity());
        this.testLocker1.getItemCount(baseballBat.getType());
        assertEquals(10, this.testLocker1.getCapacity());
    }

    /**
     * GET AVAILABLE CAPACITY TESTS
     * notice that testing the method when LTS is full appears in another test imn this class.
     */
    @Test
    public void availbleCapacityTrivialTests() {
        assertEquals(10, this.testLocker1.getAvailableCapacity());
        this.testLocker1.addItem(baseballBat, 1);//basic addition, 2 unit storage
        assertEquals(8, this.testLocker1.getAvailableCapacity());
        this.testLocker1.addItem(smallHelmet, 1);//fill locker
        this.testLocker1.addItem(bigHelmet, 1);
        assertEquals(0, this.testLocker1.getAvailableCapacity());
        this.testLocker1.addItem(bigHelmet, 1);//failed addition
        assertEquals(0, this.testLocker1.getAvailableCapacity());
        this.testLocker1.removeItem(bigHelmet, 1);//removal 5 storage units
        assertEquals(5, this.testLocker1.getAvailableCapacity());
        this.testLocker1.addItem(baseballBat, 2);//add 2 bats such that they both will transferred to LTS.
        assertEquals(5, this.testLocker1.getAvailableCapacity());
        this.testLocker1.addItem(baseballBat, 1);//add 1 bat, should stay in locker (below threshold).
        assertEquals(3, this.testLocker1.getAvailableCapacity());
    }

    /**
     * CONTRADICTING ITEMS TESTS
     */
    @Test
    public void contradictFootball() {
        this.testLocker1.addItem(footBall, 1);
        assertEquals("adding football to a locker containing baseball bat", CONTRADICTION_FAILURE,
                this.testLocker1.addItem(baseballBat, 1));
    }

    @Test
    public void contradictBaseballBat() {
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("adding basball bat to a locker containing football ", CONTRADICTION_FAILURE,
                this.testLocker1.addItem(footBall, 1));
    }

    @Test
    public void contradictItemWithNonValidInput() {
        this.testLocker1.addItem(baseballBat, 1);
        assertEquals("check conditions priority", CONTRADICTION_FAILURE,
                this.testLocker1.addItem(footBall, -1));
        assertEquals("check conditions priority", CONTRADICTION_FAILURE,
                this.testLocker1.addItem(footBall, 0));
        assertEquals("check conditions priority", FAIL,
                this.testLocker1.addItem(null, 0));
    }

    @Test
    public void contradictionEffectOnLocker() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.addItem(footBall, 1);
        expectedMap.put(baseballBat.getType(), 1);
        assertEquals("un expected change in inventory", expectedMap, this.testLocker1.getInventory());
        assertEquals("un expected change in inventory", 0,
                this.testLocker1.getItemCount(footBall.getType()));
        assertEquals("un expected change in inventory", 1,
                this.testLocker1.getItemCount(baseballBat.getType()));
        assertEquals("un expected change in available capacity", 8,
                this.testLocker1.getAvailableCapacity());
    }

    @Test
    public void checkContradictionAfterFix() {
        this.testLocker1.addItem(baseballBat, 1);
        this.testLocker1.removeItem(baseballBat, 1);
        assertEquals("un expected change in inventory", SUCCESS,
                this.testLocker1.addItem(footBall, 1));
    }
}
