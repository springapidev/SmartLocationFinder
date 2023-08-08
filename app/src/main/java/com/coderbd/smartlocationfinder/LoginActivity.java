package com.coderbd.smartlocationfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity {

    Button btnSignup,btnSignIn;
    SharedPreferences sharedpreferences;
    TextView errorMsg;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(SignupActivity.BMI_SIGNUP_PREFERENCES, Context.MODE_PRIVATE);

        String signInStatus = sharedpreferences.getString(SignupActivity.SIGN_IN_STATUS,"false");
        if(signInStatus.equalsIgnoreCase("true")){
            Intent intent=new Intent(LoginActivity.this,MapsActivity.class);
            startActivity(intent);
        }

        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn = findViewById(R.id.btnSignin);
        errorMsg =findViewById(R.id.errorMsg);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveName = sharedpreferences.getString(SignupActivity.FULL_NAME,"");
                String saveEmail = sharedpreferences.getString(SignupActivity.EMAIL,"");
                String savePass = sharedpreferences.getString(SignupActivity.PASSWORD,"");
                String getEmail=email.getText().toString();
                String getPassword=password.getText().toString();
                if(saveEmail.equalsIgnoreCase(getEmail) && savePass.equalsIgnoreCase(getPassword)){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString(SignupActivity.FULL_NAME, saveName);
//                    editor.putString(SignupActivity.EMAIL, saveEmail);
//                    editor.putString(SignupActivity.PASSWORD, savePass);
                    editor.putString(SignupActivity.SIGN_IN_STATUS, "true");
                    editor.commit();

                    Intent intent=new Intent(LoginActivity.this,MapsActivity.class);
                    startActivity(intent);
                }else {
                    errorMsg.setText("Invalid Credentials!");
                }
            }
        });
    }
}