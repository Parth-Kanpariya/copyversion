package com.example.copyversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class InfoAdapter extends ArrayAdapter<com.example.copyversion.DonorInfo>{

    public InfoAdapter(@NonNull Context context, ArrayList<com.example.copyversion.DonorInfo> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.demo, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        com.example.copyversion.DonorInfo currentNumberPosition = getItem(position);



        // then according to the position of the view assign the desired TextView 1 for the same
//        TextView textView1 = currentItemView.findViewById(R.id.demo1);
//        textView1.setText(currentNumberPosition.getDonorName());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.demo2);
        textView2.setText(currentNumberPosition.getDonorAddress());

        // then according to the position of the view assign the desired TextView 3 for the same
        TextView textView3 = currentItemView.findViewById(R.id.demo3);
        textView3.setText(currentNumberPosition.getDonorMainCourse());

        // then according to the position of the view assign the desired TextView 4 for the same
        TextView textView4 = currentItemView.findViewById(R.id.demo4);
        textView4.setText(currentNumberPosition.getPeople());


        ImageView imageView1 = currentItemView.findViewById(R.id.show_photo);
        String s= (currentNumberPosition.getFoodPhotoUrl());

        if(s!=null)
        {
            Picasso.get().load(s).into(imageView1);

        }






        // then return the recyclable view
        return currentItemView;
    }


}