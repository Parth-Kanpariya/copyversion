package com.example.copyversion;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedAdapterForChat extends RecyclerView.Adapter<FeedAdapterForChat.FeedHolder> {
    private ArrayList<ChatMessage> FeedList;
    private Context context;
    private FeedAdapter.ListItemClickListener mOnItemClickListner;

    FeedAdapterForChat(ArrayList<ChatMessage> FeedList, FeedAdapter.ListItemClickListener listItemClickListener, Context context) {
        this.FeedList = FeedList;
        this.context = context;
        this.mOnItemClickListner = listItemClickListener;
    }


    @NonNull
    @Override
    public FeedAdapterForChat.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_phermo, parent, false);

        return new FeedAdapterForChat.FeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position) {


        ChatMessage address = FeedList.get(position);
        holder.messageView.setText(address.getMessage());
        Long Timestamp = Long.parseLong(address.getTs());
        Date timeD = new Date(Timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

        String Time = sdf.format(timeD);


        holder.timestamp.setText(Time);
        String uid = address.getID();
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String myUid = Auth.getCurrentUser().getUid();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//        holder.recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                // then scroll to specific offset
//
//                    holder.recyclerView.scrollToPosition(10);
//
//            }
//        });

        if (address.getReplay() == null) {

            holder.materialCardView.setVisibility(View.GONE);
        } else {
            holder.textViewForReplay.setText(address.getReplay());
            holder.materialCardView.setVisibility(View.VISIBLE);
        }


        if (uid.equals(myUid)) {


            holder.linearLayout.setGravity(Gravity.END);


        } else {


//
            holder.linearLayout.setGravity(Gravity.START);


        }

//        holder.recyclerView.smoothScrollToPosition(getItemCount()-1);

    }


    @Override
    public int getItemCount() {
        return FeedList.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView messageView, timestamp;
        MaterialCardView materialCardView;
        TextView textViewForReplay;


        private MaterialCardView m;
        private LinearLayout linearLayout;
        private RecyclerView recyclerView;


        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.messageOfChat);
            timestamp = itemView.findViewById(R.id.timestamp);
            materialCardView = itemView.findViewById(R.id.replied);
            textViewForReplay = itemView.findViewById(R.id.replied_Method);


            linearLayout = itemView.findViewById(R.id.chatLayout);
            recyclerView = itemView.findViewById(R.id.chatList);


            itemView.setOnLongClickListener(this);


        }


        @RequiresApi(api = Build.VERSION_CODES.P)
        private void creatLink(String PostId, FoodRaiseInfo d) {
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

            String shareLink = "https://copyversion.page.link/?" +
                    "link=https://www.example.com/?postid=" + PostId + "?Raise" +
                    "&apn=" + context.getPackageName() +
                    "&st=" + "Rotlo" +
                    "&si=" + "https://firebasestorage.googleapis.com/v0/b/copyversion-b749a.appspot.com/o/logo1.png?alt=media&token=d4c5f5a0-a684-459f-ab28-5eb6db842619";


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
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
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

        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAbsoluteAdapterPosition();


            ChatMessage info = FeedList.get(position);
            if (info.getID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Rotlo");
                alert.setMessage("Do you want to delete the message?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FeedList.remove(position);
                        final DatabaseReference[] rootRef = {FirebaseDatabase.getInstance().getReference("/rotlo/chat/" + info.getID() + "-" + info.getOtherUid())};
                        final DatabaseReference[] rootRef1 = new DatabaseReference[1];
                        final DatabaseReference[] rootRefOther = {FirebaseDatabase.getInstance().getReference("/rotlo/chat/" + info.getOtherUid() + "-" + info.getID())};
                        final DatabaseReference[] rootRefOther1 = new DatabaseReference[1];
                        rootRef[0].addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {

                                        if (info.getMessage().equals(ds.child("message").getValue(String.class))) {
                                            rootRef1[0] = ds.getRef();
                                            rootRef1[0].removeValue();
                                            notifyItemRemoved(position);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        rootRefOther[0].addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {

                                        if (info.getMessage().equals(ds.child("message").getValue(String.class))) {
                                            rootRefOther1[0] = ds.getRef();
                                            rootRefOther1[0].removeValue();
                                            notifyItemRemoved(position);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


//                        rootRef.removeValue();
//                        notifyItemRemoved(position);
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


//
//

    }

    interface ListItemClickListener {
        void onListItemClick(int position);

    }
}
