package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtecks.school.singleTopicPOJO.Data;
import com.mrtecks.school.singleTopicPOJO.singleTopicBean;
import com.mrtecks.school.topicsPOJO.topicBean;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class mcqTopic extends Fragment {

    MyView question;

    Button submit;
    ProgressBar progress;

    String qid;

    YouTubePlayerView player;


    MyView text1 , text2 , text3 , text4;
    CheckBox check1 , check2 , check3 , check4;

    String pos = "";


    CustomViewPager pager;

    int position;

    boolean last;

    public void setData(CustomViewPager pager , String qid , int position , boolean last)
    {
        this.pager = pager;
        this.qid = qid;
        this.position = position;
        this.last = last;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mcq_layout , container , false);


        progress = view.findViewById(R.id.progressBar6);
        question = view.findViewById(R.id.textView6);

        submit = view.findViewById(R.id.button3);
        player = view.findViewById(R.id.youTubePlayerView);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);

        check1 = view.findViewById(R.id.opt1);
        check2 = view.findViewById(R.id.opt2);
        check3 = view.findViewById(R.id.opt3);
        check4 = view.findViewById(R.id.opt4);


        getLifecycle().addObserver(player);


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
                        Call<topicBean> call = cr.submitMCQ(SharePreferenceUtils.getInstance().getString("user_id") , qid , pos);

                        call.enqueue(new Callback<topicBean>() {
                            @Override
                            public void onResponse(Call<topicBean> call, Response<topicBean> response) {

                                pager.setCurrentItem(position + 1);

                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);

                                loadData();

                            }

                            @Override
                            public void onFailure(Call<topicBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }
                    else
                    {

                    }





                }
                else
                {
                    Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
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

        loadData();

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

        Call<singleTopicBean> call = cr.getTopicById(qid , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<singleTopicBean>() {
            @Override
            public void onResponse(Call<singleTopicBean> call, Response<singleTopicBean> response) {

                if (response.body().getStatus().equals("1"))
                {

                    final Data item = response.body().getData();


                    question.setConfig(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text1.setConfig(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text2.setConfig(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");
                    text3.setConfig(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");

                    text4.setConfig(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");


                    text1.setText(item.getOption1());
                    text2.setText(item.getOption2());
                    text3.setText(item.getOption3());
                    text4.setText(item.getOption4());
                    question.setText(item.getQuestion());

                    if (item.getVideo().length() > 0)
                    {
                        player.setVisibility(View.VISIBLE);

                        player.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(YouTubePlayer youTubePlayer) {
                                String videoId = getYouTubeId(item.getVideo());
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });

                    }
                    else
                    {
                        player.setVisibility(View.GONE);
                    }



                    if (item.getStatus().equals("1"))
                    {
                        submit.setVisibility(View.GONE);
                        if (item.getYanswer().equals("1"))
                        {
                            check1.setSelected(true);
                        }
                        else if (item.getYanswer().equals("2"))
                        {
                            check2.setSelected(true);
                        }
                        else if (item.getYanswer().equals("3"))
                        {
                            check3.setSelected(true);
                        }
                        else if (item.getYanswer().equals("4"))
                        {
                            check4.setSelected(true);
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

}
