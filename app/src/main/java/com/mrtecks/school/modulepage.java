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
        View view = inflater.inflate(R.layout.module_page_layout , container , false);

        tabs = view.findViewById(R.id.tabLayout2);
        pager = view.findViewById(R.id.pager);


        pager.setPagingEnabled(false);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);


        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter
    {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            if (position == 3)
            {
                frag.setData(pager , true ,  position);
            }
            else
            {
                frag.setData(pager , false , position);
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }

}
