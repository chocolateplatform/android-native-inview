package sample.inline;

public final class Config {
    private Config() {
    }

    public static final String APP_ID = "XqjhRR";

    /**
     * Distance between the ad position and the last visible item position on screen
     * in the recyclerView before triggering a new ad request.  Because it takes
     * time (sometimes up to 3 seconds) for an ad to get fetched, it's rare that
     * the next ad postion will be exactly TRIGGER_DISTANCE apart from the previous
     * ad position, unless the user was to completely pause scrolling for several seconds
     * after each row scroll.
     */
    public static final int TRIGGER_DISTANCE = 3;
}
