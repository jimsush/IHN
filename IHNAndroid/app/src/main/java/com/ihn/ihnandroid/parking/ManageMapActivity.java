package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ihn.ihnandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageMapActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list1);
        listView = (ListView) findViewById(R.id.mapListView);

        initImageTextList1();
    }

    private void initImageTextList1(){
        try {
            SimpleAdapter simpleAdapter = initParkingImageText();
            this.listView.setAdapter(simpleAdapter);
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    private SimpleAdapter initParkingImageText(){
        String[] parks={
                "陆家嘴地下车库",
                "徐家汇港汇车库",
                "人民广场2号车库",
                "五角场万达商城车库",
                "中环百联商城车库",
                "第一八佰伴车库",
                "莘庄枢纽车库",
                "虹桥火车站车库",
                "虹桥机场2号航站楼车库",
                "浦东国际机场1号航站楼车库",
                "浦东国际机场2号航站楼车库",
                "浦东国际机场3号航站楼车库"
        };
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(String park : parks) {
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("image", R.drawable.park);
            item1.put("name", park+"  (50KB)");
            item1.put("download", R.drawable.download);
            data.add(item1);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.map_item1
                , new String[]{"image","name","download"}
                , new int[]{ R.id.parkingIcon, R.id.parkingName, R.id.downloadIcon});
        return simpleAdapter;
    }





}
