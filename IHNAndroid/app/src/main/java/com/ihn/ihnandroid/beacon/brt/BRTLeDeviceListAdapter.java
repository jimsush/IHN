package com.ihn.ihnandroid.beacon.brt;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.Utils;
import com.ihn.ihnandroid.R;

/**
 * 列表适配器
 * 
 */
public class BRTLeDeviceListAdapter extends BaseAdapter {

	private ArrayList<BRTBeacon> beacons;
	private LayoutInflater inflater;

	public BRTLeDeviceListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		this.beacons = new ArrayList<BRTBeacon>();
	}

	public void replaceWith(Collection<BRTBeacon> newBeacons) {
		this.beacons.clear();
		this.beacons.addAll(newBeacons);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return beacons.size();
	}

	@Override
	public BRTBeacon getItem(int position) {
		return beacons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflateIfRequired(view, position, parent);
		bind(getItem(position), view);
		return view;
	}

	private void bind(BRTBeacon beacon, View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.macTextView.setText(String.format("Name: %s \nMac: %s \nDistance:%.2f米 \nUUID: %s",
				TextUtils.isEmpty(beacon.getName()) ? "" : beacon.getName(), beacon.getMacAddress(),
				Utils.computeAccuracy(beacon), beacon.getUuid().toUpperCase()));
		holder.majorTextView.setText("Major: " + beacon.getMajor());
		holder.minorTextView.setText("Minor: " + beacon.getMinor());
		holder.measuredPowerTextView.setText("MeasuredPower: " + beacon.getMeasuredPower());
		holder.rssiTextView.setText("Rssi: " + beacon.getRssi());
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.brt_device_item, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	static class ViewHolder {
		final TextView macTextView;
		final TextView majorTextView;
		final TextView minorTextView;
		final TextView measuredPowerTextView;
		final TextView rssiTextView;

		ViewHolder(View view) {
			macTextView = (TextView) view.findViewWithTag("mac");
			majorTextView = (TextView) view.findViewWithTag("major");
			minorTextView = (TextView) view.findViewWithTag("minor");
			measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
			rssiTextView = (TextView) view.findViewWithTag("rssi");
		}
	}
}
