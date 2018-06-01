package com.ibamb.udm.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.BlankFragment;
import com.ibamb.udm.fragment.DeviceSearchListFragment;


/**
 * 根据需要可扩展底部菜单栏目
 */
public class UdmBottomMenuClickListener implements View.OnClickListener {
    private TextView tabDeviceList;
    private TextView tabAppSetting;

    FragmentManager fragmentManager;

    private Fragment defultFragment;
    private BlankFragment f2, f3, f4;

    public UdmBottomMenuClickListener() {

    }

    public UdmBottomMenuClickListener(FragmentManager fragmentManager, Fragment defaultFragment,
                                      TextView tabDeviceList, TextView tabAppSetting) {
        this.fragmentManager = fragmentManager;
        this.tabDeviceList = tabDeviceList;
        this.tabAppSetting = tabAppSetting;
        this.defultFragment = defaultFragment;

    }

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.tab_device_list:
                resetSelectState();
                tabDeviceList.setSelected(true);
                System.out.println("default id is 1:"+defultFragment.getId());
                if (defultFragment == null) {
                    defultFragment = DeviceSearchListFragment.newInstance("第一个Fragment", null);
                    transaction.add(R.id.fragment_container, defultFragment);
                } else {
                    transaction.show(defultFragment);
                }
                break;

            case R.id.tab_setting:
                System.out.println("default id is 2:"+defultFragment.getId());
                resetSelectState();
                tabAppSetting.setSelected(true);
                if (f4 == null) {
                    f4 = BlankFragment.newInstance("第四个Fragment", null);
                    transaction.add(R.id.fragment_container, f4);
                } else {
                    transaction.show(f4);
                }
                break;
        }
        transaction.commit();
    }



    //重置所有文本的选中状态
    public void resetSelectState() {
        tabDeviceList.setSelected(false);
        tabAppSetting.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (defultFragment != null) {
            transaction.hide(defultFragment);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }

}
