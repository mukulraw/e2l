package com.mrtecks.e2l;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mrtecks.e2l.regiterPOJO.Data;
import com.mrtecks.e2l.regiterPOJO.registerBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    Button signup , signin;
    TextInputEditText phone , password;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.button2);
        signin = findViewById(R.id.button);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        progress = findViewById(R.id.progressBar);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this , Signup.class);
                startActivity(intent);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String ph = phone.getText().toString();
                String pa = password.getText().toString();


                if (ph.length() == 10)
                {

                    if (pa.length() > 0)
                    {


                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Call<registerBean> call = cr.login(
                                ph,
                                pa
                        );

                        call.enqueue(new Callback<registerBean>() {
                            @Override
                            public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                                if (response.body().getStatus().equals("1"))
                                {

                                    Toast.makeText(Login.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();

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

                                        Intent intent = new Intent(Login.this , MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else
                                    {
                                        Intent intent = new Intent(Login.this , Survey.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(Login.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<registerBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Login.this, "Invlid phone", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }
}
