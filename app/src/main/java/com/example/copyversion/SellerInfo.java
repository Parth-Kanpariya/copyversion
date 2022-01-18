package com.example.copyversion;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class SellerInfo  implements Serializable {

    private String sellerName,approximate, sellerAddress;
    private String Url;
    private String urlForPhoto;
    private String UID;
    private String postID;
    private String username;
    private String profilePhtourl;
    private String Contact;
    private boolean isClickable;
    private double longitude;
    private double latitude;
    private Date currentTime;
    private String nameOfFood;


//

    SellerInfo()
    {

    }

    public SellerInfo (String sellerName,String approximate,String sellerAddress,String uploadUrl,String UID,String postID,double latitude,double longitude,String profilePhtourl,String username,Date currentTime,String Contact,boolean isClickable,String nameOfFood) {
        this.sellerAddress=sellerAddress;
        this.sellerName=sellerName;
        this.approximate=approximate;
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
        this.nameOfFood=nameOfFood;


    }


    public String getNameOfFood() {
        return nameOfFood;
    }

    public void setNameOfFood(String nameOfFood) {
        this.nameOfFood = nameOfFood;
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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerApproximate() {
        return approximate;
    }

    public void setSellerApproximate(String approximate) {
        this.approximate = approximate;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
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
