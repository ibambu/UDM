package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.ConnectSettingFragment;
import com.ibamb.udm.fragment.IPSettingFragment;
import com.ibamb.udm.module.net.UdmDatagramSocket;

import java.net.DatagramSocket;

/**
 * 登录设备后进入的主界面
 */
public class DeviceParamSettingActivity extends AppCompatActivity  {

    private TextView tabIpSetting;
    private TextView tabConnectSetting;
    private IPSettingFragment ipSettingFragment;
    private ConnectSettingFragment connectSettingFragment;
    private String ip;
    private String mac;
    private DatagramSocket datagramSocket;


    /**
     * 参数分类设置导航
     */
    private TextView topLinkIp;
    private TextView topLinkTcp;
    private TextView topLinkUdp;
    private TextView topLinkSerial;

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
        setContentView(R.layout.activity_device_param_setting);
        datagramSocket = UdmDatagramSocket.getDatagramSocket();
        bindView();
        Bundle bundle = getIntent().getExtras();
        ip = (String)bundle.get("HOST_ADDRESS");
        mac = (String)bundle.get("HOST_MAC");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if(ipSettingFragment ==null){
            ipSettingFragment = IPSettingFragment.newInstance(ip,mac);
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
                    resetSelected();
                    tabIpSetting.setSelected(true);
//                    tabIpSetting.setBackgroundColor(Color.LTGRAY);
//                    tabConnectSetting.setBackgroundColor(Color.WHITE);
                    if(ipSettingFragment ==null){
                        ipSettingFragment = IPSettingFragment.newInstance(ip,mac);
                        transaction.add(R.id.param_fragment_container,ipSettingFragment);
                    }else{
                        transaction.show(ipSettingFragment);
                    }
                    break;

                case R.id.menu_connect_setting:
                    resetSelected();
                    tabConnectSetting.setSelected(true);
//                    tabConnectSetting.setBackgroundColor(Color.LTGRAY);
//                    tabIpSetting.setBackgroundColor(Color.WHITE);
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
    public void resetSelected(){
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


    @Override
    public void finish() {
        super.finish();
    }

}
