package com.ihn.ihnandroid.parking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ihn.ihnandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * show all parking station using grid layout
 *
 */
public class PropertyGridActivity extends Activity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_grid);

        initGridView1();
    }

    private void initGridView1(){
        gridView=(GridView)findViewById(R.id.gridView);

        initImageTextList1();
    }

    private void initImageTextList1(){
        Map<String, Object> item1 = new HashMap<String, Object>();
        item1.put("image", R.drawable.setting_64);

        Map<String, Object> item2 = new HashMap<String, Object>();
        item2.put("image", R.drawable.car_64);

        Map<String, Object> item3 = new HashMap<String, Object>();
        item3.put("image", R.drawable.search_50);

        Map<String, Object> item4 = new HashMap<String, Object>();
        item4.put("image", R.drawable.map_64);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(item1);
        data.add(item2);
        data.add(item3);
        data.add(item4);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.property_grid_item,
                new String[]{"image"}, new int[]{ R.id.imageView_propIcon} );

        gridView.setAdapter(simpleAdapter);
    }

}
