package com.mrtecks.school;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowId;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.mrtecks.school.surveyPOJO.Datum;
import com.mrtecks.school.surveyPOJO.surveyBean;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Survey extends AppCompatActivity {

    SmartTabLayout tabs;
    CustomViewPager pager;
    ProgressBar progress;

    List<String> qs;
    List<String> as;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        qs = new ArrayList<>();
        as = new ArrayList<>();

        tabs = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progressBar2);

        pager.setPagingEnabled(false);




        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<surveyBean> call = cr.getSurvey();

        call.enqueue(new Callback<surveyBean>() {
            @Override
            public void onResponse(Call<surveyBean> call, Response<surveyBean> response) {

                PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager() , response.body().getData());
                pager.setAdapter(adapter);

                tabs.setViewPager(pager);

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<surveyBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class PagerAdapter extends FragmentStatePagerAdapter
    {

        List<Datum> list;

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }

        PagerAdapter(FragmentManager fm , List<Datum> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {

            Datum item = list.get(position);

            page frag = new page();
            if (position == list.size() - 1)
            {
                frag.setData(pager , true , position , qs , as , item.getId() , item.getQuestion() , item.getOption1() , item.getOption2() , item.getOption3() , item.getOption4() , item.getImage());
            }
            else
            {
                frag.setData(pager , false , position , qs , as , item.getId() , item.getQuestion() , item.getOption1() , item.getOption2() , item.getOption3() , item.getOption4() , item.getImage());
            }

            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
