package com.sdp.remotehealthcareapp.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sdp.remotehealthcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

    View v;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public Dashboard() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.main_tabs_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.main_tab);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Tab1(), "Tab 1");
        adapter.addFragment(new Tab2(), "Tab 2");
        adapter.addFragment(new Tab3(), "Tab 3");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Consult";
                case 1:
                    return "Home";
                case 2:
                    return "Profile";
                default:
                    return null;
                // return null to display only the icon
            }
        }
    }
}


