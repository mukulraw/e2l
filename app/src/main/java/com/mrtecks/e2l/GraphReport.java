package com.mrtecks.e2l;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

public class GraphReport extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    String mid , name;
    Button getReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);

        mid = getIntent().getStringExtra("mid");
        name = getIntent().getStringExtra("name");

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.web);
        getReport = findViewById(R.id.button5);


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

        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://mylearningplan.in/report2.php?uid=" + SharePreferenceUtils.getInstance().getString("user_id") + "&mid=" + mid);

        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GraphReport.this , ProgressReport.class);
                intent.putExtra("mid" , mid);
                intent.putExtra("name" , name);
                startActivity(intent);

            }
        });

    }
}
