package com.example.miller.cappcake;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import java.text.DecimalFormat;

import java.util.List;

import static com.squareup.picasso.Picasso.*;

/**
 * Created by Miller on 12/24/2017.
 */

public class CappAdapter extends RecyclerView.Adapter<CappAdapter.ViewHolder> {

    private Context context;
    private List<Recipes> capplist2;








    public CappAdapter(Context context, List<Recipes> capplist2) {
        this.context = context;
        this.capplist2 = capplist2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Recipes cappItems = capplist2.get(position);

        final int position2 = position;

        holder.cappTitle.setText(cappItems.getTitle());
        Log.d("IMAGE_URL", cappItems.getUrlImage());

        if(cappItems.getUrlImage()!=""){
            with(context).load(cappItems.getUrlImage()).resize(720 ,600).centerInside().into(holder.cappImage);
        }


        double avg = round(cappItems.getRanking()/cappItems.getRanking_count(),1);
        holder.cappRating.setRating((float) avg);

        Log.d("RATING",String.valueOf(cappItems.getRanking()));
        Log.d("RATING_COUNT",String.valueOf(cappItems.getRanking_count()));
        Log.d("RATING_AVG_CARDVIEW",String.valueOf(cappItems.getRanking()/cappItems.getRanking_count()));
        if(cappItems.getRanking_count() != 0){

            holder.ratingCount.setText("Puntuación: "+avg);
        } else {
            holder.ratingCount.setText("No hay votos!");
        }



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RecipeDetailsIntent = new Intent(context,RecipeDetails.class);
                RecipeDetailsIntent.putExtra("recipe_details", capplist2.get(position2).toString());

                context.startActivity(RecipeDetailsIntent);
                Log.d("INTENT_RECIPE_SENT: ","true");


            }
        });

    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public int getItemCount() {
        return capplist2.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cappTitle,ratingCount;
        public ImageView cappImage;
        public RatingBar cappRating;

        //We add this to ensure we can get onClickListeners and see the whole object's info
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);


            cappTitle = (TextView) itemView.findViewById(R.id.cappTitle);
            //cappTitle.setShadowLayer(30,0,0, Color.GRAY);
            cappImage = (ImageView) itemView.findViewById((R.id.cappImage));
            cappRating = (RatingBar) itemView.findViewById(R.id.cappRating);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            ratingCount = (TextView) itemView.findViewById(R.id.voting_count);

        }
    }


}