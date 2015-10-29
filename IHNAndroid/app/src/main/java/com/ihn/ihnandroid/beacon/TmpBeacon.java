package com.ihn.ihnandroid.beacon;

/**
 * Created by tong on 2015/10/28.
 */
public class TmpBeacon implements Comparable{
    private String mac;
    private int rssi;

    public TmpBeacon(String mac, int rssi){
        this.mac=mac;
        this.rssi=rssi;
    }

    public int getRssi(){
        return this.rssi;
    }

    public String getMac(){
        return this.mac;
    }

    @Override
    public int compareTo(Object another) {
       if(another instanceof TmpBeacon){
           TmpBeacon t=(TmpBeacon)another;
           if(this.getRssi()==t.getRssi()){
               return  0;
           }else if(this.getRssi()>t.getRssi()){
               return 1;
           }else{
               return -1;
           }
       }else{
           return 0;
       }
    }
}