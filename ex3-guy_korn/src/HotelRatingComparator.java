import oop.ex3.searchengine.Hotel;

import java.util.Comparator;

/**
 * Hotels that have the same rating will be organized according to the alphabet order of
 * the hotel’s (property) name.
 */

public class HotelRatingComparator implements Comparator<Hotel> {
    @Override
    public int compare(Hotel hotel1, Hotel hotel2) {
        int rating = hotel2.getStarRating() - hotel1.getStarRating();
        return (rating != 0 ? rating : hotel1.getPropertyName().compareTo(hotel2.getPropertyName()));
    }
}

