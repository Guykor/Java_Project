import oop.ex3.searchengine.Hotel;
import oop.ex3.searchengine.HotelDataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * this class defines Booping.com, hotel booking site, that allows for personalized search
 * methodologies. Specifically, this class provides the users  with the ability to get a list of hotels
 * based on different parameters. The hotels you are provided with are a  part of a larger hotel dataset.
 */

public class BoopingSite {
    private Hotel[] hotelDB;

    /**
     * The booping constructor, construct an Hotel array (DB) based on a file.
     * , if the name is invalid, the getHotels method will return an empty Hotel array.
     *
     * @param name name of file containing the data set
     */
    public BoopingSite(String name) {
        try {
            hotelDB = HotelDataset.getHotels(name);
        } catch (NullPointerException e) {
            hotelDB = new Hotel[0];
        }
    }

    /**
     * This method returns an array of hotels located in the given city, sorted from the highest
     * star-rating to the lowest.
     * In case there are no hotels in the given city, this method returns an empty array.
     *
     * @param city the city filter
     * @return a sorted hotel array by ranking in the given city.
     */
    public Hotel[] getHotelsInCityByRating(String city) {
        ArrayList<Hotel> cityHotels = this.filterByCity(city);
        Collections.sort(cityHotels, new HotelRatingComparator());
        return cityHotels.toArray(new Hotel[0]);
    }

    /**
     * This method returns an array of hotels, sorted according to their (euclidean) distance from the
     * given geographic location, in ascending order.
     * Hotels that are at the same distance from the given location are organized
     * according to the number of points-of-interest for which they are close to (in a decreasing order).
     * In case of illegal input, this method returns an empty array.
     *
     * @param latitude  double in WGS geo system
     * @param longitude double in WGS geo system
     * @return Hotel array sorted by proximity to location given.
     */
    public Hotel[] getHotelsByProximity(double latitude, double longitude) {
        return sortByProximityToPoint(latitude, longitude, this.hotelDB);
    }

    /**
     * This method returns an array of hotels in the given city, sorted according to their (euclidean)
     * distance
     * from the given geographic location, in ascending order. Hotels that are at the same distance from the
     * given location are organized according to the number of points-of-interest for which they are close to
     * (in a decreasing order). In case of illegal input, this method returns an empty array.
     *
     * @param city      - city name to filter table by.
     * @param latitude  double in WGS geo system
     * @param longitude double in WGS geo system
     * @return Hotel array sorted by proximity to the location given, but only hotels in the given city.
     */
    public Hotel[] getHotelsInCityByProximity(String city, double latitude, double longitude) {
        ArrayList<Hotel> cityHotels = this.filterByCity(city);
        return sortByProximityToPoint(latitude, longitude, cityHotels.toArray(new Hotel[0]));
    }

    /**
     * this method takes coordinates in WGS geo system and a Hotel array and sort it by the distance of
     * each hotel to the location given.
     */
    private Hotel[] sortByProximityToPoint(double latitude, double longitude, Hotel[] hotels) {
        if (this.coordinatesAreValid(latitude, longitude)) {
            HotelProximityComparator proximityComparatorToPoint = new HotelProximityComparator(latitude,
                    longitude);
            Hotel[] sortedArray = hotels.clone();
            Arrays.sort(sortedArray, proximityComparatorToPoint);
            return sortedArray;
        }
        return new Hotel[0];
    }

    /**
     * this method takes an Hotel array and returns a filtered ArrayList of Hotels only in the specified city.
     *
     * @param city a filed in the data set to filter by.
     * @return an ArrayList<Hotel> containing subset of the Booping Hotel DB, only for hotels in the
     * city given.
     */
    private ArrayList<Hotel> filterByCity(String city) {
        ArrayList<Hotel> filteredHotels = new ArrayList<Hotel>();
        for (Hotel h : hotelDB) {
            if (h.getCity().equals(city)) {
                filteredHotels.add(h);
            }
        }
        return filteredHotels;
    }


    /**
     * check if the given coordinates in WGS geo system are valid.
     */
    private boolean coordinatesAreValid(double latitude, double longitude) {
        return (Math.abs(latitude) <= 90 && Math.abs(longitude) <= 180);
    }
}
