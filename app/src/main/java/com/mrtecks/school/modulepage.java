package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class modulepage extends Fragment {

    CustomViewPager pager;
    SmartTabLayout tabs;
    int position;

    public void setData(CustomViewPager pager, int position) {
        this.pager = pager;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_page_layout, container, false);

        tabs = view.findViewById(R.id.tabLayout2);
        pager = view.findViewById(R.id.pager);


        pager.setPagingEnabled(true);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);


        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        String[] name = {
                "v",
                "v",
                "f",
                "f",
                "m",
                "m",
                "v",
                "v",
                "f",
                "f",
                "m",
                "m",
                "v",
                "v",
                "f",
                "f",
                "m",
                "m",
                "v",
                "f"
        };

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Topic " + String.valueOf(position + 1);
        }

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (name[position].equals("v")) {
                return new videoTopic();
            } else if (name[position].equals("f")) {
                return new fthTopic();
            } else {
                return new mcqTopic();
            }

        }

        @Override
        public int getCount() {
            return 20;
        }
    }

}
