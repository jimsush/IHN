package com.ihn.ihnandroid.beacon.brt;

import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.widget.TextView;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.MonitoringListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.ihn.ihnandroid.R;

/**
 * 演示进入感应范围离开感应范围发出通知到手机
 * 
 */
public class BRTNotifyDemoActivity extends Activity {

	private static final String TAG = BRTNotifyDemoActivity.class.getSimpleName();
	private static final int NOTIFICATION_ID = 123;

	private BRTBeaconManager beaconManager;
	private NotificationManager notificationManager;
	private BRTRegion region;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brt_notify_demo);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		BRTBeacon beacon = getIntent().getParcelableExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_BEACON);
		region = new BRTRegion("regionId", beacon.getUuid(), beacon.getMacAddress(), beacon.getMajor(),
				beacon.getMinor());
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		beaconManager = new BRTBeaconManager(this);
		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onEnteredRegion(BRTRegion arg0, List<BRTBeacon> arg1) {
				postNotification("进入感应区域");
			}

			@Override
			public void onExitedRegion(BRTRegion arg0, List<BRTBeacon> arg1) {

				postNotification("离开感应区域");
			}

		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		notificationManager.cancel(NOTIFICATION_ID);
		beaconManager.connect(new ServiceReadyCallback() {
			@Override
			public void onServiceReady() {

				try {
					beaconManager.startMonitoring(region);
				} catch (RemoteException e) {

					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		notificationManager.cancel(NOTIFICATION_ID);
		beaconManager.disconnect();
		super.onDestroy();
	}

	private void postNotification(String msg) {
		Intent notifyIntent = new Intent(BRTNotifyDemoActivity.this, BRTNotifyDemoActivity.class);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivities(BRTNotifyDemoActivity.this, 0,
				new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(BRTNotifyDemoActivity.this)
				.setSmallIcon(R.drawable.brt_beacon_gray).setContentTitle("Notify Demo").setContentText(msg)
				.setAutoCancel(true).setContentIntent(pendingIntent).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notificationManager.notify(NOTIFICATION_ID, notification);

		TextView statusTextView = (TextView) findViewById(R.id.status);
		statusTextView.setText(msg);
	}
}
