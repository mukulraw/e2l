package com.mrtecks.school;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowId;

import com.google.android.material.tabs.TabLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class Survey extends AppCompatActivity {

    SmartTabLayout tabs;
    CustomViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        tabs = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.pager);

        pager.setPagingEnabled(false);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

    }

    class PagerAdapter extends FragmentStatePagerAdapter
    {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            if (position == 9)
            {
                frag.setData(pager , true , position);
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
