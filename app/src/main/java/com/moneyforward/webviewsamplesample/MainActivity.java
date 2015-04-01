package com.moneyforward.webviewsamplesample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.moneyforward.webviewsamplesample.views.ModalWebViewFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalWebViewFragment.WebViewOptions webViewOptions = new ModalWebViewFragment.WebViewOptions();
                webViewOptions.javscriptEnabled = true;
                webViewOptions.url = "https://blog.yohei.org";
//                webViewOptions.basicName = "yohei";
//                webViewOptions.basicPassword = "yoheiblog";
                ModalWebViewFragment fragment = ModalWebViewFragment.builder()
                        .putWebViewOptions(webViewOptions)
                        .build();
                fragment.show(getSupportFragmentManager(), "hoge");
            }
        });
    }

}
