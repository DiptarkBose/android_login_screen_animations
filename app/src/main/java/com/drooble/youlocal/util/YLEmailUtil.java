package com.drooble.youlocal.util;

import android.util.Patterns;

/**
 * Created by Vanya Mihova on 1/26/2016.
 */
public class YLEmailUtil {

    public static boolean validateEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
