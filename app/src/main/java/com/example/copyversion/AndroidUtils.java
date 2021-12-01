package com.example.copyversion;

import android.content.Context;

public class AndroidUtils
{
    private float density=1f;
    public AndroidUtils()
    {


    }


    public int dp(float value, Context context) {
        if (density == 1f) {
            checkDisplaySize(context);
        }
        if (value == 0f) {
            return 0;

        } else return Integer.valueOf((int) Math.ceil(Double.valueOf(density * value)));
    }

    private void checkDisplaySize(Context context)
        {
            try {
                density = context.getResources().getDisplayMetrics().density;
            } catch (Exception e) {
            e.printStackTrace();
        }
        }



    }

