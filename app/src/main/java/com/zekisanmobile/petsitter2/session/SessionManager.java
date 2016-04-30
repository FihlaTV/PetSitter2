package com.zekisanmobile.petsitter2.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.zekisanmobile.petsitter2.util.Config;

public class SessionManager {

    Context context;

    public SessionManager(Context context) {
        this.context = context;
    }

    public void setUserId(long userId) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(Config.SESSION, Context.MODE_PRIVATE).edit();
        editor.putLong(Config.USER_ID, userId);
        editor.apply();
    }

    public long getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(Config.SESSION,	Context.MODE_PRIVATE);
        return prefs.getLong(Config.USER_ID, 0);
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
