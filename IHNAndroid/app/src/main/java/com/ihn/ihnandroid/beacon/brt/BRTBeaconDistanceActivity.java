package com.ihn.ihnandroid.beacon.brt;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.RangingListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.brtbeacon.sdk.Utils;
import com.brtbeacon.sdk.service.RangingResult;
import com.ihn.ihnandroid.R;

/**
 * 演示Beacon感应距离
 * 
 */
public class BRTBeaconDistanceActivity extends Activity {

	private static final String TAG = BRTBeaconDistanceActivity.class.getSimpleName();
	// Y positions are relative to height of bg_distance image.
	private static final double RELATIVE_START_POS = 320.0 / 1110.0;
	private static final double RELATIVE_STOP_POS = 885.0 / 1110.0;

	private BRTBeaconManager beaconManager;
	private BRTBeacon beacon;
	private BRTRegion region;

	private View dotView;
	private int startY = -1;
	private int segmentLength = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.brt_distance_view);
		dotView = findViewById(R.id.dot);

		beacon = getIntent().getParcelableExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_BEACON);
		region = new BRTRegion("regionid", beacon.getUuid(), beacon.getMacAddress(), beacon.getMajor(),
				beacon.getMinor());
		if (beacon == null) {
			Toast.makeText(this, "Beacon not found in intent extras", Toast.LENGTH_LONG).show();
			finish();
		}

		beaconManager = new BRTBeaconManager(this);
		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(final RangingResult rangingResult) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						BRTBeacon foundBeacon = null;
						for (BRTBeacon rangedBeacon : (List<BRTBeacon>) rangingResult.beacons) {
							if (rangedBeacon.getMacAddress().equals(beacon.getMacAddress())) {
								foundBeacon = rangedBeacon;
							}
						}
						if (foundBeacon != null) {
							updateDistanceView(foundBeacon);
						}
					}
				});
			}

		});

		final View view = findViewById(R.id.sonar);
		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				startY = (int) (RELATIVE_START_POS * view.getMeasuredHeight());
				int stopY = (int) (RELATIVE_STOP_POS * view.getMeasuredHeight());
				segmentLength = stopY - startY;

				dotView.setVisibility(View.VISIBLE);
				dotView.setTranslationY(computeDotPosY(beacon));
			}
		});
	}

	private void updateDistanceView(BRTBeacon foundBeacon) {
		if (segmentLength == -1) {
			return;
		}

		dotView.animate().translationY(computeDotPosY(foundBeacon)).start();
	}

	private int computeDotPosY(BRTBeacon beacon) {

		double distance = Math.min(Utils.computeAccuracy(beacon), 6.0);
		return startY + (int) (segmentLength * (distance / 6.0));
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
	protected void onStart() {
		super.onStart();

		beaconManager.connect(new ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(region);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onStop() {
		beaconManager.disconnect();

		super.onStop();
	}
}
