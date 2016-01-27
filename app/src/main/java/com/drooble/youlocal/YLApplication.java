package com.drooble.youlocal;

import android.app.Application;

import com.drooble.youlocal.manager.YLContextManager;
import com.drooble.youlocal.manager.YLPrefsManager;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        YLContextManager.Create(this);
        YLPrefsManager.GetInstance().setInit(true);
    }
}
