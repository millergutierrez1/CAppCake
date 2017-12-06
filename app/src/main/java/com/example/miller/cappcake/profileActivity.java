package com.example.miller.cappcake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Miller on 12/4/2017.
 */

public class profileActivity extends AppCompatActivity {

    TextView profileName, email, age ,password ;
    EditText profileNameInput, emailInput, ageInput, passwordInput;
    Intent intent;
    Profile user = new Profile();

    Button saveButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        profileName = (TextView) findViewById(R.id.textViewProfileName);
        profileNameInput = (EditText) findViewById(R.id.NameInput);
        email = (TextView) findViewById(R.id.textViewEmail);
        emailInput = (EditText) findViewById(R.id.EmailInput);
        age = (TextView) findViewById(R.id.textViewAge);
        ageInput = (EditText) findViewById(R.id.ageInput);

        password = (TextView) findViewById(R.id.password);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        // Receive intent from main.
        intent = getIntent();

        saveButton = (Button) findViewById(R.id.saveButton);

        SharedPreferences prefernces = getSharedPreferences("userPass", MODE_PRIVATE);

        Log.d("credentials" ,prefernces.getAll().toString());

        String inicial = String.valueOf(prefernces.getAll());
        String splitted = inicial.substring(9, inicial.length());

        String[] profile = splitted.split("-");

        //user-email-password-1312-usuario



        profileNameInput.setText(profile[0]);
        emailInput.setText(profile[1]);
        ageInput.setText(profile[3]);
        passwordInput.setText(profile[4]);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roastSave = "Saving...";
                int duration = Toast.LENGTH_LONG;






                Toast toast = Toast.makeText(getApplicationContext(),  roastSave, duration);





                user.setName(profileNameInput.getText().toString());
                user.setDateOfBirth(ageInput.getText().toString());
                user.setEmail(emailInput.getText().toString());
                user.setPassword(passwordInput.getText().toString());




                Log.d("UserInfo",user.toString());
                finish();

            }
        });
    }



}
