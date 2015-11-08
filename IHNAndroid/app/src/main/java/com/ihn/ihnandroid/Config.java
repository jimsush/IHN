package com.ihn.ihnandroid;

import com.ihn.ihnandroid.beacon.BluetoothPosition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tong on 2015/11/7.
 */
public class Config {

    private int scale=800;

    private boolean debug=false;

    private Map<String, BluetoothPosition> allBeacons=new ConcurrentHashMap<String, BluetoothPosition>();

    private Set<String> unauthBeacons=new HashSet<String>();

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

    public synchronized boolean isDebug(){
        return this.debug;
    }
    public synchronized void setDebug(boolean debug){
        this.debug=debug;
    }

    public void addNewUnAuth(String mac){
        unauthBeacons.add(mac);
    }

    public void clearNewUnAuth(String mac){
        unauthBeacons.clear();
    }

    public Set<String> getAllUnAuthBeacons(){
        return unauthBeacons;
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
