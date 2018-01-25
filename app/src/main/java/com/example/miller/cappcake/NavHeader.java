package com.example.miller.cappcake;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Miller on 1/25/2018.
 */

public class NavHeader extends AppCompatActivity{

    TextView userName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.nav_header_main);

        userName = (TextView) findViewById(R.id.userNameLoggedin);
        SharedPreferences sp = getSharedPreferences("USER_LOGGEDIN", MODE_PRIVATE);
        Log.d("USERNAME_LOGGEDIN", sp.getString("loggedin","NoName"));

        userName.setText(sp.getString("loggedin","").toString());




    }
}
