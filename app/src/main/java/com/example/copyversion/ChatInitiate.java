package com.example.copyversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copyversion.authentication.ui.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ChatInitiate extends AppCompatActivity implements FeedAdapterForEnterChat.ListItemClickListener {

    DatabaseReference databaseReference1, databaseReference2;
    String nameOfUser, profileImageUri;



    private ArrayList<ChatInitiateUtil> ListForChatting = new ArrayList<>();
    private ArrayList<ChatInitiateUtil> ListForChatting1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_initiate);

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String myUid = Auth.getCurrentUser().getUid();






        RecyclerView forChatRecyclerView = (RecyclerView) findViewById(R.id.EnterIntoChat);
        forChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ProgressDialog progressDialog
                = new ProgressDialog(ChatInitiate.this);

        progressDialog.show();
        final int[] x = new int[1];
        databaseReference1 = FirebaseDatabase.getInstance().getReference("/rotlo/chat/").getRef();
        databaseReference1.addChildEventListener(new ChildEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Set<ChatInitiateUtil> s = new LinkedHashSet<ChatInitiateUtil>();
                if (snapshot.exists()) {
                    ChatInitiateUtil chatInitiateUtil = new ChatInitiateUtil();


                    String chatMessage = snapshot.getKey();

                    String Path = "/rotlo/chat/" + chatMessage + "/";
                    int l = chatMessage.length();
                    String uidOfME = chatMessage.substring(0, chatMessage.indexOf("-"));

                    String uidOfOther = null;
                    if (uidOfME.equals(myUid)) {
                        uidOfOther = chatMessage.substring(chatMessage.indexOf("-") + 1, l);

                    }
                    DatabaseReference db1 = FirebaseDatabase.getInstance().getReference(Path).limitToFirst(1).getRef();


                    if (uidOfOther != null) {


                        chatInitiateUtil.setOtherUid(uidOfOther);

                        final int[] max = {0};
                        final String[] sd = new String[1];

                        String finalUidOfOther = uidOfOther;
                        String finalUidOfOther1 = uidOfOther;
                        db1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    for (DataSnapshot ds : snapshot.getChildren()) {
//                                                Toast.makeText(ChatInitiate.this,ds.getKey(),Toast.LENGTH_SHORT).show();
                                        int s = Integer.parseInt(ds.getKey());

                                        if (s > max[0]) {
                                            max[0] = s;

                                            sd[0] =ds.child("message").getValue(String.class);



                                        }

                                    }
//                                    Toast.makeText(ChatInitiate.this,String.valueOf(max[0]),Toast.LENGTH_SHORT).show();
                                    chatInitiateUtil.setTimeStamp(max[0]);
                                    chatInitiateUtil.setLastMessage(sd[0]);


//                                            databaseReferenced.child(finalUidOfOther).setValue(chatInitiateUtil);
//                                    DatabaseReference databaseReferenced=FirebaseDatabase.getInstance().getReference("/rotlo/kuchb").getRef();
//
//                                    databaseReferenced.push().setValue(chatInitiateUtil);
                                    ListForChatting.add(chatInitiateUtil);


                                    Collections.sort(ListForChatting, (o1, o2) -> (int) (o2.getTimeStamp() - o1.getTimeStamp()));
                                    forChatRecyclerView.setAdapter(new FeedAdapterForEnterChat(ListForChatting, ChatInitiate.this::onListItemClick, getApplicationContext()));
                                    progressDialog.dismiss();

                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





                    }


                } else {
                    Toast.makeText(ChatInitiate.this, "", Toast.LENGTH_SHORT).show();
                }





            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    @Override
    public void onResume() {
        super.onResume();

        // do not reload anymore, unless I tell you so...
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onListItemClick(int position) {

        ChatInitiateUtil x = ListForChatting.get(position);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("/rotlo/user").child(x.getOtherUid());

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    String nameOfUser = (snapshot.child("fullName").getValue(String.class));
                    String profileImageUri = snapshot.child("uri").getValue(String.class);

                    Intent intent = new Intent(ChatInitiate.this, ChatApplication.class);
                    intent.putExtra("Name", nameOfUser);
                    intent.putExtra("imagUrl", profileImageUri);
                    intent.putExtra("uidOther", x.getOtherUid());

                    startActivity(intent);






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }
}