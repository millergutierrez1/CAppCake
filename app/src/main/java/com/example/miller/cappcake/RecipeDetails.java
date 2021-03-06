package com.example.miller.cappcake;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import static com.squareup.picasso.Picasso.*;

/**
 * Created by Miller on 1/2/2018.
 */

public class RecipeDetails extends AppCompatActivity {

    Recipes r;
    TextView titleDetails, titleInstructions, titleIngredients, ingredients_biscuit, ingredients_frosting, ingredients_extra, instructions_biscuit, instructions_frosting, instructions_extra;
    RatingBar rating;
    Button save_rating;
    ImageView cappImage;
    EditText multiline;
    String httpData;
    ProgressDialog pd;
    boolean hasVoted, storedRecipe;
    Profile user = new Profile();
    FloatingActionButton floatingButton;
    Gson gson = new Gson();
    SharedPreferences sp ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storedRecipe = false;
        setContentView(R.layout.recipe_details);
        hasVoted = false;
        sp = getSharedPreferences("USER_LOGGEDIN",MODE_PRIVATE);
        titleDetails = (TextView) findViewById(R.id.titleDetails);
        Window window = RecipeDetails.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color             w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(ContextCompat.getColor(RecipeDetails.this,R.color.colorBlueLight));


        titleIngredients = (TextView) findViewById(R.id.title_ingredients);
        titleIngredients.setShadowLayer(5, 0, 0, Color.BLACK);
        ingredients_biscuit = (TextView) findViewById(R.id.ingredients_biscuits);
        ingredients_frosting = (TextView) findViewById(R.id.ingredients_frosting);
        ingredients_extra = (TextView) findViewById(R.id.ingredients_extra);
        titleInstructions = (TextView) findViewById(R.id.title_instructions);
        titleInstructions.setShadowLayer(5, 0, 0, Color.BLACK);
        instructions_biscuit = (TextView) findViewById(R.id.instru_biscuit);
        instructions_frosting = (TextView) findViewById(R.id.instru_frosting);
        instructions_extra = (TextView) findViewById(R.id.instru_extras);


        rating = (RatingBar) findViewById(R.id.ratingBar);
        rating.setIsIndicator(false);
        save_rating = (Button) findViewById(R.id.button_rating);

        cappImage = (ImageView) findViewById(R.id.capp_image);

        floatingButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);



        Gson gson = new Gson();

        //load from intent -> Extras
        r = gson.fromJson(getIntent().getStringExtra("recipe_details"), Recipes.class);

        Log.d("INTENT_RECEIVED: ", r.toString());

        //Load from object -> intentExtras
        titleDetails.setText(r.getTitle());
        titleDetails.setShadowLayer(30, 0, 0, Color.BLACK);

        String url = r.getUrlImage();
        Log.d("URLIMAGE", r.getUrlImage());


        if (r.getUrlImage() != "") {
            with(RecipeDetails.this).load(url).resize(720, 600).centerInside().placeholder(R.drawable.icecreamholder).error(R.drawable.icecreamholdererror).into(cappImage);
        }

        Log.d("CHECK_RECIPES","START");

        new LoadRecipesPerUser().execute("https://mgappssupport.com/");
        //new LoadRecipesPerUser().execute("http://10.0.2.2:8080/");
        Log.d("CHECK_RECIPES","FINISH");

        ingredients_biscuit.setText(Html.fromHtml(r.ingredientsBiscuitToString()));
        ingredients_frosting.setText(Html.fromHtml(r.ingredientsFrostingToString()));
        ingredients_extra.setText(Html.fromHtml(r.ingredientsExtratoString()));
        instructions_biscuit.setText(Html.fromHtml(r.instructionsBiscuitToString()));
        instructions_frosting.setText(Html.fromHtml(r.instructionsFrostingToString()));
        instructions_extra.setText(Html.fromHtml(r.instructionsExtraToString()));


        /*

         */

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratingResults, boolean fromUser) {

                r.setRanking(ratingResults + r.getRanking());
                Log.d("RATING_LISTENER", String.valueOf(ratingResults));


            }
        });


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RecipeDetails.this, r.getTitle(),Toast.LENGTH_SHORT).show();

                String canSave = sp.getAll().toString();

                System.out.println(canSave);


                if(canSave.contains("loggedin") && storedRecipe==false){

                    new SaveRecipeProfile().execute("https://mgappssupport.com/");
                    //new SaveRecipeProfile().execute("http://10.0.2.2:8080/");


                }else if(storedRecipe==false){
                    Toast.makeText(RecipeDetails.this, "Inicia sesión para guardar en \"Mis Recetas\"", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(RecipeDetails.this, "Receta ya añadida a \"Mis Recetas\"", Toast.LENGTH_LONG).show();

                }



            }
        });




        save_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RATING_INPUT", String.valueOf(r.getRanking()));

                String canVote = sp.getAll().toString();
                if (!hasVoted && canVote.contains("loggedin")) {
                    hasVoted=true;
                    //new SaveRating().execute("http://10.0.2.2:8080/");
                    new SaveRating().execute("https://mgappssupport.com/");
                    rating.setIsIndicator(true);


                } else if(hasVoted) {
                    Toast.makeText(RecipeDetails.this, "Ya has votado " + rating.getRating() + " estrallas!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(RecipeDetails.this, "Inicia sesión para poder valorar recetas!", Toast.LENGTH_LONG).show();
                }



            }
        });


    }


    public class SaveRating extends AsyncTask<String, Integer, String> {

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;
        boolean loggedIn = false;
        int responseCode;

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                } else if (msg.arg1 == 2) {
                    Toast.makeText(getApplicationContext(), "Votación Exitosa!", Toast.LENGTH_LONG).show();
                }

            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(RecipeDetails.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Guardando Cambios");
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            Gson gson = new Gson();
            try {
                URL urlPost = null;
                urlPost = new URL(params[0] + "insert_rating");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                r.setRanking_count(r.getRanking_count() + 1);
                httpData = gson.toJson(r);
                Log.d("CONNECTION_INFO", "Ranking: " + r.getRanking() + "- RankingCount" + r.getRanking_count());
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (responseCode == 200) {

                Message msg = handler.obtainMessage();
                msg.arg1 = 2;
                handler.sendMessage(msg);

            }
            pd.dismiss();

        }
    }




    public class SaveRecipeProfile extends AsyncTask<String, Integer,String>{

        StringBuilder sb = new StringBuilder();
        StringBuilder sbRespone = new StringBuilder();
        String httpResponse = "";
        boolean exceptionError;

        @Override
        protected void onPreExecute() {

            //progressDialog configuration
            super.onPreExecute();
            /*pd = new ProgressDialog(Register.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMax(10);
            pd.setMessage("Registrando usuario");
            pd.setProgress(0);
            pd.show();*/
            //Log.d("", "Started progressDialog");


        }

        /*
        * Need to use a handler as can't use Toasts while running an async thread.
         */

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1)
                    Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                else if(msg.arg1==2){
                    Toast.makeText(getApplicationContext(), "Usuario no creado.", Toast.LENGTH_LONG).show();
                }
            }
        };

        @Override
        protected String doInBackground(String... params) {
            BufferedReader br = null;
            HttpURLConnection connection = null;

            try {
                Log.d("POST_UserValidaton   ", "True");

                URL urlPost = null;
                urlPost = new URL(params[0] + "insert_profile_recipes");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                SaveRecipeId userRecipe = new SaveRecipeId();

                userRecipe.setName(sp.getString("loggedin","notLoggedin"));
                userRecipe.setId(String.valueOf(r.getId()));
                String httpData = gson.toJson(userRecipe);


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
                    Log.d("NetworkError: ", String.valueOf(exceptionError));

                    if (exceptionError) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        Log.d("CANCELLED: ", String.valueOf(isCancelled()));
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

            //Control progressDialogue -> 0
            /*super.onProgressUpdate(values);
            int progress = values[0];
            pd.setProgress(progress);*/
        }

        @Override
        protected void onPostExecute(String builder) {
            super.onPostExecute(null);

            if(httpResponse.contains("recipeSaved")){
                storedRecipe = true;
                floatingButton.setImageResource(R.drawable.icecreamchange);

            }else if(exceptionError){
                finish();


            }

        }


    }

    public class LoadRecipesPerUser extends AsyncTask<String, Integer, String>{

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

            pd = new ProgressDialog(RecipeDetails.this);
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
                URL urlPost = null;
                urlPost = new URL(params[0] + "recipes_per_user");
                connection = (HttpURLConnection) urlPost.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(true);
                connection.connect();

                OutputStream os = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


                httpRecipes = gson.toJson(sp.getString("loggedin","notLoggedin").toString().trim());

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

            Log.d("HTTPRESPONSE",httpResponse);
            if (!httpResponse.contains("No recipes found")) {

                Log.d("INSIDE STATUS",httpResponse);
                String[] recipes_list = httpResponse.split("-");
                for (String l:recipes_list){
                    Log.d("INSIDE_RECIPES_LOOP",l);
                    if(l.equals(String.valueOf(r.getId()))){

                        Log.d("RECIPES_LOADED",l);
                        storedRecipe = true;
                        floatingButton.setImageResource(R.drawable.icecreamchange);
                    }

                }

            }else{
                storedRecipe = false;
            }

            pd.dismiss();

        }
    }


    public class SaveRecipeId{
        String name;
        String id;

        public SaveRecipeId(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}

