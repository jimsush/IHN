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
            BeaconUtils.warn(activity, e.getMessage());
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
                    BeaconUtils.warn(activity, "开始扫描错误:" + e.getMessage());
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
        // UUID: E2C56DB5-DFFB
        config.registerNewBeacon("EC:23:B1:51:0D:BB", 1100, -1100); //plus
        config.registerNewBeacon("F4:B8:5E:A1:4A:BE", 300, -1100);  //1.1
        config.registerNewBeacon("CA:DA:C3:D6:17:79", -500, -1100); //plus
        config.registerNewBeacon("20:C3:8F:D2:7E:1B", -600, -1800); //B-tag
        config.registerNewBeacon("F4:B8:5E:A1:3A:2D", 900, -1800);  // beacon1.1, B-02
        //config.registerNewBeacon("57:06:18:A6:1A:4C", -300, -1100);
        //config.registerNewBeacon("6B:1A:3D:A5:31:BF", 0, -1800);
        //config.registerNewBeacon("72:A2:D2:C6:D3:92", 1100, -1800);
        //config.registerNewBeacon("73:2B:2A:76:FA:57", -1100, -1800);
    }

    private void init(){
        beaconManager = new BRTBeaconManager(activity);
        beaconManager.setRangingListener(new RangingListener() {
            @Override
            public void onBeaconsDiscovered(final RangingResult rangingResult) {
                if(stopped) {
                    curXY=null;
                    return;
                }

                List<BRTBeacon> scannedAllBeacons = (List<BRTBeacon>) rangingResult.beacons;
                if(config.isDebug()) {
                    String beaconInfo = getBRTBeaconString(scannedAllBeacons);
                    BeaconUtils.debug(activity, "本次扫描到" + scannedAllBeacons.size() + "个蓝牙设备" + beaconInfo, config.isDebug());
                }

                final List<BRTBeacon> scannedBeacons =filterBeacons(scannedAllBeacons);
                BeaconUtils.debug(activity, "本次扫描到"+scannedBeacons.size()+"个BRT蓝牙设备", config.isDebug());
                final boolean beaconGone=V2Compute.isRealNotFoundBeacons(scannedBeacons.size());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String beaconInfo=getBRTBeaconString(scannedBeacons);
                            activity.setSubtitle(beaconInfo);
                            if(scannedBeacons.size()==0) {
                                if(beaconGone) {
                                    activity.setSubtitle("当前位置:未知");
                                    curXY = null;
                                    loadLocalHTML("javascript:clearPosition()");
                                }else{
                                    // no change, hold current state
                                }
                                return;
                            }

                            double[] pos = getP3Position(scannedBeacons);
                            if(pos==null || pos.length==0) {
                                activity.setSubtitle("当前位置:未知");
                                curXY=null;
                                loadLocalHTML("javascript:clearPosition()");
                                return;
                            }

                            if(Double.isNaN(pos[0]) || Double.isNaN(pos[1])){
                                BeaconUtils.debug(activity, "NaN for "+beaconInfo, config.isDebug());
                            }
                            activity.setSubtitle("当前位置:(" + pos[0] + "," + pos[1] + "," + pos[2] + "m," + pos[3] + "m)");
                            if(isSamePositionWithLast(pos[0], pos[1])) {
                                // don't move so frequently
                                return;
                            }

                            curXY=new double[]{ pos[0], pos[1] };
                            loadLocalHTML("javascript:myPosition("+pos[0]+","+pos[1]+")");
                        }catch(Throwable th){
                            BeaconUtils.warn(activity, "信标搜索错误:"+th.getMessage()+" "+BeaconUtils.getExceptionStack(th));
                        }
                    }
                });
            }
        });
    }

    private boolean isSamePositionWithLast(double newPosX, double newPosY){
        if(curXY==null || curXY.length==0)
            return false;
        if(Double.isNaN(curXY[0]))
            return false;

        double delta=Math.abs(newPosX-curXY[0]);
        if(delta>=0.1)
            return false;
        delta=Math.abs(newPosY-curXY[1]);
        if(delta>=0.1)
            return false;
        return true;
    }

    private List<BRTBeacon> filterBeacons(List<BRTBeacon> all) {
        List<BRTBeacon> result=new ArrayList<BRTBeacon>();
        if(all==null || all.size()==0) {
            return result;
        }
        try {
            for (BRTBeacon b : all) {
                String uuid = b.getUuid();
                if (uuid != null && (uuid.startsWith("E2C56DB5") || uuid.startsWith("e2c56db5") )) {
                    BluetoothPosition pos = config.getBeacon(b.getMacAddress());
                    if (pos == null) {
                        BeaconUtils.debug(activity, "忽略未注册的BRT信标" + b.getMacAddress(), config.isDebug());
                        config.addNewUnAuth(b.getMacAddress());
                    } else {
                        result.add(b);
                    }
                } else {
                    // ignore uuid==null or not brt beacon
                    BeaconUtils.debug(activity, "忽略不明UUID信标" + b.getMacAddress() + " uuid:" + uuid, config.isDebug());
                }
            }
        }catch(Throwable th){
            BeaconUtils.warn(activity, "过滤信标异常:"+th.getMessage());
        }

        return result;
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
        return "蓝牙:"+i+"个,"+sb.toString();
    }

    /**
     * get the current position
     * @param scannedBeacons
     * @return [0]: x [1]:y, [2]:distance of first beacon [3]:distance of second beacon
     */
    private double[] getP3Position(List<BRTBeacon> scannedBeacons){
        Map<String, BRTBeacon> foundBrtBeacons=new HashMap<String, BRTBeacon>();
        List<TmpBeacon> tmpBeacons=new ArrayList<TmpBeacon>();
        for (BRTBeacon rangedBeacon : scannedBeacons) {
            BluetoothPosition pos=config.getBeacon(rangedBeacon.getMacAddress());
            if(pos==null) {
                continue;
            }

            tmpBeacons.add(new TmpBeacon(rangedBeacon.getMacAddress(), rangedBeacon.getRssi()));
            foundBrtBeacons.put(rangedBeacon.getMacAddress(), rangedBeacon);
        }
        Collections.sort(tmpBeacons);

        if(tmpBeacons.size()==0){
            // not found
            return null;
        }else if(tmpBeacons.size()==1){
            Iterator<TmpBeacon> it=tmpBeacons.iterator();
            TmpBeacon first=it.next();
            BluetoothPosition pos=config.getBeacon(first.getMac());
            BRTBeacon bt1 = foundBrtBeacons.get(first.getMac());
            double distance=BeaconUtils.getDistance(bt1);
            return new double[]{ pos.x, pos.y, distance, 0 };
        }else if(tmpBeacons.size()==2){
            Iterator<TmpBeacon> it = tmpBeacons.iterator();

            TmpBeacon first  = it.next();
            TmpBeacon second = it.next();

            BluetoothPosition pos1 = config.getBeacon(first.getMac());
            BluetoothPosition pos2 = config.getBeacon(second.getMac());

            BRTBeacon bt1 = foundBrtBeacons.get(first.getMac());
            BRTBeacon bt2 = foundBrtBeacons.get(second.getMac());

            double distance1 = BeaconUtils.getDistance(bt1);
            double distance2 = BeaconUtils.getDistance(bt2);
            int scale=config.getScale(); // default is 800, 800 points in map = 1 meter in real world
            PositionResult result = NearestDistance.getP3(pos1.x, pos1.y, pos2.x, pos2.y, distance1*scale, distance2*scale);
            if (result == null || result.getValueNum() == 0) {
                BeaconUtils.debug(activity, "0个可能点2", config.isDebug());
                return new double[]{ pos1.x, pos1.y, distance1, distance2 };
                //return null; //can't return null
            } else if (result.getValueNum() == 1) {
                BeaconUtils.debug(activity, "1个可能点2", config.isDebug());
                return new double[]{ BeaconUtils.round(result.p1x, 2), BeaconUtils.round(result.p1y, 2), distance1, distance2};
            } else {
                BeaconUtils.debug(activity, "2个可能点2 "+result, config.isDebug());
                double px = (result.p1x + result.p2x) / 2;
                double py = (result.p1y + result.p2y) / 2;
                return new double[]{ BeaconUtils.round(px, 2), BeaconUtils.round(py, 2), distance1, distance2};
            }
        }else{
            // 3+ beacons
            Iterator<TmpBeacon> it = tmpBeacons.iterator();

            TmpBeacon first  = it.next();
            TmpBeacon second = it.next();
            TmpBeacon third  = it.next();

            BluetoothPosition pos1 = config.getBeacon(first.getMac());
            BluetoothPosition pos2 = config.getBeacon(second.getMac());
            BluetoothPosition pos3 = config.getBeacon(third.getMac());

            BRTBeacon bt1 = foundBrtBeacons.get(first.getMac());
            BRTBeacon bt2 = foundBrtBeacons.get(second.getMac());
            BRTBeacon bt3 = foundBrtBeacons.get(third.getMac());

            V2ConsolidatedBeacon cb1=new V2ConsolidatedBeacon(first,pos1,bt1);
            V2ConsolidatedBeacon cb2=new V2ConsolidatedBeacon(second,pos2,bt2);
            V2ConsolidatedBeacon cb3=new V2ConsolidatedBeacon(third,pos3,bt3);

            V2ScanResult scanResult=new V2ScanResult(cb1, cb2, cb3);
            double[] cr=V2Compute.getPosition(activity, scanResult);
            return cr;
        }
    }

    private void loadLocalHTML(String url){
        try {
            webView.loadUrl(url);
        }catch(Throwable th){
            BeaconUtils.warn(activity, "地图定位错误:" + th.getMessage());
        }
    }

}



