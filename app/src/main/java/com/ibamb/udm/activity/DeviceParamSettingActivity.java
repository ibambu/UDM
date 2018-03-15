package com.ibamb.udm.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.fragment.ConnectSettingFragment;
import com.ibamb.udm.fragment.IPSettingFragment;
import com.ibamb.udm.service.DeviceParameterService;
import com.ibamb.udm.service.DeviceSearchService;

/**
 * 登录设备后进入的主界面
 */
public class DeviceParamSettingActivity extends AppCompatActivity implements ConnectSettingFragment.OnFragmentInteractionListener {

    private TextView tabIpSetting;
    private TextView tabConnectSetting;
    private IPSettingFragment ipSettingFragment;
    private ConnectSettingFragment connectSettingFragment;
    private String ip;
    private String mac;
    private DeviceParameterService.DeviceParameterServiceBinder parameterServiceBinder;

    @Override
    protected void onStart() {
        super.onStart();
        // 绑定Service，绑定后就会调用mConnetion里的onServiceConnected方法
        Intent bindIntent = new Intent(DeviceParamSettingActivity.this, DeviceParameterService.class);
        bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
        setContentView(R.layout.activity_device_param_setting);
        bindView();
        Bundle bundle = getIntent().getExtras();
        ip = (String)bundle.get("HOST_ADDRESS");
        mac = (String)bundle.get("HOST_MAC");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(ipSettingFragment ==null){
            ipSettingFragment = IPSettingFragment.newInstance("ip setting",null);
            transaction.add(R.id.param_fragment_container,ipSettingFragment);
        }else{
            transaction.show(ipSettingFragment);
        }
        transaction.commit();
    }

    /**
     * 菜单点击事件,切换到菜单对应的界面Fragment.
     */
    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            switch(v.getId()){
                case R.id.menu_ip_setting:
                    selected();
                    tabIpSetting.setSelected(true);
                    tabIpSetting.setBackgroundColor(Color.LTGRAY);
                    tabConnectSetting.setBackgroundColor(Color.WHITE);
                    if(ipSettingFragment ==null){
                        ipSettingFragment = IPSettingFragment.newInstance(ip,mac);
                        transaction.add(R.id.param_fragment_container,ipSettingFragment);
                    }else{
                        transaction.show(ipSettingFragment);
                    }
                    break;

                case R.id.menu_connect_setting:
                    selected();
                    tabConnectSetting.setSelected(true);
                    tabConnectSetting.setBackgroundColor(Color.LTGRAY);
                    tabIpSetting.setBackgroundColor(Color.WHITE);
                    if(connectSettingFragment==null){
                        connectSettingFragment = ConnectSettingFragment.newInstance(ip,mac);
                        transaction.add(R.id.param_fragment_container,connectSettingFragment);
                    }else{
                        transaction.show(connectSettingFragment);
                    }
                    break;

            }

            transaction.commit();
        }
    };
    /**
     * UI组件初始化与事件绑定
     */
    private void bindView() {
        tabIpSetting = (TextView)this.findViewById(R.id.menu_ip_setting);
        tabConnectSetting = (TextView)this.findViewById(R.id.menu_connect_setting);

        tabIpSetting.setOnClickListener(menuOnClickListener);
        tabConnectSetting.setOnClickListener(menuOnClickListener);

    }
    //重置所有文本的选中状态
    public void selected(){
        tabIpSetting.setSelected(false);
        tabConnectSetting.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(ipSettingFragment!=null){
            transaction.hide(ipSettingFragment);
        }
        if(connectSettingFragment!=null){
            transaction.hide(connectSettingFragment);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            parameterServiceBinder = (DeviceParameterService.DeviceParameterServiceBinder) service;
        }
    };

    /**
     *  读取设备某个通道的TCP参数设置
     * @param channelId
     * @param mac
     * @return
     */
    @Override
    public TCPChannelParameter readTCPParameterToDevice(String channelId,String mac) {
        return parameterServiceBinder.readTCPChannelParameter(channelId,mac);
    }

    /**
     * 读取设备某个通道的UDP参数设置
     * @param channelId
     * @param mac
     * @return
     */
    @Override
    public UDPChannelParameter readUDPParameterToDevice(String channelId,String mac) {
        return parameterServiceBinder.readUDPChannelParameter(channelId,mac);
    }

    /**
     * 设置设备某个通道的TCP参数
     * @param tcpParam
     * @return
     */
    @Override
    public TCPChannelParameter writeTCPParameterToDevice(TCPChannelParameter tcpParam) {
        return parameterServiceBinder.writeTCPChannelParameter(tcpParam);
    }

    /**
     * 设置设备某个通道的UDP参数
     * @param udpParam
     * @return
     */
    @Override
    public UDPChannelParameter writeUDPParameterToDevice(UDPChannelParameter udpParam) {
        return parameterServiceBinder.writeUDPChannelParameter(udpParam);
    }

}
