package com.ihn.ihnandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by tong on 2015/8/2.
 */
public class DropDownNavigationListener implements ActionBar.OnNavigationListener{

    private String[] values={"上海","深圳"};
    private Activity activity;

    public DropDownNavigationListener(Activity activity){
        this.activity=activity;
    }

    @Override
    public boolean onNavigationItemSelected(int pos,long itemId){
        String selected=values[pos%values.length];
        Toast toast= Toast.makeText(activity.getApplicationContext(),selected,Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

}
