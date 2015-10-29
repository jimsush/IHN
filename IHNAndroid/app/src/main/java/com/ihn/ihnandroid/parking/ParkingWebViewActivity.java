package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.ihn.ihnandroid.R;
import com.ihn.ihnandroid.beacon.BeaconScanner;


public class ParkingWebViewActivity extends Activity {

    private WebView webView;
    private Button btnReload;
    private Button btnPark;
    private Button btnLeave;
    private Button btnSearch;
    private EditText txtKeywords;
    private Switch switch3D;
    private BeaconScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.embedded_browser);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        initWebViewSetting();

        switch3D=(Switch)findViewById(R.id.switch1);
        switch3D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    loadLocalHTML("javascript:refreshWith3D()");
                else
                    loadLocalHTML("javascript:refreshWith2D()");
            }
        });

        btnReload =(Button)findViewById(R.id.button_goUrl);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocalHTML("file:///android_asset/car.html");
            }
        });

        btnPark =(Button)findViewById(R.id.btnPark);
        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocalHTML("javascript:startParking()");
            }
        });

        btnLeave =(Button)findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocalHTML("javascript:leaveParking()");
            }
        });

        txtKeywords=(EditText)findViewById(R.id.txtKeywords);

        btnSearch =(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords=txtKeywords.getText().toString();
                loadLocalHTML("javascript:searchShop('"+keywords+"')");
            }
        });

        try{
            scanner=new BeaconScanner(this, webView);
            scanner.onCreate();
        } catch (Throwable e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initWebViewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webSettings.setAppCacheEnabled(true);

        //webSettings.setSupportZoom(true);
        //webSettings.setDisplayZoomControls(false);
        //webSettings.setBuiltInZoomControls(true);

        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setLoadWithOverviewMode(true);
        //webView.setInitialScale(1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if(width>height){
            loadLocalHTML("file:///android_asset/car.html?orientLandscape=true");
        }else{
            loadLocalHTML("file:///android_asset/car.html");
        }

        try{
            scanner.onStart();
        } catch (Throwable e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadLocalHTML(String url){
        try {
            webView.loadUrl(url);
        }catch(Throwable th){
            Toast.makeText(this,th.getMessage(), Toast.LENGTH_LONG).show();
            th.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        scanner.onStop();
    }

}
