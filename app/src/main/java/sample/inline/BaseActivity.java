package sample.inline;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.atlassianvdopia.R;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

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
}
