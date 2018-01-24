package com.example.miller.cappcake;

import com.google.gson.Gson;

/**
 * Created by Miller on 12/21/2017.
 */

public class Recipes {

    private String title;
    private String[] ingredientsBiscuit;
    private String[] ingredientsFrosting;
    private String[] ingredientsExtra;
    private String[] instructionsBiscuit;
    private String[] instructionsFrosting;
    private String[] instructionsExtra;
    private long id;
    private long key;
    private String urlImage;
    private double ranking;
    private double ranking_count;

    public Recipes() {
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Recipes(String title, long ranking) {
        this.title = title;
        this.ranking = ranking;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] instructionsBiscuit, String[] instructionsFrosting, int id) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.id = id;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] instructionsBiscuit, String[] instructionsFrosting, int id, int ranking) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.id = id;
        this.ranking = ranking;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] ingredientExtra, String[] instructionsBiscuit, String[] instructionsFrosting, String[] insturctionsExtra, int id) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.ingredientsExtra = ingredientExtra;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.instructionsExtra = insturctionsExtra;
        this.id = id;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] ingredientExtra, String[] instructionsBiscuit, String[] instructionsFrosting, String[] insturctionsExtra) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.ingredientsExtra = ingredientExtra;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.instructionsExtra = insturctionsExtra;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] ingredientExtra, String[] instructionsBiscuit, String[] instructionsFrosting, int id, int ranking) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.ingredientsExtra = ingredientExtra;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.id = id;
        this.ranking = ranking;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] instructionsBiscuit, String[] instructionsFrosting, int id, String urlImage, int ranking) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.id = id;
        this.urlImage = urlImage;
        this.ranking = ranking;
    }

    public Recipes(String title, String[] ingredientsBiscuit, String[] ingredientsFrosting, String[] ingredientsExtra, String[] instructionsBiscuit, String[] instructionsFrosting, String[] instructionsExtra, long id, String urlImage, long ranking, double ranking_count) {
        this.title = title;
        this.ingredientsBiscuit = ingredientsBiscuit;
        this.ingredientsFrosting = ingredientsFrosting;
        this.ingredientsExtra = ingredientsExtra;
        this.instructionsBiscuit = instructionsBiscuit;
        this.instructionsFrosting = instructionsFrosting;
        this.instructionsExtra = instructionsExtra;
        this.id = id;
        this.urlImage = urlImage;
        this.ranking = ranking;
        this.ranking_count = ranking_count;
    }

    public String[] getIngredientsExtra() {
        return ingredientsExtra;
    }

    public void setIngredientsExtra(String[] ingredientsExtra) {
        this.ingredientsExtra = ingredientsExtra;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRanking() {
        return ranking;
    }

    public void setRanking(double l) {
        this.ranking = l;
    }

    public String[] getInstructionsExtra() {
        return instructionsExtra;
    }

    public void setInstructionsExtra(String[] instructionsExtra) {
        this.instructionsExtra = instructionsExtra;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getIngredientsBiscuit() {
        return ingredientsBiscuit;
    }

    public String ingredientsBiscuitToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<h3><b> Bizcocho: </b></h3>");
        for(String list:this.getIngredientsBiscuit()){

            sb.append(list+"<br>");
        }
        return sb.toString();


    }



    public void setIngredientsBiscuit(String[] ingredientsBiscuit) {
        this.ingredientsBiscuit = ingredientsBiscuit;
    }

    public String[] getIngredientsFrosting() {
        return ingredientsFrosting;
    }

    public String ingredientsFrostingToString(){

        StringBuilder sb = new StringBuilder();
        if(getTitle().contains("Buttercream")||getTitle().contains("ButterCream")||getTitle().contains("buttercream")){

            sb.append("<h3><b> Buttercream: </b></h3>");
            for(String list : ingredientsFrosting){
                sb.append(list+"<br>");
        }
            return sb.toString();
        } else{
            sb.append("<h3><b> Frosting: </b></h3>");
            for(String list : ingredientsFrosting){
                sb.append(list+"<br>");
            }
            return sb.toString();
        }

    }

    public void setIngredientsFrosting(String[] ingredientsFrosting) {
        this.ingredientsFrosting = ingredientsFrosting;
    }

    public String[] getIngredientExtra() {
        return ingredientsExtra;
    }

    public void setIngredientExtra(String[] ingredientExtra) {
        this.ingredientsExtra = ingredientExtra;
    }

    public String ingredientsExtratoString(){

        StringBuilder sb = new StringBuilder();
        if(getIngredientsExtra().length>1){
            if(getTitle().contains("Coulis")){

                sb.append("<h3><b> Coulis: </b></h3>");
                for(String list : ingredientsExtra){
                    sb.append(list+"<br>");
                }
                return sb.toString();
            } else{
                sb.append("<h3><b> Extra: </b></h3>");
                for(String list : ingredientsExtra){
                    sb.append(list+"<br>");
                }

                return sb.toString();
            }

        } return "";


    }

    public String[] getInstructionsBiscuit() {
        return instructionsBiscuit;
    }

    public void setInstructionsBiscuit(String[] instructionsBiscuit) {
        this.instructionsBiscuit = instructionsBiscuit;
    }

    public String instructionsBiscuitToString(){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        sb.append("<h3><b> Bizcocho: </b></h3>");
        for(String list : instructionsBiscuit){
            sb.append(i+". "+list+"<br>");
            i++;
        }
        return  sb.toString();

    }

    public String[] getInstructionsFrosting() {
        return instructionsFrosting;
    }

    public void setInstructionsFrosting(String[] instructionsFrosting) {
        this.instructionsFrosting = instructionsFrosting;
    }

    public String instructionsFrostingToString(){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        if(getTitle().contains("Buttercream")||getTitle().contains("ButterCream")||getTitle().contains("buttercream")){

            sb.append("<h3><b> ButterCream: </b></h3>");
            for(String list : instructionsFrosting){
                sb.append(i+". "+list+"<br>");
                i++;
            }
            return sb.toString();
        } else{
            sb.append("<h3><b> Frosting: </b></h3>");
            for(String list : instructionsFrosting){
                sb.append(i+". "+list+"<br>");
                i++;
            }
            return sb.toString();
        }
    }

    public String[] getInsturctionsExtra() {
        return instructionsExtra;
    }

    public void setInsturctionsExtra(String[] insturctionsExtra) {
        this.instructionsExtra = insturctionsExtra;
    }

    public String instructionsExtraToString(){
        StringBuilder sb = new StringBuilder();
        if(getInstructionsExtra().length>1){
            int i = 1;
            if(getTitle().contains("Coulis")){

                sb.append("<h3><b> Coulis: </b></h3>");

                for(String list : instructionsExtra){
                    sb.append(i+". "+list+"<br>");
                    i++;
                }
                return sb.toString();
            } else{
                sb.append("<h3><b> Extra: </b></h3>");
                for(String list : instructionsExtra){
                    sb.append(i+". "+list+"<br>");
                    i++;
                }

                return sb.toString();
            }

        } return "";
    }



    public void setId(int id) {
        this.id = id;
    }

    public double getRanking_count() {
        return ranking_count;
    }

    public void setRanking_count(double ranking_count) {
        this.ranking_count = ranking_count;
    }
}