package com.ibamb.udm.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.LoginComponet;

public class DeviceSearchListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private ListView mListView;


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
            LoginComponet loginComponet = new LoginComponet(getActivity(),mac,ip);
            loginComponet.setToProfile(true);
            loginComponet.setSyncMenuEnabled(true);
            loginComponet.setDeviceName("");
            loginComponet.login();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public DeviceSearchListFragment() {

    }

    public static DeviceSearchListFragment newInstance(String param1, String param2) {
        DeviceSearchListFragment fragment = new DeviceSearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_search_list, container, false);
        //取得界面浮动搜索按钮和列表控件
//        searchButton = (FloatingActionButton) view.findViewById(R.id.udm_search_button);
        mListView = (ListView) view.findViewById(R.id.search_device_list);

        //浮动按钮添加搜索事件，通过搜索事件触发搜索设备，并异步更新列表控件。
//        UdmSearchButtonClickListener searchClickListener = new UdmSearchButtonClickListener(mListView, vSearchNotice,inflater);
//        searchButton.setOnClickListener(searchClickListener);
        //给列表项添加点击事件，触发登录设备。
        mListView.setOnItemClickListener(itemOnclickListener);
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


}
