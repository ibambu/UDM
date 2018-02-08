package com.ibamb.udm.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.fragment.UdmSettingFragment;

public class UdmMainActivity extends AppCompatActivity {


    private TextView tabDeviceList;
    private TextView tabAppSetting;

    private DeviceSearchListFragment deviceSearchListFragment;
    private UdmSettingFragment udmSettingFragment;
    private Toolbar toolbar;


    /**
     * 菜单点击事件,切换到菜单对应的界面Fragment.
     */
    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            switch(v.getId()){
                case R.id.menu_device_list:
                    selected();
                    tabDeviceList.setSelected(true);
                    tabDeviceList.setBackgroundColor(Color.LTGRAY);
                    tabAppSetting.setBackgroundColor(Color.WHITE);
                    if(deviceSearchListFragment ==null){
                        deviceSearchListFragment = DeviceSearchListFragment.newInstance("ip setting",null);
                        transaction.add(R.id.home_fragment_container,deviceSearchListFragment);
                        System.out.println("111111111111");
                    }else{
                        System.out.println("aaaaaaaaaaaaa");
                        transaction.show(deviceSearchListFragment);
                    }
                    break;

                case R.id.menu_app_setting:
                    selected();
                    tabAppSetting.setSelected(true);
                    tabAppSetting.setBackgroundColor(Color.LTGRAY);
                    tabDeviceList.setBackgroundColor(Color.WHITE);
                    if(udmSettingFragment==null){
                        udmSettingFragment = UdmSettingFragment.newInstance("connecting setting",null);
                        transaction.add(R.id.home_fragment_container,udmSettingFragment);
                        System.out.println("22222222222");
                    }else{
                        transaction.show(udmSettingFragment);
                        System.out.println("bbbbbbbbbbbbbb");
                    }
                    break;

            }

            transaction.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
        setContentView(R.layout.activity_udm_main);
        toolbar = (Toolbar) findViewById(R.id.udm_toolbar);
        toolbar.setTitle("UDM");//设置主标题
//        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.tool_bar_menu);//设置右上角的填充菜单
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.id_menu_user_profile) {


                } else if (menuItemId == R.id.id_menu_or_code) {


                } else if (menuItemId == R.id.id_menu_join_cloud) {


                }
                return true;
            }
        });
        bindView();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(deviceSearchListFragment ==null){
            deviceSearchListFragment = DeviceSearchListFragment.newInstance("ip setting",null);
            transaction.add(R.id.home_fragment_container,deviceSearchListFragment);
        }else{
            transaction.show(deviceSearchListFragment);
        }
        transaction.commit();
    }

    /**
     * UI组件初始化与事件绑定
     */
    private void bindView() {
        tabDeviceList = (TextView)this.findViewById(R.id.menu_device_list);
        tabAppSetting = (TextView)this.findViewById(R.id.menu_app_setting);

        tabDeviceList.setOnClickListener(menuOnClickListener);
        tabAppSetting.setOnClickListener(menuOnClickListener);

    }
    //重置所有文本的选中状态
    public void selected(){
        tabDeviceList.setSelected(false);
        tabAppSetting.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(deviceSearchListFragment!=null){
            transaction.hide(deviceSearchListFragment);
        }
        if(udmSettingFragment!=null){
            transaction.hide(udmSettingFragment);
        }
    }

}
