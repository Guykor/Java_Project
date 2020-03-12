import oop.ex3.searchengine.Hotel;

import java.util.Comparator;

/**
 * sorted according to their (euclidean) distance from the given geographic
 * location, in ascending order. Hotels that are at the same distance from the given location are organized
 * according to the number of points-of-interest for which they are close to (in a decreasing order).
 */
public class HotelProximityComparator implements Comparator<Hotel> {
    private double[] comparisonPoint = new double[2];

    HotelProximityComparator(double latitude, double longitude) {
        this.comparisonPoint[0] = latitude;
        this.comparisonPoint[1] = longitude;

    }

    @Override
    public int compare(Hotel hotel1, Hotel hotel2) {
        int whoIsCloser = Double.compare(this.distanceTo(hotel1), this.distanceTo(hotel2));
        return (whoIsCloser != 0 ? whoIsCloser : hotel1.getNumPOI() - hotel2.getNumPOI());
    }

    private double distanceTo(Hotel hotel) {
        double longDiffQuadratic = Math.pow((hotel.getLongitude() - this.comparisonPoint[1]), 2);
        double latDiffQuadratic = Math.pow((hotel.getLatitude() - this.comparisonPoint[0]), 2);
        return Math.sqrt(latDiffQuadratic + longDiffQuadratic);

    }
}
