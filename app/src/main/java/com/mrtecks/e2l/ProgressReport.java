package com.mrtecks.e2l;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProgressReport extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progress;
    String mid , name;

    TextView average , max , total , module;

    PieChart chart;

    TabLayout tabs;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report);

        mid = getIntent().getStringExtra("mid");
        name = getIntent().getStringExtra("name");

        toolbar = findViewById(R.id.toolbar2);
        progress = findViewById(R.id.progressBar8);
        average = findViewById(R.id.textView26);
        max = findViewById(R.id.textView27);
        total = findViewById(R.id.textView23);
        module = findViewById(R.id.textView19);
        chart = findViewById(R.id.view3);
        tabs = findViewById(R.id.tabLayout3);
        pager = findViewById(R.id.pager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Progress Report");

        tabs.addTab(tabs.newTab().setText("STRONG TOPICS"));
        tabs.addTab(tabs.newTab().setText("WEAK TOPICS"));
        tabs.addTab(tabs.newTab().setText("ALL MARKS"));

        module.setText("Module: " + name);

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<progressBean> call = cr.getReport(
                mid,
                SharePreferenceUtils.getInstance().getString("user_id")
        );

        call.enqueue(new Callback<progressBean>() {
            @Override
            public void onResponse(Call<progressBean> call, Response<progressBean> response) {

                progressBean item = response.body();

                average.setText(item.getAverage());
                max.setText(item.getMax());
                total.setText(item.getAttempted());


                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(Float.parseFloat(item.getCorrect()) , "Correct"));
                entries.add(new PieEntry(Float.parseFloat(item.getIncorrect()) , "Incorrect"));

                PieDataSet dataSet = new PieDataSet(entries, "");

                ArrayList<Integer> colors = new ArrayList<>();

                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);
                chart.setData(data);


                // undo all highlights
                chart.highlightValues(null);

                chart.invalidate();

                PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager() , response.body().getStrong() , response.body().getWeak() , response.body().getMarks());
                pager.setAdapter(adapter);

                tabs.setupWithViewPager(pager);

                tabs.getTabAt(0).setText("STRONG TOPICS");
                tabs.getTabAt(1).setText("WEAK TOPICS");
                tabs.getTabAt(2).setText("ALL MARKS");


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<progressBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });



    }


    class PagerAdapter extends FragmentStatePagerAdapter
    {

        List<Strong> slist = new ArrayList<>();
        List<Strong> wlist = new ArrayList<>();
        List<Mark> mlist = new ArrayList<>();

        PagerAdapter(FragmentManager fm, List<Strong> slist, List<Strong> wlist , List<Mark> mlist) {
            super(fm);
            this.slist = slist;
            this.wlist = wlist;
            this.mlist = mlist;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                str frag = new str();
                frag.setData(slist);
                return frag;
            }
            else if (position == 1)
            {
                str frag = new str();
                frag.setData(wlist);
                return frag;
            }
            else
            {
                maa frag = new maa();
                frag.setData(mlist);
                return frag;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class str extends Fragment
    {

        RecyclerView grid;
        ProgressBar progress;
        GridLayoutManager manager;
        ProgressAdapter adapter;
        List<Strong> slist = new ArrayList<>();
        TextView ttt;

        void setData(List<Strong> slist)
        {
            this.slist = slist;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.progress_layout , container , false);

            grid = view.findViewById(R.id.grid);
            progress = view.findViewById(R.id.progressBar7);
            ttt = view.findViewById(R.id.textView14);
            manager = new GridLayoutManager(getContext() , 1);
            adapter = new ProgressAdapter(getContext() , slist);

            ttt.setVisibility(View.GONE);

            grid.setAdapter(adapter);
            grid.setLayoutManager(manager);

            return view;
        }

        class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>
        {
            List<Strong> list = new ArrayList<>();
            Context context;
            boolean isstart = false;

            public ProgressAdapter(Context context , List<Strong> list)
            {
                this.context = context;
                this.list = list;
            }

            public void setData(List<Strong> list , boolean isstart)
            {
                this.list = list;
                this.isstart = isstart;
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.progress_module_list2 , parent , false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

                final Strong item = list.get(position);

                holder.status.setText("Marks - " + item.getMarks());

                holder.module.setText(item.getTopic());



            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder
            {

                TextView module , status;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    module = itemView.findViewById(R.id.textView16);
                    status = itemView.findViewById(R.id.textView20);

                }
            }
        }

    }

    public static class maa extends Fragment
    {

        RecyclerView grid;
        ProgressBar progress;
        GridLayoutManager manager;
        ProgressAdapter adapter;
        List<Mark> slist = new ArrayList<>();
        TextView ttt;

        void setData(List<Mark> slist)
        {
            this.slist = slist;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.progress_layout , container , false);

            grid = view.findViewById(R.id.grid);
            progress = view.findViewById(R.id.progressBar7);
            ttt = view.findViewById(R.id.textView14);
            manager = new GridLayoutManager(getContext() , 1);
            adapter = new ProgressAdapter(getContext() , slist);

            ttt.setVisibility(View.GONE);

            grid.setAdapter(adapter);
            grid.setLayoutManager(manager);

            return view;
        }

        class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>
        {
            List<Mark> list = new ArrayList<>();
            Context context;
            boolean isstart = false;

            public ProgressAdapter(Context context , List<Mark> list)
            {
                this.context = context;
                this.list = list;
            }

            public void setData(List<Mark> list , boolean isstart)
            {
                this.list = list;
                this.isstart = isstart;
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.progress_module_list3 , parent , false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

                final Mark item = list.get(position);

                holder.status.setText(Html.fromHtml("<font color=\"black\">Marks - </font>" + item.getMarks()));

                holder.module.setText(item.getTopicName());

                holder.question.setText(Html.fromHtml("<font color=\"black\">Q - </font>" + item.getQuestion()));


                if (item.getAtype().equals("TEXT"))
                {
                    holder.yans.setText(Html.fromHtml("<font color=\"black\">Your Ans. - </font>" + item.getYans()));
                    holder.rans.setText(Html.fromHtml("<font color=\"black\">Correct Ans. - </font>" + item.getRans()));

                    holder.yans.setVisibility(View.VISIBLE);
                    holder.rans.setVisibility(View.VISIBLE);

                }
                else
                {
                    holder.yans.setVisibility(View.GONE);
                    holder.rans.setVisibility(View.GONE);
                }

            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder
            {

                TextView module , status , question , yans , rans;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    module = itemView.findViewById(R.id.textView16);
                    status = itemView.findViewById(R.id.textView20);
                    question = itemView.findViewById(R.id.textView30);
                    yans = itemView.findViewById(R.id.textView28);
                    rans = itemView.findViewById(R.id.textView29);

                }
            }
        }

    }

}
