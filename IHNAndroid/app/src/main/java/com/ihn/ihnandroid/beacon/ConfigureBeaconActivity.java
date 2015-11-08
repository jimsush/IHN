package com.ihn.ihnandroid.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.ihn.ihnandroid.Config;
import com.ihn.ihnandroid.R;


public class ConfigureBeaconActivity extends Activity {

    private Button btnScale;
    private Button btnRegisterBeacon;

    private EditText txtScale;

    private EditText txtBeaconMac;
    private EditText txtBeaconPosX;
    private EditText txtBeaconPosY;

    private Switch switchDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_beacon);

        btnScale = (Button) findViewById(R.id.btnScale);
        btnRegisterBeacon = (Button) findViewById(R.id.btnRegisterBeacon);

        txtScale = (EditText) findViewById(R.id.txtScale);
        txtBeaconMac = (EditText) findViewById(R.id.txtBeaconMac);
        txtBeaconPosX = (EditText) findViewById(R.id.txtPosX);
        txtBeaconPosY = (EditText) findViewById(R.id.txtPosY);

        switchDebug=(Switch)findViewById(R.id.switchDebug);

        switchDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.getInstance().setDebug(isChecked);
            }
        });

        btnScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String scaleTxt = txtScale.getText().toString();
                    int scale = 800;
                    try {
                        scale = Integer.valueOf(scaleTxt.trim());
                        if (scale <= 0) {
                            Toast.makeText(ConfigureBeaconActivity.this, "请输入>0的数字", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Throwable th) {
                        Toast.makeText(ConfigureBeaconActivity.this, "不是正确的数字" + th.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Config.getInstance().setScale(scale);
                }catch(Throwable th2){
                    Toast.makeText(ConfigureBeaconActivity.this, "保存坐标比例出错:" + th2.getMessage(), Toast.LENGTH_LONG).show();
                }

                Toast.makeText(ConfigureBeaconActivity.this, "坐标比例设置成功", Toast.LENGTH_LONG).show();
            }
        });

        btnRegisterBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String macTxt = txtBeaconMac.getText().toString().trim().toUpperCase();
                    if (macTxt.length() != 17) {
                        Toast.makeText(ConfigureBeaconActivity.this, "不是正确的MAC地址", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String posxTxt = txtBeaconPosX.getText().toString().trim();
                    double posx = Double.valueOf(posxTxt);

                    String posyTxt = txtBeaconPosY.getText().toString().trim();
                    double posy = Double.valueOf(posyTxt);
                    Config.getInstance().registerNewBeacon(macTxt, posx, posy);
                }catch(Throwable th){
                    Toast.makeText(ConfigureBeaconActivity.this, "注册蓝牙信标出错:"+th.getMessage(), Toast.LENGTH_LONG).show();
                }

                Toast.makeText(ConfigureBeaconActivity.this, "注册蓝牙信标完成", Toast.LENGTH_LONG).show();
            }
        });

    }





}
