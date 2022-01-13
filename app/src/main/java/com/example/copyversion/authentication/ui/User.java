package com.example.copyversion.authentication.ui;

import android.net.Uri;
import android.widget.ImageView;

public class User {
    private String fullName;
    private String email;
    private String status;
    private String profileUri;

    public User(String fullName, String email,String profileUri) {
        this.fullName = fullName;
        this.email = email;
        this.profileUri=profileUri;
    }


    public User(){

    }
    public String getUri() {
        return profileUri;
    }

    public void setUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
