package com.ibamb.udm.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.SyncReportListAdapter;
import com.ibamb.udm.module.beans.DeviceSyncMessage;

import java.util.ArrayList;


public class SyncSuccessFragment extends Fragment {

    private static final String SYNC_SUCCESS_DEVICE_INFO = "SYNC_SUCCESS_DEVICE_INFO";


    private String mSyncSuccessDeviceInfo;

    private ListView vSuccessList;
    private ArrayList<DeviceSyncMessage> successDeviceList;

    public SyncSuccessFragment() {
        // Required empty public constructor
    }

    public static SyncSuccessFragment newInstance(String successDeviceInfo) {
        SyncSuccessFragment fragment = new SyncSuccessFragment();
        Bundle args = new Bundle();
        args.putString(SYNC_SUCCESS_DEVICE_INFO, successDeviceInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSyncSuccessDeviceInfo = getArguments().getString(SYNC_SUCCESS_DEVICE_INFO);
            if (mSyncSuccessDeviceInfo != null) {
                int index = 1;
                String[] deviceArray = mSyncSuccessDeviceInfo.split("@");
                successDeviceList = new ArrayList<>();
                for (String deviceInfo : deviceArray) {
                    String[] device = deviceInfo.split("#");
                    if (device.length > 2) {
                        DeviceSyncMessage deviceSyncMessage = new DeviceSyncMessage(index++,device[1],device[2]);
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
        if(!successDeviceList.isEmpty()){
            ListAdapter adapter = new SyncReportListAdapter(R.layout.sync_item_device_layout, getLayoutInflater(), successDeviceList);
            vSuccessList.setAdapter(adapter);
            view.findViewById(R.id.line_container).setVisibility(View.VISIBLE);
        }
        return view;
    }

}
