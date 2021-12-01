package com.example.copyversion;

public class ChatInitiateUtil {
    private String uid,profileUrl,Name,lastMessage;
    private int timeStamp;
    ChatInitiateUtil()
    {

    }
   public ChatInitiateUtil(String profileUrl,String name,int timestamp,String uid)
    {
        this.Name=name;
        this.profileUrl=profileUrl;
        this.uid=uid;
        this.timeStamp=timestamp;


//        this.uid=uid;

    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public String getInitiateName() {
        return Name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getOtherUid() {
        return uid;
    }

    public void setInitiateName(String name) {
        Name = name;
    }

    public void setOtherUid(String uid) {
        this.uid = uid;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
