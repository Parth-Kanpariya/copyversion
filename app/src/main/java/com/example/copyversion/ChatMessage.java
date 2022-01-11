package com.example.copyversion;

import com.google.firebase.messaging.Constants;

import java.sql.Time;
import java.util.Date;

public class ChatMessage {
    String Message,ID,UidOfOther,ts,nameOfOther,PostId,ImageUrl;
    String date;
    String quote="";
    int timeLabelId;
    int quotePos=-1,type;
    public ChatMessage(String Message,String ID,String myUid,String ts,String quote,int quotePos,String nameOfOther)
    {
        this.Message=Message;
        this.ID=ID;
        this.UidOfOther=myUid;
        this.ts=ts;
        this.quote=quote;
        this.quotePos=quotePos;
        this.nameOfOther=nameOfOther;


    }

    public ChatMessage(String Message,String ID,String myUid,String ts,String quote,int quotePos,int type,String nameOfOther,String PostId,String ImageUrl,String date,int timeLabelId)
    {
        this.Message=Message;
        this.ID=ID;
        this.UidOfOther=myUid;
        this.ts=ts;
        this.quote=quote;
        this.quotePos=quotePos;
        this.type=type;
        this.nameOfOther=nameOfOther;
        this.PostId=PostId;
        this.ImageUrl=ImageUrl;
        this.date=date;
        this.timeLabelId=timeLabelId;

    }
    public ChatMessage(String Message,String ID,String myUid,String ts,int quotePos,String nameOfOther)
    {
        this.Message=Message;
        this.ID=ID;
        this.UidOfOther=myUid;
        this.ts=ts;
        this.quotePos=quotePos;
        this.nameOfOther=nameOfOther;

    }

    public void setTimeLabelId(int timeLabelId) {
        this.timeLabelId = timeLabelId;
    }

    public int getTimeLabelId() {
        return timeLabelId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setNameOfOther(String nameOfOther) {
        this.nameOfOther = nameOfOther;
    }

    public String getNameOfOther() {
        return nameOfOther;
    }

    public void setTypeOfMessage(int type) {
        this.type = type;
    }

    public int getTypeOfMessage() {
        return type;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
    public String getQuote() {
        return quote;
    }



    public void setQuotePos(int quotePos) {
        this.quotePos = quotePos;
    }
    public int getQuotePos() {
        return quotePos;
    }



    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTs() {
        return ts;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getID() {
        return ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setOtherUid(String myUid) {
        this.UidOfOther= myUid;
    }

    public String getOtherUid() {
        return UidOfOther;
    }


}
