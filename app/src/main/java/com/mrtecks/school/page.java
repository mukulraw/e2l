package com.mrtecks.school;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class page extends Fragment {

    CustomViewPager pager;
    boolean last;
    int position;
    Button next;

    public void setData(CustomViewPager pager , boolean last , int position)
    {
        this.pager = pager;
        this.last = last;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_layout , container , false);

        next = view.findViewById(R.id.button3);

        if (last)
        {
            next.setText("CONTINUE");
        }
        else
        {
            next.setText("NEXT");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (last)
                {
                    Intent intent = new Intent(getContext() , MainActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
                else
                {
                    pager.setCurrentItem(position + 1);
                }

            }
        });

        return view;
    }
}
