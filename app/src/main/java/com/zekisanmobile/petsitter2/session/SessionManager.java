package com.zekisanmobile.petsitter2.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final String SESSION = "Session";
    private final String USER_ID = "userId";
    private final String TOKEN_SENT_TO_SERVER = "tokenSentToServer";

    Context context;

    public SessionManager(Context context) {
        this.context = context;
    }

    public void setUserId(long userId) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SESSION, Context.MODE_PRIVATE).edit();
        editor.putLong(USER_ID, userId);
        editor.apply();
    }

    public long getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(SESSION,	Context.MODE_PRIVATE);
        return prefs.getLong(USER_ID, 0);
    }

    public void setTokenSentToServer(boolean tokenSentToServer) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SESSION, Context.MODE_PRIVATE).edit();
        editor.putBoolean(TOKEN_SENT_TO_SERVER, tokenSentToServer);
        editor.apply();
    }

    public boolean getTokenSentToServer() {
        SharedPreferences prefs = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        return prefs.getBoolean(TOKEN_SENT_TO_SERVER, false);
    }

    public void cleanAllEntries() {
        SharedPreferences.Editor editor = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE).edit();
        editor.remove(USER_ID);
        editor.remove(TOKEN_SENT_TO_SERVER);
        editor.apply();
    }
}
