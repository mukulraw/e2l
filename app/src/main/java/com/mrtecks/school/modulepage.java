package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mrtecks.school.modulePOJO.moduleBean;
import com.mrtecks.school.topicsPOJO.Datum;
import com.mrtecks.school.topicsPOJO.topicBean;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class modulepage extends Fragment {

    CustomViewPager pager;
    SmartTabLayout tabs;
    int position;
    String mid;
    ProgressBar progress;
    String status;

    public void setData(CustomViewPager pager, int position , String mid , String status) {
        this.pager = pager;
        this.position = position;
        this.mid = mid;
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_page_layout, container, false);

        tabs = view.findViewById(R.id.tabLayout2);
        pager = view.findViewById(R.id.pager);
        progress = view.findViewById(R.id.progressBar5);


        pager.setPagingEnabled(true);







        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<topicBean> call = cr.getTopics(mid , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<topicBean>() {
            @Override
            public void onResponse(Call<topicBean> call, Response<topicBean> response) {

                try {

                    PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), response.body().getData());
                    pager.setAdapter(adapter);



                    tabs.setViewPager(pager);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<topicBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });



        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> list = new ArrayList<>();

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTopicName();
        }

        PagerAdapter(FragmentManager fm , List<Datum> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {

            if (list.get(position).getAtype().equals("FILE")) {
                videoTopic frag = new videoTopic();
                if (position == list.size() - 1)
                {
                    frag.setData(pager , list.get(position).getId() , position , true);
                }
                else
                {
                    frag.setData(pager , list.get(position).getId() , position , false);
                }

                return frag;
            }else {
                mcqTopic frag = new mcqTopic();
                if (position == list.size() - 1)
                {
                    frag.setData(pager , list.get(position).getId() , position , true);
                }
                else
                {
                    frag.setData(pager , list.get(position).getId() , position , false);
                }
                return frag;
            }

        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
