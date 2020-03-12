import oop.ex3.spaceship.Item;

import java.util.HashMap;

/**
 * A long term storage facility on board of the USS Discovery ship.
 * the ship has only also a single centralized long-term storage, which has a capacity of 1000 storage units.
 */

public class LongTermStorage extends SpaceShipDepository {

    private static final int LTS_CAPACITY = 1000;

    /**
     * LTS constructor, just initialize the storage map, when the capacity is fixed
     */
    public LongTermStorage() {
        this.storageCapacity = LTS_CAPACITY;
        this.resetInventory();
    }

    /**
     * This method resets the long-term storage’s inventory (i.e. after
     * it is invoked the inventory does not contain any Item
     */
    @Override
    public int addItem(Item item, int n) {
        if (item != null && item.getVolume() > 0) {
            if (n > 0) {
                if (this.isThereEnoughSpace(item.getVolume(), n)) {
                    addStorageData(item, n);
                    System.out.println(MOVED_TO_LTS_MSG);
                    return LTS_SUCCESS;
                } else {
                    System.out.println(String.format(NOT_ENOUGH_ROOM_MSG, n, item.getType()));
                    return FAILED_OPERATION;
                }
            } else if (n == 0) {
                return LTS_SUCCESS;

            } else {//n is negative
                System.out.println(ERROR_MSG);
                return FAILED_OPERATION;

            }

        } else {//invalid item volume, non positive or null item.
            System.out.println(ERROR_MSG);
            return FAILED_OPERATION;
        }
    }

    /**
     * wipes the inventory clean and create a new one.
     */
    public void resetInventory() {
        this.inventory = new HashMap<String, Integer>();
        occupiedStorage = 0;
    }

}



