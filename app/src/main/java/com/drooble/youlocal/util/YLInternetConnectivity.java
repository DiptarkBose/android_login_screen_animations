package com.drooble.youlocal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.drooble.youlocal.manager.YLContextManager;

/**
 * Created by Cec on 3/24/15.
 */
public class YLInternetConnectivity {

    public static boolean hasInternet() {
        Context context = YLContextManager.GetInstance().getContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }
}
