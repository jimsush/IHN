package com.ihn.ihnandroid;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ihn.ihnandroid.beacon.ConfigureBeaconActivity;
import com.ihn.ihnandroid.beacon.brt.AllDemosActivity;
import com.ihn.ihnandroid.parking.ParkingWebViewActivity;
import com.ihn.ihnandroid.parking.PreferenceActivity;
import com.ihn.ihnandroid.parking.ManageMapActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyListActivity extends Activity {

    private ListView listView;

    public MyListActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_demo_list1);

        listView = (ListView) findViewById(R.id.listView1);

        initActionBar1();
        initImageTextList1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_list, menu);
        boolean result=super.onCreateOptionsMenu(menu);

        initSearchPlaceInActionBar1(menu);
        return result;
    }

    private void initActionBar1(){
        try {
            ActionBar actionBar = getActionBar();
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);

            String[] values={"上海","深圳"};
            ArrayAdapter<String> spinnerAdaptor = new ArrayAdapter<String>(this, R.layout.navigation_item, values);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(spinnerAdaptor, new DropDownNavigationListener(this));

            initActionBarTabs1(actionBar);
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    private void initImageTextList1(){
        SimpleAdapter simpleAdapter=initParkingIcons();
        this.listView.setAdapter(simpleAdapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openHTML5MapView1();
            }
        });
    }

    private SimpleAdapter initParkingIcons(){
        Object[] images=new Object[]{
                R.drawable.p1,
                R.drawable.p2,
                R.drawable.p3,
                R.drawable.p4,
                R.drawable.p5
        };
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(Object image : images) {
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("image", image);
            data.add(item1);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.property_image_item2,
                new String[]{"image"}, new int[]{ R.id.imageView_prop});
        return simpleAdapter;
    }

    private void initSearchPlaceInActionBar1(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.action_findplace);
        SearchView searchView =(SearchView)searchItem.getActionView();
        if(searchView!=null) {
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MyListActivity.this,v.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                    System.out.println(v.getClass().getSimpleName());
                }
            });
        }
    }

    private void initActionBarTabs1(ActionBar actionBar){
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("个人中心").setTabListener(new MyTabMenuListener()));
        actionBar.addTab(actionBar.newTab().setText("地图管理").setTabListener(new MyTabMenuListener()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.home:
                Intent i = new Intent(this, MyListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;

            case R.id.action_test:
                showMyDialog(1);
                return true;
            */

            case R.id.action_findcar:
                openHTML5MapView1();
                return true;

            case R.id.action_managemap:
                openManageMapView1();
                return true;

            case R.id.action_settings:
                //Toast.makeText(this,"settings is under construction", Toast.LENGTH_LONG).show();
                openPreferenceView1();
                return true;

            case R.id.action_brt:
                openBrtBeacon();
                return true;

            case R.id.action_config:
                openConfigureBeacon();
                return true;
        }

        return true;
    }

    private void showMyDialog(int type){
        MyAlertDialogFragment fragment=new MyAlertDialogFragment();
        fragment.show(getFragmentManager(),"basic");
    }

    private void openHTML5MapView1(){
        Intent intent=new Intent();
        intent.setClass(MyListActivity.this,ParkingWebViewActivity.class);
        startActivity(intent);
    }

    private void openManageMapView1(){
        Intent intent=new Intent();
        intent.setClass(MyListActivity.this, ManageMapActivity.class);
        startActivity(intent);
    }

    private void openPreferenceView1(){
        Intent intent=new Intent();
        intent.setClass(MyListActivity.this,PreferenceActivity.class);
        startActivity(intent);
    }

    private void openBrtBeacon(){
        Intent intent=new Intent();
        intent.setClass(MyListActivity.this,AllDemosActivity.class);
        startActivity(intent);
    }

    private void openConfigureBeacon(){
        Intent intent=new Intent();
        intent.setClass(MyListActivity.this, ConfigureBeaconActivity.class);
        startActivity(intent);

    }

}