package sample.inline.preroll;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.PreRollVideoAd;
import com.vdopia.ads.lw.PrerollAdListener;

import java.util.ArrayList;

import sample.inline.Config;
import sample.inline.R;
import sample.inline.databinding.FragmentPrerollBinding;

public class PrerollFragment extends Fragment implements PrerollAdListener {

    public static final String TAG = "PrerollFragment";

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        PrerollFragment fragment = new PrerollFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentPrerollBinding binding;
    private boolean isPrerollAdLoaded;
    private boolean isMainContentFullscreen = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preroll, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.preroll);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestPreroll();
    }

    private PreRollVideoAd preRollVideoAd;

    private void requestPreroll() {
        LVDOAdSize adSize = new LVDOAdSize(300, 250);

        preRollVideoAd = PreRollVideoAd.getInstance(getActivity());
        preRollVideoAd.setMediaController(new MediaController(getActivity()));

        preRollVideoAd.setPrerollAdListener(new PrerollAdListener() {
            @Override
            public void onPrerollAdLoaded(View prerollAd) {
                Log.v(TAG, "onPrerollAdLoaded..." + prerollAd);
                synchronized (this) {
                    if (isPrerollAdLoaded) {
                        return;
                    }
                    isPrerollAdLoaded = true;
                }
                showPrerollAd();
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

        preRollVideoAd.loadAd(getAdRequest(), Config.APP_ID, adSize, getActivity(), isMainContentFullscreen);
    }

    private LVDOAdRequest getAdRequest() {
        LVDOAdRequest adRequest = new LVDOAdRequest(getActivity());

        //        ArrayList<LVDOConstants.PARTNERS> mPartnerNames = new ArrayList<>();
        //        LVDOConstants.PARTNERS partner = LVDOConstants.PARTNERS.INMOBI;
        //        mPartnerNames.add(partner);
        //        adRequest.setPartnerNames(mPartnerNames);

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

    private void showPrerollAd() {
        LVDOAdRequest adRequest = getAdRequest();

        ArrayList<LVDOConstants.PARTNERS> mPartnerNames = new ArrayList<>();
        LVDOConstants.PARTNERS partner = LVDOConstants.PARTNERS.ALL;
        mPartnerNames.add(partner);
        adRequest.setPartnerNames(mPartnerNames);

        preRollVideoAd = PreRollVideoAd.getInstance(getActivity());
        preRollVideoAd.setAdContext(getActivity());

        String contentVideo = "http://cdn.vdopia.com/files/happy.mp4";
        preRollVideoAd.setVideoPath(contentVideo);
        preRollVideoAd.setAdRequest(adRequest);
        preRollVideoAd.setPrerollAdListener(this);

        ViewGroup parent = (ViewGroup) preRollVideoAd.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        binding.adLayout.addView(preRollVideoAd);

        preRollVideoAd.showAd();
    }

    @Override
    public void onPrerollAdLoaded(View prerollAd) {
        Log.d(TAG, "PreRoll Video Ad onPrerollAdLoaded");
    }

    @Override
    public void onPrerollAdFailed(View prerollAd, LVDOConstants.LVDOErrorCode errorCode) {
        Log.d(TAG, "PreRoll Video Ad onPrerollAdFailed: " + errorCode);
    }

    @Override
    public void onPrerollAdShown(View prerollAd) {
        Log.d(TAG, "PreRoll Video Ad onPrerollAdShown");
    }

    @Override
    public void onPrerollAdClicked(View prerollAd) {
        Log.d(TAG, "PreRoll Video Ad onPrerollAdClicked");
    }

    @Override
    public void onPrerollAdCompleted(View prerollAd) {
        Log.d(TAG, "PreRoll Video Ad onPrerollAdCompleted");
    }

    @Override
    public void onPrepareMainContent(MediaPlayer player) {
        Log.d(TAG, "PreRoll Video onPrepareMainContent");
    }

    @Override
    public void onErrorMainContent(MediaPlayer player, int code) {
        Log.d(TAG, "PreRoll Video onErrorMainContent : " + code);
        setContentVisibility();
    }

    @Override
    public void onCompleteMainContent(MediaPlayer player) {
        Log.d(TAG, "PreRoll Video onCompleteMainContent");
        setContentVisibility();
    }

    private void setContentVisibility() {
        //preRollVideoAd.destroyView();
        if (isMainContentFullscreen) {
            //todo: user has to hit back button, but we need to fix that
        } else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!isMainContentFullscreen && preRollVideoAd != null)
            preRollVideoAd.destroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams lp_L = (LinearLayout.LayoutParams) binding.adLayout.getLayoutParams();
            lp_L.width = LinearLayout.LayoutParams.MATCH_PARENT;
            lp_L.height = LinearLayout.LayoutParams.MATCH_PARENT;
            binding.adLayout.setLayoutParams(lp_L);

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } else {
            LinearLayout.LayoutParams lp_L = (LinearLayout.LayoutParams) binding.adLayout.getLayoutParams();
            lp_L.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_L.height = ViewGroup.LayoutParams.MATCH_PARENT;
            binding.adLayout.setLayoutParams(lp_L);

            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }

}
