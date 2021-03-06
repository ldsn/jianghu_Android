package com.ldustu.jianghu;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by jianghu on 22/11/2017.
 */

public class WV  extends Application {
    private static WV instance = null;

    public WebView appView;
    public WebView popView;

    private boolean status = false;

    private List<List<String>> messageList = new LinkedList<>();

    WV() {
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setWebView(WebView appView, WebView popView) {
        this.appView = appView;
        this.popView = popView;

        initWebView();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView () {
        WebSettings appViewSettings = appView.getSettings();
        appViewSettings.setJavaScriptEnabled(true);
        appViewSettings.setDomStorageEnabled(true);
        appView.addJavascriptInterface(new Bridge(Bridge.WVType.APP_VIEW), "__BRIDGE");//AndroidtoJS类对象映射到js的test对象

        WebSettings popViewSettings = popView.getSettings();
        popViewSettings.setJavaScriptEnabled(true);
        appViewSettings.setDomStorageEnabled(true);
        popView.addJavascriptInterface(new Bridge(Bridge.WVType.POP_VIEW), "__BRIDGE");//AndroidtoJS类对象映射到js的test对象
        popView.setBackgroundColor(0);
        initClient();
        resetUA();
    }

    private void initClient() {
        appView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl(view.getContext().getString(R.string.page));
//                super.onReceivedError(view, request, error);
            }
        });
        appView.setWebChromeClient(new WebChromeClient(){
        });

        popView.setWebViewClient(new WebViewClient());
    }

    public void flushMessage() {
        setStatus(true);
        if (!messageList.isEmpty()) {
            for(List<String> s : messageList) {
                sendMessage(s.get(0), s.get(1));
            }
            messageList.clear();
        }
    }

    private void resetUA () {
        String version;
        try {
            PackageInfo pInfo = appView.getContext().getPackageManager().getPackageInfo(appView.getContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "0.0.0";
        }
        Log.d(version,version);

        String ua = appView.getSettings().getUserAgentString();
        appView.getSettings().setUserAgentString(ua+"; JH " + version + " APP_VIEW " + "JH_Android");
        ua = popView.getSettings().getUserAgentString();
        popView.getSettings().setUserAgentString(ua+"; JH " + version + " POP_VIEW " + "JH_Android");
    }
    static WV getInstance() {
        if (instance == null) {
            instance = new WV();
        }
        return instance;
    }
    public void sendMessage(String evt, String arg) {
        if (!status) {
            List<String> list = new LinkedList<>();
            list.add(evt);
            list.add(arg);
            messageList.add(list);
            return;
        } else {
            String script = "window.__receiveJHMessage('" + evt + "','" + arg + "')";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appView.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            } else {
                appView.loadUrl(script);
            }
        }
    }
}
