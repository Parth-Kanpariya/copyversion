package com.example.copyversion;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class FoodRaiseInfo  implements Serializable {

    private String RaiserName,People, RaiserAddress;
    private String Url;
    private String urlForPhoto;
    private String UID;
    private String postID;
    private String username;
    private String Contact;
    private String profilePhtourl;
    private boolean isClickable;
    private double longitude;
    private double latitude;
    private Date currentTime;

//

    FoodRaiseInfo ()
    {

    }

    public FoodRaiseInfo  (String RaiserName,String People,String RaiserAddress,String uploadUrl,String UID,String postID,double latitude,double longitude,String profilePhtourl,String username,Date currentTime,String Contact,boolean isClickable) {
        this.RaiserAddress=RaiserAddress;
        this.RaiserName=RaiserName;
        this.People=People;
        this.urlForPhoto=uploadUrl;
        this.UID=UID;
        this.postID=postID;
        this.longitude=longitude;
        this.latitude=latitude;
        this.profilePhtourl=profilePhtourl;
        this.username=username;
        this.currentTime=currentTime;
        this.Contact=Contact;
        this.isClickable=isClickable;


    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }




    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public void setPostID(String postID)
    {
        this.postID=postID;
    }
    public String getPostID() {
        return this.postID;
    }

    public void setUid(String uid) {this.UID=uid;}
    public String getUid() {return this.UID;}
    public void setFoodPhotoUrl(String url) {this.urlForPhoto=url;}

    public String getFoodPhotoUrl(){return urlForPhoto;}

    public String getRaiserName() {
        return RaiserName;
    }

    public void setRaiserName(String RaiserName) {
        this.RaiserName = RaiserName;
    }

    public String getPeople() {
        return People;
    }

    public void setPeople(String People) {
        this.People =People;
    }

    public String getRaiserAddress() {
        return RaiserAddress;
    }

    public void setRaiserAddress(String RaiserAddress) {
        this.RaiserAddress = RaiserAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhtourl() {
        return profilePhtourl;
    }

    public void setProfilePhtourl(String profilePhtourl) {
        this.profilePhtourl = profilePhtourl;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
