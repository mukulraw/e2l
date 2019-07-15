package com.mrtecks.school;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtecks.school.regiterPOJO.Data;
import com.mrtecks.school.regiterPOJO.registerBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class page extends Fragment {

    CustomViewPager pager;
    boolean last;
    int position;
    Button next;
    List<String> qs;
    List<String> as;

    String qid , qtext , op1 , op2 , op3 , op4 , image;

    TextView question;
    RadioGroup group;
    ProgressBar progress;

    RadioButton option1 , option2 , option3 , option4;

    public void setData(CustomViewPager pager , boolean last , int position , List<String> qs , List<String> as , String qid , String qtext , String op1 , String op2 , String op3 , String op4 , String image)
    {
        this.pager = pager;
        this.last = last;
        this.position = position;
        this.qs = qs;
        this.as = as;
        this.qid = qid;
        this.qtext = qtext;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.image = image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_layout , container , false);

        question = view.findViewById(R.id.textView6);
        group = view.findViewById(R.id.group);
        progress = view.findViewById(R.id.progressBar3);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        next = view.findViewById(R.id.button3);

        if (last)
        {
            next.setText("CONTINUE");
        }
        else
        {
            next.setText("NEXT");
        }




        question.setText("Q.  " + qtext);
        option1.setText(op1);
        option2.setText(op2);
        option3.setText(op3);
        option4.setText(op4);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int iidd = group.getCheckedRadioButtonId();


                if (iidd > -1)
                {

                    RadioButton btn = group.findViewById(iidd);

                    if (last)
                    {

                        qs.add(position , qid);
                        as.add(position , btn.getText().toString());

                        String qqss = TextUtils.join(",", qs);
                        String aass = TextUtils.join(",", as);

                        Log.d("qs" , qqss);
                        Log.d("as" , aass);



                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getActivity().getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Call<registerBean> call = cr.submitSurvey(
                                SharePreferenceUtils.getInstance().getString("user_id"),
                                qqss,
                                aass
                        );

                        call.enqueue(new Callback<registerBean>() {
                            @Override
                            public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                                if (response.body().getStatus().equals("1"))
                                {

                                    Toast.makeText(getContext() , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                    Data item = response.body().getData();

                                    SharePreferenceUtils.getInstance().saveString("user_id" , item.getUserId());
                                    SharePreferenceUtils.getInstance().saveString("name" , item.getName());
                                    SharePreferenceUtils.getInstance().saveString("photo" , item.getPhoto());
                                    SharePreferenceUtils.getInstance().saveString("contact" , item.getContact());
                                    SharePreferenceUtils.getInstance().saveString("school_id" , item.getSchoolId());
                                    SharePreferenceUtils.getInstance().saveString("class" , item.getClass_());
                                    SharePreferenceUtils.getInstance().saveString("rollno" , item.getRollno());
                                    SharePreferenceUtils.getInstance().saveString("password" , item.getPassword());
                                    SharePreferenceUtils.getInstance().saveString("status" , item.getStatus());
                                    SharePreferenceUtils.getInstance().saveString("created" , item.getCreated());



                                    if (item.getStatus().equals("Active"))
                                    {

                                        Intent intent = new Intent(getContext() , MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                    else
                                    {
                                        Intent intent = new Intent(getContext() , Survey.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getContext(), response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<registerBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


/*
                        Intent intent = new Intent(getContext() , MainActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
*/

                    }
                    else
                    {
                        qs.add(position , qid);
                        as.add(position , btn.getText().toString());
                        pager.setCurrentItem(position + 1);
                    }


                }
                else
                {
                    Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                }




            }
        });

        return view;
    }
}
