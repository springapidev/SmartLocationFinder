package com.coderbd.smartlocationfinder;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    EditText name,email, password;
    public static final String BMI_SIGNUP_PREFERENCES = "BmiSignup" ;
    public static final String FULL_NAME = "nameKey";
    public static final String EMAIL = "emailKey";
    public static final String PASSWORD = "passwordKey";
    public static final String SIGN_IN_STATUS = "false";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignIn = findViewById(R.id.btnSignin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        name=findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btnSignup);
        sharedpreferences = getSharedPreferences(BMI_SIGNUP_PREFERENCES, Context.MODE_PRIVATE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName=name.getText().toString();
                String getEmail=email.getText().toString();
                String getPassword=password.getText().toString();
                if(fullName.length() < 2){
                    Toast.makeText(SignupActivity.this,"Name should be at least 2 Characters!", Toast.LENGTH_SHORT).show();
                }else if(!getEmail.contains("@") || !getEmail.contains(".")){
                    Toast.makeText(SignupActivity.this,"A Valid Email is required",Toast.LENGTH_SHORT).show();
                }else if(password.length() < 3){
                    Toast.makeText(SignupActivity.this,"Password should be at least 2 Characters!", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(FULL_NAME, fullName);
                    editor.putString(EMAIL, getEmail);
                    editor.putString(PASSWORD, getPassword);
                    editor.putString(SIGN_IN_STATUS, "false");
                    editor.commit();
                    Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}