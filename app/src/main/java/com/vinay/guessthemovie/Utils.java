package com.vinay.guessthemovie;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public class Utils {


    public static void showAlert(Activity activity, String message) {
        new AlertDialog.Builder(activity).setMessage((CharSequence) message).setPositiveButton((CharSequence) "Ok", null).create().show();
    }

    public static void internetAlert(Activity activity) {
        new AlertDialog.Builder(activity).setMessage((CharSequence) "Please check internet connection.").setPositiveButton((CharSequence) "Ok", null).create().show();
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        Log.v("TAG", "Internet Connection Not Present");
        return false;
    }
}
