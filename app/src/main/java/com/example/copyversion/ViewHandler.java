package com.example.copyversion;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.logging.LogRecord;

public class ViewHandler extends Handler {

    private View myView;
    ViewHandler(View view)
    {
        this.myView=view;
    }


     @Override
    public void handleMessage(Message msg) {
        myView.setVisibility(View.GONE);
    }

}
