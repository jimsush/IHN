package com.ihn.ihnandroid;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class MyListActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_demo_list1);

        try {
            ActionBar actionBar = getActionBar();
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);

            String[] values={"上海","深圳"};

            ArrayAdapter<String> spinnerAdaptor = new ArrayAdapter<String>(this, R.layout.navigation_item, values);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(spinnerAdaptor, new DropDownNavigationListener(this));
        }catch(Throwable th){
            th.printStackTrace();
        }

    }

    public MyListActivity() {
        super();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_list, menu);
        boolean result=super.onCreateOptionsMenu(menu);

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

        return result;
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
                showMyDialog(1);
                return true;

            /*case R.id.action_findplace:
                showMyDialog(2);
                return true;
            */

            case R.id.action_managemap:
                showMyDialog(3);
                return true;

            case R.id.action_settings:
                Toast.makeText(this,"settings is under construction", Toast.LENGTH_LONG).show();
                return true;
        }

        return true;
    }

    private void showMyDialog(int type){
        MyAlertDialogFragment fragment=new MyAlertDialogFragment();
        fragment.show(getFragmentManager(),"basic");
    }
}