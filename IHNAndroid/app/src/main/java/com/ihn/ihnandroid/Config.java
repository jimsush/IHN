package com.ihn.ihnandroid;

import com.ihn.ihnandroid.beacon.BluetoothPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tong on 2015/11/7.
 */
public class Config {

    private int scale=800;

    private Map<String, BluetoothPosition> allBeacons=new ConcurrentHashMap<String, BluetoothPosition>();

    private static Config instance=new Config();
    private Config(){}
    public static Config getInstance(){
        return instance;
    }

    public synchronized int getScale(){
        return this.scale;
    }

    public synchronized void setScale(int scale){
        this.scale=scale;
    }

    public void registerNewBeacon(String mac, double x, double y){
        this.allBeacons.put(mac, new BluetoothPosition(mac, x, y));
    }

    public BluetoothPosition getBeacon(String mac){
        return this.allBeacons.get(mac);
    }

    public Map<String, BluetoothPosition> getAllBeacons(){
        return this.allBeacons;
    }

}
