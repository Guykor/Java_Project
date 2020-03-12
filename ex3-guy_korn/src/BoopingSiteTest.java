import oop.ex3.searchengine.Hotel;
import org.junit.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This is a Test Class for the Booping site method, this test uses two toy data sets to check desired output.
 */

public class BoopingSiteTest {
    /**
     * First Data Set containing 70 entries, all in the city of manali.
     */
    private static BoopingSite shortHotelSite;

    /**
     * an empty data set, will be used to check edge cases.
     */
    private static BoopingSite emptySite;

    /**
     * the whole data set supplied, containing more than 3000 records.
     */
    private static BoopingSite fullHotelSite;

    /**
     * initialize Sites with the test data sets.
     */
    @BeforeClass
    public static void createTestObjects() {
        shortHotelSite = new BoopingSite("hotels_tst1.txt");
        emptySite = new BoopingSite("hotels_tst2.txt");
        fullHotelSite = new BoopingSite("hotels_dataset.txt");
    }

    /////////////// CONSTRUCTOR TESTS ///////////////

    /**
     * this test examines that the API methods acts properly when an invalid file name is given to the
     * Booping Site constructor.
     */
    @Test
    public void nonExistingTable() {
        BoopingSite NullSite = new BoopingSite("fail");
        Hotel[] emptyArr = new Hotel[0];
        assertArrayEquals("should return empty hotel array", emptyArr, NullSite.getHotelsInCityByRating("M"));
        assertArrayEquals("should return empty hotel array", emptyArr, NullSite.getHotelsByProximity(0, 0));
        assertArrayEquals("should return empty hotel array", emptyArr, NullSite.getHotelsInCityByProximity(
                "mcdsf", 0, 0));
    }


    ///////////GET HOTELS BY CITY TESTS ////////////

    /**
     * check that the method works properly given a invalid city, or that the city doesn't appear in data
     * set.
     */
    private void nonRegisteredCityTest(BoopingSite tested) {
        Hotel[] expectedHotels = new Hotel[0];
        assertArrayEquals("check for empty string as a city", expectedHotels,
                tested.getHotelsInCityByRating(""));
        assertArrayEquals("check for non recognized String", expectedHotels,
                tested.getHotelsInCityByRating("dcfgvhbjnk"));
        assertArrayEquals("check for non recognized String", expectedHotels,
                tested.getHotelsInCityByRating("454213"));
    }

    /**
     * check that the method works properly given a invalid city, or that the city doesn't appear in data
     * set.
     */
    @Test
    public void TestUnregisteredCity() {
        this.nonRegisteredCityTest(shortHotelSite);
        this.nonRegisteredCityTest(emptySite);
    }

    /**
     * basic check when city is in data set
     */
    @Test
    public void checkForCityRecognition() {
        assertEquals("check if all the entries of a specified city were filtered ", 70,
                shortHotelSite.getHotelsInCityByRating("manali").length);
    }

    /**
     * this method check if every entry in an array is from a city name used to filter the dataset, namely
     * the filtering works.
     *
     * @param expectedCity - the city used to filter the dataset.
     * @param resultArray  - the array returned from a city filter method.
     */
    private void checkAllResultInTheSameCity(String expectedCity, Hotel[] resultArray) {
        for (Hotel h : resultArray) {
            assertEquals(expectedCity, h.getCity());
        }
    }

    @Test
    public void checkIfOnlyManaliAppears() {
        this.checkAllResultInTheSameCity("manali", fullHotelSite.getHotelsInCityByRating("manali"));
    }

    /**
     * checks if the method truly returned what it should have return, compared to results who were made
     * manually. this test check only a sample of the data set (top and bottom five hotels).
     */
    @Test
    public void cityOutputCheck() {
        String[] topFiveRanked = {
                "baragarh villa",
                "bharhka countryside cottage resort",
                "citrus manali resorts",
                "the serenity resort &amp; spa",
                "thomas villa, hotel and cottages"};
        String[] bottomFiveRanked = {
                "woodyvu mansari cottage",
                "wood valley cottages",
                "white pod manali",
                "village view",
                "village classic cottage"
        };
        Hotel[] result = shortHotelSite.getHotelsInCityByRating("manali");
        this.checkForSampleOutput(topFiveRanked, bottomFiveRanked, result);
    }

    /**
     * checks if the method truly returned what it should have return, compared to results who were made
     * manually. this test preform the check automatically, checking if all the records are organized like
     * they should.
     */
    @Test
    public void checkRankOutputIteratively() {
        Hotel[] result = shortHotelSite.getHotelsInCityByRating("manali");
        for (int i = 1; i < result.length - 1; i++) {
            assertTrue(result[i].getStarRating() >= result[i + 1].getStarRating());
        }
    }

    //////////GET HOTELS BY PROXIMITY TESTS ///////////

    /**
     * this test will check for invalid coordinates given to the get hotel method. llegal
     * coordinates in
     * WGS mapping system is when latitude in range [-90,90] and longitude in [-180,180].
     */
    @Test
    public void checkForInvalidCoordinates() {
        Hotel[] expected = new Hotel[0];
        assertArrayEquals(expected, shortHotelSite.getHotelsByProximity(100, 360));
        assertArrayEquals(expected, shortHotelSite.getHotelsByProximity(-100, -360));
        assertArrayEquals(expected, shortHotelSite.getHotelsByProximity(100, -360));
        assertArrayEquals(expected, shortHotelSite.getHotelsByProximity(-100, 360));
        assertArrayEquals("for an empty data set", expected,
                emptySite.getHotelsByProximity(100, 360));
    }

    /**
     * Test a sample of top and bottom 5 hotels that suppose to be in the edges of the sorted array
     * according to manual check on the dataset.
     * this test check proximity to the point (0,0).
     */
    @Test
    public void proximityOutputCheck() {
        //Distance From (0,0)
        String[] topFive = {
                "bazira cottages",
                "country cottage manali",
                "hotel ragini",
                "baragarh regency",
                "bharhka countryside cottage resort"};
        String[] bottomFive = {
                "solang cottage",
                "wood valley cottages",
                "hotel river view",
                "whispering valley resort",
                "hotel river inn"};
        this.checkForSampleOutput(topFive, bottomFive, shortHotelSite.getHotelsByProximity(0, 0));
    }


    /**
     * this method checks for a given array if the top and bottom 5 entries are as expected.
     *
     * @param topHotels    top five hotels in the array.
     * @param bottomHotels bottom five hotels in the array.
     * @param actual       the array needs to be checked.
     */
    private void checkForSampleOutput(String[] topHotels, String[] bottomHotels, Hotel[] actual) {
        for (int i = 0; i < 4; i++) {
            assertEquals("check for top star ranked hotel in first alphabetical order",
                    topHotels[i], actual[i].getPropertyName());
            assertEquals("check for last star rank and alphabetical order", bottomHotels[i],
                    actual[actual.length - 1 - i].getPropertyName());
        }
    }


    /**
     * Test to check if all the output entries in the toy data set are following the distance condition of
     * the comparing methods.
     */
    @Test
    public void checkProximityOutputIteratively() {
        Hotel[] result = shortHotelSite.getHotelsByProximity(0, 0);
        HotelProximityComparator comparator = new HotelProximityComparator(0, 0);
        for (int i = 1; i < result.length - 1; i++) {
            assertTrue(comparator.compare(result[i], result[i + 1]) <= 0);
        }
    }

    /**
     * Test to check if all the output entries in the toy data set are ordered by their POI number if they
     * have the same distance to (0,0).
     */
    @Test
    public void checkProximityPoiOutputIteratively() {
        Hotel[] result = shortHotelSite.getHotelsByProximity(0, 0);
        HotelProximityComparator comparator = new HotelProximityComparator(0, 0);
        for (int i = 1; i < result.length - 1; i++) {
            if (comparator.compare(result[i], result[i + 1]) == 0) {
                assertTrue(result[i].getNumPOI() >= result[i + 1].getNumPOI());
            }
        }
    }


    /////////////TESTS FOR THE CITY PROXIMITY METHOD //////////////////

    /**
     * This test relies on the implementation assumption that this method relies on the city and proxmity
     * sorting methods. I have decided to maintain only this test because it's seem reasonable to check the
     * code according to my implementation in order to avoid code duplication.
     */
    @Test
    public void checkForSingleCityDataSet() {
        assertArrayEquals(shortHotelSite.getHotelsInCityByProximity("manali", 0, 0),
                (shortHotelSite.getHotelsByProximity(0, 0)));
    }

    /**
     * make sure that only the city required is returned in the array.
     */
    @Test
    public void checkCityFilterOnBigTable() {
        this.checkAllResultInTheSameCity("manali", fullHotelSite.getHotelsInCityByProximity("manali", 0, 0));
    }

}



