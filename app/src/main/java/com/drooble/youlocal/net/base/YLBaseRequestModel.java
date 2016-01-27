package com.drooble.youlocal.net.base;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public class YLBaseRequestModel<T> {

    @Expose
    public String success;

    @Expose
    public String aboutMe;

    @Expose
    public String avatarOriginal;

    @Expose
    public int code;

    @Expose
    public String error;

    @Expose
    public Errors errors;


    public boolean isSuccess() {
        return (!TextUtils.isEmpty(success));
    }


    public static class Errors {
        @Expose
        public String password;
        @Expose
        public String email;
    }



}
