package com.example.miller.cappcake;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Miller on 12/6/2017.
 */

public class Login extends AppCompatActivity {


    String usernameForm;
    String uPassword;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor ed;

    EditText userName, password;

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sp = this.getSharedPreferences("USER_LOGGEDIN", MODE_PRIVATE);
        String preLogin = sp.getAll().toString().toString();
        Log.d("LoadingLogin", preLogin);

        if(preLogin.contains("loggedin")){
            Log.d("PRELOGIN", preLogin);
            Intent userLoggedin = new Intent(this, MainActivity.class);
            startActivity(userLoggedin);
        } else{
            Log.d("PRELOGIN", "notloggedin  ");
        }

        Window window = Login.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color             w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(ContextCompat.getColor(Login.this,R.color.colorBlueLight));

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.Password);
        Button signin = (Button) findViewById(R.id.signinButton);
        Button register = (Button) findViewById(R.id.registerBtn);
        TextView forgotPassword = (TextView) findViewById(R.id.forgotText);
        TextView skip = (TextView) findViewById(R.id.skipText);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // In case the user hasn't loggedIn
                Intent loginScreen = new Intent(Login.this, MainActivity.class);
                SharedPreferences sp = getSharedPreferences("USER_LOGGEDIN",MODE_PRIVATE);
                ed = sp.edit();

                //Empty SharedPreferences -> this will not load user info in the Profile activity.
                ed.clear().commit();
                Log.d("LOGGEDIN-ACTIVITY: ", "Skipped login");
                startActivity(loginScreen);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameForm = userName.getText().toString();
                uPassword = password.getText().toString();


                if(usernameForm.isEmpty() || uPassword.isEmpty()){
                    Log.d("LOGIN-FORM: ","Empty");
                    Toast.makeText(Login.this,"Primero introduce un usuario/contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                * Initiate AsyncTask for login using user/pass
                */

                //new login().execute("http://10.0.2.2:8080/");
                new login().execute("https://mgappssupport.com/");

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Allows user to register.

                Intent loginScreen = new Intent(Login.this, Register.class);
                startActivity(loginScreen);
            }
        });




    }

    public class login extends AsyncTask<String, Integer, String> {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;
        boolean loggedIn = false;

        @Override
        protected void onPreExecute() {


            super.onPreExecute();

            pd = new ProgressDialog(Login.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Iniciando sesión..");
            pd.setProgress(0);
            Log.d("DIALOG-LOGIN: ", "Started");
            pd.show();
        }

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                //Handler to avoid thread to die with a toast -> exception
                if (msg.arg1 == 1){
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                } else if(msg.arg1==2){
                    Toast.makeText(getApplicationContext(), "Usuario/Contraseña erroneo!", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(exceptionError){
                pd.dismiss();
                return;
            }

            if(httpResponse.contains("False")|| httpResponse==""){
                System.out.println("TRY-AGAIN");
                Message msg = handler.obtainMessage();
                msg.arg1 = 2;
                handler.sendMessage(msg);
                cancel(true);
                pd.dismiss();
                return;

            } else{
                SharedPreferences sp = getSharedPreferences("USER_LOGGEDIN",MODE_PRIVATE);
                ed = sp.edit();
                ed.clear();
                ed.putString("loggedin",usernameForm);
                ed.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


                Log.d("USER-LOGIN","true");

                pd.dismiss();


            }

        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            try {
                URL urlPost = null;
                urlPost = new URL(params[0] + "signin");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                Gson gson = new Gson();

                LoginInfo dataLogin = new LoginInfo();
                dataLogin.setUser(usernameForm);
                dataLogin.setPassword(uPassword);


                String httpData = gson.toJson(dataLogin);

                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.d("CONNECTION_INFO", httpData);
                bw.write(httpData);

                //Release resources

                bw.flush();
                bw.close();
                os.close();

                Log.d("CONNECTION_POST_ATTEMPT", connection.getResponseCode() + "-" + connection.getResponseMessage().toString());
                int responseCode = connection.getResponseCode();
                if (responseCode == connection.HTTP_OK) {

                    BufferedReader brResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";

                    while ((line = brResponse.readLine()) != null) {
                        sbRespone.append(line).toString();
                    }

                    httpResponse = sbRespone.toString();
                    brResponse.close();
                    Log.d("HTTP_RESPONSE", httpResponse);
                    publishProgress(10);

                    return httpResponse;

                }
            } catch (MalformedURLException e) {
                Log.d("CONNECTION-MAL:", "Unsuccessful");
                e.printStackTrace();
                Log.d("MALFORMED-URL: ",  e.getMessage());
                exceptionError = true;
            } catch (IOException e) {
                Log.d("IO-URL: ",  e.getMessage());
                Log.d("CONNECTION-IO:", "Unsuccessful");
                e.printStackTrace();
                exceptionError = true;


            } finally {
                if (connection != null) {
                    Log.d("CONNECTION:", "Closing connection");
                    System.out.println("NetworkError" + exceptionError);
                    if (exceptionError) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        Log.d("Cancelled: ",  String.valueOf(isCancelled()));
                    }
                    connection.disconnect();
                }

                if (br != null) {
                    try {

                        br.close();
                        Log.d("BUFFERED-READER:", "CLOSED");
                    } catch (IOException e) {
                        Log.d("IO: ",  e.getMessage());
                        e.printStackTrace();
                    }

                }
                return httpResponse;
            }

        }


    }

    public class LoginInfo {

        private String user;
        private String password;

        public LoginInfo(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public LoginInfo(){

        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}


