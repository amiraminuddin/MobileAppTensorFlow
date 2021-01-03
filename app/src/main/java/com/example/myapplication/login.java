package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase Authentication
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }

        //start view content
        setContentView(R.layout.activity_login);

        inputEmail = (EditText)findViewById(R.id.editTextUsername);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.sign_up_button);
        btnLogin = (Button) findViewById(R.id.login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //get Firebase authentication instance
        auth = FirebaseAuth.getInstance();

        //activity for intent to sign up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signUp.class));
            }
        });

        //activity for intent reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, ResetPassword.class));
            }
        });

        //acticity for login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                final String Password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Password))
                {
                    Toast.makeText(getApplicationContext(),"enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, Password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful())
                                {
                                    if(Password.length() <6 )
                                    {
                                        inputPassword.setError("Password too short, enter minimum 6 characters!");
                                    }else
                                    {
                                        Toast.makeText(login.this,"Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                                    }
                                }else
                                {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
            }
        });


    }
}
