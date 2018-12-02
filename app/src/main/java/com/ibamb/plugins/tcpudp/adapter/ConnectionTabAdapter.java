package com.ibamb.plugins.tcpudp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ConnectionTabAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<Fragment> fragments;

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }


    public ConnectionTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
