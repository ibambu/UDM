package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.SearchDeviceListPagerAdapter;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.beans.Device;
import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.search.DeviceSearch;

import java.util.ArrayList;
import java.util.List;


public class DeviceSearchAsyncTask extends AsyncTask<String, String, ArrayList<DeviceModel>> {

    private Activity activity;
    private FragmentManager supportFragmentManager;
    private ArrayList<DeviceModel> deviceList;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    /**
     * 后台搜索设备（工作线程执行）
     *
     * @param strings
     * @return
     */
    @Override
    protected ArrayList<DeviceModel> doInBackground(String... strings) {
        if (deviceList != null) {
            deviceList.clear();
        }

        String keyword = strings != null && strings.length > 0 ? strings[0] : null;
        publishProgress(UdmConstant.INFO_SEARCH_PROGRESS);
        deviceList = DeviceSearch.searchDevice(keyword);
        if (deviceList == null) {
            int tryMaxCount = 3;
            for (int i = tryMaxCount; i > 0; i--) {
                deviceList = DeviceSearch.searchDevice(keyword);
                if (deviceList != null && !deviceList.isEmpty()) {
                    break;
                }
            }
            if(deviceList== null ){
                deviceList = new ArrayList<>();
            }
        }

        return deviceList;
    }

    /**
     * 将搜索到的设备更新界面列表（主线程执行）
     *
     * @param dataList
     */
    @Override
    protected void onPostExecute(ArrayList<DeviceModel> dataList) {

        super.onPostExecute(dataList);

        List<String> titles = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        View mainView = activity.getWindow().getDecorView();
        TabLayout tabLayout = mainView.findViewById(R.id.device_list_tab);
        ViewPager viewPager = mainView.findViewById(R.id.device_list_pager);
        LinearLayout tabLineLayout = mainView.findViewById(R.id.tab_line_layout);

        List<Device> allDeviceList = new ArrayList<>();
        for(DeviceModel deviceInfo :dataList){
            Device device = Device.toDevice(deviceInfo);
            allDeviceList.add(device);
        }
        /**
         * 分页展现搜索到的设备
         */
        int fromIndex =0;
        int endIndex = 0;
        int pageCount = 0;
        int maxRows = 25;
        while(fromIndex < allDeviceList.size()) {
            if(endIndex+maxRows>dataList.size()){
                endIndex = dataList.size();
            }else{
                endIndex +=maxRows;
            }
            pageCount++;
            List<Device> onePageData = allDeviceList.subList(fromIndex,endIndex);
            String page = String.valueOf(pageCount);
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(page);
            tabLayout.addTab(tab);
            String start = String.format("%03d",onePageData.get(0).getIndex());
            String end = String.format("%03d",onePageData.get(onePageData.size()-1).getIndex());
            titles.add(page+"("+start+"~"+end+")");
            fromIndex = endIndex;
            StringBuilder deviceInfoBuffer = new StringBuilder();
            for(Device device:onePageData){
                deviceInfoBuffer.append(device.toString()).append("@");
            }
            if(deviceInfoBuffer.length()>0){
                deviceInfoBuffer.deleteCharAt(deviceInfoBuffer.length()-1);
            }
            fragmentList.add(DeviceSearchListFragment.newInstance(deviceInfoBuffer.toString()));
        }
        if(pageCount<2){
            tabLayout.setVisibility(View.GONE);
        }else{
            tabLayout.setVisibility(View.VISIBLE);
        }
        if(pageCount<6){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else{
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        tabLayout.setupWithViewPager(viewPager);
        SearchDeviceListPagerAdapter adapter = new SearchDeviceListPagerAdapter(supportFragmentManager,titles,fragmentList);
        viewPager.setAdapter(adapter);

        /**
         * 搜索结果提示信息
         */
        String notice = "";
        if (dataList.size() == 0) {
            notice = UdmConstant.INFO_SEARCH_FAIL;
        } else {
            notice = "Found Device:" + String.valueOf(dataList.size());
        }
        Toast.makeText(activity, notice, Toast.LENGTH_SHORT).show();
        TextView vDeviceList = mainView.findViewById(R.id.tab_device_list);
        if(dataList.size()>0){
            vDeviceList.setText("Device List("+dataList.size()+")");
        }
        ProgressBar progressbar = mainView.findViewById(R.id.search_progress_bar);
        progressbar.setVisibility(View.GONE);
        tabLineLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 搜索进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        View mainView = activity.getWindow().getDecorView();
        ProgressBar progressbar = mainView.findViewById(R.id.search_progress_bar);
        progressbar.setVisibility(View.VISIBLE);
    }

}
