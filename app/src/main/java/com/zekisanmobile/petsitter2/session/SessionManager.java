package com.zekisanmobile.petsitter2.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.zekisanmobile.petsitter2.util.Config;

public class SessionManager {

    Context context;

    public SessionManager(Context context) {
        this.context = context;
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(Config.SESSION, Context.MODE_PRIVATE).edit();
        editor.putString(Config.USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(Config.SESSION,	Context.MODE_PRIVATE);
        return prefs.getString(Config.USER_ID, "");
    }

    public void setTokenSentToServer(boolean tokenSentToServer) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(Config.SESSION, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Config.TOKEN_SENT_TO_SERVER, tokenSentToServer);
        editor.apply();
    }

    public boolean getTokenSentToServer() {
        SharedPreferences prefs = context.getSharedPreferences(Config.SESSION, Context.MODE_PRIVATE);
        return prefs.getBoolean(Config.TOKEN_SENT_TO_SERVER, false);
    }

    public void cleanAllEntries() {
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.SESSION, Context.MODE_PRIVATE).edit();
        editor.remove(Config.USER_ID);
        editor.remove(Config.TOKEN_SENT_TO_SERVER);
        editor.apply();
    }
}
