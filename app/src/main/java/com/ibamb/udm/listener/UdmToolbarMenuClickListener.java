package com.ibamb.udm.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.AppUpdateActivity;
import com.ibamb.udm.activity.DeviceSyncReportActivity;
import com.ibamb.udm.activity.ScanQRCodeActivity;
import com.ibamb.udm.activity.SpeciallySearchActivity;
import com.ibamb.udm.activity.UserProfileActivity;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.task.DeviceSearchAsyncTask;

/**
 * APP顶部导航菜单点击事件监听器
 */

public class UdmToolbarMenuClickListener implements Toolbar.OnMenuItemClickListener {

    private Activity activity;
    private FragmentManager fragmentManager;
    DeviceSearchListFragment searchListFragment;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public UdmToolbarMenuClickListener(Activity activity) {
        this.activity = activity;
    }

    public UdmToolbarMenuClickListener(Activity activity,DeviceSearchListFragment searchListFragment) {
        this.activity = activity;
        this.searchListFragment = searchListFragment;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.id_menu_user_profile) {
            Intent intent = new Intent(activity, UserProfileActivity.class);
            activity.startActivity(intent);

        } else if (menuItemId == R.id.id_menu_or_code) {

            Intent intent = new Intent(activity, ScanQRCodeActivity.class);
//            Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
//            startActivityForResult(openCameraIntent, 0);
            activity.startActivityForResult(intent,-1);


        }  else if (menuItemId == R.id.id_load_param_def) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//设置类型.
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            activity.startActivityForResult(intent,1);

        } /*else if (menuItemId == R.id.id_upgrade_device) {
//            Intent intent = new Intent(activity, DeviceUpgradeActivity.class);
//            activity.startActivityForResult(intent, 1);

        }*/else if (menuItemId == R.id.spec_search_toolbar) {
            View mainView = activity.getWindow().getDecorView();
            mainView.findViewById(R.id.tab_line_layout).setVisibility(View.GONE);
            TextView bottomTtile = mainView.findViewById(R.id.tab_device_list);
            bottomTtile.setText("Device List");
            DeviceSearchAsyncTask task = new DeviceSearchAsyncTask();
            task.setActivity(activity);
            task.setSupportFragmentManager(fragmentManager);
            task.execute();

        } else if(menuItemId==R.id.id_menu_sync_report){
            Intent intent = new Intent(activity,DeviceSyncReportActivity.class);
            intent.putExtra("SYNC_ENABLED",true);
            activity.startActivity(intent);
        }/*else if (menuItemId == R.id.id_menu_exit) {

            activity.finish();
        }*/else if (menuItemId == R.id.app_about) {
            Intent intent = new Intent(activity,AppUpdateActivity.class);
            activity.startActivity(intent);
        }
        return true;
    }
}
