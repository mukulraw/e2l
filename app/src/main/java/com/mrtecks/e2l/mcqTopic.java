package com.mrtecks.e2l;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mrtecks.e2l.singleTopicPOJO.Data;
import com.mrtecks.e2l.singleTopicPOJO.singleTopicBean;
import com.mrtecks.e2l.topicsPOJO.topicBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class mcqTopic extends Fragment {

    MathView question;
    ProgressDialog prog2;
    Button submit;
    ProgressBar progress;

    String qid;

    YouTubePlayerView player;

    YouTubePlayer youTubePlayer;


    MathView text1 , text2 , text3 , text4;
    RadioButton check1 , check2 , check3 , check4;

    String pos = "" , mid;


    CustomViewPager pager;

    int position;

    TextView filelabel , videolabel , filename;

    CardView file;

    boolean last;

    module co;

    String furl = "" , fname;

    DownloadZipFileTask downloadZipFileTask;

    ImageView image;

    TextView mark;

    private static final String TAG = "MainActivity";

    public void setData(CustomViewPager pager , String qid , int position , boolean last , String mid , module co)
    {
        this.pager = pager;
        this.qid = qid;
        this.position = position;
        this.last = last;
        this.mid = mid;
        this.co = co;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mcq_layout , container , false);

        prog2 = new ProgressDialog(getContext());
        prog2.setMessage("Downloading File...");
        prog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prog2.setIndeterminate(false);

        progress = view.findViewById(R.id.progressBar6);
        image = view.findViewById(R.id.image);
        question = view.findViewById(R.id.textView6);
        mark = view.findViewById(R.id.mark);

        submit = view.findViewById(R.id.button3);
        player = view.findViewById(R.id.youTubePlayerView);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);

        filelabel = view.findViewById(R.id.textView10);
        videolabel = view.findViewById(R.id.textView12);
        file = view.findViewById(R.id.textView11);
        filename = view.findViewById(R.id.filename);
        check1 = view.findViewById(R.id.opt1);
        check2 = view.findViewById(R.id.opt2);
        check3 = view.findViewById(R.id.opt3);
        check4 = view.findViewById(R.id.opt4);






        getLifecycle().addObserver(player);
        player.enableBackgroundPlayback(false);


        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    pos = "1";
                    setChech();
                }
                else
                {
                    pos = "";
                    setChech();
                }

            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    pos = "2";
                    setChech();
                }
                else
                {
                    pos = "";
                    setChech();
                }

            }
        });

        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    pos = "3";
                    setChech();
                }
                else
                {
                    pos = "";
                    setChech();
                }

            }
        });

        check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    pos = "4";
                    setChech();
                }
                else
                {
                    pos = "";
                    setChech();
                }

            }
        });






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos.length() > 0)
                {


                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getActivity().getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);



                    if (!last)
                    {
                        Call<topicBean> call = cr.submitMCQ(SharePreferenceUtils.getInstance().getString("user_id") , qid , pos , mid);

                        call.enqueue(new Callback<topicBean>() {
                            @Override
                            public void onResponse(Call<topicBean> call, Response<topicBean> response) {

                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);

                                loadData();

                                pager.setCurrentItem(position + 1);

                            }

                            @Override
                            public void onFailure(Call<topicBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }
                    else
                    {

                        Call<topicBean> call = cr.submitMCQmodule(
                                SharePreferenceUtils.getInstance().getString("user_id") ,
                                qid ,
                                pos ,
                                mid,
                                SharePreferenceUtils.getInstance().getString("school_id"),
                                SharePreferenceUtils.getInstance().getString("class")
                        );

                        call.enqueue(new Callback<topicBean>() {
                            @Override
                            public void onResponse(Call<topicBean> call, Response<topicBean> response) {

                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);

                                loadData();

                                co.loadData();
                                //pager.setCurrentItem(position + 1);

                            }

                            @Override
                            public void onFailure(Call<topicBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }





                }
                else
                {
                    Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                }

            }
        });


        loadData();

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<ResponseBody> call = cr.downloadFileByUrl(furl);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        downloadZipFileTask = new DownloadZipFileTask();
                        downloadZipFileTask.execute(response.body());


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            }
        });


        return view;
    }


    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], fname);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].second == 100)
                prog2.dismiss();
            //Toast.makeText(getContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);

                if (currentProgress == 100)
                {
                    prog2.dismiss();
                    Toast.makeText(getContext(), "File downloaded successfully in Downloads", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    prog2.setProgress(currentProgress);
                    prog2.show();
                }



            }

            if (progress[0].first == -1) {
                Toast.makeText(getContext(), "Download failed", Toast.LENGTH_SHORT).show();
                prog2.dismiss();
            }

        }

        void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }


    private String getYouTubeId (String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }


    @Override
    public void onResume() {
        super.onResume();


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

        Log.d("user" , SharePreferenceUtils.getInstance().getString("user_id"));

        Call<singleTopicBean> call = cr.getTopicById(qid , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<singleTopicBean>() {
            @Override
            public void onResponse(Call<singleTopicBean> call, Response<singleTopicBean> response) {

                if (response.body().getStatus().equals("1"))
                {

                    final Data item = response.body().getData();


                    question.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text1.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text2.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");
                    text3.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text4.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getImage() , image , options);


                    text1.setText(item.getOption1());
                    text2.setText(item.getOption2());
                    text3.setText(item.getOption3());
                    text4.setText(item.getOption4());
                    question.setText(item.getQuestion());

                    if (item.getVideo().length() > 0)
                    {
                        videolabel.setVisibility(View.VISIBLE);
                        player.setVisibility(View.VISIBLE);

                        player.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(YouTubePlayer youTubePlayer) {
                                mcqTopic.this.youTubePlayer = youTubePlayer;
                                String videoId = getYouTubeId(item.getVideo());
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });

                    }
                    else
                    {
                        player.setVisibility(View.GONE);
                    }


                    mark.setText(item.getLevel() + " - " + item.getMark() + " Mark");

                    if (item.getFile().length() > 0)
                    {
                        filename.setText(item.getFilename());
                        furl = item.getFile();
                        fname = item.getFilename();
                        file.setVisibility(View.VISIBLE);
                        filelabel.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        file.setVisibility(View.GONE);
                        filelabel.setVisibility(View.GONE);
                    }



                    if (item.getStatus().equals("1"))
                    {
                        submit.setVisibility(View.GONE);
                        videolabel.setVisibility(View.GONE);

                        Log.d("pos" , item.getYanswer());

                        switch (item.getYanswer()) {
                            case "1":
                                check1.setChecked(true);
                                check1.setEnabled(false);
                                check2.setEnabled(false);
                                check3.setEnabled(false);
                                check4.setEnabled(false);
                                break;
                            case "2":
                                check2.setChecked(true);
                                check1.setEnabled(false);
                                check2.setEnabled(false);
                                check3.setEnabled(false);
                                check4.setEnabled(false);
                                break;
                            case "3":

                                check3.setChecked(true);
                                check1.setEnabled(false);
                                check2.setEnabled(false);
                                check3.setEnabled(false);
                                check4.setEnabled(false);
                                break;
                            case "4":
                                check4.setChecked(true);
                                check1.setEnabled(false);
                                check2.setEnabled(false);
                                check3.setEnabled(false);
                                check4.setEnabled(false);
                                break;
                        }
                    }
                    else
                    {
                        submit.setVisibility(View.VISIBLE);
                    }

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<singleTopicBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    private void setChech()
    {



        switch (pos) {
            case "1":
                check1.setChecked(true);
                check2.setChecked(false);
                check3.setChecked(false);
                check4.setChecked(false);
                break;
            case "2":
                check1.setChecked(false);
                check2.setChecked(true);
                check3.setChecked(false);
                check4.setChecked(false);
                break;
            case "3":
                check1.setChecked(false);
                check2.setChecked(false);
                check3.setChecked(true);
                check4.setChecked(false);
                break;
            case "4":
                check1.setChecked(false);
                check2.setChecked(false);
                check3.setChecked(false);
                check4.setChecked(true);
                break;
            default:
                check1.setChecked(false);
                check2.setChecked(false);
                check3.setChecked(false);
                check4.setChecked(false);
                break;
        }

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (!visible && youTubePlayer != null)
            youTubePlayer.pause();
    }

}
