package sample.inline.preroll;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.PreRollVideoAd;
import com.vdopia.ads.lw.PrerollAdListener;

import sample.inline.Config;
import sample.inline.R;
import sample.inline.databinding.LayoutPrerollBinding;

public class PrerollActivity extends AppCompatActivity implements PrerollAdListener {

    public static final String TAG = "PrerollActivity";

    private LayoutPrerollBinding binding;
    private boolean isMainContentFullscreen = true;
    private PreRollVideoAd preRollVideoAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_preroll);
        getSupportActionBar().setTitle(R.string.preroll_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestPreroll();
    }

    private void requestPreroll() {

        preRollVideoAd = new PreRollVideoAd(this);
        preRollVideoAd.setMediaController(new MediaController(this));

        if (preRollVideoAd.isReady()) {
            Log.d(TAG, "requestPreroll() play ad from cache");
            showPrerollAd();
        } else {
            Log.d(TAG, "requestPreroll() request new ads");
            preRollVideoAd.loadAd(getAdRequest(), Config.APP_ID, LVDOAdSize.IAB_MRECT, PrerollActivity.this, isMainContentFullscreen);
        }
    }

    private LVDOAdRequest getAdRequest() {
        LVDOAdRequest adRequest = new LVDOAdRequest(this);

        //ArrayList<LVDOConstants.PARTNERS> partnerNames = new ArrayList<>();
        //LVDOConstants.PARTNERS partner = LVDOConstants.PARTNERS.ALL;
        //partnerNames.add(partner);
        //adRequest.setPartnerNames(partnerNames);

        //        LocationData locationData = new LocationData(AdListActivity.this);
        //        adRequest.setLocation(locationData.getDeviceLocation());

        adRequest.setAge("27");
        adRequest.setDmaCode("807");
        adRequest.setEthnicity("Asian");
        adRequest.setPostalCode("110096");
        adRequest.setCurrPostal("201301");
        adRequest.setMaritalStatus(LVDOAdRequest.LVDOMartialStatus.SINGLE);
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

    private void showPrerollAd() {
        LVDOAdRequest adRequest = getAdRequest();
        String contentVideo = "http://cdn.vdopia.com/files/happy.mp4";
        preRollVideoAd.setVideoPath(contentVideo);

        ViewGroup parent = (ViewGroup) preRollVideoAd.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        binding.adLayout.addView(preRollVideoAd);

        preRollVideoAd.showAd();
    }

    private void setContentVisibility() {
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams = binding.adLayout.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            binding.adLayout.setLayoutParams(layoutParams);

            ((AppCompatActivity) this).getSupportActionBar().hide();
        } else {
            ViewGroup.LayoutParams layoutParams = binding.adLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.preroll_video_height);
            binding.adLayout.setLayoutParams(layoutParams);

            ((AppCompatActivity) this).getSupportActionBar().show();
        }
    }

    @Override
    public void onPrerollAdLoaded(View prerollAd) {
        Log.v(TAG, "onPrerollAdLoaded..." + prerollAd + " prerollAd.isReady(): " + "" + preRollVideoAd.isReady());
        /**
         * Preroll is now ready to be played.
         */
        showPrerollAd();
    }

    @Override
    public void onPrerollAdFailed(View prerollAd, LVDOConstants.LVDOErrorCode errorCode) {
        Log.v(TAG, "onPrerollAdFailed..." + errorCode.toString() + " Just play video " + "content");
        /**
         * If a preroll ad was requested, but failed (due to no inventory), then showPrerollAd()
         * will result in the main content to be played.
         */
        showPrerollAd();
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
        setContentVisibility();
    }

    @Override
    public void onCompleteMainContent(MediaPlayer player) {
        Log.v(TAG, "onCompleteMainContent...");
        setContentVisibility();
    }

}
