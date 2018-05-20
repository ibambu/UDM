package com.ibamb.udm.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.UDPConnectionActivity;
import com.ibamb.udm.activity.UserProfileActivity;

/**
 * APP顶部导航菜单点击事件监听器
 * Created by luotao on 18-4-14.
 */

public class UdmToolbarMenuClickListener implements Toolbar.OnMenuItemClickListener {

    private Context context;

    public UdmToolbarMenuClickListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.id_menu_user_profile) {
            Intent intent = new Intent(context, UserProfileActivity.class);
            context.startActivity(intent);

        } else if (menuItemId == R.id.id_menu_or_code) {


        } else if (menuItemId == R.id.id_menu_join_cloud) {

        } else if (menuItemId == R.id.id_menu_exit) {
            System.exit(0);
        }
        return true;
    }
}
