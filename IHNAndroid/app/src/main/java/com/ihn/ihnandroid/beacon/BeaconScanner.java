package com.ihn.ihnandroid.beacon;


import android.webkit.WebView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.RangingListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.brtbeacon.sdk.Utils;
import com.brtbeacon.sdk.service.RangingResult;
import com.ihn.ihnandroid.Config;
import com.ihn.ihnandroid.parking.ParkingWebViewActivity;

import java.math.BigDecimal;
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

    private ParkingWebViewActivity activity;
    private WebView webView;
    private BRTBeaconManager beaconManager;
    private Map<String, BRTBeacon> foundBeacons=new HashMap<String, BRTBeacon>();
    private boolean stopped=false;
    private double[] curXY;
    private Config config;

    private static final BRTRegion ALL_BRIGHT_BEACONS_REGION = new BRTRegion("rid", null, null, null, null);

    public BeaconScanner(ParkingWebViewActivity activity, WebView webView){
        this.activity=activity;
        this.webView=webView;
        this.config=Config.getInstance();
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    activity.setSubtitle("开始扫描蓝牙信标");
                    }
                });

                try {
                    beaconManager.startRanging(ALL_BRIGHT_BEACONS_REGION);
                    stopped=false;
                } catch (Throwable e) {
                    Toast.makeText(BeaconScanner.this.activity, "开始扫描错误:"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onStop(){
        stopped=true;
        beaconManager.disconnect();
        curXY=null;
    }

    public double[] getCurrentPosition(){
        if(curXY==null || curXY.length==0)
            return null;
        return new double[]{ curXY[0], curXY[1] };
    }

    private void loadBeaconMetadata(){
        config.registerNewBeacon("EC:23:B1:51:0D:BB", 1100, -1100);
        config.registerNewBeacon("F4:B8:5E:A1:4A:BE", 300, -1100);
    }

    private void init(){
        beaconManager = new BRTBeaconManager(activity);
        beaconManager.setRangingListener(new RangingListener() {
            @Override
            public void onBeaconsDiscovered(final RangingResult rangingResult) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(stopped) {
                            curXY=null;
                            return;
                        }

                        try{
                            foundBeacons.clear();
                            List<BRTBeacon> scannedBeacons = (List<BRTBeacon>) rangingResult.beacons;
                            activity.setSubtitle(getBRTBeaconString(scannedBeacons));
                            if(scannedBeacons.size()==0) {
                                curXY=null;
                                loadLocalHTML("javascript:clearPosition()");
                                return;
                            }

                            double[] pos = getP3Position(scannedBeacons);
                            if(pos==null || pos.length==0) {
                                activity.setSubtitle("当前位置:未知");
                                curXY=null;
                                loadLocalHTML("javascript:clearPosition()");
                                return;
                            }

                            activity.setSubtitle("当前位置:(" + pos[0] + "," + pos[1] + "," + pos[2] + "m," + pos[3] + "m)");
                            if(isSamePositionWithLast(pos[0], pos[1])) {
                                // don't move so frequently
                                return;
                            }

                            curXY=new double[]{ pos[0], pos[1] };
                            loadLocalHTML("javascript:myPosition("+pos[0]+","+pos[1]+")");
                        }catch(Throwable th){
                            Toast.makeText(activity, "信标搜索错误:"+th.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private boolean isSamePositionWithLast(double newPosX, double newPosY){
        if(curXY==null || curXY.length==0)
            return false;
        if(curXY[0]==Double.NaN)
            return false;

        double delta=Math.abs(newPosX-curXY[0]);
        if(delta>=0.1)
            return false;
        delta=Math.abs(newPosY-curXY[1]);
        if(delta>=0.1)
            return false;
        return true;
    }

    private String getBRTBeaconString(List<BRTBeacon> scannedBeacons){
        if(scannedBeacons==null || scannedBeacons.size()==0)
            return "蓝牙:0个";

        StringBuilder sb=new StringBuilder();
        int i=0;
        for(BRTBeacon beacon : scannedBeacons){
            if(i>0){
                sb.append(",");
            }
            sb.append("mac:").append(beacon.getMacAddress()).append(" rssi:").append(beacon.getRssi());
            i++;
        }
        return sb.toString();
    }

    /**
     * get the current position
     * @param scannedBeacons
     * @return [0]: x [1]:y, [2]:distance of first beacon [3]:distance of second beacon
     */
    private double[] getP3Position(List<BRTBeacon> scannedBeacons){
        List<TmpBeacon> tmpBeacons=new ArrayList<TmpBeacon>();
        for (BRTBeacon rangedBeacon : scannedBeacons) {
            BluetoothPosition pos=config.getBeacon(rangedBeacon.getMacAddress());
            if(pos==null){
                // unauthorized beacons, so ignore it
                Toast.makeText(activity, "忽略不明信标"+rangedBeacon.getMacAddress(), Toast.LENGTH_LONG).show();
                continue;
            }

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
            BluetoothPosition pos=config.getBeacon(first.getMac());
            BRTBeacon bt1 = foundBeacons.get(first.getMac());
            double distance=getDistance(bt1);
            return new double[]{ pos.x, pos.y, distance, 0 };
        }else {
            Iterator<TmpBeacon> it = tmpBeacons.iterator();

            TmpBeacon first = it.next();
            TmpBeacon second = it.next();

            BluetoothPosition pos1 = config.getBeacon(first.getMac());
            BluetoothPosition pos2 = config.getBeacon(second.getMac());

            BRTBeacon bt1 = foundBeacons.get(first.getMac());
            BRTBeacon bt2 = foundBeacons.get(second.getMac());
            double distance1 = getDistance(bt1);
            double distance2 = getDistance(bt2);
            int scale=config.getScale(); // default is 800, 800 points in map = 1 meter in real world
            PositionResult result = NearestDistance.getP3(pos1.x, pos1.y, pos2.x, pos2.y, distance1*scale, distance2*scale);
            if (result == null || result.getValueNum() == 0) {
                return new double[]{ pos1.x, pos1.y, distance1, distance2 };
                //return null; //can't return null
            } else if (result.getValueNum() == 1) {
                Toast.makeText(activity, "1个可能点", Toast.LENGTH_LONG).show();
                return new double[]{result.p1x, result.p1y, distance1, distance2};
            } else {
                Toast.makeText(activity, "2个可能点"+result, Toast.LENGTH_LONG).show();
                double px = (result.p1x + result.p2x) / 2;
                double py = (result.p1y + result.p2y) / 2;
                return new double[]{px, py, distance1, distance2};
            }
        }
    }

    private double getDistance(BRTBeacon beacon){
        double distance=Utils.computeAccuracy(beacon);
        BigDecimal bg = new BigDecimal(distance);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void loadLocalHTML(String url){
        try {
            webView.loadUrl(url);
        }catch(Throwable th){
            Toast.makeText(activity, "地图定位错误:"+th.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}



