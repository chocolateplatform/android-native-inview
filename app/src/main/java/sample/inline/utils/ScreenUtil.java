package sample.inline.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


/**
 * Helper class for getting screen dimensions and doing dimensional pixel conversion;
 *
 */
public final class ScreenUtil {

    private ScreenUtil() {
    }

    private static float density = -1;

    static class Dimension {
        int width, height;
        float density;
    }

    /**
     * Get Screen Dimension related to a specified orientation
     *
     * @param context
     * @param rotation
     * @return
     */
    public static Dimension getDimensions(Context context, int rotation) {
        Dimension d = new Dimension();
        d.height = getScreenHeight(context);
        d.width = getScreenWidth(context);
        d.density = getDensity(context);
        return d;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static float getDensity(Context context) {
        if (density == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getDefaultDisplay(context).getMetrics(metrics);
            density = metrics.density;
        }
        return density;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * If device has physical navigation keys (BACK KEY) return 0.
     * However, if device does not have physical buttons, return
     * the height of the navigation bar.
     *
     * @return
     */
    public static int getNavigationBarHeight(final Activity activity) {

        int height = 0;

        final boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasBackKey) {
            final int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            height = resourceId > 0 ? activity.getResources().getDimensionPixelSize(resourceId) : 0;
        }

        if (height == 0)
            height = getCurrentNavigationBarHeight(activity);

        return height;

    }

    /**
     * Warning: only works on jelly bean mr1 or higher.
     *
     * @param activity
     * @return
     */
    private static int getCurrentNavigationBarHeight(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            final DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            final int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * For APIs that support it, calculate the actual screen width _currently_ taken up by on-screen buttons. This can happen on any
     * device. In landscape mode, it's ubiquitous on phones and can occur on tablets. This method is typically only useful in landscape
     * mode.
     *
     * @param activity
     * @return The actual screen width _currently_ taken up by on-screen buttons, if any.
     */
    @SuppressLint("NewApi")
    public static int getCurrentNavigationBarWidth(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            final DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final int usableWidth = metrics.widthPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            final int realWidth = metrics.widthPixels;
            if (realWidth > usableWidth) {
                return realWidth - usableWidth;
            } else {
                return 0;
            }
        } else {
            // If we can't calculate it, assume that any device that has soft keys is displaying the navigation bar right now.
            return getNavigationBarHeight(activity);
        }
    }

    /**
     * Attempts to apply a 'safe-zone' padding to a given view.
     * To be used in tv-related apps.
     *
     * @param view
     */
    public static void applySafeZoneToView(final View view) {

        final Display display = getDefaultDisplay(view.getContext());
        final Point size = new Point();
        display.getSize(size);
        final int padWidthPixels = (size.x / 10) / 2;
        final int padHeightPixels = (size.y / 10) / 2;
        view.setPadding(padWidthPixels, padHeightPixels, padWidthPixels, padHeightPixels);
    }

    public static void hideKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(final View view) {
        if (view != null) {
            ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
