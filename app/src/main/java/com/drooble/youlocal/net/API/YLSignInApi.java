package com.drooble.youlocal.net.API;

import com.drooble.youlocal.config.YLConstants;
import com.drooble.youlocal.net.base.YLBaseRequestModel;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public interface YLSignInApi {
    @FormUrlEncoded
    @POST(YLConstants.URL_SIGIN_IN)
    void doSignIn(
            @Field("email") String email,
            @Field("password") String password,
            Callback<YLBaseRequestModel<Void>> response);
}

