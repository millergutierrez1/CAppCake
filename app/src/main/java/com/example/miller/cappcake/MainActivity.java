package com.example.miller.cappcake;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences afterLogin;
    String loggedIn_value;
    TextView userName;
    NavHeader nav;
    MenuItem login_logout;
    boolean rating_asc, rating_desc,refreshedError;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Recipes> cappList = new ArrayList<>();
    int countpost = 0;
    ProgressDialog pd;
    SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        refreshedError = false;
        rating_asc = false;
        rating_desc = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new LoadRecipes().execute("http://10.0.2.2:8080/");
        new LoadRecipes().execute("https://mgappssupport.com/");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color             w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorBlueLight));



        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

        }


        afterLogin = getSharedPreferences("USER_LOGGEDIN", MODE_PRIVATE);
        loggedIn_value = String.valueOf(afterLogin.getAll());




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav = navigationView.getMenu();
        login_logout = menuNav.findItem(R.id.nav_salir);
        if(!loggedIn_value.contains("loggedin")){
            login_logout.setTitle("Iniciar Sesión");
        }


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);




        /*
        * Handle the swipe refresh -> Call te load items within the refreshItems
        */


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
                onItemsLoadComplete();

            }
        });



    }


    public List<Recipes> sortingList(ArrayList<Recipes> list, String type) {

        ArrayList<Recipes> sortedList = list;
        RecipeSortingRating recipeSorter;
        double max;
        double min;
        double current;
        if (!sortedList.isEmpty())
            switch (type) {
                case "ratingAscending":
                    recipeSorter = new RecipeSortingRating(sortedList);
                    ArrayList<Recipes> sortedRecipesId = recipeSorter.getSortedByRating();
                    for (Recipes l : sortedRecipesId) {
                        Log.d("SORTED_ASC", String.valueOf(l.getRanking() / l.getRanking_count()));
                    }
                    break;

                case "ratingDescending":
                    recipeSorter = new RecipeSortingRating(sortedList);
                    sortedRecipesId = recipeSorter.getSortedByRatingDesc();
                    for (Recipes l : sortedRecipesId) {
                        Log.d("SORTED_DESC", String.valueOf(l.getRanking()/l.getRanking_count()));
                    }

                    break;
            }


        return sortedList;


    }


    public class RecipeSortingRating {

        ArrayList<Recipes> ratingList = new ArrayList<>();

        public RecipeSortingRating(ArrayList<Recipes> ratingList) {
            this.ratingList = ratingList;
        }

        public ArrayList<Recipes> getSortedByRating() {
            Collections.sort(ratingList, Recipes.ratingComparator);
            return ratingList;
        }

        public ArrayList<Recipes> getSortedByRatingDesc() {
            Collections.sort(ratingList, Recipes.ratingComparatorDesc);
            return ratingList;
        }


        public ArrayList<Recipes> getSortedByid() {

            Collections.sort(ratingList, Recipes.idComparator);
            return ratingList;


        }
    }


    public void refreshItems() {
        //new LoadRecipes().execute("http://10.0.2.2:8080/");

        new LoadRecipes().execute("https://mgappssupport.com/");
        Log.d("REFRESHPAGE", "True");

    }

    void onItemsLoadComplete() {

        Log.d("REFRESHPAGE", "Complete");

        mSwipeRefreshLayout.setRefreshing(false);
    }


    public class LoadRecipes extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
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
                    Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña Incorrectos!", Toast.LENGTH_LONG).show();
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
                    Log.d("CONNECTION:", "Closing connection LoadRecipes");
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
                refreshedError=true;
                finish();
            }

            if (!cappList.isEmpty()) {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                if(rating_asc){

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    adapter = new CappAdapter(getApplicationContext(), cappList);
                    recyclerView.setAdapter(adapter);
                    sortingList(cappList, "ratingAscending");
                    Log.d("RAITNG_ASC", String.valueOf(rating_asc));
                    rating_asc=false;

                }else if(rating_desc){

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    adapter = new CappAdapter(getApplicationContext(), cappList);
                    recyclerView.setAdapter(adapter);
                    sortingList(cappList, "ratingDescending");
                    Log.d("RATING_DESC",String.valueOf(rating_desc));
                    rating_desc=false;

                }

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                adapter = new CappAdapter(getApplicationContext(), cappList);
                recyclerView.setAdapter(adapter);




            }

            pd.dismiss();

            countpost++;
            Log.d("POST-EXECUTE ITEMS", String.valueOf(countpost));

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (loggedIn_value.contains("loggedin")) {


        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.asc_rating) {
            rating_asc=true;
            refreshItems();


            return true;
        } else if(id == R.id.desc_rating){
            rating_desc = true;
            refreshItems();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        // Handle navigation view item clicks here.



        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            //Start intent towards ProfileActivity


            if(!loggedIn_value.contains("loggedin")){
                Toast.makeText(this, "Primero debes iniciar sesión", Toast.LENGTH_LONG).show();
            } else{
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivityForResult(profileIntent, 1);

            }

            // Handle the camera action
        } else if (id == R.id.nav_recipes) {

            if(!loggedIn_value.contains("loggedin")){
                Toast.makeText(this, "Primero debes iniciar sesión", Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(getApplicationContext(), Main2Profile.class);
                startActivityForResult(intent, 1);

            }



        } else if (id == R.id.nav_salir) {

            if(loggedIn_value.contains("loggedin")){

                SharedPreferences sp = getSharedPreferences("USER_LOGGEDIN",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                Log.d("sp_loggedin", loggedIn_value+": "+sp.getString("USER_LOGGEDIN","NotLoggedIn"));
                ed.clear().commit();

                finish();


            }else{
                finish();
            }

        }


        return true;
    }


}





