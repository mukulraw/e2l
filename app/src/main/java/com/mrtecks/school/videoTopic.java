package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class videoTopic extends Fragment {


    String qid , q;
    TextView question;
    ProgressBar progress;
    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_layout , container , false);

        qid = getArguments().getString("qid");
        q = getArguments().getString("q");


        question = view.findViewById(R.id.textView6);
        progress = view.findViewById(R.id.progressBar6);
        submit = view.findViewById(R.id.button3);

        question.setText(q);

        return view;
    }

}
