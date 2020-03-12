/**
 * a data structure of type hash set based on quadratic probing to resolve collision.
 */

public class ClosedHashSet extends SimpleHashSet {

    private static final int VALUE_NOT_FOUND = -1;

    private StringObject[] hashTable;


    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     *
     * @param upperLoadFactor - The upper load factor of the hash table.
     * @param lowerLoadFactor - The lower load factor of the hash table.
     */
    public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor) {
        super(upperLoadFactor, lowerLoadFactor);
        this.hashTable = this.constructHashTable(INITIAL_CAPACITY);
    }

    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16), upper load
     * factor (0.75) and lower load factor (0.25).
     */
    public ClosedHashSet() {
        super();
        this.hashTable = this.constructHashTable(INITIAL_CAPACITY);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one. Duplicate values should
     * be ignored. The new table has the default values of initial capacity (16), upper load factor (0.75),
     * and lower load factor (0.25).
     *
     * @param data - Values to add to the set.
     */
    public ClosedHashSet(java.lang.String[] data) {
        this();
        for (String s : data) {
            this.add(s);
        }
    }

    /**
     * constructor for the actual data structure, construct a String array in a given size.
     * In addition, create a boolean array representing the cells who were deleted in the array.
     *
     * @param tableSize int stating the required table size.
     * @return new hash table.
     */
    private StringObject[] constructHashTable(int tableSize) {
        return new StringObject[tableSize];
    }

    /**
     * This method calls whenever the upper or lower bound for the hash table load factor has been violated.
     * When called, this method will create a new String array and will place
     * every element in the older hash table in the new one, after rehashing them.
     * Attention - this method also resets the counter for the hash set, in order to preform the count
     * again when unpacking elements to the new hash table.
     *
     * @param newSize the requested new size for the new array.
     */
    private void resizeTable(int newSize) {
        if (newSize <= TABLE_MINIMAL_SIZE) {
            newSize = TABLE_MINIMAL_SIZE;
        }
        StringObject[] newHashTable = this.constructHashTable(newSize);
        this.recordsNum = 0;
        this.reHash(newHashTable);
    }

    /**
     * Rehashes the elements stored in the instance hash table into a new different sized array of Strings.
     *
     * @param newHashTable new hash table object that will replace the current one.
     */
    private void reHash(StringObject[] newHashTable) {
        StringObject[] oldHashTable = this.hashTable;
        this.hashTable = newHashTable;
        for (StringObject cell : oldHashTable) {
            if (cell != null && cell.getValue() != null) {
                this.insertElement(cell.getValue());
            }
        }
    }

    /**
     * This function uses quadratic probing on the hash table trying to find an empty cell, namely a cell
     * that has a null value.
     *
     * @param s the string to evaluate it's hash code.
     * @return the proper index for the string provided based on it's hashcode and probing, after clamping
     * it to the actual table size.
     */
    private int findEmptyCell(String s) {
        int idx = this.clamp(s.hashCode());
        for (int i = 1; i < this.capacity(); i++) {
            if ((this.hashTable[idx] == null) || this.hashTable[idx].getValue() == null) {
                break;
            }
            idx = probe(s, i);
        }
        return idx;
    }

    /**
     * This method is used to probe element in the hash table using quadratic probing. Namely this method
     * holds the arithmetic formula to calculate the next index for a string in the hash table.
     *
     * @param s the string to eveluate in the hash code.
     * @param i the number of attempts in the probing process.
     * @return index in the hash table compatible with the string evaluated.
     */
    private int probe(String s, int i) {
        return clamp(s.hashCode() + ((i + (i * i)) / 2));
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
            return true; // because false returns only iff the element in the set already.
        }
        // Resizing happens before actual insertion if needed to prevent failed attempt to insert to a full
        // array.
        if (this.upperBoundWillBeViolated()) {
            this.resizeTable(this.capacity() * TABLE_SIZE_FACTOR);
        }
        insertElement(newValue);
        return true;
    }

    private void insertElement(String newValue) {
        int targetCellIdx = this.findEmptyCell(newValue);
        if (this.hashTable[targetCellIdx] == null) {
            this.hashTable[targetCellIdx] = new StringObject();
        }
        this.hashTable[targetCellIdx].setValue(newValue);
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
        int idx = this.searchValueIndex(searchVal);
        return (idx != VALUE_NOT_FOUND);
    }


    /**
     * this method uses quadratic probing to find a value in the array based on it's hash code.
     *
     * @param stringInput string to evaluate it's index in the array
     * @return the index of the searched value in the array, default value if the string has'nt found.
     */
    private int searchValueIndex(String stringInput) {
        int idx = this.clamp(stringInput.hashCode());
        for (int i = 1; i < this.capacity(); i++) {
            if (this.hashTable[idx] == null ||
                    (this.hashTable[idx].getValue() == null & !this.hashTable[idx].isDeleted())) {
                break;
            }
            if (this.hashTable[idx].getValue() != null && this.hashTable[idx].getValue().equals(stringInput))
            {
                return idx;
            } else {
                idx = probe(stringInput, i);
            }
        }
        return VALUE_NOT_FOUND;
    }

    /**
     * Remove the input element from the set.
     *
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    @Override
    public boolean delete(java.lang.String toDelete) {
        if (toDelete == null) {
            return false;
        }
        int idx = this.searchValueIndex(toDelete);
        if (idx == VALUE_NOT_FOUND) {
            return false;
        }
        this.hashTable[idx].setValue(null);
        this.recordsNum--;
        if (this.lowerBoundViolated() && this.size() <= (this.capacity() / TABLE_SIZE_FACTOR)) {
            this.resizeTable(this.capacity() / TABLE_SIZE_FACTOR);
        }
        return true;
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

