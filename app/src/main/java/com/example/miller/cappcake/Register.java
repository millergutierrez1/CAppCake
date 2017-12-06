package com.example.miller.cappcake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Miller on 12/6/2017.
 */

public class Register extends AppCompatActivity{

     final String CREDENTIALS = "userPass";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);



        final EditText nameInput = (EditText) findViewById(R.id.NameInput);
        final EditText emailInput = (EditText) findViewById(R.id.EmailInput);
        final EditText ageInput = (EditText) findViewById(R.id.ageInput);
        final EditText userInput = (EditText) findViewById(R.id.userInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);

        Button button = (Button) findViewById(R.id.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences  preferences = getSharedPreferences(CREDENTIALS, MODE_PRIVATE);

                Profile user = new Profile();
                user.setName(nameInput.getText().toString());
                user.setDateOfBirth(ageInput.getText().toString());
                user.setEmail(emailInput.getText().toString());
                user.setUser(userInput.getText().toString());
                user.setPassword(passwordInput.getText().toString());


                SharedPreferences.Editor editor = preferences.edit();

                //Make a value kay-pair We'll be using profile1
                editor.putString("profile1",user.getName() + "-"+ user.getEmail() + "-"+user.getPassword() + "-" + user.getDateOfBirth()+"-"+user.getUser());
                editor.commit();

                Intent loginScreen = new Intent(Register.this, Login.class );
                startActivity(loginScreen);

            }
        });




    }
}
