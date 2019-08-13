package com.mrtecks.e2l;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

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

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<progressBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });



    }
}
