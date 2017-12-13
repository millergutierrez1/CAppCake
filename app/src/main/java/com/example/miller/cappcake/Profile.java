package com.example.miller.cappcake;

import com.google.gson.Gson;

/**
 * Created by Miller on 12/6/2017.
 */

public class Profile {

    private String name;
    private String email;
    private String password;
    private String dateOfBirth;
    private String user;
    private int id;

    public Profile() {
    }

    public Profile(String name, String email, String password, String dateOfBirth, String user) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String   getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;

        Profile profile = (Profile) o;

        if (name != null ? !name.equals(profile.name) : profile.name != null) return false;
        return email != null ? email.equals(profile.email) : profile.email == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        //Serialization.
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
