package com.ibamb.udm.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.DeviceSyncReportActivity;
import com.ibamb.udm.activity.DeviceUpgradeActivity;
import com.ibamb.udm.activity.FilePickerActivity;
import com.ibamb.udm.activity.LoadParamDefActivity;
import com.ibamb.udm.activity.ScanQRCodeActivity;
import com.ibamb.udm.activity.SpeciallySearchActivity;
import com.ibamb.udm.activity.UserProfileActivity;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.task.DeviceSearchAsyncTask;

/**
 * APP顶部导航菜单点击事件监听器
 * Created by luotao on 18-4-14.
 */

public class UdmToolbarMenuClickListener implements Toolbar.OnMenuItemClickListener {

    private Activity activity;
    DeviceSearchListFragment searchListFragment;

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


        } else if (menuItemId == R.id.id_menu_join_cloud) {

        } else if (menuItemId == R.id.id_spec_search) {

            Intent intent = new Intent(activity, SpeciallySearchActivity.class);
            activity.startActivityForResult(intent, 1);

        } else if (menuItemId == R.id.id_load_param_def) {
//            Intent intent = new Intent(activity, FilePickerActivity.class);
//            activity.startActivity(intent);

        } else if (menuItemId == R.id.id_upgrade_device) {
//            Intent intent = new Intent(activity, DeviceUpgradeActivity.class);
//            activity.startActivityForResult(intent, 1);

        }else if (menuItemId == R.id.global_search) {
            View view = searchListFragment.getView();
            ListView mListView =  view.findViewById(R.id.search_device_list);
            TextView vSearchNotice = view.findViewById(R.id.search_notice_info);
            DeviceSearchAsyncTask task = new DeviceSearchAsyncTask(mListView,vSearchNotice,
                    searchListFragment.getLayoutInflater());
            task.execute();

        } else if(menuItemId==R.id.id_menu_sync_report){
            Intent intent = new Intent(activity,DeviceSyncReportActivity.class);
            intent.putExtra("SYNC_ENABLED",true);
            activity.startActivity(intent);
        }else if (menuItemId == R.id.id_menu_exit) {

            activity.finish();
        }
        return true;
    }
}
