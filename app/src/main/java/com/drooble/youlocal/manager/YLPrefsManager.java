package com.drooble.youlocal.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public class YLPrefsManager {

	private static YLPrefsManager Instance;
	
	private SharedPreferences mSharedPref;

	private static final String PACKAGE = "com.drooble.youlocal.";

	private static final String SP_INIT = PACKAGE + "SharedPrefInit";

	// Setting Global
	public static final String SP_NAME = PACKAGE + "YLPrefsManager";

	public static final String SP_USER_NAME = PACKAGE + "userName";
	public static final String SP_USER_IMAGE_URL = PACKAGE + "userImageUrl";
	public static final String SP_USER_ABOUT_ME = PACKAGE + "userAboutMe";


	public static final String SP_TRUE = PACKAGE + "true";
	public static final String SP_FALSE = PACKAGE + "false";


	
	public static YLPrefsManager GetInstance() {
		if(Instance == null ) {
			Instance = new YLPrefsManager(YLContextManager.GetInstance().getContext());
		}
		return Instance;
	}

	private YLPrefsManager(Context context) {
		mSharedPref = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	public SharedPreferences getPreferences() {
		return mSharedPref;
	}
	
	public boolean isInit() {
		return mSharedPref.getBoolean(SP_INIT, false) == true;
	}
	
	public void setInit(Boolean isInit) {
		mSharedPref.edit().putBoolean(SP_INIT, isInit).commit();
	}
	
	public void setFloat(String key, Float value) {
		mSharedPref.edit().putFloat(key, value).commit();
	}
	
	public Float getFloat(String key) {
		Float f = mSharedPref.getFloat(key, Float.MIN_VALUE);
		return f == Float.MIN_VALUE ? null : f;
	}
	
	public void setString(String key, String value) {
		mSharedPref.edit().putString(key, value).commit();
	}
	
	public String getString(String key) {
		return mSharedPref.getString(key, null);
	}
	
	public void setInt(String key, Integer value) {
		mSharedPref.edit().putInt(key, value).commit();
	}
	
	public Integer getInt(String key) {
		Integer i = mSharedPref.getInt(key, Integer.MIN_VALUE);
		return i == Integer.MIN_VALUE ? null : i;
	}
	
	public void setLong(String key, Long value) {
		mSharedPref.edit().putLong(key, value).commit();
	}
	
	public Long getLong(String key) {
		Long i = mSharedPref.getLong(key, Long.MIN_VALUE);
		return i == Long.MIN_VALUE ? null : i;
	}
	
}
