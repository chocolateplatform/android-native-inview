package sample.inline;

/**
 * Created by kkawai on 10/12/17.
 */

public final class Config {
    private Config() {
    }

    //your vdopia app id; replace with your own
    //public static final String APP_ID = "Ea7TkX";
    //public static final String APP_ID = "llpHX8";
    //public static final String APP_ID = "XqjhRR";
    public static final String APP_ID = "XqjhRR";
    //public static final String APP_ID = "8fd33f36a843e42d7b5b725cb9989f17";

    /**
     * Distance between the ad position and the last visible item position on screen
     * in the recyclerView before triggering a new ad request.  Because it takes
     * time (sometimes up to 3 seconds) for an ad to get fetched, it's rare that
     * the next ad postion will be exactly TRIGGER_DISTANCE apart from the previous
     * ad position, unless the user was to completely pause scrolling for several seconds
     * after each row scroll.
     */
    public static final int TRIGGER_DISTANCE = 2;
}
