package com.drooble.youlocal.util;

import android.content.Context;

import com.drooble.youlocal.manager.YLContextManager;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLResourceUtil {

    public static String getStringFromResource(int resourceId) {
        Context context = YLContextManager.GetInstance().getContext();
        return context.getString(resourceId);
    }

}
