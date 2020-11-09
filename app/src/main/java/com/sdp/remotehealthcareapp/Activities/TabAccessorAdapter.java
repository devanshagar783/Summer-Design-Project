package com.sdp.remotehealthcareapp.Activities;

import com.sdp.remotehealthcareapp.Fragments.Tab1;
import com.sdp.remotehealthcareapp.Fragments.Tab2;
import com.sdp.remotehealthcareapp.Fragments.Tab3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;

            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;

            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Tab1";

            case 1:
                return "Tab2";

            case 2:
                return "Tab3";

            default:
                return null;
        }
    }
}
