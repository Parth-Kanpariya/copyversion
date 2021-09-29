package com.example.copyversion;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SellerInfo  implements Serializable {

    private String sellerName,approximate, sellerAddress;
    private String Url;
    private String urlForPhoto;

//

    SellerInfo()
    {

    }

    public SellerInfo (String sellerName,String approximate,String sellerAddress,String uploadUrl) {
        this.sellerAddress=sellerAddress;
        this.sellerName=sellerName;
        this.approximate=approximate;
        this.urlForPhoto=uploadUrl;


    }






    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }



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



}
