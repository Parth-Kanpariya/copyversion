package com.example.copyversion;

import android.graphics.Bitmap;

import java.io.Serializable;

public class DonorInfo  implements Serializable {

    private String donorName, mainCourse, donorAddress;
    private String people;
    private String Url;
    private String urlForPhoto;
//

    public DonorInfo(String donorName,String people,String mainCourse,String donorAddress,String uploadUrl) {
        this.donorAddress=donorAddress;
        this.donorName=donorName;
        this.people=people;
        this.mainCourse=mainCourse;
        this.urlForPhoto=uploadUrl;

//

    }

    public DonorInfo(String donorName,String people,String mainCourse,String donorAddress) {
        this.donorAddress=donorAddress;
        this.donorName=donorName;
        this.people=people;
        this.mainCourse=mainCourse;


//

    }

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

}
