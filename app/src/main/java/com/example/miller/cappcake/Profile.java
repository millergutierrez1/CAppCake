package com.example.miller.cappcake;

/**
 * Created by Miller on 12/6/2017.
 */

public class Profile {

    private String name;
    private String email;
    private String password;
    private String dateOfBirth;
    private String user;

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

        if (!name.equals(profile.name)) return false;
        if (!email.equals(profile.email)) return false;
        return dateOfBirth.equals(profile.dateOfBirth);

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
        return "Profile{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", user='" + user + '\'' +
                '}';
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
