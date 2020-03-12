import oop.ex3.spaceship.Item;

import java.util.Map;

public abstract class SpaceShipDepository implements PhysicalStorage {
    // Constants
    static final int SUCCESSFUL_OPERATION = 0;
    static final int FAILED_OPERATION = -1;
    static final int LTS_SUCCESS = 1;
    static final int CONTRADICTION_FAILURE = -2;
    String MOVED_TO_LTS_MSG = "Warning: Action successful, but has caused items to be moved to " +
            "storage";
    String ERROR_MSG = "Error: Your request cannot be completed at this time.";

    String NOT_ENOUGH_ROOM_MSG = ERROR_MSG + " Problem: no room for %d items of type %s.";

    String TOO_HIGH_REMOVAL_REQUEST_MSG = ERROR_MSG + " Problem: the locker does not contain %d " +
            "items of type %s";
    String NEGATIVE_REMOVAL_MSG = ERROR_MSG + " Problem: cannot remove a negative number of items of" +
            " type %s.";
    String CONTRADICTING_TYPES_MSG = "Problem: the locker cannot contain items of type %s, as it contains" +
            " a contradicting item.";

    // Data Members
    /**
     * the actual storage, actually saves only metadata about items, not the items objects.
     */
    Map<String, Integer> inventory;

    /**
     * Storage capacity for the storage unit.
     */
    int storageCapacity;

    /**
     * current occupied storage units, according to storage volume for each item.
     */
    int occupiedStorage;

    /**
     * This method adds n Items of the given type to the locker, if there's room.
     *
     * @param item item object that it's object of it's type will be added to the locker.
     * @param n    - number of object to add.
     * @return - 0 if the insertion was successful; 1 if there has been a transfer to the long term storage,
     * -1 if the insertion has failed. the method will print an informative msg.
     */
    public abstract int addItem(Item item, int n);


    /**
     * This method returns the number of Items of type type
     * the storage contains.
     * Notice that if the type is not valid, the item will not be in the array in the
     * first place (cause insertion of item make use in ItemFactory), so the method will return 0;
     *
     * @param type - the type of items to count
     * @return amount of items in storage.
     */
    public int getItemCount(String type) {
        if (this.inventory.containsKey(type)) {
            return this.inventory.get(type);
        }
        return 0; //if the inventory do not contain the type, it's amount is zero.
    }

    /**
     * This method returns a map of all the item types
     * contained in the locker, and their respective quantities. For example: {”Baseball bat”=1, ”helmet,
     * size 3”=5}.
     *
     * @return a map object of the inventory
     */
    public Map<String, Integer> getInventory() {
        return this.inventory;
    }

    /**
     * This method returns the locker’s total capacity.
     *
     * @return int of the fixed storage capacity.
     */
    public int getCapacity() {
        return this.storageCapacity;
    }

    /**
     * This method returns the locker’s available capacity, i.e.
     * how many storage units are unoccupied by Items
     *
     * @return int, the difference between the capacity to the amount of items that are currently in storage.
     */
    public int getAvailableCapacity() {
        return this.storageCapacity - this.occupiedStorage;
    }


    /**
     * calculates if there is enough room in locker for the required insertion.
     *
     * @param itemVolume - how much storage units a single item of this type requires.
     * @param n          - how many items are we trying to insert.
     * @return true if there's room, false otherwise.
     */
    boolean isThereEnoughSpace(int itemVolume, int n) {
        return ((this.getAvailableCapacity() - (n * itemVolume)) >= 0);
    }

    /**
     * update inventory data given positive number to increase the amount of this item in storage.
     *
     * @param item - Item object desired to be modified in storage.
     * @param n    - number of items to add to storage.
     */
    void addStorageData(Item item, int n) {
        String type = item.getType();
        if (this.inventory.containsKey(type)) {
            this.inventory.put(type, this.inventory.get(type) + n);
            this.occupiedStorage += (n * item.getVolume());
        } else {
            this.inventory.put(item.getType(), n);
            this.occupiedStorage += (n * item.getVolume());
        }
    }

    /**
     * update inventory data given negative to decrease the amount of this item in storage.
     * Notice that the item is surely in storage already.
     *
     * @param item Item object desired to be modified in storage.
     * @param n    - number of items to subtract from storage.
     */
    void subtractStorageData(Item item, int n) {
        String type = item.getType();
        this.occupiedStorage -= (n * item.getVolume());
        this.inventory.put(type, this.inventory.get(type) - n);
    }

    /**
     * overloaded method, when given only an item, remove the whole entry from Storage and updates the
     * occupied Storage counter.
     *
     * @param item - item to remove
     */
    void subtractStorageData(Item item) {
        occupiedStorage -= (this.inventory.get(item.getType()) * item.getVolume());
        this.inventory.remove(item.getType());
    }

    /**
     * calculates a given item type occupation percentage (in storage units) in the current locker,
     *
     * @param item - the item to check
     * @return a double represent the percentage.
     */
    double getTypePercentage(Item item) { // it's rounded down! check it for  odd and
        // even  numbers divided (capacities)
        return ((this.getItemCount(item.getType()) * item.getVolume()) / (double) this.getCapacity());
    }
}