package com.example.copyversion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapterForEnterChat extends RecyclerView.Adapter<FeedAdapterForEnterChat.FeedHolder>  {
    private ArrayList<ChatInitiateUtil> FeedList;
    private Context context;
  private FeedAdapter.ListItemClickListener mOnItemClickListner;

    FeedAdapterForEnterChat(ArrayList<ChatInitiateUtil>FeedList, FeedAdapter.ListItemClickListener listItemClickListener, Context context){
        this.FeedList=FeedList;
        this.context=context;
        this.mOnItemClickListner=listItemClickListener;
    }





    @NonNull
    @Override
    public FeedAdapterForEnterChat.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatinitiatelayout, parent, false);
        return new FeedAdapterForEnterChat.FeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position) {

        ChatInitiateUtil chatInitiateUtil=FeedList.get(position);
        String uidOfOther=chatInitiateUtil.getOtherUid();
        String last=chatInitiateUtil.getLastMessage();
        if(last.length()>20)
        {
            last=last.substring(0,20);
        }



        DatabaseReference connectOther=FirebaseDatabase.getInstance().getReference("/rotlo/user").child(uidOfOther);
        String finalLast = last;
        connectOther.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String s=snapshot.child("status").getValue(String.class);
               if(s.equals("typing...."))
                {
                    holder.textViewChat.setText(s);
                }
               else
               {
                   holder.textViewChat.setText(finalLast);
               }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.textViewChat.setText(last);



        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(uidOfOther);

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    String nameOfUser = (snapshot.child("fullName").getValue(String.class));
                     String profileImageUri = snapshot.child("uri").getValue(String.class);


                    holder.textView.setText(nameOfUser);
                    if(profileImageUri!=null)
                    {
                        Picasso.get().load(profileImageUri).into(holder.imageView);
                    }else
                    {
                        profileImageUri="https://www.nicepng.com/png/full/136-1366211_group-of-10-guys-login-user-icon-png.png";
                        Picasso.get().load( profileImageUri).into(holder.imageView);

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









//        holder.recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                // then scroll to specific offset
//
//                    holder.recyclerView.scrollToPosition(10);
//
//            }
//        });




//        holder.recyclerView.smoothScrollToPosition(getItemCount()-1);

    }



    @Override
    public int getItemCount() {
        return FeedList.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView textView;
        private ImageView imageView;
        private TextView textViewChat;




        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profileImageChatInitiate);
            textView=itemView.findViewById(R.id.NameOfUserInChatInitiate);
            textViewChat=itemView.findViewById(R.id.chatUserInChatInitiate);


            itemView.setOnClickListener(this);





        }




        @RequiresApi(api = Build.VERSION_CODES.P)
        private void creatLink(String PostId,FoodRaiseInfo d) {
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
                    "link=https://www.example.com/?postid="+PostId+"?Raise"+
                    "&apn="+ context.getPackageName() +
                    "&st="+"Rotlo"+
                    "&si="+"https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/logo1.png?alt=media&token=d4c5f5a0-a684-459f-ab28-5eb6db842619";




            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(shareLink))
                    .setDomainUriPrefix("https://copyversion.page.link/")
                    // Set parameters
                    // ...
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

        @Override
        public void onClick(View v) {
            int position=this.getAdapterPosition();
            mOnItemClickListner.onListItemClick(position);

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


//

//

    }
    interface ListItemClickListener {
        void onListItemClick(int position);

    }

}
