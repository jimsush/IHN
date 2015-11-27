package com.ihn.ihnandroid.beacon;

import android.app.Activity;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.Utils;

import java.math.BigDecimal;

/**
 * Created by tong on 2015/11/27.
 */
public class BeaconUtils {

    public static double getDistance(BRTBeacon beacon){
        double distance= Utils.computeAccuracy(beacon);
        if(Double.isNaN(distance) || distance<0){
            return 10;
        }

        return round(distance, 2);
    }

    public static double round(double d, int scale){
        if(Double.isNaN(d)){
            return d;
        }

        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void warn(Activity activity, String msg){
        info(activity,msg);
    }

    public static void info(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    public static void debug(Activity activity ,String msg, boolean isDebug){
        if(isDebug) {
            Thread tr=Thread.currentThread();
            Toast.makeText(activity,tr.getName() + "["+tr.getId()+"] " + msg, Toast.LENGTH_LONG).show();
        }
    }

    public static String getExceptionStack(Throwable th){
        StackTraceElement[] stack=th.getStackTrace();
        if(stack==null || stack.length==0){
            return "";
        }

        StringBuilder sb=new StringBuilder();
        int i=0;
        for(StackTraceElement ex : stack){
            if(i>0) {
                sb.append(",");
            }
            sb.append(i).append(ex.getFileName()).append(":").append(ex.getLineNumber());
            sb.append(ex.getClassName()).append(".").append(ex.getMethodName());

            i++;
            if(i>=3)
                break;
        }
        return sb.toString();
    }



}
