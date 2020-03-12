import java.util.Iterator;

/**
 * Wraps an underlying Collection and serves to both simplify its API and give it a common type with
 * the implemented SimpleHashSets.
 */
public class CollectionFacadeSet implements SimpleSet, Iterable<String> {

    protected java.util.Collection<java.lang.String> collection;

    private boolean isCollectionSet;

    private String STRING_DUMMY = "Dummy";

    /**
     * Creates a new facade wrapping the specified collection.
     *
     * @param collection - The Collection<String> to wrap.
     */
    public CollectionFacadeSet(java.util.Collection<java.lang.String> collection) {
        this.collection = collection;
        this.isCollectionSet();
    }

    /**
     * Add a specified element to the set if it's not already in it.
     *
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set.
     */
    public boolean add(java.lang.String newValue) {
        if (this.isCollectionSet) {
            return this.collection.add(newValue);
        } else { //if the collection is not a set, add duplicate check functionality.
            if (!this.contains(newValue)) {
                return this.collection.add(newValue);
            }
        }
        return false; //iff the contains returned true.
    }


    /**
     * Look for a specified value in the set.
     *
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    public boolean contains(java.lang.String searchVal) {
        return this.collection.contains(searchVal);
    }

    /**
     * Remove the input element from the set.
     *
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    public boolean delete(java.lang.String toDelete) {
        return this.collection.remove(toDelete); //returns true iff the action caused collection to change.
    }

    /**
     * @return The number of elements currently in the set
     */
    public int size() {
        return this.collection.size();
    }


    /**
     * since this class wraps general collection, this method will be called when initializing an instance
     * to determine whether the wrapped collection is a set, or there is a need to add a duplicate check
     * functionality to the add method.
     * Suppose to work even if a non empty collection has inserted to the wrapper.
     */
    private void isCollectionSet() {
        if (this.collection.contains(STRING_DUMMY)) {
            this.isCollectionSet = !this.collection.add(STRING_DUMMY); //will return true if the collection has
            // changed, meaning it is not a set.
            this.collection.remove(STRING_DUMMY);
        } else {
            this.collection.add(STRING_DUMMY);
            this.isCollectionSet = !this.collection.add(STRING_DUMMY); //will return true if the collection has
            // changed, meaning it is not a set.
            this.collection.remove(STRING_DUMMY);
            this.collection.remove(STRING_DUMMY);
        }
    }

    /**
     * @return the delegated collection iterator.
     */
    @Override
    public Iterator<String> iterator() {
        return collection.iterator();
    }

}
