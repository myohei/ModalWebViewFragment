# ModalWebViewFragment

## Usage

```
ModalWebViewFragment.WebViewOptions webViewOptions = new ModalWebViewFragment.WebViewOptions();
webViewOptions.javscriptEnabled = true;
webViewOptions.url = "https://blog.yohei.org";
// webViewOptions.basicName = "yohei";
// webViewOptions.basicPassword = "yoheiblog";
ModalWebViewFragment fragment = ModalWebViewFragment.builder()
                        .putWebViewOptions(webViewOptions)
                        .build();
fragment.show(getSupportFragmentManager(), "hoge");
```