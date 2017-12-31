package com.bobo.normalman.bobobubble;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bobo.normalman.bobobubble.dribble.auth.Auth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    public static String KEY_URL = "AUTH_URL";
    public static String KEY_CODE = "code";
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_acitivity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Auth.REDIRECT_URL)) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent();
                    intent.putExtra(KEY_CODE, uri.getQueryParameter(KEY_CODE));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        String url = getIntent().getStringExtra(KEY_URL);
        webview.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
