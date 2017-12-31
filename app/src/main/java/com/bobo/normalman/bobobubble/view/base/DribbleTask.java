package com.bobo.normalman.bobobubble.view.base;

import android.os.AsyncTask;

/**
 * Created by xiaobozhang on 8/26/17.
 */

public abstract class DribbleTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public Exception exception;

    public abstract void onSuccess(Result result);

    public abstract void onFailure(Exception exception);

    public abstract Result doJob(Params... params) throws Exception;

    @Override
    protected void onPostExecute(Result result) {
        if (exception != null) {
            onFailure(exception);
        } else {
            onSuccess(result);
        }
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return doJob();
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
            return null;
        }
    }


}
