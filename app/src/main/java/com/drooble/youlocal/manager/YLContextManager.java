package com.drooble.youlocal.manager;

import android.content.Context;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLContextManager {

    private static YLContextManager Instance;
    
    private Context mApplicationContext;
    
    public static YLContextManager GetInstance() {
        if(Instance == null) {
            throw new RuntimeException("Before use YLContextManager you should init him");
        }
        return Instance;
    }
    
    public static void Create(Context context) {
        if(Instance == null) {
        	Instance = new YLContextManager(context.getApplicationContext());
        }
    }
    
    private YLContextManager(Context context) {
    	this.mApplicationContext = context;
    }
    
    public Context getContext() {
    	return mApplicationContext;
    }
    
}
