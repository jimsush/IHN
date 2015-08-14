package com.ihn.ihnandroid;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by tong on 2015/7/28.
 */
public class MyHandler extends Handler {

    private TextView detailTxtView;

    public void setTextView(TextView detailTxtView){
        this.detailTxtView=detailTxtView;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        detailTxtView.setText(msg.obj.toString());
    }
}
