package com.ibamb.plugins.tcpudp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ibamb.plugins.tcpudp.adapter.ConnectionTabAdapter;
import com.ibamb.plugins.tcpudp.context.Constant;
import com.ibamb.plugins.tcpudp.fragment.TCPUDPWorkSpaceFragment;
import com.ibamb.plugins.tcpudp.fragment.WorkSpaceFragment;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MutilWorkSpaceActivity extends AppCompatActivity {
    private TabLayout connectionTab;
    private ViewPager connectonViewPager;
    private ImageView newConnImg;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles;
    private HashMap<String, Intent> fragmentParam = new HashMap();

    private ConnectionTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutil_work_space);
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

        connectionTab = findViewById(R.id.connection_tab);
        connectonViewPager = findViewById(R.id.connection_tab_content);
        newConnImg = findViewById(R.id.new_connection);
        newConnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MutilWorkSpaceActivity.this, ConnectionActivity.class);
                startActivityForResult(intent, 9001);
            }
        });

        connectionTab.setupWithViewPager(connectonViewPager);
        TCPUDPWorkSpaceFragment fragment = new TCPUDPWorkSpaceFragment();
        fragmentList.add(fragment);

        adapter = new ConnectionTabAdapter(getSupportFragmentManager());
        titles = new ArrayList<>();
        adapter.setTitles(titles);
        adapter.setFragments(fragmentList);

        connectonViewPager.setAdapter(adapter);
        connectonViewPager.setOffscreenPageLimit(fragmentList.size());
        if (fragmentList.size() < 2) {
            connectionTab.setVisibility(View.GONE);
        } else {
            connectionTab.setVisibility(View.VISIBLE);
        }
        if (fragmentList.size() < 6) {
            connectionTab.setTabMode(TabLayout.MODE_FIXED);
            connectionTab.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            connectionTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StringBuilder buffer = new StringBuilder();
        buffer.append(data.getStringExtra("TCP_REMOTE_HOST")).append("&&")
                .append(data.getStringExtra("TCP_REMOTE_PORT")).append("&&")
                .append(data.getStringExtra("TCP_LOCAL_PORT")).append("&&")
                .append(data.getStringExtra("UDP_UNI_TARGET_HOST")).append("&&")
                .append(data.getStringExtra("UDP_UNI_TARGET_PORT")).append("&&")
                .append(data.getStringExtra("UDP_UNI_LOCAL_PORT")).append("&&")
                .append(data.getStringExtra("UDP_MUL_ADDRESS")).append("&&")
                .append(data.getStringExtra("UDP_MUL_PORT")).append("&&")
                .append(data.getStringExtra("UDP_BROADCAST_PORT")).append("&&")
                .append(data.getStringExtra("CONNECTION_TYPE")).append("&&")
                .append(data.getStringExtra("WORK_ROLE"));
        TCPUDPWorkSpaceFragment fragment = TCPUDPWorkSpaceFragment.newInstance(buffer.toString());
//        Bundle bundle = new Bundle();
//        bundle.putString("TCP_REMOTE_HOST", data.getStringExtra("TCP_REMOTE_HOST"));
//        bundle.putString("TCP_REMOTE_PORT", data.getStringExtra("TCP_REMOTE_PORT"));
//        bundle.putString("TCP_LOCAL_PORT", data.getStringExtra("TCP_LOCAL_PORT"));
//
//        bundle.putString("UDP_UNI_TARGET_HOST", data.getStringExtra("UDP_UNI_TARGET_HOST"));
//        bundle.putString("UDP_UNI_TARGET_PORT", data.getStringExtra("UDP_UNI_TARGET_PORT"));
//        bundle.putString("UDP_UNI_LOCAL_PORT", data.getStringExtra("UDP_UNI_LOCAL_PORT"));
//
//        bundle.putString("UDP_MUL_ADDRESS", data.getStringExtra("UDP_MUL_ADDRESS"));
//        bundle.putString("UDP_MUL_PORT", data.getStringExtra("UDP_MUL_PORT"));
//
//        bundle.putString("UDP_BROADCAST_PORT", data.getStringExtra("UDP_BROADCAST_PORT"));
//
//        bundle.putString("CONNECTION_TYPE", data.getStringExtra("CONNECTION_TYPE"));
//
//        bundle.putInt("WORK_ROLE", data.getIntExtra("WORK_ROLE", Constant.CONN_ROLE_CLIENT));
//        fragment.setArguments(bundle);

        int index = titles.size() + 1;
        titles.add(String.valueOf(index));
        fragmentParam.put(String.valueOf(index), data);

        fragmentList.add(fragment);

        if (fragmentList.size() < 2) {
            connectionTab.setVisibility(View.GONE);
        } else {
            connectionTab.setVisibility(View.VISIBLE);
        }
        if (fragmentList.size() < 6) {
            connectionTab.setTabMode(TabLayout.MODE_FIXED);
            connectionTab.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            connectionTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        adapter.notifyDataSetChanged();

    }
}
