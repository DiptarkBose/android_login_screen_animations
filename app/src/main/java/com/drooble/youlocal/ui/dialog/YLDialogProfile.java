package com.drooble.youlocal.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drooble.youlocal.R;
import com.drooble.youlocal.manager.YLPrefsManager;
import com.drooble.youlocal.ui.view.YLCircularImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLDialogProfile extends Dialog {

    @Bind(R.id.image_view_profile_image)
    YLCircularImageView imageView;
    @Bind(R.id.txt_username) TextView txtUsername;
    @Bind(R.id.txt_about_me_content) TextView txtAboutMeContent;

    public YLDialogProfile(Context context) {
        super(context);
        init();
    }

    public YLDialogProfile(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected YLDialogProfile(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        getWindow().getAttributes().windowAnimations = R.style.DialogProfileAnimation;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        setContentView(R.layout.dialog_profile);
        ButterKnife.bind(this);

        txtAboutMeContent.setMovementMethod(new ScrollingMovementMethod());

        setProfileImage(YLPrefsManager.GetInstance().getString(YLPrefsManager.SP_USER_IMAGE_URL));
        setProfileName(YLPrefsManager.GetInstance().getString(YLPrefsManager.SP_USER_NAME));
        setAboutMeContent(YLPrefsManager.GetInstance().getString(YLPrefsManager.SP_USER_ABOUT_ME));
    }

    public YLDialogProfile setProfileImage(String urlImage) {
        if(!TextUtils.isEmpty(urlImage))
            Glide.with(getContext())
                    .load(urlImage)
                    .into(imageView);
        else
            Glide.with(getContext())
                    .load("http://thesocialmediamonthly.com/wp-content/uploads/2015/08/photo.png")
                    .into(imageView);
        return this;
    }

    public YLDialogProfile setProfileName(String username) {
        if(!TextUtils.isEmpty(username))
            txtUsername.setText(username);
        else
            txtUsername.setText("");
        return this;
    }

    public YLDialogProfile setAboutMeContent(String content) {
        if(!TextUtils.isEmpty(content))
            txtAboutMeContent.setText(content);
        else
            txtAboutMeContent.setText("");
        return this;
    }


    @Override
    public void dismiss() {
        super.dismiss();

        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_NAME, "");
        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_IMAGE_URL, "");
        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_ABOUT_ME, "");
    }
}
