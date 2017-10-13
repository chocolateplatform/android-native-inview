package sample.inline;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by kkawai on 10/12/17.
 */

public class TheApp extends MultiDexApplication {

   @Override
   protected void attachBaseContext(Context base) {
      super.attachBaseContext(base);
      MultiDex.install(this);

   }
}
