package com.bobo.normalman.bobobubble;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by xiaobozhang on 8/18/17.
 */

public class BOBOBubbleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
