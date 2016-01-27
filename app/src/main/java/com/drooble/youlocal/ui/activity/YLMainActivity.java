package com.drooble.youlocal.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drooble.youlocal.R;
import com.drooble.youlocal.manager.YLPrefsManager;
import com.drooble.youlocal.net.base.YLBaseRequestModel;
import com.drooble.youlocal.net.base.YLBaseRestConnection;
import com.drooble.youlocal.net.restconnection.YLSignInRestConnection;
import com.drooble.youlocal.ui.dialog.YLDialogProfile;
import com.drooble.youlocal.util.YLEmailUtil;
import com.drooble.youlocal.util.YLInternetConnectivity;
import com.drooble.youlocal.util.YLResourceUtil;
import com.drooble.youlocal.util.enums.YLAnimationLoginState;
import com.drooble.youlocal.util.enums.YLLoginViewState;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLMainActivity extends AppCompatActivity {

    @Bind(R.id.image_logo) ImageView mLogo;
    @Bind(R.id.input_layout_username) TextInputLayout mUsernameLayout;
    @Bind(R.id.input_layout_password) TextInputLayout mPasswordLayout;
    @Bind(R.id.btn_login) FrameLayout mBtnSignIn;
    @Bind(R.id.txt_of_login_btn) TextView mTxtOfBtnSignIn;
    @Bind(R.id.btn_forgotten_password) TextView mBtnForgottenPassword;
    @Bind(R.id.et_username) EditText mUsername;
    @Bind(R.id.et_password) EditText mPassword;

    boolean isLoginViewShown = true;

    YLDialogProfile mDialog;
    YLAnimationLoginState mCurrentAnimationState = YLAnimationLoginState.FINISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mDialog != null)
            mDialog.dismiss();

        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_NAME, "");
        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_IMAGE_URL, "");
        YLPrefsManager.GetInstance().setString(YLPrefsManager.SP_USER_ABOUT_ME, "");
    }


    @Override
    public void onBackPressed() {
        if(mDialog != null)
            mDialog.dismiss();
        else
            super.onBackPressed();
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_logo_appear);
        mLogo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_inputs_appear);
        mUsernameLayout.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_inputs_appear);
        animation.setStartOffset(100);
        mPasswordLayout.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_inputs_appear);
        animation.setStartOffset(200);
        mBtnSignIn.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_inputs_appear);
        animation.setStartOffset(300);
        mBtnForgottenPassword.startAnimation(animation);
    }


    @OnClick(R.id.btn_login)
    public void onSignInClick() {

        if(YLInternetConnectivity.hasInternet()) {
            boolean isError = false;

            if (TextUtils.isEmpty(mUsername.getText().toString())) {
                mUsernameLayout.setError(YLResourceUtil.getStringFromResource(R.string.text_error_required_field));
                isError = true;
            } else {
                if (!YLEmailUtil.validateEmail(mUsername.getText().toString())) {
                    mUsernameLayout.setError(YLResourceUtil.getStringFromResource(R.string.text_error_unvalidated_email));
                    isError = true;
                } else {
                    mUsernameLayout.setError(null);
                }
            }

            if (TextUtils.isEmpty(mPassword.getText().toString())) {
                mPasswordLayout.setError(YLResourceUtil.getStringFromResource(R.string.text_error_required_field));
                isError = true;
            } else {
                mPasswordLayout.setError(null);
            }

            if (!isError) {
                YLSignInRestConnection.GetInstance()
                        .setParams(mUsername.getText().toString(), mPassword.getText().toString())
                        .setConnectionListener(new YLBaseRestConnection.ConnectionListener() {
                            @Override
                            public void onSuccess(YLBaseRequestModel model) {
                                mDialog = new YLDialogProfile(YLMainActivity.this);
                                mDialog.show();
                            }

                            @Override
                            public void onFailure(String errorMessage, YLBaseRequestModel.Errors errors) {

                                if (!TextUtils.isEmpty(errorMessage))
                                    Toast.makeText(YLMainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(YLMainActivity.this, YLResourceUtil.getStringFromResource(R.string.text_error_default), Toast.LENGTH_SHORT).show();

                                if (errors != null) {
                                    if (!TextUtils.isEmpty(errors.email))
                                        mUsernameLayout.setError(errors.email);

                                    if (!TextUtils.isEmpty(errors.password))
                                        mPasswordLayout.setError(errors.password);
                                }

                            }
                        }).onConnect();
            }

        } else {
            Toast.makeText(YLMainActivity.this, YLResourceUtil.getStringFromResource(R.string.text_error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btn_forgotten_password)
    public void onForgottenPasswordClick() {

        mUsernameLayout.setError(null);
        mPasswordLayout.setError(null);

        if(mCurrentAnimationState == YLAnimationLoginState.FINISH) {
            mCurrentAnimationState = YLAnimationLoginState.STARTING;
            if (isLoginViewShown) {
                animateSwitchViews(YLLoginViewState.FORGOTTEN_PASSWORD);
            } else {
                animateSwitchViews(YLLoginViewState.LOGIN);
            }
        }

    }


    private void animateSwitchViews(final YLLoginViewState state) {
        ScaleAnimation scaleAnimation;

        if(state == YLLoginViewState.FORGOTTEN_PASSWORD) {
            scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animateSwipeLoginToResetButton(state);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (state == YLLoginViewState.FORGOTTEN_PASSWORD) {
                    mPasswordLayout.setVisibility(View.INVISIBLE);
                    isLoginViewShown = false;
                } else {
                    mPasswordLayout.setVisibility(View.VISIBLE);
                    isLoginViewShown = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mPasswordLayout.startAnimation(scaleAnimation);
    }


    private void animateSwipeLoginToResetButton(final YLLoginViewState state) {
        ObjectAnimator objectAnimator;
        if(state == YLLoginViewState.FORGOTTEN_PASSWORD) {
            objectAnimator = ObjectAnimator.ofFloat(mBtnSignIn, "translationY", 0, mUsernameLayout.getTranslationY() - mUsernameLayout.getHeight() - mBtnSignIn.getTranslationY());
        }  else {
            objectAnimator = ObjectAnimator.ofFloat(mBtnSignIn, "translationY", mBtnSignIn.getTranslationY(), 0);
        }
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (state == YLLoginViewState.FORGOTTEN_PASSWORD) {
                    mPassword.setEnabled(false);
                } else {
                    mPassword.setEnabled(true);
                }
                changeButtonsTexts(state);
            }
            @Override
            public void onAnimationEnd(Animator animation) { }
            @Override
            public void onAnimationCancel(Animator animation) { }
            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        objectAnimator.start();
    }


    private void changeButtonsTexts(YLLoginViewState state) {
        Animation animationFadeOut = new AlphaAnimation(1f, 0f);
        animationFadeOut.setDuration(1000);

        Animation animationFadeIn = new AlphaAnimation(0f, 1f);
        animationFadeIn.setDuration(1000);
        animationFadeOut.setAnimationListener(null);

        switch (state) {
            case FORGOTTEN_PASSWORD:
                mTxtOfBtnSignIn.startAnimation(animationFadeOut);
                mBtnForgottenPassword.startAnimation(animationFadeOut);
                animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mTxtOfBtnSignIn.setText(YLResourceUtil.getStringFromResource(R.string.text_reset));
                        mBtnForgottenPassword.setText(YLResourceUtil.getStringFromResource(R.string.text_back_to_login));
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mCurrentAnimationState = YLAnimationLoginState.FINISH;
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                mTxtOfBtnSignIn.startAnimation(animationFadeIn);
                mBtnForgottenPassword.startAnimation(animationFadeIn);
                break;
            case LOGIN:
                mTxtOfBtnSignIn.startAnimation(animationFadeOut);
                mBtnForgottenPassword.startAnimation(animationFadeOut);
                animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mTxtOfBtnSignIn.setText(YLResourceUtil.getStringFromResource(R.string.text_login));
                        mBtnForgottenPassword.setText(YLResourceUtil.getStringFromResource(R.string.text_forgotten_password));
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mCurrentAnimationState = YLAnimationLoginState.FINISH;
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                mTxtOfBtnSignIn.startAnimation(animationFadeIn);
                mBtnForgottenPassword.startAnimation(animationFadeIn);
                break;
        }

    }




}
