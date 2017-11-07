package sample.inline;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;

import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.PreRollVideoAd;
import com.vdopia.ads.lw.PrerollAdListener;

import java.util.ArrayList;

import sample.inline.preroll.PrerollActivity;
import sample.inline.preroll.PrerollFragment;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPreroll();  //Comment this line out if you don't want to prefetch preroll ads
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.preroll_fragment) {
            showPrerollAdFragment();
            return true;
        } else if (id == R.id.preroll_activity) {
            showPrerollAdActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPrerollAdFragment() {
        Fragment fragment = PrerollFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment, PrerollFragment.TAG).addToBackStack(null).commit();
    }

    private void showPrerollAdActivity() {
        startActivity(new Intent(this, PrerollActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                getSupportActionBar().setTitle(R.string.app_name);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            return;
        }
        super.onBackPressed();
    }

    private PreRollVideoAd preRollVideoAd;

    private void requestPreroll() {
        LVDOAdSize adSize = new LVDOAdSize(300, 250);

        preRollVideoAd = PreRollVideoAd.getInstance(this);
        preRollVideoAd.setMediaController(new MediaController(this));

        preRollVideoAd.setPrerollAdListener(new PrerollAdListener() {
            @Override
            public void onPrerollAdLoaded(View prerollAd) {
                Log.v(TAG, "onPrerollAdLoaded..." + prerollAd);
            }

            @Override
            public void onPrerollAdFailed(View prerollAd, LVDOConstants.LVDOErrorCode errorCode) {
                Log.v(TAG, "onPrerollAdFailed..." + errorCode.toString());
            }

            @Override
            public void onPrerollAdShown(View prerollAd) {
                Log.v(TAG, "onPrerollAdShown...");
            }

            @Override
            public void onPrerollAdClicked(View prerollAd) {
                Log.v(TAG, "onPrerollAdClicked...");
            }

            @Override
            public void onPrerollAdCompleted(View prerollAd) {
                Log.v(TAG, "onPrerollAdCompleted...");
            }

            @Override
            public void onPrepareMainContent(MediaPlayer player) {
                Log.v(TAG, "onPrepareMainContent...");
            }

            @Override
            public void onErrorMainContent(MediaPlayer player, int code) {
                Log.v(TAG, "onErrorMainContent...");
            }

            @Override
            public void onCompleteMainContent(MediaPlayer player) {
                Log.v(TAG, "onCompleteMainContent...");
            }
        });

        preRollVideoAd.loadAd(getAdRequest(), Config.APP_ID, adSize, this, true);
    }

    private LVDOAdRequest getAdRequest() {
        LVDOAdRequest adRequest = new LVDOAdRequest(this);

        ArrayList<LVDOConstants.PARTNERS> partnerNames = new ArrayList<>();
        LVDOConstants.PARTNERS partner = LVDOConstants.PARTNERS.ALL;
        partnerNames.add(partner);
        adRequest.setPartnerNames(partnerNames);

        //        LocationData locationData = new LocationData(AdListActivity.this);
        //        adRequest.setLocation(locationData.getDeviceLocation());

        adRequest.setAge("27");
        adRequest.setDmaCode("807");
        adRequest.setEthnicity("Asian");
        adRequest.setPostalCode("110096");
        adRequest.setCurrPostal("201301");
        adRequest.setMaritalStatus(LVDOAdRequest.LVDOMartialStatus.Single);
        //        adRequest.setBirthday(Utils.getDate());
        adRequest.setGender(LVDOAdRequest.LVDOGender.MALE);

        adRequest.setRequester("Vdopia");
        adRequest.setAppBundle("chocolateApp");
        adRequest.setAppDomain("vdopia.com");
        adRequest.setAppName("VdopiaSampleApp");
        adRequest.setAppStoreUrl("play.google.com");
        adRequest.setCategory("prerollad");
        adRequest.setPublisherDomain("vdopia.com");

        return adRequest;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preRollVideoAd != null) {
            preRollVideoAd.destroyView();
        }
    }
}
