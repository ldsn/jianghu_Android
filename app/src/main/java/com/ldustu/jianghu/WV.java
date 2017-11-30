package com.ldustu.jianghu;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
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
                setStatus(true);
                if (!messageList.isEmpty()) {
                    for(List<String> s : messageList) {
                        sendMessage(s.get(0), s.get(1));
                    }
                    messageList.clear();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl(getString(R.string.errorPage));
//                super.onReceivedError(view, request, error);
            }
        });
        appView.setWebChromeClient(new WebChromeClient(){
        });

        popView.setWebViewClient(new WebViewClient());
    }


    private void resetUA () {
        // 修改ua使得web端正确判断
        String ua = appView.getSettings().getUserAgentString();
        appView.getSettings().setUserAgentString(ua+"; JH " + Info.version + " APP_VIEW " + "JH_Android");
        ua = popView.getSettings().getUserAgentString();
        popView.getSettings().setUserAgentString(ua+"; JH " + Info.version + " POP_VIEW " + "JH_Android");
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
        }
    }
}
