package com.ibamb.udm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class BottomNavigationMenuPagerAdapter extends FragmentPagerAdapter{
    private List<String> menuTitles;
    private List<Fragment> fragmentList;

    public BottomNavigationMenuPagerAdapter(FragmentManager fm,
                                            List<String> menuTitles,
                                            List<Fragment> fragmentList) {
        super(fm);
        this.menuTitles = menuTitles;
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return menuTitles.size();
    }

}
