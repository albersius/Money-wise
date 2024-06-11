package com.moneywise.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.moneywise.constant.Constant;

public class Util {
    public static int getCurrentUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPref.getInt(Constant.ID_USER_KEY, -1);
    }
}
