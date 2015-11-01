package com.ihn.ihnandroid.beacon.brt;

import com.brtbeacon.sdk.BRTBeaconManager;
import com.ihn.ihnandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 演示功能列表
 */
public class AllDemosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.brt_all_demos);
		// 注册应用，初始化，在应用启动Activity或者Application派生类执行
		BRTBeaconManager.registerApp(this, "6698fd205e834f3cb5eee4e8819a863f");
        //BRTBeaconManager.registerApp(this, "e71e63ce42a40f3d43b3e47c64344075");

		findViewById(R.id.distance_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                try {
                    Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
                    intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,BRTBeaconDistanceActivity.class.getName());
                    startActivity(intent);
                }catch(Throwable ex){
                    Toast.makeText(AllDemosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
			}
		});

		findViewById(R.id.notify_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                try {
                    Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
                    intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY, BRTNotifyDemoActivity.class.getName());
                    startActivity(intent);
                }catch(Throwable ex){
                    Toast.makeText(AllDemosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
			}
		});

		findViewById(R.id.characteristics_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                try{
                    Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
                    intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY, BRTBeaconConnectionDemoActivity.class.getName());
                    startActivity(intent);
                }catch(Throwable ex){
                    Toast.makeText(AllDemosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
			}
		});

	}

}
