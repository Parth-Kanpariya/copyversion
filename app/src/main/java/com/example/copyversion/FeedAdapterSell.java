package com.example.copyversion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyversion.DonorInfo;
import com.example.copyversion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedAdapterSell extends RecyclerView.Adapter<FeedAdapterSell.FeedHolder> {

    // Main-list item titles will be stored here
    private ArrayList<SellerInfo> FeedList;
    private Context context=new MainActivity();
    final private ListItemClickListener mOnClickListener;
    private FirebaseAuth auth;
    private MainActivity mainActivity;

    // Parameterized constructor of this
    // class to initialize tutorialList
    public FeedAdapterSell(ArrayList<SellerInfo> FeedList,ListItemClickListener listItemClickListener,Context context) {
        this.FeedList = FeedList;
        mOnClickListener=listItemClickListener;
        this.context=context;

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

        SellerInfo address = FeedList.get(position);
        holder.textView2.setText(address.getSellerApproximate());
        holder.textView3.setText(address.getSellerAddress());
        holder.textView4.setText(address.getSellerName());
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("ObjectSeller", address);
                if(address.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
//                    Navigation.findNavController((Activity) context, R.id.nav_host_fragment).navigate(R.id.action_my_post_to_profileOfOtherUser,bundle);

                }else if(!address.isClickable())
                {

                }else
                {
                    Navigation.findNavController((Activity) context, R.id.nav_host_fragment).navigate(R.id.action_homePager_fragment_to_profileOfOtherUser,bundle);

                }


            }
        });

        Date createdAt = address.getCurrentTime();//get the date the message was created from parse backend
        long now = new Date().getTime();//get current date
        String convertedDate = DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(), now, DateUtils.SECOND_IN_MILLIS).toString();
        holder.timeForPost.setText(convertedDate);
        auth= FirebaseAuth.getInstance();

//        if(address.getUid().equals(auth.getUid())) {
//            holder.imageViewforKebap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
//                    MenuInflater inflater = popupMenu.getMenuInflater();
//                    inflater.inflate(R.menu.postmenu, popupMenu.getMenu());
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            if (item.getItemId() == R.id.menuDeletePost) {
//                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling").child(address.getPostID());
//                                rootRef.removeValue();
//                                return true;
//                            }
//                            return false;
//                        }
//                    });
//
//                    popupMenu.show();
//
//
//                }
//            });
//        }


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(address.getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String nameForPost =(snapshot.child("fullName").getValue(String.class));
                holder.username.setText(nameForPost);

                 String ProfilePhotoUrl=(snapshot.child("uri")).getValue(String.class);

                if (ProfilePhotoUrl != null) {
                    Picasso.get().load(ProfilePhotoUrl).into(holder.userProfile);
//            Picasso.get().load(sl).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
//                    .into(holder.userProfile);
                }



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mDatabase.addListenerForSingleValueEvent(eventListener);





        String s = (address.getFoodPhotoUrl());


        if (s != null) {
//            Picasso.get().load(s).into(holder.imageView1);
            Picasso.get().load(s).resize(2048, 1600).onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                    .into(holder.imageView1);

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
    public class FeedHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private TextView textView2;
        private TextView textView3;
        private TextView textView4,username;
        private TextView timeForPost;
        private ImageView imageView1,userProfile;
        private ImageButton imageViewforKebap;


        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.demo2);
            textView3 = itemView.findViewById(R.id.demo3);
            textView4 = itemView.findViewById(R.id.demo4);
            imageView1 = itemView.findViewById(R.id.show_photo);
            userProfile=itemView.findViewById(R.id.profilePhotoInPost);
            username=itemView.findViewById(R.id.UserNameInPost);
            timeForPost=itemView.findViewById(R.id.TimeFromPostCreate);
            Button button1 = itemView.findViewById(R.id.more_vertt);
            button1.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onClick(View v) {

                    int position1=getAbsoluteAdapterPosition();
//                    Toast.makeText(context,""+position1,Toast.LENGTH_SHORT).show();

                    SellerInfo d=FeedList.get(position1);
                    creatLink(d.getPostID(),d);
                }
            });




            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=this.getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
        @Override
        public boolean onLongClick(View v) {

            int position = getAbsoluteAdapterPosition();
            SellerInfo info = FeedList.get(position);

            if (info.getUid().equals(auth.getUid())) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Rotlo");
                alert.setMessage("Do you want to delete the post?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(itemView.getContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
                        FeedList.remove(position);
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/rotlo/post/selling").child(info.getPostID());
                        rootRef.removeValue();
                        notifyItemRemoved(position);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();


            }


            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        private void creatLink(String PostId,SellerInfo d) {
//            Toast.makeText(context,"hii",Toast.LENGTH_SHORT).show();



            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.example.com/"))

                    .setDomainUriPrefix("https://copyversion.page.link/")

                    // Open links with this app on Android
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    // Open links with com.example.ios on iOS
//                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                    .buildDynamicLink();

            Uri dynamicLinkUri = dynamicLink.getUri();

            String shareLink="https://copyversion.page.link/?"+
                    "link=https://www.example.com/?postid="+PostId+"?Seller"+
                    "&apn="+ context.getPackageName() +
                    "&st="+"Rotlo"+
                    "&si="+"https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/logo1.png?alt=media&token=d4c5f5a0-a684-459f-ab28-5eb6db842619";




            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(shareLink))

                    .setDomainUriPrefix("https://copyversion.page.link/")
                    // Set parameters
                    // ...
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setImageUrl(Uri.parse(d.getFoodPhotoUrl()))
                                    .build())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(context.getMainExecutor(), new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Intent intent=new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                                intent.setType("text/plain");
                                context.startActivity(intent);
                            } else {
                                // Error
                                // ...
                            }
                        }
                    });

        }



//
//

    }
    interface ListItemClickListener{
        void onListItemClick(int position);
    }
}