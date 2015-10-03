package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.ihn.ihnandroid.R;

/**
 * .
 */
public class ParkingWebViewActivity extends Activity {

    private WebView webView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.embedded_browser);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        editText=(EditText)findViewById(R.id.editText_url);

        button=(Button)findViewById(R.id.button_goUrl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=editText.getText().toString();

                try {
                    if (url == null || url.length() == 0)
                        webView.loadUrl("file:///android_asset/car.html");
                    else
                        webView.loadUrl(url);
                }catch(Throwable th){
                    th.printStackTrace();
                }
            }
        });

    }

}
