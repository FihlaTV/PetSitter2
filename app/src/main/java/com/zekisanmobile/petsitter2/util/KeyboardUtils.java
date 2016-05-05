package com.zekisanmobile.petsitter2.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content)
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

}
