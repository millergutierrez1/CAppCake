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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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
 * Created by Miller on 12/4/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, email, age, password;
    EditText profileNameInput, emailInput, ageInput, passwordInput;
    Intent intent;
    Profile user = new Profile();
    ProgressDialog pd;
    Toolbar toolbarProfile;

    Button saveButton;
    String httpData;

    Gson gson = new Gson();
    boolean bolPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        bolPassword=false;

        toolbarProfile = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbarProfile);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

        }

        Window window = ProfileActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color             w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(ContextCompat.getColor(ProfileActivity.this,R.color.colorBlueLight));


        profileName = (TextView) findViewById(R.id.textViewProfileName);
        profileNameInput = (EditText) findViewById(R.id.NameInput);
        email = (TextView) findViewById(R.id.textViewEmail);
        emailInput = (EditText) findViewById(R.id.EmailInput);


        password = (TextView) findViewById(R.id.password);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordInput.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                passwordInput.setText("");
            }
        });

        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordInput.setText("");
            }
        });

        // Receive intent from main.
        intent = getIntent();

        saveButton = (Button) findViewById(R.id.saveButton);
        emailInput.setInputType(0);

        SharedPreferences prefernces = getSharedPreferences("USER_LOGGEDIN", MODE_PRIVATE);

        Log.d("USER_LOGGEDIN", prefernces.getAll().toString());

        httpData = String.valueOf(prefernces.getString("loggedin", "NotLoggedIn"));



        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = passwordInput.getText().toString();
                boolean uppercase = !pass.equals(pass.toLowerCase());

                if (pass.length() < 8) {
                    passwordInput.setError("Mínimo 8 caracteres!");
                    bolPassword = false;
                } else if (!uppercase) {
                    passwordInput.setError("Debe contener al menos 1 letra mayúscula");
                    bolPassword = false;
                } else if (pass.matches("(\\W)+(\\w)*|(\\s)+(\\w)*")) {
                    passwordInput.setError("La Contraseña debe empezar con una letra");
                    bolPassword = false;

                } else if(pass.contains(" ")){
                    passwordInput.setError("La Contraseña no puede tener espacios");
                    bolPassword = false;
                }

                else {
                    Log.d("PASSWORD_INPUT: ", "Valid");
                    bolPassword = true;
                }

            }
        });


        //user-email-password-1312-usuario
        //new UserData().execute("http://10.0.2.2:8080/");
        new UserData().execute("https://mgappssupport.com/");

        /*
        * Back to Main
         */

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bolPassword) {
                    String roastSave = "Saving...";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getApplicationContext(), roastSave, duration);
                    user.setName(profileNameInput.getText().toString());
                    user.setEmail(emailInput.getText().toString());
                    //user.setPassword(passwordInput.getText().toString());
                    byte[] salt;

                    try {
                        salt = getSalt();
                        String hashedPass = get_SHA_256_SecurePassword(passwordInput.getText().toString(), salt);
                        String hashed64 = android.util.Base64.encodeToString(salt, 16);
                        user.setPassword(hashedPass + "#" + hashed64);


                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                    httpData = user.toString();

                    //new ChangeUserData().execute("http://10.0.2.2:8080/");
                    new ChangeUserData().execute("https://mgappssupport.com/");

                    Log.d("USER_INFO", user.toString());
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    finish();
                }else{
                    Toast.makeText(ProfileActivity.this, "Has hecho todos los cambios?",Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class ChangeUserData extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;
        boolean loggedIn = false;
        int responseCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ProfileActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Guardando Cambios");
            pd.setProgress(0);
            pd.show();
        }

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 2) {
                    Toast.makeText(getApplicationContext(), "Usuario/Contraseña erroneo!", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 3) {
                    Toast.makeText(getApplicationContext(), "No has iniciado sesión", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 4) {
                    Toast.makeText(getApplicationContext(), "Cambios Guardados!", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 5) {
                    Toast.makeText(getApplicationContext(), "Cambios No Realizados", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            // Had to handle the IllegalArgument as without the try catch, my code would fail.
            // after the finish()
            try {
                if (httpResponse != null && !httpResponse.contains("NOT") && responseCode != 500) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = 4;
                    handler.sendMessage(msg);


                } else {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = 5;
                    handler.sendMessage(msg);

                }

                if ((ProfileActivity.this.pd != null) && ProfileActivity.this.pd.isShowing()) {
                    Log.d("DISMISS","true");
                    ProfileActivity.this.pd.dismiss();
                }

            } catch (final IllegalArgumentException e) {


                // Handle or log or ignore
            } finally {
                Log.d("DISMISS","true");
                ProfileActivity.this.pd.dismiss();
            }


        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            try {
                URL urlPost = null;
                urlPost = new URL(params[0] + "profile_changes");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.d("CONNECTION_INFO", httpData);
                bw.write(httpData);

                //Release resources

                bw.flush();
                bw.close();
                os.close();

                Log.d("CONNECTION_POST_ATTEMPT", connection.getResponseCode() + "-" + connection.getResponseMessage().toString());
                responseCode = connection.getResponseCode();
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
                exceptionError = true;
            } catch (IOException e) {
                Log.d("CONNECTION-IO:", "Unsuccessful");
                e.printStackTrace();
                exceptionError = true;


            } finally {
                if (connection != null) {
                    Log.d("CONNECTION:", "Closing connection");
                    Log.d("NetworkError:", String.valueOf(exceptionError));
                    if (exceptionError) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        Log.d("CANCELLED:", String.valueOf(isCancelled()));

                    }
                    connection.disconnect();
                }

                if (br != null) {
                    try {

                        br.close();
                        Log.d("BUFFERED-READER:", "CLOSED");
                    } catch (IOException e) {
                        Log.d("IO: ", e.getMessage());
                        e.printStackTrace();
                    }

                }
                return null;
            }
        }
    }



    /*
    Async to gather information from DB based on the user LoggedIn
     */

    public class UserData extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;
        boolean loggedIn = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Cargando datos..");
            pd.setProgress(0);
            pd.show();
        }

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 2) {
                    Toast.makeText(getApplicationContext(), "Usuario/Contraseña erroneo!", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 3) {
                    Toast.makeText(getApplicationContext(), "No has iniciado sesión", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (httpResponse != null) {
                user = gson.fromJson(httpResponse, Profile.class);
                if (user == null) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = 3;
                    handler.sendMessage(msg);
                    pd.dismiss();
                    finish();
                    return;
                }

                Log.d("PROFILE: ", user.toString());
                profileNameInput.setText(user.getName());
                emailInput.setText(user.getEmail());

                //It will not populate the real Password as it contains the hash+pass
                passwordInput.setText(user.getPassword());
                pd.dismiss();


            } else {

                Message msg = handler.obtainMessage();
                msg.arg1 = 1;
                handler.sendMessage(msg);
                pd.dismiss();
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            try {
                URL urlPost = null;
                urlPost = new URL(params[0] + "profile_data");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

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
                exceptionError = true;
            } catch (IOException e) {
                Log.d("CONNECTION-IO:", "Unsuccessful");
                e.printStackTrace();
                exceptionError = true;


            } finally {
                if (connection != null) {
                    Log.d("CONNECTION:", "Closing connection");
                    Log.d("NetworkError:", String.valueOf(exceptionError));
                    if (exceptionError) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        System.out.println("Cancelled: " + isCancelled());
                        System.out.println("toast");
                    }
                    connection.disconnect();
                }

                if (br != null) {
                    try {

                        br.close();
                        Log.d("BUFFERED-READER:", "CLOSED");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }


        }


    }
}




