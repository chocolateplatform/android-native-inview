package sample.inline;

/**
 * Created by kkawai on 10/12/17.
 */

public final class Config {
   private Config() {
   }

   //your vdopia app id; replace with your own
   //public static final String APP_ID = "Ea7TkX";
   public static final String APP_ID = "llpHX8";

   /**
    * Distance between ads position and the last completely visible item position
    * in the recyclerView before triggering a new ad request.  Because it takes
    * time (sometimes up to 3 seconds) for an ad to get fetched, it's rare that
    * the next ad postion will be exactly TRIGGER_DISTANCE apart from the previous
    * ad position, unless the user was to completely stop scrolling for like 5 seconds
    * after scrolling each tile.
    */
   public static final int TRIGGER_DISTANCE = 2;
}
