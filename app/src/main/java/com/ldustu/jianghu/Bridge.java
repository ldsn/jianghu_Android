package com.ldustu.jianghu;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


/**
 * Created by jianghu on 20/11/2017.
 */

public class Bridge extends Object {
    public Bridge(WVType webViewType) {
        wvType = webViewType;
    }

    static enum WVType {
        APP_VIEW, POP_VIEW
    }

    private WVType wvType;
    @JavascriptInterface
    public void bridge(String type, String msg) {
        Log.d(type, msg);

        WebView wv = null;

        switch (wvType) {
            case APP_VIEW:
                wv = WV.getInstance().appView;
                break;
            case POP_VIEW:
                wv = WV.getInstance().popView;
                break;
        }
        switch (type) {
            case "show":
                show();
                break;
            case "hide":
                hide();
                break;
            case "load":
                load(msg);
                break;
            case "back":
                back(wv);
                break;
        }
    }


    private Handler handler = new Handler();

    private void show() {

        // 构建Runnable对象，在runnable中更新界面
        final Runnable runnableUi = new Runnable(){
            @Override
            public void run() {
                WebView popView = WV.getInstance().popView;
                popView.setVisibility(View.VISIBLE);
            }
        };


        new Thread() {
            public void run() {
                handler.post(runnableUi);
            }
        }.start();
    }
    private void hide() {

        // 构建Runnable对象，在runnable中更新界面
        final Runnable runnableUi = new Runnable(){
            @Override
            public void run() {
                WebView popView = WV.getInstance().popView;
                popView.setVisibility(View.GONE);
            }
        };


        new Thread() {
            public void run() {
                handler.post(runnableUi);
            }
        }.start();
    }

    private void load(final String msg) {

        // 构建Runnable对象，在runnable中更新界面
        final Runnable runnableUi = new Runnable(){
            @Override
            public void run() {
                WebView popView = WV.getInstance().popView;
                popView.loadUrl(msg);
            }
        };


        new Thread() {
            public void run() {
                handler.post(runnableUi);
            }
        }.start();
    }

    private void back(final WebView wv) {

        // 构建Runnable对象，在runnable中更新界面
        final Runnable runnableUi = new Runnable(){
            @Override
            public void run() {
                wv.goBack();
            }
        };

        new Thread() {
            public void run() {
                handler.post(runnableUi);
            }
        }.start();
    }

}
