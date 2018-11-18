package com.ibamb.udm.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.ibamb.udm.activity.MainActivity;
import com.ibamb.udm.adapter.InetAddressListAdapter;
import com.ibamb.udm.component.guide.MainActivityGuide;
import com.ibamb.udm.component.login.LoginComponent;
import com.ibamb.udm.beans.Device;
import com.ibamb.udm.guide.guideview.Guide;
import com.ibamb.udm.guide.guideview.GuideBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeviceSearchListFragment extends Fragment {
    private static final String ARG_DEVICE_LIST = "DEVICE_LIST";

    private ListView mListView;

    private List<Device> dataList;

    /**
     * 设备列表中点击事件，触发登录远程设备。
     */
    private AdapterView.OnItemClickListener itemOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView macView =  view.findViewById(R.id.device_mac);
            //绑定登录设备事件。
            String mac = macView.getText().toString();
            String ip= ((TextView) view.findViewById(R.id.device_ip)).getText().toString();
            LoginComponent loginComponent = new LoginComponent(getActivity(),mac,ip);
            loginComponent.setToProfile(true);
            loginComponent.setSyncMenuEnabled(true);
            loginComponent.setDeviceName("");
            loginComponent.login();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public DeviceSearchListFragment() {

    }

    public static DeviceSearchListFragment newInstance(String deviceInfo) {
        DeviceSearchListFragment fragment = new DeviceSearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_LIST,deviceInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            dataList = new ArrayList<>();
            String allDeviceInfo = getArguments().getString(ARG_DEVICE_LIST);
            if(allDeviceInfo!=null){
                String[] deviceArray = allDeviceInfo.split("@");
                for(String deviceStr:deviceArray){
                    String[] dArray = deviceStr.split("#");
                    if(dArray.length>5){
                        Device device = new Device();
                        device.setIndex(Integer.parseInt(dArray[0]));
                        device.setDeviceName(dArray[1]);
                        device.setIp(dArray[2]);
                        device.setMac(dArray[3]);
                        device.setFirmwareVersion(dArray[4]);
                        device.setProductName(dArray[5]);
                        dataList.add(device);
                    }
                }
            }
        }
        View view = inflater.inflate(R.layout.fragment_device_search_list, container, false);
        mListView = view.findViewById(R.id.search_device_list);
        ListAdapter adapterData =   new InetAddressListAdapter(R.layout.item_device_layout, inflater, dataList);

        mListView.setAdapter(adapterData);
        //给列表项添加点击事件，触发登录设备。
        mListView.setOnItemClickListener(itemOnclickListener);
        mListView.setVisibility(View.VISIBLE);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showGuideView() {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView( mListView.getChildAt(0))
                .setFullingViewId( mListView.getChildAt(0).getId())
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override public void onShown() {
            }

            @Override public void onDismiss() {
            }
        });

        builder.addComponent(new MainActivityGuide("Click to login"));
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
