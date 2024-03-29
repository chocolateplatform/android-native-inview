package sample.inline;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chocolateplatform.sample.inview.R;
import com.google.gson.Gson;
import com.vdopia.ads.lw.Chocolate;
import com.vdopia.ads.lw.InitCallback;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOBannerAd;
import com.vdopia.ads.lw.LVDOBannerAdListener;
import com.vdopia.ads.lw.LVDOConstants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.inline.model.Item;

public class MainActivity extends AppCompatActivity implements LVDOBannerAdListener {

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
        Chocolate.enableChocolateTestAds(true);
        Chocolate.enableLogging(true);
        if (Chocolate.isInitialized()) {
            loadData();
        } else {
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
        bannerAd = new LVDOBannerAd(this, LVDOAdSize.MEDIUM_RECT_300_250, this);
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
                    Log.d(TAG, "onBannerAdLoaded but there's already an ad nearby.  dont ad to list");
                    return;//there's already an ad nearby in the list
                }
            }
            vis = vis < Config.TRIGGER_DISTANCE ? Config.TRIGGER_DISTANCE : vis;
            Log.d(TAG, "onBannerAdLoaded and inserted");
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
        adapter.cleanUp();
        isAdRequestInProgress = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_choose_partners:
                choosePartners();
                return true;
            case R.id.menu_show_current_winner:
                if (bannerAd != null) {
                    new AlertDialog.Builder(this).setMessage("Current Winning Partner: " + bannerAd.getWinningPartnerName()).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choosePartners() {
        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INVIEW, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInviewPartners(adRequest);
            }
        });
    }
}
