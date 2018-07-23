package com.ibamb.udm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SearchDeviceListPagerAdapter extends FragmentPagerAdapter {

    private List<String> pages;//页码
    private List<Fragment> fragmentList;//页


    public SearchDeviceListPagerAdapter(FragmentManager fm, List<String> pages, List<Fragment> fragmentList) {
        super(fm);
        this.pages = pages;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position);
    }
}
