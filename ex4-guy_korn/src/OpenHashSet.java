import java.util.LinkedList;

/**
 * a data structure of type hash set based on chaining to resolve collision.
 * it's capacity of this kind of hash set is simply the number of buckets.
 */

public class OpenHashSet extends SimpleHashSet {

    private CollectionFacadeSet[] hashTable;

    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     *
     * @param upperLoadFactor - The upper load factor of the hash table.
     * @param lowerLoadFactor - The lower load factor of the hash table.
     */
    public OpenHashSet(float upperLoadFactor, float lowerLoadFactor) {
        super(upperLoadFactor, lowerLoadFactor);
        this.hashTable = this.constructHashTable(INITIAL_CAPACITY);
    }

    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16), upper load
     * factor (0.75) and lower load factor (0.25).
     */
    public OpenHashSet() {
        super();
        this.hashTable = this.constructHashTable(INITIAL_CAPACITY);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one. Duplicate values should be
     * ignored. The new table has the default values of initial capacity (16),  upper load factor (0.75),
     * and lower load factor (0.25).
     *
     * @param data - Values to add to the set.
     */
    public OpenHashSet(java.lang.String[] data) {
        this();
        for (String s : data) {
            this.add(s);
        }
    }

    /**
     * constructor for the actual data structure, construct an array in a given size of linked
     * lists<String> wrapped in a facade class.
     *
     * @param tableSize int stating the required array size.
     * @return new array of linked lists <String></String>.
     */
    private CollectionFacadeSet[] constructHashTable(int tableSize) {
        CollectionFacadeSet[] newHashTable = new CollectionFacadeSet[tableSize];
        for (int i = 0; i < newHashTable.length; i++) {
            newHashTable[i] = new CollectionFacadeSet(new LinkedList<String>());
        }
        return newHashTable;
    }

    /**
     * This method calls whenever the upper or lower bound for the hash table load factor has been violated.
     * When called, this method will create a new array of linked lists<String></String> and will place
     * every element in the older hash table in the new one, after rehashing them.
     * Attention - this method also resets the counter for the hash set, in order to preform the count
     * again when unpacking elements to the new hash table.
     *
     * @param newSize the requested new size for the new array.
     */
    private void resizeTable(int newSize) {
        if (newSize < TABLE_MINIMAL_SIZE) {
            newSize = TABLE_MINIMAL_SIZE;
        }
        CollectionFacadeSet[] newHashTable = this.constructHashTable(newSize);
        this.recordsNum = 0;
        this.reHash(newHashTable);
    }

    /**
     * Rehashes the elements stored in the instance hash table into a new different sized array of linked
     * lists<String></String>.
     *
     * @param newHashTable new hash table object that will replace the current one.
     */
    private void reHash(CollectionFacadeSet[] newHashTable) {
        CollectionFacadeSet[] oldHashTable = this.hashTable;
        this.hashTable = newHashTable;
        for (CollectionFacadeSet cell : oldHashTable) {
            if (cell != null) {
                for (String s : cell) {
                    this.insertElement(s);
                }
            }
        }
    }

    /**
     * Add a specified element to the set if it's not already in it.
     *
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set
     */
    @Override
    public boolean add(java.lang.String newValue) {
        if (this.contains(newValue)) {
            return false;
        } else if (newValue == null) {
            return true; //because false returns only iff the element in the set already.
        } else {
            if (this.upperBoundWillBeViolated()) {
                this.resizeTable(this.capacity() * TABLE_SIZE_FACTOR);
            }
            this.insertElement(newValue);
            return true;
        }
    }

    private void insertElement(String newValue) {
        CollectionFacadeSet targetLinkedList = this.hashTable[clamp(newValue.hashCode())];
        targetLinkedList.add(newValue);
        this.recordsNum++;
    }

    /**
     * Look for a specified value in the set.
     *
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    @Override
    public boolean contains(java.lang.String searchVal) {
        if (searchVal == null) {
            return false;
        }
        CollectionFacadeSet targetLinkedList = this.hashTable[clamp(searchVal.hashCode())];
        if (targetLinkedList == null) {
            return false;
        } else {
            return targetLinkedList.contains(searchVal);
        }
    }

    /**
     * Remove the input element from the set.
     *
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    @Override
    public boolean delete(java.lang.String toDelete) {
        if (this.contains(toDelete)) {
            CollectionFacadeSet targetLinkedList = this.hashTable[clamp(toDelete.hashCode())];
            targetLinkedList.delete(toDelete);
            this.recordsNum--;
            if (this.lowerBoundViolated()) {
                this.resizeTable(this.capacity() / TABLE_SIZE_FACTOR);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * The number of elements currently in the set
     */
    public int size() {
        return recordsNum;
    }

    /**
     * @return The current capacity (number of cells) of the table.
     */
    public int capacity() {
        return this.hashTable.length;
    }


}
