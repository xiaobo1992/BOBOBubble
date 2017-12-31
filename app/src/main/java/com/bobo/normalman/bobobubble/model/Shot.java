package com.bobo.normalman.bobobubble.model;

import java.util.HashMap;

/**
 * Created by xiaobozhang on 8/18/17.
 */

public class Shot {
    public static final String IMAGE_NORMAL_KEY = "normal";
    public static final String IMAGE_HIDPI_KEY = "hidpi";
    public String id;
    public int views_count;
    public int likes_count;
    public int comments_count;
    public int buckets_count;
    public String title;

    public User user;
    public boolean liked;
    public boolean bucketed;

    public HashMap<String, String> images;

    public String description;
    public String getImageUrl() {
        if (images == null) {
            return null;
        }
        String url = images.containsKey(IMAGE_HIDPI_KEY) ?
                images.get(IMAGE_HIDPI_KEY) : images.get(IMAGE_NORMAL_KEY);
        return url == null ? "" : url;
    }
}
