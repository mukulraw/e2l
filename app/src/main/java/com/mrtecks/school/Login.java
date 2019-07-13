package com.mrtecks.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;

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

                }
                else
                {

                }


                Intent intent = new Intent(Login.this , MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

    }
}
