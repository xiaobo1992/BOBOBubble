package com.bobo.normalman.bobobubble.dribble;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bobo.normalman.bobobubble.model.Bucket;
import com.bobo.normalman.bobobubble.model.Like;
import com.bobo.normalman.bobobubble.model.Shot;
import com.bobo.normalman.bobobubble.model.User;
import com.bobo.normalman.bobobubble.util.ModelUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xiaobozhang on 7/24/17.
 */

public class Dribble {

    private static final String SP_AUTH = "auth";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";
    private static final String KEY_BUCKET_NAME = "name";
    private static final String KEY_SHOT_ID = "shot_id";

    private static final String KEY_BUCKET_DESCRIPTION = "description";
    private static final String API_URL = "https://api.dribbble.com/v1/";
    public static final String USER_ENDPOINT_URL = API_URL + "user";
    public static final String SHOT_ENDPOINT_URL = API_URL + "shots";

    public static TypeToken<User> userType = new TypeToken<User>() {
    };
    public static TypeToken<List<Shot>> shotsType = new TypeToken<List<Shot>>() {
    };
    public static TypeToken<Like> likeType = new TypeToken<Like>() {
    };
    public static TypeToken<List<Like>> likesType = new TypeToken<List<Like>>() {
    };
    public static TypeToken<Bucket> bucketType = new TypeToken<Bucket>() {
    };
    public static TypeToken<List<Bucket>> bucketsType = new TypeToken<List<Bucket>>() {
    };

    static String accessToken = null;
    static User user = null;

    public static void init(Context context) {
        Dribble.accessToken = loadAccessToken(context);
        if (accessToken != null) {
            Log.d("AccessToken", accessToken);
            user = loadUser(context);
        }
    }

    public static void login(Context context, String accessToken) throws IOException {
        Dribble.accessToken = accessToken;
        storeAccessToken(context, accessToken);
        Dribble.user = getUser();
        storeUser(context, user);
    }

    public static void logout(Context context) {
        Dribble.accessToken = null;
        storeAccessToken(context, null);
        Dribble.user = null;
        storeUser(context, null);
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void storeAccessToken(Context context, String accessToken) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public static String loadAccessToken(Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static Response makeRequest(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Request buildGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        return request;
    }

    public static <T> T parseResponse(Response response, TypeToken<T> token) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(response.body().string(), token.getType());
    }

    public static User getUser() throws IOException {
        Request request = buildGetRequest(USER_ENDPOINT_URL);
        return parseResponse(makeRequest(request), userType);
    }

    public static void storeUser(Context context, User user) {
        ModelUtil.save(context, KEY_USER, user);
    }

    public static User loadUser(Context context) {
        return ModelUtil.read(context, KEY_USER, userType);
    }

    public static User getCurrentUser() {
        return Dribble.user;
    }

    public static List<Shot> getShots(int page) throws IOException {
        Request request = buildGetRequest(SHOT_ENDPOINT_URL + "?page=" + page);
        Response response = makeRequest(request);
        return parseResponse(response, shotsType);
    }

    public static List<Shot> getShots(int page, String type) throws IOException {
        //Log.d("url", SHOT_ENDPOINT_URL + "/" + "list=" + type + "&page=" + page);
        Request request = buildGetRequest(SHOT_ENDPOINT_URL + "/?" + "list=" + type + "&page=" + page);
        Response response = makeRequest(request);
        return parseResponse(response, shotsType);
    }

    public static List<Shot> getLikeShots(int page) throws IOException {
        Request request = buildGetRequest(USER_ENDPOINT_URL + "/likes?page=" + page);
        Response response = makeRequest(request);
        List<Like> likes = parseResponse(response, likesType);
        List<Shot> shots = new ArrayList<Shot>();
        for (Like like : likes) {
            shots.add(like.shot);
        }
        return shots;
    }

    public static Boolean isLikeAShot(String id) throws Exception {
        String url = Dribble.SHOT_ENDPOINT_URL + "/" + id + "/like";
        Response response = makeRequest(buildGetRequest(url));
        switch (response.code()) {
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                throw new Exception(response.message());
        }
    }

    public static List<Bucket> getBuckets(int page) throws IOException {
        String url = Dribble.USER_ENDPOINT_URL + "/buckets?page=" + page;
        Request request = buildGetRequest(url);
        Response response = makeRequest(request);
        List<Bucket> buckets = parseResponse(response, new TypeToken<List<Bucket>>() {
        });
        return buckets;
    }

    public static Bucket addABucket(String name, String description) throws IOException {
        String url = API_URL + "buckets";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(KEY_BUCKET_NAME, name)
                .add(KEY_BUCKET_DESCRIPTION, description).build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return parseResponse(response, bucketType);
    }

    public static List<Shot> listShotForBucket(String bucketId, int page) throws IOException {
        String url = API_URL + "buckets/" + bucketId + "/shots?page=" + page;
        Request request = buildGetRequest(url);
        Response response = makeRequest(request);
        return parseResponse(response, shotsType);
    }

    public static List<Bucket> getAllBucketForShot(String shotId) throws IOException {
        String url = SHOT_ENDPOINT_URL + "/" + shotId + "/buckets?per_page=" + Integer.MAX_VALUE;
        Request request = buildGetRequest(url);
        Response response = makeRequest(request);
        return parseResponse(response, bucketsType);
    }

    public static List<Bucket> getAllUserBucket() throws IOException {
        String url = Dribble.USER_ENDPOINT_URL + "/buckets?per_page=" + Integer.MAX_VALUE;
        Request request = buildGetRequest(url);
        Response response = makeRequest(request);
        List<Bucket> buckets = parseResponse(response, bucketsType);
        return buckets;
    }

    public static void addShotForBucket(String shotId, String bucketId) throws IOException {
        String url = API_URL + "buckets/" + bucketId + "/shots";
        RequestBody body = new FormBody.Builder().add(KEY_SHOT_ID, shotId).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .put(body)
                .build();
        Response response = makeRequest(request);
    }

    public static void removeShotForBucket(String shotId, String bucketId) throws IOException {
        String url = API_URL + "buckets/" + bucketId + "/shots";
        RequestBody body = new FormBody.Builder().add(KEY_SHOT_ID, shotId).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .delete(body)
                .build();
        Response response = makeRequest(request);

    }

    public static String getAccessToken() {
        if (accessToken == null) {
            return "";
        } else {
            return accessToken;
        }
    }
}
