package com.example.copyversion;

import java.sql.Time;

public class ChatMessage {
    String Message,ID,UidOfOther,ts,Replay;
    public ChatMessage(String Message,String ID,String myUid,String ts,String replay)
    {
        this.Message=Message;
        this.ID=ID;
        this.UidOfOther=myUid;
        this.ts=ts;
        this.Replay=replay;
    }


    public void setReplay(String replay) {
        Replay = replay;
    }

    public String getReplay() {
        return Replay;
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
