package com.example.copyversion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyversion.DonorInfo;
import com.example.copyversion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {

    // Main-list item titles will be stored here
    private ArrayList<DonorInfo> FeedList;
    private Context context;
    final private ListItemClickListener mOnClickListener;

    // Parameterized constructor of this 
    // class to initialize tutorialList
    public FeedAdapter(ArrayList<DonorInfo> FeedList,ListItemClickListener listItemClickListener) {
        this.FeedList = FeedList;
        mOnClickListener=listItemClickListener;
    }

    // Attach the item layout with the proper xml file
    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo, parent, false);
        return new FeedHolder(view);
    }

    // It deals with the setting of different data and methods
    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position) {

        DonorInfo address = FeedList.get(position);
        holder.textView2.setText(address.getDonorMainCourse());
        holder.textView3.setText(address.getDonorAddress());
        holder.textView4.setText(address.getPeople());
        String s = (address.getFoodPhotoUrl());

        if (s != null) {
            Picasso.get().load(s).into(holder.imageView1);

        }
//        holder.textView2.setText(address);

    }


    // It returns the length of the RecyclerView
    @Override
    public int getItemCount() {
        return FeedList.size();
    }

    // The ViewHolder is a java class that stores 
    // the reference to the item layout views
    public class FeedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private ImageView imageView1;


        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.demo2);
            textView3 = itemView.findViewById(R.id.demo3);
            textView4 = itemView.findViewById(R.id.demo4);
            imageView1 = itemView.findViewById(R.id.show_photo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=this.getAdapterPosition();
//            DonorInfo donorInfo=FeedList.get(position);
//            Intent intent = new Intent(context, FullInfoOfPost.class);
//                intent.putExtra("hi", donorInfo);
//                context.startActivity(intent);
//            Toast.makeText(context,"position is",Toast.LENGTH_SHORT).show();
            mOnClickListener.onListItemClick(position);
        }
//
//        public void setData(DonorInfo donorInfo) {
//            this.textView3.setText(donorInfo.getDonorMainCourse());
//            this.textView2.setText(donorInfo.getDonorAddress());
//            this.textView4.setText(donorInfo.getPeople());
//            String s = (donorInfo.getFoodPhotoUrl());
//
//            if (s != null) {
//                Picasso.get().load(s).into(this.imageView1);
//
//            }
//
//
//        }

        // setText in Main-List title text

    }
    interface ListItemClickListener{
        void onListItemClick(int position);
    }
}