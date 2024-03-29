package com.mrtecks.e2l;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mrtecks.e2l.modulePOJO.Datum;
import com.mrtecks.e2l.modulePOJO.moduleBean;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class module extends Fragment {

    SmartTabLayout tabs;
    CustomViewPager pager;

    ImageView left, right;

    int pos;

    ProgressBar progress;
    boolean first, last;

    FragmentManager fm;

    module co;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_layout, container, false);

        co = this;

        tabs = view.findViewById(R.id.tabLayout2);
        pager = view.findViewById(R.id.pager);
        left = view.findViewById(R.id.imageButton);
        right = view.findViewById(R.id.imageButton3);
        progress = view.findViewById(R.id.progressBar4);


        //pager.setPagingEnabled(false);

        fm = getChildFragmentManager();


        loadData();


        return view;
    }


    void loadData()
    {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<moduleBean> call = cr.getModules(SharePreferenceUtils.getInstance().getString("school_id"), SharePreferenceUtils.getInstance().getString("class") , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<moduleBean>() {
            @Override
            public void onResponse(Call<moduleBean> call, Response<moduleBean> response) {

                try {

                    int flag = -1;

                    for (int i = 0; i < response.body().getData().size(); i++) {

                        if (response.body().getData().get(i).getStatus().equals("ongoing") || response.body().getData().get(i).getStatus().equals("completed"))
                        {
                            flag = i;
                        }

                    }

                    if (flag > -1)
                    {
                        PagerAdapter adapter = new PagerAdapter(fm, response.body().getData() , false);
                        pager.setAdapter(adapter);
                        pager.setOffscreenPageLimit(response.body().getData().size() - 1);
                        tabs.setViewPager(pager);
                    }
                    else
                    {
                        PagerAdapter adapter = new PagerAdapter(fm, response.body().getData() , true);
                        pager.setAdapter(adapter);
                        pager.setOffscreenPageLimit(response.body().getData().size() - 1);
                        tabs.setViewPager(pager);
                    }


                    pager.setCurrentItem(flag);



                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<moduleBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("school" , SharePreferenceUtils.getInstance().getString("school_id"));
        Log.d("class" , SharePreferenceUtils.getInstance().getString("class"));

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> list = new ArrayList<>();
        boolean isstart = false;

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getModuleName();
        }

        PagerAdapter(FragmentManager fm, List<Datum> list , boolean isstart) {
            super(fm);
            this.list = list;
            this.isstart = isstart;
        }

        @Override
        public Fragment getItem(int position) {


            if (isstart)
            {
                if (position == 0)
                {
                    modulepage frag = new modulepage();
                    frag.setData(co, position , list.get(position).getId() , list.get(position).getStatus());
                    return frag;
                }
                else
                {
                    return new lockModule();
                }
            }
            else
            {
                if (list.get(position).getStatus().equals("ongoing") || list.get(position).getStatus().equals("completed")) {
                    modulepage frag = new modulepage();
                    frag.setData(co, position , list.get(position).getId() , list.get(position).getStatus());
                    return frag;
                } else {
                    return new lockModule();
                }
            }


        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
