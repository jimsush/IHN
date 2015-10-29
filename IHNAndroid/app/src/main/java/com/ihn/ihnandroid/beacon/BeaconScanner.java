package com.ihn.ihnandroid.beacon;

import android.app.Activity;
import android.os.RemoteException;
import android.webkit.WebView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.RangingListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.brtbeacon.sdk.Utils;
import com.brtbeacon.sdk.service.RangingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tong on 2015/10/16.
 */
public class BeaconScanner {

    private Activity activity;
    private WebView webView;
    private BRTBeaconManager beaconManager;
    private Map<String, BluetoothPosition> allBeacons=new HashMap<String, BluetoothPosition>();
    private Map<String, BRTBeacon> foundBeacons=new HashMap<String, BRTBeacon>();

    private static final BRTRegion ALL_BRIGHT_BEACONS_REGION = new BRTRegion("rid", null, null, null, null);

    public BeaconScanner(Activity activity, WebView webView){
        this.activity=activity;
        this.webView=webView;
    }

    public void onCreate(){
        try{
            BRTBeaconManager.registerApp(activity, "6698fd205e834f3cb5eee4e8819a863f");
            loadBeaconMetadata();
            init();
        } catch (Throwable e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onStart(){
        beaconManager.connect(new ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_BRIGHT_BEACONS_REGION);
                } catch (Throwable e) {
                    Toast.makeText(BeaconScanner.this.activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onStop(){
        beaconManager.disconnect();
    }

    private void loadBeaconMetadata(){
        allBeacons.put("EC:23:B1:51:00:BB", new BluetoothPosition("EC:23:B1:51:00:BB", 0.1, 0.1));
        //allBeacons.put("mac2", new BluetoothPosition());
    }

    private void init(){
        beaconManager = new BRTBeaconManager(activity);
        beaconManager.setRangingListener(new RangingListener() {
            @Override
            public void onBeaconsDiscovered(final RangingResult rangingResult) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foundBeacons.clear();
                        List<BRTBeacon> scannedBeacons = (List<BRTBeacon>) rangingResult.beacons;
                        double[] pos = getP3Position(scannedBeacons);
                        loadLocalHTML("javascript:myPosition("+pos[0]+","+pos[1]+")");
                    }
                });
            }
        });
    }

    private double[] getP3Position(List<BRTBeacon> scannedBeacons){
        List<TmpBeacon> tmpBeacons=new ArrayList<TmpBeacon>();
        for (BRTBeacon rangedBeacon : scannedBeacons) {
            tmpBeacons.add( new TmpBeacon(rangedBeacon.getMacAddress(),rangedBeacon.getRssi() ));
            foundBeacons.put(rangedBeacon.getMacAddress(), rangedBeacon);
        }
        Collections.sort(tmpBeacons);

        if(tmpBeacons.size()==0){
            // not found
            return null;
        }else if(tmpBeacons.size()==1){
            Iterator<TmpBeacon> it=tmpBeacons.iterator();
            TmpBeacon first=it.next();
            BluetoothPosition pos=allBeacons.get(first.getMac());
            return new double[]{ pos.x, pos.y };
        }else {
            Iterator<TmpBeacon> it = tmpBeacons.iterator();
            TmpBeacon first = it.next();
            TmpBeacon second = it.next();

            BluetoothPosition pos1 = allBeacons.get(first.getMac());
            BluetoothPosition pos2 = allBeacons.get(second.getMac());

            BRTBeacon bt1 = foundBeacons.get(first.getMac());
            BRTBeacon bt2 = foundBeacons.get(second.getMac());
            double distance1 = getDistance(bt1);
            double distance2 = getDistance(bt2);
            PositionResult result = NearestDistance.getP3(pos1.x, pos1.y, pos2.x, pos2.y, distance1, distance2);
            if (result == null || result.getValueNum() == 0) {
                return null;
            } else if (result.getValueNum() == 1) {
                return new double[]{result.p1x, result.p1y};
            } else {
                double px = (result.p1x + result.p2x) / 2;
                double py = (result.p1y + result.p2y) / 2;
                return new double[]{px, py};
            }
        }
    }

    private double getDistance(BRTBeacon beacon){
        double distance = Math.min(Utils.computeAccuracy(beacon), 6.0);
        return distance;
    }

    private void loadLocalHTML(String url){
        try {
            webView.loadUrl(url);
        }catch(Throwable th){
            Toast.makeText(activity, th.getMessage(), Toast.LENGTH_LONG).show();
            th.printStackTrace();
        }
    }

}



