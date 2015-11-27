package com.ihn.ihnandroid.beacon;

import android.app.Activity;

import com.ihn.ihnandroid.Config;

/**
 * Created by tong on 2015/11/27.
 */
public class V2Compute {
    public final static String STATUS_NA="NA";
    public final static String STATUS_GOOD="GOOD";

    private static long lastActiveTime=0;
    private static String lastStatus=STATUS_NA;
    private static long GONE_TIME_MS_THRESHOLD=5000;

    /**
     * judge whether person/car has really left the beacon zone...
     * @param currentBeaconNumber
     * @return  true (no beacons found)   false (found some beacons)
     */
    public static boolean isRealNotFoundBeacons(int currentBeaconNumber){
        long now1=System.currentTimeMillis();
        if(currentBeaconNumber==0){
            lastStatus=STATUS_NA;

            if(lastActiveTime==0){
                return true;
            }

            long elapsedMS=now1-lastActiveTime;
            if(elapsedMS>=GONE_TIME_MS_THRESHOLD){
                lastActiveTime = 0; //reset
                return true;
            }else {
                return false;
            }
        }else{
            lastStatus=STATUS_GOOD;
            lastActiveTime=now1;
            return false;
        }
    }

    /**
     * 9 possibilities
     * <br> NA NA => A1
     * <br> X1 X2 => (X1+X2)/2
     * <br> NA X2 => X2
     * <br> X1 NA => X1
     * <br> X1 Y1 X2 Y2 => min(X1->X2, Y2, Y1 ->X2, Y2)
     * <br> X1 Y1 NA => (X1+Y1)/2
     * <br> X1 Y1 X2 => min(X1->X2, Y1->X2)
     * <br> X1 X2 Y2 => min(X2->X1, Y2->X1)
     * <br> NA X2 Y2 => (X2+Y2)/2
     *
     * @param scanResult
     * @return
     */
    public static double[] getPosition(Activity activity, V2ScanResult scanResult){
        //+ 3 points
        V2ConsolidatedBeacon A1=scanResult.get(0);
        V2ConsolidatedBeacon A2=scanResult.get(1);
        V2ConsolidatedBeacon A3=scanResult.get(2);

        double distance1 = BeaconUtils.getDistance(A1.getBRTBeacon());
        double distance2 = BeaconUtils.getDistance(A2.getBRTBeacon());
        double distance3 = BeaconUtils.getDistance(A3.getBRTBeacon());

        Config config=Config.getInstance();
        BluetoothPosition pos1 = A1.getBluetoothPosition();
        BluetoothPosition pos2 = A2.getBluetoothPosition();
        BluetoothPosition pos3 = A3.getBluetoothPosition();

        int scale= config.getScale(); // default is 800, 800 points in map = 1 meter in real world
        PositionResult result1 = NearestDistance.getP3(pos1.x, pos1.y, pos2.x, pos2.y, distance1 * scale, distance2 * scale);
        PositionResult result2 = NearestDistance.getP3(pos1.x, pos1.y, pos3.x, pos3.y, distance1 * scale, distance3 * scale);

        if (result1 == null || result1.getValueNum() == 0) {
            if (result2 == null || result2.getValueNum() == 0) {
                //A1
                return new double[]{ pos1.x, pos1.y, distance1, distance2 };
            }else if(result2.getValueNum()==1){
                //X2
                return new double[]{ BeaconUtils.round(result2.p1x, 2), BeaconUtils.round(result2.p1y, 2), distance1, distance3};
            }else{
                //(X2+Y2)/2
                double px = (result2.p1x + result2.p2x) / 2;
                double py = (result2.p1y + result2.p2y) / 2;
                return new double[]{ BeaconUtils.round(px, 2), BeaconUtils.round(py, 2), distance1, distance3};
            }
        } else if (result1.getValueNum() == 1) {
            BeaconUtils.debug(activity, "1个可能点3", config.isDebug());
            if (result2 == null || result2.getValueNum() == 0) {
                //X1
                return new double[]{ BeaconUtils.round(result1.p1x, 2), BeaconUtils.round(result1.p1y, 2), distance1, distance2};
            }else if(result2.getValueNum()==1){
                //(X1+X2)/2
                double px = (result1.p1x + result2.p1x) / 2;
                double py = (result1.p1y + result2.p1y) / 2;
                return new double[]{BeaconUtils.round(px, 2), BeaconUtils.round(py,2), distance1, distance2};
            }else{
                //min(X2,Y2) with X1
                double d1=(result1.p1x-result2.p1x)*(result1.p1x-result2.p1x)+(result1.p1y-result2.p1y)*(result1.p1y-result2.p1y);
                double d2=(result1.p1x-result2.p2x)*(result1.p1x-result2.p2x)+(result1.p1y-result2.p2y)*(result1.p1y-result2.p2y);
                if(d1<=d2){
                    // X2 is better
                    return new double[]{BeaconUtils.round(result2.p1x, 2), BeaconUtils.round(result2.p1y, 2), distance1, distance3};
                }else{
                    // Y2 is better
                    return new double[]{BeaconUtils.round(result2.p2x, 2), BeaconUtils.round(result2.p2y, 2), distance1, distance3};
                }
            }
        } else {
            BeaconUtils.debug(activity, "2个可能点3 "+result1, config.isDebug());
            if (result2 == null || result2.getValueNum() == 0) {
                //X1+Y1 /2
                double px = (result1.p1x + result1.p2x) / 2;
                double py = (result1.p1y + result1.p2y) / 2;
                return new double[]{BeaconUtils.round(px, 2), BeaconUtils.round(py,2), distance1, distance2};
            }else if(result2.getValueNum()==1){
                //min(X1,Y1) with X2
                double d1=(result2.p1x-result1.p1x)*(result2.p1x-result1.p1x)+(result2.p1y-result1.p1y)*(result2.p1y-result1.p1y);
                double d2=(result2.p1x-result1.p2x)*(result2.p1x-result1.p2x)+(result2.p1y-result1.p2y)*(result2.p1y-result1.p2y);
                if(d1<=d2){
                    // X1 is better
                    return new double[]{BeaconUtils.round(result1.p1x, 2), BeaconUtils.round(result1.p1y, 2), distance1, distance2};
                }else{
                    // Y1 is better
                    return new double[]{BeaconUtils.round(result1.p2x, 2), BeaconUtils.round(result1.p2y, 2), distance1, distance2};
                }
            }else{
                //min(X1,Y1) with (X2, Y2)
                BeaconUtils.debug(activity, "4个可能点3 "+result1+","+result2, config.isDebug());

                double d11=(result1.p1x-result2.p1x)*(result1.p1x-result2.p1x)+(result1.p1y-result2.p1y)*(result1.p1y-result2.p1y);
                double d21=(result1.p2x-result2.p1x)*(result1.p2x-result2.p1x)+(result1.p2y-result2.p1y)*(result1.p2y-result2.p1y);
                double d12=(result1.p1x-result2.p2x)*(result1.p1x-result2.p2x)+(result1.p1y-result2.p2y)*(result1.p1y-result2.p2y);
                double d22=(result1.p2x-result2.p2x)*(result1.p2x-result2.p2x)+(result1.p2y-result2.p2y)*(result1.p2y-result2.p2y);
                double minD1=0, minD2=0;
                int smallPoint1=0, smallPoint2=0;
                if(d11<d21){
                    minD1=d11;
                    smallPoint1=1;
                }else{
                    minD1=d21;
                    smallPoint1=2;
                }
                if(d12<d22){
                    minD2=d12;
                    smallPoint2=1;
                }else{
                    minD2=d22;
                    smallPoint2=2;
                }
                if(minD1<minD2){
                    if(smallPoint1==1){
                        //1
                        return new double[]{BeaconUtils.round(result1.p1x, 2), BeaconUtils.round(result1.p1y, 2), distance1, distance2};
                    }else{
                        //2
                        return new double[]{BeaconUtils.round(result1.p2x, 2), BeaconUtils.round(result1.p2y, 2), distance1, distance2};
                    }
                }else{
                    if(smallPoint2==1){
                        //1
                        return new double[]{BeaconUtils.round(result1.p1x, 2), BeaconUtils.round(result1.p1y, 2), distance1, distance2};
                    }else{
                        //2
                        return new double[]{BeaconUtils.round(result1.p2x, 2), BeaconUtils.round(result1.p2y, 2), distance1, distance2};
                    }
                }
            }
        }
    }

}
