package com.example.miller.cappcake;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Profile extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Recipes> cappList = new ArrayList<>();
    int countpost = 0;
    ProgressDialog pd;
    Gson gson = new Gson();
    Recipes r;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_profile);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setRefreshing( false );
        swipeRefreshLayout.setEnabled( false );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

        }



        sp = getSharedPreferences("USER_LOGGEDIN",MODE_PRIVATE);
        r = gson.fromJson(getIntent().getStringExtra("recipe_details"), Recipes.class);

        String loggedIn = sp.getString("loggedin","notLoggedin");
        if(loggedIn.contains("notLoggeding")){
            finish();
        } else{
            new LoadRecipes().execute("https://mgappssupport.com/");
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class LoadRecipes extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Main2Profile.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Cargando Recetas...");
            pd.setProgress(0);
            pd.show();
        }

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 2) {
                    Toast.makeText(getApplicationContext(), "Usuario/Contraseña erroneo!", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            try {
                URL urlGet = null;
                urlGet = new URL(params[0] + "recipes_list");
                connection = (HttpURLConnection) urlGet.openConnection();
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.connect();

                Log.d("CONNECTION_POST_ATTEMPT", connection.getResponseCode() + "-" + connection.getResponseMessage().toString());
                int responseCode = connection.getResponseCode();
                if (responseCode == connection.HTTP_OK) {

                    BufferedReader brResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";

                    while ((line = brResponse.readLine()) != null) {
                        sbRespone.append(line).toString();
                    }
                    httpResponse = sbRespone.toString().trim();
                    Log.d("HTTP_RESPONSE", httpResponse);
                    Log.d("SIZE: PRE GSON ", String.valueOf(cappList.size()));

                    Gson gson = new Gson();

                    Type capplistType = new TypeToken<ArrayList<Recipes>>() {
                    }.getType();
                    cappList = gson.fromJson(httpResponse, capplistType);
                    Log.d("SIZE: POST GSON ", String.valueOf(cappList.size()));

                    brResponse.close();

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
                        Log.d("CANCELLED", String.valueOf(isCancelled()));
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
                return httpResponse;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        /*
        * Recycler View Configuration
         */

            if (exceptionError) {
                pd.dismiss();
                finish();
            }

            Log.d("LOAD-RECIPES-USER:","START");


            pd.dismiss();

            countpost++;

            Log.d("POST-EXECUTE ITEMS", String.valueOf(countpost));

            //new LoadRecipesPerUser().execute("http://10.0.2.2:8080/");

            new LoadRecipesPerUser().execute("https://mgappssupport.com/");
        }
    }

    public class LoadRecipesPerUser extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;
        boolean loggedIn = false;
        int responseCode;
        String httpRecipes;

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Main2Profile.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Actualizando recetas...");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            Gson gson = new Gson();
            try {
                Log.d("INSIDE_RECIPES_USER","TRUE");
                URL urlPost = null;
                urlPost = new URL(params[0] + "recipes_per_user");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);

                Log.d("INSIDE_RECIPES_USER","TRUE");
                connection.connect();
                Log.d("INSIDE_RECIPES_USER","aferConnect()");
                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                Log.d("INSIDE_RECIPES_USER","aferBufferedWriter()");
                httpRecipes = gson.toJson(sp.getString("loggedin", "notLoggedin").toString().trim());
                Log.d("INSIDE_RECIPES_USER",httpRecipes);
                Log.d("CONNECTION_INFO", httpRecipes);
                bw.write(httpRecipes);

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<Recipes> finalCappList = new ArrayList<>();
            ArrayList<Recipes> copy = new ArrayList<>();
            Log.d("HTTPRESPONSE", httpResponse);
            int counter = 0;
            if (!httpResponse.contains("No recipes found")) {

                if (!cappList.isEmpty()) {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

                    String[] recipes_list = httpResponse.split("-");

                    for(int j = 0; j<recipes_list.length;j++){
                        Log.d("INSIDE-LOOP",recipes_list[j]);

                        for (int i = 0; i<cappList.size();i++){

                            if(cappList.get(i).getId()==Long.parseLong(recipes_list[j])){
                                Log.d("RECIPEFOUND",cappList.get(i).toString());
                                finalCappList.add(cappList.get(i));


                            }

                        }
                    }

                    adapter = new CappAdapter(getApplicationContext(), finalCappList);
                    recyclerView.setAdapter(adapter);



                }

                /*Log.d("INSIDE STATUS", httpResponse);
                String[] recipes_list = httpResponse.split("-");
                for (String l : recipes_list) {
                    Log.d("INSIDE_RECIPES_LOOP", l);
                    if (l.equals(String.valueOf(r.getId()))) {

                        Log.d("RECIPES_LOADED", l);


                    }

                }*/



            }
            pd.dismiss();

        }
    }
}



