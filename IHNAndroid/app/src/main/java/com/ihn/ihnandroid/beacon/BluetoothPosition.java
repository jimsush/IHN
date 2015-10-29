package com.ihn.ihnandroid.beacon;

/**
 * Created by tong on 2015/10/28.
 */
public class BluetoothPosition {
    String mac;
    String uuid;
    double distance;
    double x;
    double y;

    public BluetoothPosition(String mac, double x, double y){
        this.mac=mac;
        this.x=x;
        this.y=y;
    }

    public String getMac(){ return this.mac; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }

    @Override
    public String toString(){
        return mac+", ("+x+", "+y+")";
    }
}
