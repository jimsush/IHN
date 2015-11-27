package com.ihn.ihnandroid.beacon;

import com.brtbeacon.sdk.BRTBeacon;
import com.ihn.ihnandroid.beacon.BluetoothPosition;
import com.ihn.ihnandroid.beacon.TmpBeacon;

/**
 * Created by tong on 2015/11/27.
 */
public class V2ConsolidatedBeacon {

    private TmpBeacon tmpBeacon;
    private BluetoothPosition bluetoothPosition;
    private BRTBeacon brtBeacon;

    public V2ConsolidatedBeacon(TmpBeacon tmpBeacon, BluetoothPosition bluetoothPosition, BRTBeacon brtBeacon){
        this.tmpBeacon=tmpBeacon;
        this.bluetoothPosition=bluetoothPosition;
        this.brtBeacon=brtBeacon;
    }

    public TmpBeacon getTmpBeacon(){
        return this.tmpBeacon;
    }

    public BluetoothPosition getBluetoothPosition(){
        return this.bluetoothPosition;
    }

    public BRTBeacon getBRTBeacon(){
        return this.brtBeacon;
    }



}
