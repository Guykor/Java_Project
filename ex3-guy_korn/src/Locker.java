import oop.ex3.spaceship.Item;

import java.util.HashMap;

/**
 * A locker class for the starship USS Discovery, ment to keep items the crew.
 * can contain different types of items. Each of these items have a unique identifying type.
 * All items of the same type take up the same amount of storage units in the locker.
 * Storage units are positive integers.
 * Each locker has a capacity, which is the total amount of storage units it can hold. The capacity of a
 * locker is a non-negative integer, and cannot be changed once it has been set. Each item can only be added
 * in full.
 * In addition to the aforementioned lockers, the USS Discovery also has a single centralized long-term
 * storage, which has a capacity of 1000 storage units.
 * If items of a specific type take up more than 50% of the storage units of a specific locker, some of
 * them are automatically moved to the long-term storage.
 * The remaining amount will take up to 20% of the storage units of that locker (i.e. the remaining number is
 * the maximal number which occupies up to, and including, 20% of the locker’s capacity).
 */

public class Locker extends SpaceShipDepository {
    static final LongTermStorage longTermStorage = new LongTermStorage();
    private static final double STORAGE_PER_TYPE_LIMIT = 0.5;
    private static final double STORAGE_REMAINDER_UPPER_BOUND = 0.2;
    static int DEFAULT_CAPACITY = 10;
    private static String[] itemsCantResideTogether = {"football", "baseball bat"};


    /**
     * This constructor initializes a Locker object with the given capacity.
     *
     * @param capacity - capacity in whole units (integer) of storage in the locker.
     */
    public Locker(int capacity) {
        if (capacity > 0) {
            this.storageCapacity = capacity;
        } else {
            this.storageCapacity = DEFAULT_CAPACITY;
        }
        occupiedStorage = 0;
        this.inventory = new HashMap<String, Integer>();
    }

    /**
     * This method adds n Items of the given type to the locker, if there's room.
     * If items of a specific type take up more than 50% of the storage capacity, some of them are
     * automatically moved to the long-term storage, such that the remaining
     * amount should only take up to 20% of the storage capacity.
     *
     * @param item item object that it's object of it's type will be added to the locker.
     * @param n    - number of object to add.
     * @return 0 if the insertion was successful; 1 if there has been a transfer to the long term storage,
     * -1 if the insertion has failed. the method will print an informative msg.
     */
    @Override
    public int addItem(Item item, int n) {
        if (item != null && item.getVolume() > 0) {
            if (this.checkIfItemContradictsWithAnother(item)) {
                System.out.println(String.format(CONTRADICTING_TYPES_MSG, item.getType()));
                return CONTRADICTION_FAILURE;
            }
            if (n > 0) {
                if (this.isThereEnoughSpace(item.getVolume(), n)) {
                    addStorageData(item, n);
                    if (this.storageThresholdViolated(item)) {
                        int itemsToTransfer = this.prepareBatchToTransfer(item);
                        int successCode = longTermStorage.addItem(item, itemsToTransfer);
                        switch (successCode) {
                            case LTS_SUCCESS:
                                this.subtractStorageData(item, itemsToTransfer);
                                return LTS_SUCCESS;
                            case FAILED_OPERATION:
                                this.subtractStorageData(item, n);
                                return FAILED_OPERATION;
                        }
                    } else {
                        return SUCCESSFUL_OPERATION;
                    }
                } else {
                    System.out.println(String.format(NOT_ENOUGH_ROOM_MSG, n, item.getType()));
                    return FAILED_OPERATION;
                }
            } else if (n < 0) {
                System.out.println(ERROR_MSG);
                return FAILED_OPERATION;
            }
            //else n == 0;
            return SUCCESSFUL_OPERATION;
        } else {//invalid item, non positive volume or null if the item was illegal.
            System.out.println(ERROR_MSG);
            return FAILED_OPERATION;
        }
    }

    private boolean checkIfItemContradictsWithAnother(Item item) {
        boolean lockerCurrentlyStoringFlaggedItem = false;
        boolean itemIsFlagged = false;
        for (String s : itemsCantResideTogether) {
            if (s.equals(item.getType())) {
                itemIsFlagged = true;
            } else if (this.inventory.containsKey(s)) {
                lockerCurrentlyStoringFlaggedItem = true;
            }
        }
        return (itemIsFlagged && lockerCurrentlyStoringFlaggedItem);
    }

    /**
     * This method removes n Items of the type type from the locker.
     * In case there are less than n Items of this type in the locker, no Items should be removed, and a
     * message should be printed. In case n is negative, no
     * Items should be removed, the method should return -1, and the following message should be printed
     * to System.out.println: .
     *
     * @param item - item to remove.
     * @param n    - number of items to remove from the given type.
     * @return 0 if success, -1 otherwise.
     */
    public int removeItem(Item item, int n) {
        if (item != null && this.inventory.containsKey(item.getType())) {
            if (n > 0) {
                int currentQuantity = this.inventory.get(item.getType());
                if (currentQuantity == n) {
                    this.subtractStorageData(item);
                    return SUCCESSFUL_OPERATION;
                } else if (currentQuantity > n) {
                    this.subtractStorageData(item, currentQuantity - n);
                    return SUCCESSFUL_OPERATION;
                } else { //if lower than quantity exists in storage.
                    System.out.println(String.format(TOO_HIGH_REMOVAL_REQUEST_MSG, n, item.getType()));
                    return FAILED_OPERATION;
                }
            } else if (n < 0) {
                System.out.println(String.format(NEGATIVE_REMOVAL_MSG, item.getType()));
                return FAILED_OPERATION;
            } else { //n==0
                return SUCCESSFUL_OPERATION;
            }
        } else if (item == null) { //item is null (non valid item)
            System.out.println(ERROR_MSG);
            return FAILED_OPERATION;
        } else {//item requested is not in storage at all,
            System.out.println(String.format(TOO_HIGH_REMOVAL_REQUEST_MSG, n, item.getType()));
            return FAILED_OPERATION;
        }
    }


    /**
     * preforms a check if a given type of item has exceed it's maximal occupation in the locker, the
     * check
     * runs on this type percentage, given a fixed threshold.
     *
     * @param item - Item object to check.
     * @return - true if type is above threshold, false otherwise.
     */
    private boolean storageThresholdViolated(Item item) {
        double itemTypePercentage = getTypePercentage(item);
        return (itemTypePercentage > STORAGE_PER_TYPE_LIMIT);
    }


    /**
     * calculates the minimal items that should be transferred from the locker to the long term
     * storage, in
     * order to maintain a fixed upper bound of remaining items in the locker.
     *
     * @param item item checked.
     * @return int of the amount of items needs to be transferred in order to fulfill requirements.
     */
    private int prepareBatchToTransfer(Item item) {
        int numberOfUnitsAllowed =
                (int) ((this.storageCapacity * STORAGE_REMAINDER_UPPER_BOUND) / item.getVolume());
        //notice that
        // only items above volume 0 will get this far in the process.
        return (this.inventory.get(item.getType()) - numberOfUnitsAllowed);
    }
}