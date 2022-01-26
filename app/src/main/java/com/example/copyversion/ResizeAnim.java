package com.example.copyversion;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ResizeAnim extends Animation {
    private int startHeight,targetHeight;
    private View view;
    ResizeAnim(View view, int startHeight, int targetHeight)
    {
        this.startHeight=startHeight;
        this.targetHeight=targetHeight;
        this.view=view;

    }
    @Override
    public void applyTransformation( float interpolatedTime, Transformation t) {
        if (startHeight == 0 || targetHeight == 0) {
//            view.setLayoutParams();
//            view.layoutParams.height =
//                    (startHeight + (targetHeight - startHeight) * interpolatedTime).toInt();
        } else {
//            view.layoutParams.height = (startHeight + targetHeight * interpolatedTime).toInt();
        }
        view.requestLayout();
    }
}
