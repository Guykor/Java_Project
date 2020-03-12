/**
 * A superclass for implementations of hash-sets implementing the SimpleSet interface.
 */

public abstract class SimpleHashSet implements SimpleSet {
    /**
     * Table size factor, in which the table will be increase or decrease.
     */
    protected static final int TABLE_SIZE_FACTOR = 2;
    /**
     * Table minimal size;
     */
    protected static final int TABLE_MINIMAL_SIZE = 1;
    /**
     * Describes the higher load factor of a newly created hash set
     */
    protected static float DEFAULT_HIGHER_CAPACITY = 0.75f;
    /**
     * Describes the lower load factor of a newly created hash set
     */
    protected static float DEFAULT_LOWER_CAPACITY = 0.25f;
    /**
     * Describes the capacity of a newly created hash set.
     */
    protected static int INITIAL_CAPACITY = 16;
    /**
     * Describes the load factor upper bound for the hash set.
     */
    private final float loadFactorUpperBound;
    /**
     * Describes the load factor lower bound for the hash set.
     */
    private final float loadFactorLowerBound;

    /**
     * number of records in the data structure.
     */
    protected int recordsNum;

    /**
     * Constructs a new hash set with the default capacities given in DEFAULT_LOWER_CAPACITY and
     * DEFAULT_HIGHER_CAPACITY
     */
    protected SimpleHashSet() {
        this.loadFactorUpperBound = DEFAULT_HIGHER_CAPACITY;
        this.loadFactorLowerBound = DEFAULT_LOWER_CAPACITY;
    }

    /**
     * Constructs a new hash set with capacity INITIAL_CAPACITY
     *
     * @param upperLoadFactor - the upper load factor before rehashing
     * @param lowerLoadFactor - the lower load factor before rehashing
     */

    protected SimpleHashSet(float upperLoadFactor, float lowerLoadFactor) {
        this.loadFactorUpperBound = upperLoadFactor;
        this.loadFactorLowerBound = lowerLoadFactor;
    }

    /**
     * @return The current capacity (number of cells) of the table.
     */
    public abstract int capacity();

    /**
     * Clamps hashing indices to fit within the current table capacity.
     *
     * @param index - the index before clamping
     * @return an index properly clamped
     */
    protected int clamp(int index) {
        return index & (this.capacity() - 1);
    }

    /**
     * @return The lower load factor of the table.
     */
    protected float getLowerLoadFactor() {
        return loadFactorLowerBound;
    }

    /**
     * @return The higher load factor of the table.
     */
    protected float getUpperLoadFactor() {
        return this.loadFactorUpperBound;
    }

    /**
     * this method determines if the hashset load factor has violated it's lower bound.
     *
     * @return true if the bound has violated, false otherwise.
     */
    protected boolean lowerBoundViolated() {
        return (this.size() / (float) this.capacity()) < this.getLowerLoadFactor();
    }

    /**
     * this method determines if the hashset load factor IS ABOUT to violate it's upper bound.
     * the reason this check is preformed pre the action of adding the actual element is to make sure that
     * there is enough room in the hashset for a new element.
     *
     * @return true if bound is about to be violated, false otherwise.
     */
    protected boolean upperBoundWillBeViolated() {
        return (this.size() + 1) / (float) this.capacity() > this.getUpperLoadFactor();
    }
}