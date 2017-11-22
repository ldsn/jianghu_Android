package com.ldustu.jianghu;

import android.annotation.SuppressLint;
import android.app.Application;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jianghu on 22/11/2017.
 */

public class WV  extends Application {
    private static WV instance = null;

    public WebView appView;
    public WebView popView;

    WV() {
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
        appView.addJavascriptInterface(new Bridge(Bridge.WVType.APP_VIEW), "bridge");//AndroidtoJS类对象映射到js的test对象
        appView.setWebViewClient(new WebViewClient(){
        });
        appView.setWebChromeClient(new WebChromeClient(){
        });


        WebSettings popViewSettings = popView.getSettings();
        popViewSettings.setJavaScriptEnabled(true);
        popView.addJavascriptInterface(new Bridge(Bridge.WVType.POP_VIEW), "bridge");//AndroidtoJS类对象映射到js的test对象

        resetUA();
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
}
