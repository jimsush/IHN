package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.ihn.ihnandroid.R;
import com.ihn.ihnandroid.beacon.BeaconScanner;


public class ParkingWebViewActivity extends Activity {

    private WebView webView;
    private Button btnPark;
    private Button btnLeave;
    private Button btnSearch;
    private Button btnDrawPath;
    private EditText txtKeywords;
    private Switch switch3D;
    private BeaconScanner scanner;
    private Spinner spinner;

    private String currentShowFloor="B1";

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

        btnDrawPath =(Button)findViewById(R.id.button_drawPath);
        btnDrawPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] posXY=scanner.getCurrentPosition();
                if(posXY==null || posXY.length<2){
                    Toast.makeText(ParkingWebViewActivity.this, "无法标注导航路线因为无法标定当前的位置", Toast.LENGTH_LONG).show();
                }else {
                    loadLocalHTML("javascript:drawPath("+posXY[0]+","+posXY[1]+")");
                }
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

        //ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.getAllFloors());
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this, R.layout.spinner_layout, this.getAllFloors());
        spinner =(Spinner)findViewById(R.id.spinner);
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] floors=getAllFloors();
                showFloor(floors[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setVisibility(View.VISIBLE);

        try{
            scanner=new BeaconScanner(this, webView);
            scanner.onCreate();
        } catch (Throwable e) {
            Toast.makeText(this, "初始化蓝牙扫描器异常:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initWebViewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
            Toast.makeText(this, "开始扫描异常:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadLocalHTML(String url){
        try {
            webView.loadUrl(url);
        }catch(Throwable th){
            Toast.makeText(this, "刷新地图异常:"+th.getMessage(), Toast.LENGTH_LONG).show();
            //th.printStackTrace();
        }
    }

    public void setSubtitle(String title){
        try {
            getActionBar().setSubtitle(title);
        }catch(Throwable th){
            Toast.makeText(this, "设置标题错误:"+th.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanner.onStop();
    }

    private String[] getAllFloors(){
        return new String[]{ "B1", "B2", "F1", "F2"};
    }

    private void showFloor(String floorNumber){
        if(floorNumber==null || floorNumber.length()<2)
            return;
        if(floorNumber.equals(currentShowFloor))
            return;

        loadLocalHTML("javascript:showHideFloor('"+floorNumber+"', '"+currentShowFloor+"');");
        currentShowFloor=floorNumber;
    }

}
