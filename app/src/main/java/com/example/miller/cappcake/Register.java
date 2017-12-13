package com.example.miller.cappcake;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


/**
 * Created by Miller on 12/6/2017.
 */

public class Register extends AppCompatActivity {

    final String CREDENTIALS = "userPass";
    String asyncOutput;
    Profile user = new Profile();
    private DatePickerDialog.OnDateSetListener mDataSetlistener;
    private int year, month, day;
    private boolean bolName, bolEmail, bolDate, bolUser, bolPassword;
    Gson gson = new Gson();
    String dbResult = "";
    Profile[] listDbProfiles;
    boolean registerPass = false;
    String userRegistration = "";
    ProgressDialog pd;

    EditText nameInput, emailInput, ageInput, userInput, passwordInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nameInput = (EditText) findViewById(R.id.NameInput);
        emailInput = (EditText) findViewById(R.id.EmailInput);
        ageInput = (EditText) findViewById(R.id.ageInput);
        userInput = (EditText) findViewById(R.id.userInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);


        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        //Inciamos la conexion al servior para comprobar correo y usuario





        /*
         *TextWatcher Name -> Control name input
         */

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String name = nameInput.getText().toString();

                if (emptyText(name)) {
                    Log.d("NameInputChange:", "Empty");
                    nameInput.setError("el campo Nombre no puede estar vacio!");
                    bolName = false;
                } else if (name.length() > 25) {
                    Log.d("NameInputChange:", "Too large");
                    nameInput.setError("Nombre muy largo! Inténtalo con uno mas pequeño");
                    bolName = false;
                } else {
                    bolName = true;
                }
            }
        });

        /*
        * TextWatcher Email
        */

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = emailInput.getText().toString().trim();

                if (!email.matches("(\\W|^)[\\w.\\-]{0,25}@(\\w{0,25})\\.(com|net|\\w{0,5})\\.?(\\w{2,3})($)")) {
                    Log.d("EmailInputChange:", "Not Valid Email");
                    emailInput.setError("Inserta un email válido!");
                    bolEmail = false;
                } else {
                    bolEmail = true;


                }


            }
        });

        /*
        * TextWatched AgeInput
        */

        ageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String age = ageInput.getText().toString();
                if (emptyText(age)) {
                    Log.d("EmptyAge", "Empty age");
                } else if (!age.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
                    Log.d("AgeRegex: ", age + " is Invalid ");
                    ageInput.setError("Formato Inválido (1989-12-31)");
                    bolDate = false;
                } else {
                    bolDate = true;
                }

            }
        });

        /*
        * Show Dialog Calendar  DATE of birth
         */

        ageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetlistener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /*
        * TextWatcher - UserInput
         */


        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String user = userInput.getText().toString();

                if (user.length() < 8) {
                    userInput.setError("Mínimo 8 caracteres!");
                    bolUser = false;

                } else {
                    userRegistration = user;
                    bolUser = true;
                }

            }
        });

        /*
        * TextWatcher - PasswordInput
        *
         */

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
                } else {
                    bolPassword = true;
                }


            }
        });



        /*
        * Main registration process
        * Check whether the user is in out db (user and/or email)
        * Will also make if if register pass is true. Then I'll perform an insert in the background
         */
        Button registrationBtn = (Button) findViewById(R.id.registerButton);

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextisEmpty(nameInput) || editTextisEmpty(emailInput) ||
                        editTextisEmpty(ageInput) || editTextisEmpty(userInput) || editTextisEmpty(passwordInput)) {

                    Toast.makeText(Register.this, "Rellena todos los campos", Toast.LENGTH_LONG).show();
                    return;

                }

                if (!bolDate || !bolName || !bolEmail || !bolPassword || !bolUser) {
                    System.out.println();
                    return;
                }


                user.setName(nameInput.getText().toString());
                user.setDateOfBirth(ageInput.getText().toString());
                user.setEmail(emailInput.getText().toString());
                user.setUser(userInput.getText().toString());
                user.setPassword(passwordInput.getText().toString());

                SharedPreferences preferences = getSharedPreferences(CREDENTIALS, MODE_PRIVATE);


                //JSON Formated. ToString is modified to gson
                //("user1", "user@hotmail.com","testingpass", "1990-12-21", "user3" )
                System.out.println(user);


                registerPass = true;
                //new userExistsDB().execute("http://10.0.2.2:8080/");
                new userExistsDB().execute("http://mgappssupport.com/");

                //System.out.println("HTTP_Response"+asyncOutput);

                SharedPreferences.Editor editor = preferences.edit();

                //Make a value kay-pair We'll be using profile1
                editor.putString("profile1", user.getName() + "-" + user.getEmail() + "-" + user.getPassword() + "-" + user.getDateOfBirth() + "-" + user.getUser());
                editor.commit();


            }
        });

        mDataSetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String date = year + "-" + month + "-" + dayOfMonth;

                ageInput.setText(date);
                bolDate = true;
                Log.d("DatePicker:", year + "-" + month + "-" + dayOfMonth);


            }
        };
    }


    public class userExistsDB extends AsyncTask<String, Integer, String> {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse;
        boolean exceptionError;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Register.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Registrando usuario");
            pd.setProgress(0);
            pd.show();


        }

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1)
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
            }
        };

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;
            try {
                Log.d("POST_UserValidaton   ", "True");

                URL urlPost = null;
                urlPost = new URL(params[0] + "insert");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                String httpData = user.getUser() + '-' + user.getEmail();
                exceptionError = false;

                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.d("CONNECTION_INFO", httpData);
                bw.write(httpData);


                //Release resources

                bw.flush();
                bw.close();
                os.close();

                //Check Connection HTTP response:
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
                    System.out.println("NetworkError" + exceptionError);
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

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            int progress = values[0];
            pd.setProgress(progress);
        }

        @Override
        protected void onPostExecute(String builder) {
            super.onPostExecute(null);
            pd.dismiss();


            if (httpResponse != null) {
                if (!httpResponse.contains("User Doesn't")) {
                    userInput.setError("Usuario Ya existe!");
                    bolUser = false;

                    cancel(true);


                }
                if (!httpResponse.contains("Email Doesn't")) {
                    emailInput.setError("Email ya existe!");
                    bolEmail=false;
                    cancel(true);

                }


            }

            if (isCancelled()){
                System.out.println("Date"+bolDate);
                System.out.println("Email"+bolEmail);
                System.out.println("Name"+bolName);
                System.out.println("User"+bolUser);
                System.out.println("Password"+bolPassword);

                return;



            }
            Intent loginScreen = new Intent(Register.this, Login.class);
            startActivity(loginScreen);


        }
    }

    /*
    Registration AsyncTask
     */

    public class registrationTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;
            try {

                if (registerPass) {

                    Log.d("POST_REGISTRATION", "True");

                    URL urlPost = null;
                    urlPost = new URL(params[0] + "insert");
                    connection = (HttpURLConnection) urlPost.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoOutput(true);
                    connection.connect();

                    String httpData = user.toString();

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    Log.d("CONNECTION_INFO", httpData);
                    bw.write(httpData);


                    //Release resources

                    bw.flush();
                    bw.close();
                    os.close();

                    //Check Connection HTTP response:
                    Log.d("CONNECTION_POST_ATTEMPT", connection.getResponseCode() + "-" + connection.getResponseMessage().toString());

                    int responseCode = connection.getResponseCode();
                    if (responseCode == connection.HTTP_OK) {

                        BufferedReader brResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sbRespone = new StringBuilder();

                        String line = "";

                        while ((line = brResponse.readLine()) != null) {
                            sbRespone.append(line);


                        }

                        brResponse.close();

                        Log.d("HTTP_RESPONSE", sbRespone.toString());
                        return sbRespone.toString();

                    } else {

                        Toast.makeText(getApplicationContext(), "Usuario no pudo ser insertado", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    Log.d("CONNECTION:", "Closing connection (successfull)  ");
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

    public static boolean emptyText(String text) {

        if (text.equals("") || text.startsWith(" ") || text.isEmpty() || text.length() == 0) {
            return true;
        }

        return false;
    }


    public boolean editTextisEmpty(EditText et) {
        String text = et.getText().toString();
        if (text.equals("") || text.startsWith(" ") || text.isEmpty() || text.length() == 0) {
            et.setError("Campo vacio");

            return true;
        }
        return false;
    }


}
