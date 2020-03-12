/**
 * This class wraps String element, by adding a boolean property that states if the element has been
 * deleted and not just null.
 */
public class StringObject {

    /**
     * the string value
     */
    private String value;
    /**
     * boolean flag stating if the element has deleted.
     */
    private boolean isDeleted;

    public StringObject() {
    }

    /**
     * @return true if the element has deleted.
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    /**
     * get the String value
     *
     * @return String value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * sets a String value.
     * when the value field holds a proper string and this method is called with a null input (==deletion)
     * null will be placed in the value field, and the deletion flag will be raised.
     * other wise, any insertion will keep the flag updated.
     *
     * @param s value to keep in the element.
     */
    public void setValue(String s) {
        if (this.getValue() != null && s == null) {
            this.isDeleted = true;
        } else if (this.getValue() == null && s != null) {
            this.isDeleted = false;
        }
        this.value = s;
    }

    /**
     * @return modified string representation containing deletion status.
     */
    @Override
    public String toString() {
        return this.getValue() + ", is deleted: " + this.isDeleted();
    }
}
