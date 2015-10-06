package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.ihn.ihnandroid.R;


public class ParkingWebViewActivity extends Activity {

    private WebView webView;
    private Button btnReload;
    private Button btnPark;
    private Button btnLeave;
    private Button btnSearch;
    private EditText txtKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.embedded_browser);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        initWebViewSetting();

        btnReload =(Button)findViewById(R.id.button_goUrl);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocalHTML();
            }
        });

        btnPark =(Button)findViewById(R.id.btnPark);
        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    webView.loadUrl("javascript:startParking()");
                }catch(Throwable th){
                    th.printStackTrace();
                }
            }
        });

        btnLeave =(Button)findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    webView.loadUrl("javascript:leaveParking()");
                }catch(Throwable th){
                    th.printStackTrace();
                }
            }
        });

        txtKeywords=(EditText)findViewById(R.id.txtKeywords);

        btnSearch =(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String keywords=txtKeywords.getText().toString();
                    webView.loadUrl("javascript:searchShop('"+keywords+"')");
                }catch(Throwable th){
                    th.printStackTrace();
                }
            }
        });
    }

    private void initWebViewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webSettings.setAppCacheEnabled(true);

        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //webView.setInitialScale(1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadLocalHTML();
    }

    private void loadLocalHTML(){
        try {
            webView.loadUrl("file:///android_asset/car.html");
        }catch(Throwable th){
            th.printStackTrace();
        }
    }


}
