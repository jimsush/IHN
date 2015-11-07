package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ihn.ihnandroid.R;
import com.ihn.ihnandroid.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * show all parking station using grid layout
 *
 */
public class PreferenceActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list1);

        initListView1();
    }

    private void initListView1(){
        listView=(ListView)findViewById(R.id.mapListView);

        initList();
    }

    private void initList(){
        String[] items=new String[]{ "登录与注册         >  ", "个性化设置         >  "
                , "检查更新             >  ", "关于本软件         >  " };
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(String item : items) {
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("name", item);
            data.add(item1);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.preference_item1
                , new String[]{"name"}, new int[]{ R.id.preferenceName} );

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openLoginView1();
            }
        });
    }

    private void openLoginView1(){
        Intent intent=new Intent();
        intent.setClass(PreferenceActivity.this,LoginActivity.class);
        startActivity(intent);
    }


}
