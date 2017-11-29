package com.ldustu.jianghu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Created by fanmingfei on 27/11/2017.
 */

public class WebViewFrame extends FrameLayout {


    public WebViewFrame(@NonNull Context context) {
        super(context);
        init();
    }

    public WebViewFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WebView mWebview;

    private void init () {
        final WebViewFrame that = this;
        class Webview extends WebView {

            public Webview(Context context) {
                super(context);
            }
            @Override
            public void setVisibility(int visibility) {
                that.setVisibility(visibility);
                super.setVisibility(visibility);
            }
        };
        mWebview = new Webview(this.getContext());
        addView(mWebview);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mWebview.destroyDrawingCache();
        mWebview.buildDrawingCache();
        Bitmap bitmap = mWebview.getDrawingCache();
        int color = bitmap.getPixel((int)ev.getX(),(int)ev.getY());
        float alpha = Color.alpha(color);
        return alpha/255.0 < 0.2;
    }
}
