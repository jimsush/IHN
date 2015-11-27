package com.ihn.ihnandroid.beacon;

/**
 * Created by tong on 2015/11/27.
 */
public class V2ScanResult {

    private int validBeaconNumber=0;
    private V2ConsolidatedBeacon[] beacons;

    public V2ScanResult(){
        validBeaconNumber=0;
        beacons=null;
    }

    public V2ScanResult(V2ConsolidatedBeacon beacon){
        validBeaconNumber=1;
        beacons=new V2ConsolidatedBeacon[]{beacon};
    }

    public V2ScanResult(V2ConsolidatedBeacon beacon1,V2ConsolidatedBeacon beacon2){
        validBeaconNumber=2;
        beacons=new V2ConsolidatedBeacon[]{beacon1, beacon2};
    }

    public V2ScanResult(V2ConsolidatedBeacon beacon1,V2ConsolidatedBeacon beacon2, V2ConsolidatedBeacon beacon3){
        validBeaconNumber=3;
        beacons=new V2ConsolidatedBeacon[]{beacon1, beacon2, beacon3};
    }

    public V2ConsolidatedBeacon get(int index){
        return beacons[index];
    }


}
