package com.ibamb.udm.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.SyncReportListAdapter;
import com.ibamb.udm.component.LoginComponet;
import com.ibamb.udm.module.beans.DeviceSyncMessage;

import java.util.ArrayList;


public class SyncFailFragment extends Fragment {

    private static final String SYNC_FAIL_DEVICE_INFO = "SYNC_FAIL_DEVICE_INFO";
    private static final String SYNC_MENU_ENABLED = "SYNC_MENU_ENABLED";

    private String mSyncFailDeviceInfo;//同步失败字符串
    private boolean isSyncMenuEnabled;

    private ListView vFailList;
    private ArrayList<DeviceSyncMessage> failDeviceList;

    public SyncFailFragment() {
        // Required empty public constructor
    }


    public static SyncFailFragment newInstance(String failDeviceInfo, boolean isSyncMenuEnabled) {
        SyncFailFragment fragment = new SyncFailFragment();
        Bundle args = new Bundle();
        args.putString(SYNC_FAIL_DEVICE_INFO, failDeviceInfo);
        args.putBoolean(SYNC_MENU_ENABLED, isSyncMenuEnabled);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSyncFailDeviceInfo = getArguments().getString(SYNC_FAIL_DEVICE_INFO);
            isSyncMenuEnabled = getArguments().getBoolean(SYNC_MENU_ENABLED);
            if (mSyncFailDeviceInfo != null) {
                int index = 1;
                String[] deviceArray = mSyncFailDeviceInfo.split("@");
                failDeviceList = new ArrayList<>();
                for (String deviceInfo : deviceArray) {
                    String[] device = deviceInfo.split("#");
                    if (device.length > 3) {
                        DeviceSyncMessage deviceSyncMessage = new DeviceSyncMessage(index++, device[1], device[2]);
                        deviceSyncMessage.setResultInfo(device[3]);
                        failDeviceList.add(deviceSyncMessage);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_fail, container, false);
        vFailList = view.findViewById(R.id.sync_fail_list);
        vFailList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        if (!failDeviceList.isEmpty()) {
            ListAdapter adapter = new SyncReportListAdapter(R.layout.sync_item_device_layout, getLayoutInflater(), failDeviceList);
            vFailList.setAdapter(adapter);
            view.findViewById(R.id.line_container).setVisibility(View.VISIBLE);
            /**
             * 添加点击事件，点击后登录设备。
             */
            vFailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView macView = view.findViewById(R.id.device_mac);
                    //绑定登录设备事件。
                    String mac = macView.getText().toString();
                    String ip = ((TextView) view.findViewById(R.id.device_ip)).getText().toString();
                    LoginComponet loginComponet = new LoginComponet(getActivity(), mac, ip);
                    loginComponet.setToProfile(true);
                    loginComponet.setSyncMenuEnabled(isSyncMenuEnabled);
                    loginComponet.login();
                }
            });
        }
        return view;
    }

}
