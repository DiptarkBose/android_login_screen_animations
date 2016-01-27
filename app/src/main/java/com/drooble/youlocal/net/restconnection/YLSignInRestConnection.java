package com.drooble.youlocal.net.restconnection;

import android.util.Log;


import com.drooble.youlocal.manager.YLPrefsManager;
import com.drooble.youlocal.net.API.YLSignInApi;
import com.drooble.youlocal.net.base.YLBaseRequestModel;
import com.drooble.youlocal.net.base.YLBaseRestConnection;
import com.drooble.youlocal.util.enums.YLContentType;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public class YLSignInRestConnection extends YLBaseRestConnection {

    public static final String TAG = YLSignInRestConnection.class.getSimpleName();

    private static YLSignInRestConnection Instance;

    String mPassword;
    String mEmail;

    public static YLSignInRestConnection GetInstance() {
        if(Instance == null)
            Instance = new YLSignInRestConnection();
        return Instance;
    }

    private YLSignInRestConnection() {
        super();
        setTag(TAG);
    }

    public YLSignInRestConnection setParams(String username, String password) {
        this.mEmail = username;
        this.mPassword = password;
        return this;
    }

    @Override
    public void onConnect() {
        super.onConnect();

        YLSignInApi signInApi = mRestAdapter.create(YLSignInApi.class);
        signInApi.doSignIn(mEmail, mPassword, new Callback<YLBaseRequestModel<Void>>() {

            @Override
            public void success(YLBaseRequestModel<Void> baseRequestModel, Response response) {
                if (baseRequestModel.isSuccess()) {
                    Log.e(TAG, "[POST] Fetching Sign In successful");

                    YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_NAME, mEmail);
                    YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_IMAGE_URL, baseRequestModel.avatarOriginal);
                    YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_ABOUT_ME, baseRequestModel.aboutMe);

                    if (mConnectionListener != null)
                        mConnectionListener.onSuccess(baseRequestModel);
                } else {
                    Log.e(TAG, "[POST] Fetching Sign In unsuccessful");

                    if (mConnectionListener != null)
                        mConnectionListener.onFailure(baseRequestModel.error, baseRequestModel.errors);
                }
                finishTask();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "[POST] Fetching Sign In failure");
                errorHandler(error);
                finishTask();
            }

        });

    }


    @Override
    protected RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", YLContentType.URL_ENCODED.getType());
            }
        };
    }




}
