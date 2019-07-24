package com.mrtecks.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrtecks.school.notiPOJO.Datum;
import com.mrtecks.school.notiPOJO.notiBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Notification extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    List<Datum> list;
    GridLayoutManager manager;
    ProgressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        list = new ArrayList<>();

        grid = findViewById(R.id.grid);
        toolbar = findViewById(R.id.toolbar2);
        progress = findViewById(R.id.progressBar8);

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
        toolbar.setTitle("Notifications");


        manager = new GridLayoutManager(this , 1);
        adapter = new ProgressAdapter(this , list);
        progress.setVisibility(View.VISIBLE);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<notiBean> call = cr.getNoti();

        call.enqueue(new Callback<notiBean>() {
            @Override
            public void onResponse(Call<notiBean> call, Response<notiBean> response) {

                adapter.setData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<notiBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>
    {
        List<Datum> list = new ArrayList<>();
        Context context;


        ProgressAdapter(Context context, List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.progress_module_list , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Datum item = list.get(position);

            holder.status.setText(item.getCreated());

            holder.module.setText(item.getText());

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
