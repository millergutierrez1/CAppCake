package com.example.miller.cappcake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Miller on 12/6/2017.
 */

public class Login extends AppCompatActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        SharedPreferences preferences = getSharedPreferences("userPass",MODE_PRIVATE);

        Log.d("credentials" ,preferences.getAll().toString());


        String test = preferences.getAll().toString();
        String nuevo = test.split("-").toString();
        Log.d("credentialsProcessed" ,nuevo);


        final EditText userName = (EditText) findViewById(R.id.userName);
        final EditText password = (EditText) findViewById(R.id.Password);
        Button signin = (Button) findViewById(R.id.signinButton);
        Button register = (Button) findViewById(R.id.registerBtn);
        TextView forgotPassword = (TextView) findViewById(R.id.forgotText);
        TextView skip = (TextView) findViewById(R.id.skipText);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(Login.this, MainActivity.class );
                startActivity(loginScreen);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(Login.this, Register.class );
                startActivity(loginScreen);
            }
        });




    }
}
