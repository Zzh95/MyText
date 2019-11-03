package com.example.myapplication.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.R;

import org.jetbrains.annotations.Nullable;

public class MyWebViewTestActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    private void initView() {
        mWebView= (WebView) findViewById(R.id.web_view);
        mWebView.loadUrl("https://www.hao123.com/?tn=90226939_hao_pg");
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
