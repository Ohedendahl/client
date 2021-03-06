package tig167.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ActivitySwitcher {

    private static final String LOG_TAG = ActivitySwitcher.class.getSimpleName();

    public static void switchActivity(Class c, Activity context) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

  /*  public static void switchToSemiSeparateActivity(Activity context) {
        switchActivity(SemiSeparateActivity.class, context);
    }*/

    public static void switchMainActivity(Activity context) {
        switchActivity(MainActivity.class, context);
    }

  /*  public static void switchToVolleyActivity(Activity context) {
        switchActivity(VolleyActivity.class, context);
    }*/

    public static void showToast(Context context, String msg) {
        Log.d(LOG_TAG, " showToast: " + msg);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }


}