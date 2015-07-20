package com.tmobtech.tmobbeaconproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PageAdapter extends FragmentPagerAdapter {

    Context context;

    public PageAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0: return  new SetBeaconFragment();
            case 1:return  new SetPlaceFragment();
        }
        return  null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}


