/**
 * This class represents a library, which hold a collection of books. Patrons can register at the library
 * to be able to check out books, if a copy of the requested book is available.
 */
class Library {
    /**
     * The maximal number of books this library can hold.
     */
    int bookCapacity;

    /**
     * The maximal number of books this library allows a single patron to borrow at the same time.
     */
    int borrowLimit;

    /**
     * The maximal number of registered patrons this library can handle.
     */
    int patronsCapacity;

    /**
     * the library books storage array, contains books objects
     */
    Book[] bookShelf;

    /**
     * counter of current books number in library.
     */
    int booksCount;

    /**
     * the library patrons data base, containing it's registered patrons and their details.
     */
    Patron[] patronsDb;

    /**
     * counter of current patrons registered to the library
     */
    int patronCount;


    /*----=  Constructors  =-----*/

    /**
     * Creates a new library with the given parameters.
     * * @param maxBookCapacity   - The maximal number of books this library can hold.
     *
     * @param maxBorrowedBooks  - The maximal number of books this library allows a single patron to
     *                          borrow at the same time.
     * @param maxPatronCapacity - The maximal number of registered patrons this library can handle.
     */

    Library(int maxBookCapacity, int maxBorrowedBooks, int maxPatronCapacity) {
        bookCapacity = maxBookCapacity;
        borrowLimit = maxBorrowedBooks;
        patronsCapacity = maxPatronCapacity;
        bookShelf = new Book[bookCapacity];
        booksCount = 0;
        patronsDb = new Patron[patronsCapacity];
        patronCount = 0;
    }



    /*----=  Library - Book Methods  =-----*/
    /**checks if there is a free spot for more book(s) in the library*/
    boolean freeSpotOnShelf() {
        return booksCount < bookCapacity;
    }


    /**
     * Returns the non-negative id number of the given book if he is owned by this library, -1 otherwise.
     * @param book - The book for which to find the id number.
     * @return a non-negative id number of the given book if he is owned by this library, -1 otherwise.
     */

    //    TODO: check what suppose to happen if the same book name, have different values in other fields.
    int getBookId(Book book) {
        for (int i = 0; i < bookShelf.length; i++) {
            if (bookShelf[i] != null) {
                if (bookShelf[i] == book) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Adds the given book to this library, if there is place available, and it isn't already in the library.
     * @param book - The book to add to this library.
     * @return a non-negative id number for the book if there was a spot and the book was successfully
     * added, or if the book was already in the library; a negative number otherwise.
     */
    int addBookToLibrary(Book book) {
        int bookLocation = getBookId(book);
        if (bookLocation >= 0) { // if book is already in library return it's id (location)
            return bookLocation;
        } else {
            if (freeSpotOnShelf()) {
                bookShelf[booksCount] = book; //add the book the next free spot on the shelf array, and
                int bookId = booksCount;
                // update the counter
                booksCount++;
                return bookId;
            }
            return -1; //no more room in library;
        }
    }

    /**
     * Returns true if the given number is an id of some book in the library, false otherwise.
     * @param bookId - The id to check.
     * @return true if the given number is an id of some book in the library, false otherwise.
     */

    boolean isBookIdValid(int bookId) {
        if (bookId >= 0 && bookId < bookShelf.length) {
            return bookShelf[bookId] != null;
        } else {
            return false;
        }
    }


    /**
     * Returns true if the book with the given id is available, false otherwise.
     * @param bookId - The id number of the book to check.
     * @return true if the book with the given id is available, false otherwise.
     */
    boolean isBookAvailable(int bookId) {
        if (isBookIdValid(bookId)) {
            return bookShelf[bookId].getCurrentBorrowerId() == -1; // -1 defined in the Book class to
            // represent non borrowed state for the book.
        }
        return false;
    }



    /*----=  Library - Patron Methods  =-----*/

    /**checks if there is room available for more patron(s) to register to the library.*/
    boolean freePatronSpot() {
        return patronCount < patronsCapacity;
    }

    /**
     * Registers the given Patron to this library, if there is a spot available.
     * @param patron - The patron to register to this library.
     * @return a non-negative id number for the patron if there was a spot and the patron was successfully
     * registered or if the patron was already registered. a negative number otherwise.
     */
    int registerPatronToLibrary(Patron patron) {
        int patronId = getPatronId(patron);
        if (patronId >= 0) {
            return patronId;
        } else {
            if (freePatronSpot()) {
                patronsDb[patronCount] = patron; //add the patron instance to the patrons array
                int id = patronCount;
                patronCount++; //update the patrons counter.
                return id;
            }
        }
        return -1; // no more room for patrons.

    }


    /**
     * Returns the non-negative id number of the given patron if he is registered to this library, -1
     * otherwise.
     * @param patron - The patron for which to find the id number.
     * @return a non-negative id number of the given patron if he is registered to this library, -1
     * otherwise.
     */
    int getPatronId(Patron patron) {
        for (int i = 0; i < patronsDb.length; i++) {
            if (patronsDb[i] != null) {
                if (patronsDb[i] == patron) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns true if the given number is an id of a patron in the library, false otherwise.
     * @param patronId - The id to check.
     * @return true if the given number is an id of a patron in the library, false otherwise.
     */
    boolean isPatronIdValid(int patronId) {
        if (patronId >= 0 && patronId < patronsDb.length) { //fail safe for out of range int supplied.
            return patronsDb[patronId] != null;
        } else {
            return false;
        }
    }

    /**
     * a private class method, count the number of books a given patron has borrowed from the libraray.
     * pay attention that this method is used in the borrowBook method, that makes the important test of
     * patronId validity.
     * @param patronId - the id of the patron, this method is called after this id was ensured as a
     *                 library patron id.
     * @return amount of books this patron has borrowed.
     */
    int patronBorrowedBooks(int patronId){
        int result = 0;
        for (int i = 0; i < bookShelf.length; i++) {
            if (bookShelf[i] != null ){
                if (bookShelf[i].getCurrentBorrowerId() == patronId){
                    result++;}
            }
        } return result;
    }


    /*----=  Library - Book - Patron Methods  =-----*/


    /**
     * Marks the book with the given id number as borrowed by the patron with the given patron id, if this
     * book is available, the given patron isn't already borrowing the maximal number of books allowed, and
     * if the patron will enjoy this book.
     * @param bookId - The id number of the book to borrow.
     * @param patronId - The id number of the patron that will borrow the book.
     * @return true if the book was borrowed successfully, false otherwise.
     */
    boolean borrowBook(int bookId, int patronId) {
        if (isBookIdValid(bookId) && isPatronIdValid(patronId) && //fail safe for later conditions.
                isBookAvailable(bookId) &&
                (patronBorrowedBooks(patronId) < borrowLimit) &&  //patron's below library borrow limit
                (patronsDb[patronId].willEnjoyBook(bookShelf[bookId]))) { //patron will enjoy this book
                bookShelf[bookId].setBorrowerId(patronId);
            return true;
        }
        return false;
    }

    /**
     * Return the given book.
     * @param bookId - The id number of the book to return.
     */
    void returnBook(int bookId){
        if (isBookIdValid(bookId)){
            bookShelf[bookId].returnBook();
        }
    }

    /**
     * Suggest the patron with the given id the book he will enjoy the most, out of all available books he
     * will enjoy, if any such exist.
     * @param patronId - The id number of the patron to suggest the book to.
     * @return The available book the patron with the given ID will enjoy the most. Null if no book is
     * available.
     */
    Book suggestBookToPatron(int patronId){
        int highestScore = 0; //book score by the preferences of this patron
        Book suggestedBook = null;
        if (isPatronIdValid(patronId)){
            for (int i = 0; i < bookShelf.length; i++) {
                if (isBookIdValid(i) && isBookAvailable(i)) {
                    int currentScore = patronsDb[patronId].getBookScore(bookShelf[i]);
                    if (patronsDb[patronId].willEnjoyBook(bookShelf[i]) && currentScore > highestScore){
                        highestScore = currentScore;
                        suggestedBook = bookShelf[i];
                    }
                }
            }
        } return suggestedBook;
    }
}

