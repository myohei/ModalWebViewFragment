package com.moneyforward.webviewsamplesample.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.moneyforward.webviewsamplesample.R;

/**
 * Created by maeda on 15/03/31.
 */
public class ModalWebViewFragment extends DialogFragment {

    /**
     * web view.
     */
    private WebView mWebView;
    /**
     * progress.
     */
    private ProgressBar mProgressBar;
    /**
     * error view.
     */
    private View mErrorView;
    @Nullable
    private WebViewOptions mWebViewOptions;
    @NonNull
    private String mUrl;

    private static final String EXTRA_URL = "modalWebViewFragment.EXTRA_URL";
    private static final String EXTRA_JAVASCRIPT_ENABLED = "modalWebViewFragment.EXTRA_JAVASCRIPT_ENABLED";
    private static final String EXTRA_WEBVIEW_OPTIONS = "modalWebViewFragment.EXTRA_WEBVIEW_OPTIONS";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        mWebViewOptions = getArguments().getParcelable(EXTRA_WEBVIEW_OPTIONS);
        mUrl = getArguments().getString(EXTRA_URL);
        if (TextUtils.isEmpty(mUrl) && mWebViewOptions != null) {
            mUrl = mWebViewOptions.url;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorView = view.findViewById(R.id.error_view);
        mErrorView.setVisibility(View.GONE);
        setupWebView();
        mWebView.loadUrl(mUrl);
    }

    /**
     * setup webview.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mProgressBar.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                if (mWebViewOptions == null) {
                    return;
                }
                if (!TextUtils.isEmpty(mWebViewOptions.basicName) && !TextUtils.isEmpty(mWebViewOptions.basicPassword)) {
                    handler.proceed(mWebViewOptions.basicName, mWebViewOptions.basicPassword);
                }
            }
        });
        if (!getArguments().containsKey(EXTRA_WEBVIEW_OPTIONS)) {
            return;
        }
        final WebViewOptions webViewOptions = getArguments().getParcelable(EXTRA_WEBVIEW_OPTIONS);
        mWebView.getSettings().setJavaScriptEnabled(webViewOptions.javscriptEnabled);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        final WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.windowAnimations = R.style.AppTheme_ModalWebView;
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        lp.width = metrics.widthPixels;
        lp.height = metrics.heightPixels;
        dialog.getWindow().setAttributes(lp);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Bundle args) {
        return new Builder(args);
    }

    public static class WebViewOptions implements Parcelable {
        public String basicName;
        public String basicPassword;
        public boolean javscriptEnabled = false;
        public String url;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.basicName);
            dest.writeString(this.basicPassword);
            dest.writeByte(javscriptEnabled ? (byte) 1 : (byte) 0);
            dest.writeString(this.url);
        }

        public WebViewOptions() {
        }

        private WebViewOptions(Parcel in) {
            this.basicName = in.readString();
            this.basicPassword = in.readString();
            this.javscriptEnabled = in.readByte() != 0;
            this.url = in.readString();
        }

        public static final Parcelable.Creator<WebViewOptions> CREATOR = new Parcelable.Creator<WebViewOptions>() {
            public WebViewOptions createFromParcel(Parcel source) {
                return new WebViewOptions(source);
            }

            public WebViewOptions[] newArray(int size) {
                return new WebViewOptions[size];
            }
        };
    }

    public static class Builder {
        private final Bundle mArgs;

        public Builder() {
            mArgs = new Bundle();
        }

        public Builder(Bundle args) {
            mArgs = new Bundle(args);
        }

        public Builder putUrl(String url) {
            if (!TextUtils.isEmpty(url)) {
                mArgs.putString(EXTRA_URL, url);
            }
            return this;
        }

        public Builder putJavascriptEnabled(boolean enabled) {
            mArgs.putBoolean(EXTRA_JAVASCRIPT_ENABLED, enabled);
            return this;
        }

        public Builder putWebViewOptions(WebViewOptions options) {
            if (options != null) {
                mArgs.putParcelable(EXTRA_WEBVIEW_OPTIONS, options);
            }
            return this;
        }

        public ModalWebViewFragment build() {
            ModalWebViewFragment fragment = new ModalWebViewFragment();
            fragment.setArguments(mArgs);
            return fragment;
        }
    }
}
