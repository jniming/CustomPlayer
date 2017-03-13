/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License��);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ggpl.player.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ggpl.player.R;
import com.ggpl.player.common.DebugLog;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.DefaultHandler;


/**
 * Author: Tau.Chen Email: 1076559197@qq.com | tauchen1990@gmail.com Date:
 * 2015/3/31. Description:
 * <p>
 * <p>
 * 2016
 */
public class BrowserLayout extends LinearLayout {

    private Context mContext = null;
    private BridgeWebView mWebView = null;

    private int mBarHeight = 1;

    private String mLoadUrl;

    private ResultLisener result;
    private ProgressBar mProgressBar;

    public BrowserLayout(Context context) {
        super(context);
        init(context);
    }

    public BrowserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
        mProgressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress, null);
//		mProgressBar.setScrollBarStyle();
//		mProgressBar.setProgress(80);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
        addView(mProgressBar, LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        mBarHeight, getResources().getDisplayMetrics()));

        mWebView = new BridgeWebView(context);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 设置滚动条风格

        webSettings.setJavaScriptEnabled(true);
        // webSettings.setDefaultFontSize(50); //设置默认默认的字体大小
        webSettings.setDefaultTextEncodingName("UTF-8"); // 设置默认编码
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置缓存
        webSettings.setSupportMultipleWindows(true); // 是否支持多窗口
        webSettings.setUseWideViewPort(false); // 套的WebView是否应启用的“视”HTML
        // meta标签的支持还是应该使用宽视
        // 该方法为false时,内容会根据屏幕快读自动换行
        webSettings.setLoadWithOverviewMode(true); // 设置是否以概览模式中的WebView加载页面，也就是缩小内容由宽度以适应屏幕上的
        webSettings.setSupportZoom(false); // 设置的WebView是否应该支持使用其屏幕上的变焦控制和手势缩放
        webSettings.setPluginState(WebSettings.PluginState.ON); // 设置webview是否加载插件
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true); // 设置的WebView是否应加载图像资源
        webSettings
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); // 自动缩放屏幕
        // webSettings.setMinimumFontSize(50); //设置最小的字体大小
        // webSettings.setTextZoom(1);
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        addView(mWebView, lps);

        mWebView.setDefaultHandler(new DefaultHandler());

        mWebView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setProgress(0);


                }
                if (result != null)
                    result.press(newProgress);
            }

        });
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversa");

        mWebView.setWebViewClient(new BridgeWebViewClient(mWebView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                DebugLog.d("url-->"+url);
                mLoadUrl = url;
                if (result != null)
                    result.onResult();
            }

            // @Override
            // public void onReceivedHttpError(WebView view, WebResourceRequest
            // request, WebResourceResponse errorResponse) {
            // super.onReceivedHttpError(view, request, errorResponse);
            // LogUtil.d("网页错误");
            // }
//		     @Override
//	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//	                return true;
//	            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

//				if (result != null)
//					result.error();
            }

        });

    }


    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void loadDataWithBaseURL(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    public void loadData(String html) {
        mWebView.loadData(html, "text/html", null);
    }

    /**
     * �����Ƿ��ܷ���
     *
     * @return
     */
    public boolean canGoBack() {
        return null != mWebView && mWebView.canGoBack();
    }

    /**
     * �����Ƿ���ǰ��
     *
     * @return
     */
    public boolean canGoForward() {
        return null != mWebView && mWebView.canGoForward();
    }

    /**
     * ���÷���
     */
    public void goBack() {
        if (null != mWebView) {
            mWebView.goBack();
        }
    }

    /**
     * ����ǰ��
     */
    public void goForward() {
        if (null != mWebView) {
            mWebView.goForward();
        }
    }

    /**
     * ˢ��
     */
    public void refrish() {
        mWebView.loadUrl(mLoadUrl);
    }

    public BridgeWebView getWebView() {
        return mWebView != null ? mWebView : null;
    }


    public void setResultListen(ResultLisener result) {
        this.result = result;
    }

    // �Ľӿ���Ҫ��������ҳ���ص�100%��ʱ��������Ҫ���ؼ���ҳ��,������Ҫ�ص�֪ͨ,,,ټ����Ҫ�˹���,ֱ�Ӳ�Ҫ���ü���
    public interface ResultLisener {
        void onResult();

        void press(int press);

//		void error();
    }

}

