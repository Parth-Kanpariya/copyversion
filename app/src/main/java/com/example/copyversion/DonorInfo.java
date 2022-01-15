package com.example.copyversion;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class DonorInfo  implements Serializable {

    private String donorName, mainCourse, donorAddress;
    private String people;
    private String Url;
    private String urlForPhoto;
    private String uid;
    private String username;
    private String profilePhtourl;
    private String postID;
    private String Contact;
    private boolean isClickable;
    private double longitude;
    private double latitude;
    private Date currentTime;




    public DonorInfo(String donorName,String people,String mainCourse,String donorAddress,String uploadUrl,String uid,String postID,double latitude,double longitude,String profilePhtourl,String username,Date currentTime,String Contact,boolean isClickable) {
        this.donorAddress=donorAddress;
        this.donorName=donorName;
        this.people=people;
        this.mainCourse=mainCourse;
        this.urlForPhoto=uploadUrl;
        this.uid=uid;
        this.postID=postID;
        this.longitude=longitude;
        this.latitude=latitude;
        this.profilePhtourl=profilePhtourl;
        this.username=username;
        this.currentTime=currentTime;
        this.Contact=Contact;
        this.isClickable=isClickable;



//

    }

//    public DonorInfo(String donorName,String people,String mainCourse,String donorAddress,String uploadUrl,String switched) {
//        this.donorAddress=donorAddress;
//        this.donorName=donorName;
//        this.people=people;
//        this.mainCourse=mainCourse;
//        this.urlForPhoto=uploadUrl;
//        this.switched=switched;
//
//    }


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

    public void settUid(String uid) {this.uid=uid;}

    public void setPostID(String postID)
    {
        this.postID=postID;
    }
    public String getPostID() {
        return this.postID;
    }

    public String getUid() {return this.uid;}

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public DonorInfo(){

    }

    public void setFoodPhotoUrl(String url) {this.urlForPhoto=url;}

    public String getFoodPhotoUrl(){return urlForPhoto;}

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorMainCourse() {
        return mainCourse;
    }

    public void setDonorMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getPeople() {
        return people;
    }

    public void setDonorPeople(String people) {
        this.people = people;
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

    public String getContact() {
        return Contact;
    }
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
