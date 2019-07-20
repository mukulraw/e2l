package com.mrtecks.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class mcqTopic extends Fragment {

    TextView question;
    RadioGroup group;
    RadioButton option1 , option2 , option3 , option4;
    Button submit;
    ProgressBar progress;

    String qid , q , op1 , op2 , op3 , op4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mcq_layout , container , false);

        qid = getArguments().getString("qid");
        q = getArguments().getString("q");
        op1 = getArguments().getString("op1");
        op2 = getArguments().getString("op2");
        op3 = getArguments().getString("op3");
        op4 = getArguments().getString("op4");

        progress = view.findViewById(R.id.progressBar6);
        question = view.findViewById(R.id.textView6);
        group = view.findViewById(R.id.group);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        submit = view.findViewById(R.id.button3);

        option1.setText(op1);
        option2.setText(op2);
        option3.setText(op3);
        option4.setText(op4);
        question.setText(q);




        return view;
    }
}
