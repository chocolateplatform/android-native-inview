package sample.inline;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.vdopia.ads.lw.Chocolate;
import com.vdopia.ads.lw.InitCallback;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOBannerAd;
import com.vdopia.ads.lw.LVDOBannerAdListener;
import com.vdopia.ads.lw.LVDOConstants;

import net.atlassianvdopia.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.inline.model.Item;

public class MainActivity extends BaseActivity implements LVDOBannerAdListener {

    public static final String TAG = "MainActivity";

    private Adapter adapter;
    private LinearLayoutManager lm;
    private LVDOBannerAd bannerAd;
    private boolean isAdRequestInProgress;
    private int scrollDy;
    private LVDOAdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new LVDOAdRequest(this);
        //Chocolate.enableChocolateTestAds(true);
        //VdopiaLogger.enable(true);
        if (Chocolate.isInitialized()) {
            loadData();
        } else {
            LVDOAdRequest adRequest = new LVDOAdRequest(this);
            Chocolate.init(this, Config.APP_ID, adRequest, new InitCallback() {
                @Override
                public void onSuccess() {
                    loadData();
                }

                @Override
                public void onError(String s) {

                }
            });
        }

        adapter = new Adapter(this);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager((lm = new LinearLayoutManager(this)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "SCROLL_STATE_IDLE");
                    int adPos = adapter.getAdPosition();
                    int vis = scrollDy >= 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();
                    if (Math.abs(vis - adPos) >= Config.TRIGGER_DISTANCE) {
                        requestAd();
                        Log.d(TAG, "REQUEST NEW AD");
                    } else {
                        Log.d(TAG, "SCROLL_STATE_IDLE BUT DON'T REQUEST NEW AD");
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "scrolled dy: " + dy);
                /**
                 * A negative dy value means the user is scrolling in the upwards direction
                 * and a positive dy value means the user is scrolling downwards.
                 */
                scrollDy = dy;
            }
        });
    }

    private void loadData() {
        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected void onPostExecute(List<Item> items) {
                ArrayList<Adapter.ItemWrapper> list = new ArrayList<>(items.size());
                for (Item item : items) {
                    list.add(new Adapter.ItemWrapper(item));
                }
                adapter.setItemWrappers(list);
                requestAd();
            }

            @Override
            protected List<Item> doInBackground(Void... voids) {
                try {
                    return Arrays.asList(new Gson().fromJson(new InputStreamReader(MainActivity.this.getAssets().open("media_list.json")), Item[].class));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    private void requestAd() {
        if (isAdRequestInProgress) {
            Log.d(TAG, "requestAd (don't do; already in progress)");
            return;
        } else {
            Log.d(TAG, "requestAd");
            isAdRequestInProgress = true;
        }
        if (bannerAd == null)
            bannerAd = new LVDOBannerAd(this, LVDOAdSize.INVIEW_LEADERBOARD, Config.APP_ID, this);
        bannerAd.loadAd(adRequest);
    }

    @Override
    public void onBannerAdLoaded(View banner) {
        Log.d(TAG, "onBannerAdLoaded");
        isAdRequestInProgress = false;
        if (banner != null) {
            int vis = scrollDy >= 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();

            int adPos = adapter.getAdPosition();
            if (adPos != -1) {
                if (Math.abs(vis - adPos) < Config.TRIGGER_DISTANCE) {
                    return;//there's already an ad nearby in the list
                }
            }
            vis = vis < Config.TRIGGER_DISTANCE ? Config.TRIGGER_DISTANCE : vis;
            adapter.insertAd(vis, banner, bannerAd);
        }
    }

    @Override
    public void onBannerAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        Log.d(TAG, "onBannerAdFailed: " + lvdoErrorCode);
        isAdRequestInProgress = false;
    }

    @Override
    public void onBannerAdClicked(View view) {
        Log.d(TAG, "onBannerAdClicked");
    }

    @Override
    public void onBannerAdClosed(View view) {
        Log.d(TAG, "onBannerAdClosed");
        adapter.removeAd();
        isAdRequestInProgress = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.removeAd();
    }
}
