package com.mrtecks.school;

import android.content.Context;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtecks.school.modulePOJO.Datum;
import com.mrtecks.school.modulePOJO.moduleBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class progress extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<Datum> list;
    ProgressAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_layout , container , false);

        list = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar7);
        manager = new GridLayoutManager(getContext() , 1);
adapter = new ProgressAdapter(getContext() , list);
        progress.setVisibility(View.VISIBLE);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<moduleBean> call = cr.getModules(SharePreferenceUtils.getInstance().getString("school_id"), SharePreferenceUtils.getInstance().getString("class") , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<moduleBean>() {
            @Override
            public void onResponse(Call<moduleBean> call, Response<moduleBean> response) {

                try {

                    int flag = 0;

                    for (int i = 0; i < response.body().getData().size(); i++) {

                        if (response.body().getData().get(i).getStatus().equals("ongoing") || response.body().getData().get(i).getStatus().equals("completed"))
                        {
                            flag++;
                        }

                    }

                    if (flag > 0)
                    {

                        adapter.setData(response.body().getData() , false);

                    }
                    else
                    {

                        adapter.setData(response.body().getData() , true);

                    }




                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<moduleBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        return view;
    }


    class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>
    {
        List<Datum> list = new ArrayList<>();
        Context context;
        boolean isstart = false;

        public ProgressAdapter(Context context , List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list , boolean isstart)
        {
            this.list = list;
            this.isstart = isstart;
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

            if (isstart)
            {
                if (position == 0)
                {
                    holder.status.setText("ONGOING");
                }
                else
                {
                    holder.status.setText(item.getStatus());
                }
            }
            else
            {
                holder.status.setText(item.getStatus());
            }

            holder.module.setText(item.getModuleName());

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
