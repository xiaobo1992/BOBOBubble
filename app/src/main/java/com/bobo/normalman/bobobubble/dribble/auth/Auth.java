package com.bobo.normalman.bobobubble.dribble.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bobo.normalman.bobobubble.AuthActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xiaobozhang on 8/17/17.
 */

public class Auth {

    public static int REQ_CODE = 100;
    public static String KEY_CLIENT_ID = "client_id";
    public static String KEY_CLIENT_SECRET = "client_secret";
    public static String KEY_CODE = "code";
    public static String KEY_SCOPE = "scope";
    public static String KEY_ACCESS_TOKEN = "access_token";


    public static String CLIENT_ID = "79557e52a49bfd72823ea8ea2b099792ac63eeb25d235e0f5af2deb54cff411c";
    public static String CLIENT_SECRET = "1d8886f3adbc7fc5c5458267fed4cdaac04cf3cdde6f45dd4af2d9d1090e37dd";

    public static String DRIBBBLE_OAUTH_URL = "https://dribbble.com/oauth/authorize";
    public static String DRIBBBLE_OAUTH_TOKEN_URL = "https://dribbble.com/oauth/token";
    public static String REDIRECT_URL = "https://www.google.com";
    public static String SCOPE = "public+write";

    public static String loadAuthUrl() {
        String url = Uri.parse(DRIBBBLE_OAUTH_URL)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .build()
                .toString();
        return url + "&" + KEY_SCOPE + "=" + SCOPE;
    }

    public static void startAuthActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra(AuthActivity.KEY_URL, loadAuthUrl());
        activity.startActivityForResult(intent, REQ_CODE);
    }


    public static String getAccessToken(String code) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(KEY_CLIENT_ID, CLIENT_ID)
                .add(KEY_CODE, code)
                .add(KEY_CLIENT_SECRET, CLIENT_SECRET)
                .build();

        Request request = new Request.Builder()
                .url(DRIBBBLE_OAUTH_TOKEN_URL)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        try {
            JSONObject object = new JSONObject(responseString);
            return object.getString(KEY_ACCESS_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
