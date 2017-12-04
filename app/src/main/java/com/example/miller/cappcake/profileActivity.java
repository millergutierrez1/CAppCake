package com.example.miller.cappcake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Miller on 12/4/2017.
 */

public class profileActivity extends AppCompatActivity {

    TextView profileName, email, age ;
    EditText profileNameInput, emailInput, ageInput;
    Intent intent;

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

        // Receive intent from main.
        intent = getIntent();

        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
