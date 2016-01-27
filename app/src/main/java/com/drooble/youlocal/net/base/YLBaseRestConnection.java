package com.drooble.youlocal.net.base;

import android.text.TextUtils;
import android.util.Log;

import com.drooble.youlocal.config.YLConstants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public class YLBaseRestConnection {

    public String TAG = YLBaseRestConnection.class.getSimpleName();

    OnFinishConnectionListener mOnFinishConnectionListener;
    protected ConnectionListener mConnectionListener;
    protected RestAdapter mRestAdapter;


    public YLBaseRestConnection() { }

    synchronized public void onConnect() {
        mRestAdapter = createRestAdapter(TAG);
    }




    public RestAdapter createRestAdapter(final String tagName) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
//                .setConverter(getGsonConverter())
                .setRequestInterceptor(getRequestInterceptor())
                .setEndpoint(YLConstants.URL_BASE)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.i(tagName, msg);
                    }
                });
        
        return builder.build();
    }


    public void setTag(String tag) {
        this.TAG = tag;
    }


    protected RequestInterceptor getRequestInterceptor() {
        return null;
    }


//    protected GsonConverter getGsonConverter() {
//        Gson gson = new GsonBuilder()
//                .setExclusionStrategies(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaredClass().equals(ModelAdapter.class);
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> clazz) {
//                        return false;
//                    }
//                })
//                .create();
//        return new GsonConverter(gson);
//    }

    protected void errorHandler(RetrofitError error) {
        YLBaseRequestModel baseRequestModel = new YLBaseRequestModel();
        baseRequestModel.errors = new YLBaseRequestModel.Errors();

        if (error != null && error.getKind() == RetrofitError.Kind.HTTP) {

            try {
                if (error.getBody() != null && error.getBody() instanceof YLBaseRequestModel) {
                    YLBaseRequestModel model = (YLBaseRequestModel) error.getBody();
                    baseRequestModel.error = model.error;
                    baseRequestModel.errors = model.errors;
                } else {
                    baseRequestModel.error = error.getMessage();
                }
            } catch (Exception e) {
                baseRequestModel.error = error.getMessage();
            }

            if (mConnectionListener != null)
                mConnectionListener.onFailure(baseRequestModel.error, baseRequestModel.errors);

        } else {
            if (mConnectionListener != null) {
                if(error != null)
                    mConnectionListener.onFailure(error.getMessage(), null);
                else
                    mConnectionListener.onFailure(null, null);
            }
        }
    }


    protected void finishTask() {
        if(mOnFinishConnectionListener != null)
            mOnFinishConnectionListener.onFinish();
    }

//    protected void sendHandlerMessage() {
//        if (mClientMessenger != null) {
//            try {
//                mClientMessenger.send(mMessage);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public YLBaseRestConnection setConnectionListener(ConnectionListener listener) {
        this.mConnectionListener = listener;
        return this;
    }

    public interface ConnectionListener {
        void onSuccess(YLBaseRequestModel model);
        void onFailure(String errorMessage, YLBaseRequestModel.Errors errors);
    }


    public YLBaseRestConnection setOnFinishConnectionListener(OnFinishConnectionListener listener) {
        this.mOnFinishConnectionListener = listener;
        return this;
    }
    public interface OnFinishConnectionListener {
        void onFinish();
    }

}
