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
import com.ibamb.udm.component.login.LoginComponent;
import com.ibamb.udm.module.beans.DeviceSyncMessage;

import java.util.ArrayList;


public class SyncSuccessFragment extends Fragment {

    private static final String SYNC_SUCCESS_DEVICE_INFO = "SYNC_SUCCESS_DEVICE_INFO";
    private static final String SYNC_MENU_ENABLED = "SYNC_MENU_ENABLED";

    private String mSyncSuccessDeviceInfo;
    private boolean isSyncMenuEnabled;

    private ListView vSuccessList;
    private ArrayList<DeviceSyncMessage> successDeviceList;

    public SyncSuccessFragment() {
        // Required empty public constructor
    }

    public static SyncSuccessFragment newInstance(String successDeviceInfo, boolean isSyncEnabled) {
        SyncSuccessFragment fragment = new SyncSuccessFragment();
        Bundle args = new Bundle();
        args.putString(SYNC_SUCCESS_DEVICE_INFO, successDeviceInfo);
        args.putBoolean(SYNC_MENU_ENABLED, isSyncEnabled);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSyncSuccessDeviceInfo = getArguments().getString(SYNC_SUCCESS_DEVICE_INFO);
            isSyncMenuEnabled = getArguments().getBoolean(SYNC_MENU_ENABLED);
            if (mSyncSuccessDeviceInfo != null) {
                int index = 1;
                String[] deviceArray = mSyncSuccessDeviceInfo.split("@");
                successDeviceList = new ArrayList<>();
                for (String deviceInfo : deviceArray) {
                    String[] device = deviceInfo.split("#");
                    if (device.length > 3) {
                        DeviceSyncMessage deviceSyncMessage = new DeviceSyncMessage(index++, device[1], device[2]);
                        deviceSyncMessage.setResultInfo( device[3]);
                        successDeviceList.add(deviceSyncMessage);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_success, container, false);
        vSuccessList = view.findViewById(R.id.sync_success_list);
        vSuccessList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        if (!successDeviceList.isEmpty()) {
            ListAdapter adapter = new SyncReportListAdapter(R.layout.sync_item_device_layout, getLayoutInflater(), successDeviceList);
            vSuccessList.setAdapter(adapter);
            view.findViewById(R.id.line_container).setVisibility(View.VISIBLE);
            /**
             * 添加点击事件，点击后登录设备。
             */
            vSuccessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView macView = view.findViewById(R.id.device_mac);
                    //绑定登录设备事件。
                    String mac = macView.getText().toString();
                    String ip = ((TextView) view.findViewById(R.id.device_ip)).getText().toString();
                    LoginComponent loginComponent = new LoginComponent(getActivity(), mac, ip);
                    loginComponent.setToProfile(true);
                    loginComponent.setSyncMenuEnabled(isSyncMenuEnabled);
                    loginComponent.login();
                }
            });
        }
        return view;
    }

}
