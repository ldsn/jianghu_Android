package com.ldustu.jianghu;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import static cn.jpush.android.api.JPushInterface.init;
import static cn.jpush.android.api.JPushInterface.setDebugMode;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebView popView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webview);

        WebViewFrame f = (WebViewFrame) findViewById(R.id.webviewFrame);
        popView = f.mWebview;


        WV wv = WV.getInstance();
        wv.setStatus(false);
        wv.setWebView(webView, popView);
        webView.loadUrl(getString(R.string.mainUrl));
//        popView.loadUrl("http://mt2.wapa.taobao.com/core/preview/act/mtakn8.html");
//        popView.setVisibility(View.VISIBLE);

        setDebugMode(true);
        init(getApplicationContext());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected  void evaluateJs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("alert(123)", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d("x",s);
                }
            });
        } else {
            webView.loadUrl("javascript:alert(123)");
        }
    }
}

