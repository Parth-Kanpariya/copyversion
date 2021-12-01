package com.example.copyversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatApplication extends AppCompatActivity implements FeedAdapterForChat.ListItemClickListener,SwipeControllerActions {

    DatabaseReference databaseReference1,databaseReference2;
    RecyclerView chatListRecyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText typedMessage;
    String so=null;
    private int quotedMessagePos = -1;


    private ArrayList<ChatMessage> ListForChatting= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_application);
        getSupportActionBar().hide();












        Intent i = getIntent();

        String Name= (String) i.getSerializableExtra("Name");
        String imagUrl = (String) i.getSerializableExtra("imagUrl");
        String uidOfOther=(String)i.getSerializableExtra("uidOther");
        FirebaseAuth Auth= FirebaseAuth.getInstance();
        String myUid=Auth.getCurrentUser().getUid();
        String Path2=uidOfOther+"-"+myUid;
        String Path1=myUid+"-"+uidOfOther;




         chatListRecyclerView = (RecyclerView) findViewById(R.id.chatList);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference1 = FirebaseDatabase.getInstance().getReference("/rotlo/chat/"+Path1);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("/rotlo/chat/"+Path2);

        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("/rotlo/chat/");

        linearLayoutManager= (LinearLayoutManager) chatListRecyclerView.getLayoutManager();
        linearLayoutManager.setStackFromEnd(true);



        TextView name=findViewById(R.id.NameOfUserInChat);
        ImageView PhotoInChat=findViewById(R.id.chatProfilePhoto);
        name.setText(Name);
        if(imagUrl!=null) {
            Picasso.get().load(imagUrl).into(PhotoInChat);
        }
        else
        {
            imagUrl="https://www.nicepng.com/png/full/136-1366211_group-of-10-guys-login-user-icon-png.png";
            Picasso.get().load(imagUrl).into(PhotoInChat);
        }

        Button sendButton=findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typedMessage=findViewById(R.id.TypeMessage);
                String message=typedMessage.getText().toString();
                hideReplyLayout();
                if(message.length()!=0)
                {
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    ChatMessage chatMessage=new ChatMessage(message,myUid,uidOfOther,ts,so);




                    databaseReference1.child(ts).setValue(chatMessage);
                    databaseReference2.child(ts).setValue(chatMessage);

//                    databaseReference1.push().setValue(chatMessage);
//                    databaseReference2.push().setValue(chatMessage);
                    typedMessage.getText().clear();
                }




            }
        });

        ImageButton cancleButtonn=findViewById(R.id.cancelButton);
        cancleButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReplyLayout();
                so=null;
            }
        });

        databaseReference1 = FirebaseDatabase.getInstance().getReference("/rotlo/chat/"+Path1);

        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                        String chatMessage=snapshot.child("message").getValue(String.class);
                        String ID=snapshot.child("id").getValue(String.class);

                         String ts = snapshot.child("ts").getValue(String.class);
                         String s=snapshot.child("replay").getValue(String.class);
                        ListForChatting.add(new ChatMessage(chatMessage,ID,uidOfOther,ts,s));




                }else
                {
                    Toast.makeText(ChatApplication.this, "hi", Toast.LENGTH_SHORT).show();
                }
                chatListRecyclerView.setAdapter(new FeedAdapterForChat(ListForChatting,ChatApplication.this::onListItemClick,ChatApplication.this));


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

        setUpItemTouchHelper();

    }

    private void hideReplyLayout() {
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.GONE);
    }

    private void setUpItemTouchHelper() {




        MessageSwipeController messageSwipeController=new MessageSwipeController(this,

                new SwipeControllerActions() {
                    @Override
                    public void showReplyUI(int position) {
                        quotedMessagePos = position;

                        showQuotedMessage(ListForChatting.get(position));

                    }
                }

        );

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(messageSwipeController);

        itemTouchHelper.attachToRecyclerView(chatListRecyclerView);
    }

    private void showQuotedMessage(ChatMessage chatMessage) {
//        typedMessage.requestFocus();
//        InputMathodManager inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager?.showSoftInput(edit_message, InputMethodManager.SHOW_IMPLICIT)
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.VISIBLE);
        TextView textView=findViewById(R.id.txtQuotedMsg);
        textView.setText(chatMessage.getMessage());
        so=chatMessage.getMessage();
//        Toast.makeText(ChatApplication.this,chatMessage.getMessage(),Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onListItemClick(int position) {

    }

//    @Override
//    public void showReplyUI(int position) {
//
//    }
}