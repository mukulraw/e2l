package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class module extends Fragment {

    SmartTabLayout tabs;
    CustomViewPager pager;

    ImageView left, right;

    int pos;
    boolean first, last;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_layout, container, false);

        tabs = view.findViewById(R.id.tabLayout2);
        pager = view.findViewById(R.id.pager);
        left = view.findViewById(R.id.imageButton);
        right = view.findViewById(R.id.imageButton3);


        pager.setPagingEnabled(false);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!first)
                {
                    pager.setCurrentItem(pos - 1);
                }


            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!last)
                {
                    pager.setCurrentItem(pos + 1);
                }


            }
        });

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pos = position;
                if (position == 0) {
                    first = true;
                    //left.setVisibility(View.GONE);
                } else {
                    first = false;
                    //left.setVisibility(View.VISIBLE);
                }

                if (position == tabs.getChildCount() - 1) {
                    last = true;
                    //right.setVisibility(View.GONE);
                } else {
                    last = false;
                    //right.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Module " + String.valueOf(position + 1);
        }

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            modulepage frag = new modulepage();
            if (position == 9) {
                frag.setData(pager, position);
            } else {
                frag.setData(pager, position);
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
