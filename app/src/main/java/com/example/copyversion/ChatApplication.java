package com.example.copyversion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatApplication extends AppCompatActivity implements FeedAdapterForChat.ListItemClickListener,SwipeControllerActions,FeedAdapterForChat.QuoteClickListener {

    DatabaseReference databaseReference1,databaseReference2;
    DatabaseReference connectOther;
    RecyclerView chatListRecyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText typedMessage;
    DatabaseReference status1,connect1;
    String so=null;
    private ImageButton mapButton;
    private int quotedMessagePos = -1;
    final long ANIMATION_DURATION= 300;
    ConstraintLayout replyLayout;
    TextView textView,statusOfUser;
    String PostId=null,ImageUrl=null;


    private ArrayList<ChatMessage> ListForChatting= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_application);
        getSupportActionBar().hide();

        replyLayout=findViewById(R.id.reply_layout);
        textView=findViewById(R.id.textQuotedMessage);
        statusOfUser=findViewById(R.id.StatusOfUser);
//        mapButton=findViewById(R.id.map);
//
//
//
//        mapButton.setVisibility(View.GONE);
//           mapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Uri gmmIntentUri = Uri.parse("geo:22.2106385,72.8818069");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
//
//            }
//        });


















        Intent i = getIntent();

        String Name= (String) i.getSerializableExtra("Name");
        String imagUrl = (String) i.getSerializableExtra("imagUrl");
        String uidOfOther=(String)i.getSerializableExtra("uidOther");
        DonorInfo donorInfo= (DonorInfo) i.getSerializableExtra("object1");
        SellerInfo sellerInfo= (SellerInfo) i.getSerializableExtra("object1seller");
        FoodRaiseInfo foodRaiseInfo= (FoodRaiseInfo) i.getSerializableExtra("object1foodraise");
        FirebaseAuth Auth= FirebaseAuth.getInstance();
        String myUid=Auth.getCurrentUser().getUid();
        String Path2=uidOfOther+"-"+myUid;
        String Path1=myUid+"-"+uidOfOther;

        if(donorInfo!=null)
        {
            showQuotedMessageAsStatus(donorInfo);
        }
        if(sellerInfo!=null)
        {
            showQuotedMessageAsSellStatus(sellerInfo);
        }
        if(foodRaiseInfo!=null)
        {
            showQuotedMessageAsRaiseFoodStatus(foodRaiseInfo);
        }


        status1=FirebaseDatabase.getInstance().getReference("/rotlo/user").child(myUid);
        connect1=FirebaseDatabase.getInstance().getReference(".info/connected");
        connect1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected=snapshot.getValue(Boolean.class);
                if(connected)
                {
                    status1.child("status").setValue("online");

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    status1.child("status").onDisconnect().setValue(ts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        connectOther=FirebaseDatabase.getInstance().getReference("/rotlo/user").child(uidOfOther);
        connectOther.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String s=snapshot.child("status").getValue(String.class);
                if(s.equals("online"))
                {
                    statusOfUser.setText(s);

                }
                else if(s.equals("typing...."))
                {
                    statusOfUser.setText(s);
                }
                 else
                {
                    statusOfUser.setText("");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        typedMessage=findViewById(R.id.edit_message);

        typedMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {



                status1=FirebaseDatabase.getInstance().getReference("/rotlo/user").child(myUid);
                connect1=FirebaseDatabase.getInstance().getReference(".info/connected");
                connect1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean connected=snapshot.getValue(Boolean.class);
                        if(connected)
                        {

                            if(!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1)
                            { status1.child("status").setValue("typing....");

                            }
                            else if(s.toString().trim().length() == 0)
                            {
                                status1.child("status").setValue("online");

                            }

                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            status1.child("status").onDisconnect().setValue(ts);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
//
//








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


        Long tsLong = System.currentTimeMillis();
        Long tsLongForDateLabelKey=System.currentTimeMillis();
        String tsForDateKey=tsLongForDateLabelKey.toString();
        String ts = tsLong.toString();
        String quote=textView.getText().toString();

        Calendar now = Calendar.getInstance();






        Calendar smsTime = Calendar.getInstance();
//        smsTime.setTimeInMillis(Long.parseLong(ListForChatting.get(ListForChatting.size()-1).getTs()));
        Date datee=new Date();
          datee.setTime(System.currentTimeMillis());
        String formattedDatee=new SimpleDateFormat("EEE, MMM d, ''yy").format(datee);


        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        ChatMessage chatMessageTime=new ChatMessage("",myUid,uidOfOther,ts,quote,quotedMessagePos,Name);
        chatMessageTime.setPostId(PostId);




        ImageButton sendButton=findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typedMessage=findViewById(R.id.edit_message);
                String message=typedMessage.getText().toString();


                if(message.length()!=0)
                {
                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();
                    String quote=textView.getText().toString();
                    Long tsLongForTimeStamp=System.currentTimeMillis()/1000;
                    String tsLongForTimeStampString=tsLongForTimeStamp.toString();

                    String dateOfMessage = null;








                    if(replyLayout.getVisibility()==View.VISIBLE)
                    {
                        hideReplyLayout();

                        ChatMessage chatMessage=new ChatMessage(message,myUid,uidOfOther,tsLongForTimeStampString,quote,quotedMessagePos,Name);
                        chatMessage.setImageUrl(ImageUrl);
                        chatMessage.setPostId(PostId);
                        chatMessage.setDate(formattedDatee);
                        if(PostId!=null && ImageUrl!=null)
                        {

                            if(ListForChatting.size()!=0)
                            { dateOfMessage=ListForChatting.get(ListForChatting.size()-1).getDate().toString();}

//                            Toast.makeText(ChatApplication.this, ""+dateOfMessage, Toast.LENGTH_SHORT).show();
                            if (ListForChatting.size()==0) {


                                chatMessageTime.setMessage(formattedDatee);
                                chatMessageTime.setTypeOfMessage(-90);
                                chatMessageTime.setDate(formattedDatee);
                                chatMessageTime.setTimeLabelId(000001);
                                databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                                databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }else if(!dateOfMessage.equals(formattedDatee))
                            {

                                chatMessageTime.setMessage(formattedDatee);
                                chatMessageTime.setTypeOfMessage(-90);
                                chatMessageTime.setDate(formattedDatee);
                                chatMessageTime.setTimeLabelId(000001);
                                databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                                databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }



                            chatMessage.setTypeOfMessage(-3);
                            databaseReference1.child(ts).setValue(chatMessage);
                            chatMessage.setTypeOfMessage(-4);
                            databaseReference2.child(ts).setValue(chatMessage);

                        }else
                        {



                            if(ListForChatting.size()!=0)
                            { dateOfMessage=ListForChatting.get(ListForChatting.size()-1).getDate().toString();}

//                            Toast.makeText(ChatApplication.this, ""+dateOfMessage, Toast.LENGTH_SHORT).show();
                            if (ListForChatting.size()==0) {


                                chatMessageTime.setMessage(formattedDatee);
                                chatMessageTime.setTypeOfMessage(-90);
                                chatMessageTime.setTimeLabelId(000001);
                                chatMessageTime.setDate(formattedDatee);
                                databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                                databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }else if(!dateOfMessage.equals(formattedDatee))
                            {

                                chatMessageTime.setMessage(formattedDatee);
                                chatMessageTime.setTypeOfMessage(-90);
                                chatMessageTime.setTimeLabelId(000001);
                                chatMessageTime.setDate(formattedDatee);
                                databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                                databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }



                            chatMessage.setTypeOfMessage(1);
                            databaseReference1.child(ts).setValue(chatMessage);
                            chatMessage.setTypeOfMessage(2);
                            databaseReference2.child(ts).setValue(chatMessage);
                        }




                    }else
                    {
                        hideReplyLayout();

                        if(ListForChatting.size()!=0)
                        { dateOfMessage=ListForChatting.get(ListForChatting.size()-1).getDate().toString();}

//                        Toast.makeText(ChatApplication.this, ""+dateOfMessage, Toast.LENGTH_SHORT).show();
                        if (ListForChatting.size()==0) {


                            chatMessageTime.setMessage(formattedDatee);
                            chatMessageTime.setTypeOfMessage(-90);
                            chatMessageTime.setTimeLabelId(000001);
                            chatMessageTime.setDate(formattedDatee);
                            databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                            databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }else if(!dateOfMessage.equals(formattedDatee))
                        {

                            chatMessageTime.setMessage(formattedDatee);
                            chatMessageTime.setTypeOfMessage(-90);
                            chatMessageTime.setTimeLabelId(000001);
                            chatMessageTime.setDate(formattedDatee);
                            databaseReference1.child(tsForDateKey).setValue(chatMessageTime);
                            databaseReference2.child(tsForDateKey).setValue(chatMessageTime);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        tsLong=System.currentTimeMillis();
                        ts=tsLong.toString();
                        ChatMessage chatMessage=new ChatMessage(message,myUid,uidOfOther,tsLongForTimeStampString,quotedMessagePos,Name);
                        chatMessage.setImageUrl(ImageUrl);
                        chatMessage.setPostId(PostId);
                        chatMessage.setDate(formattedDatee);
                        chatMessage.setTypeOfMessage(1);
                        databaseReference1.child(ts).setValue(chatMessage);
                        chatMessage.setTypeOfMessage(2);
                        databaseReference2.child(ts).setValue(chatMessage);
                    }






//                    databaseReference1.push().setValue(chatMessage);
//                    databaseReference2.push().setValue(chatMessage);
                    typedMessage.getText().clear();
                }
                hideReplyLayout();
                quotedMessagePos=-1;
                PostId=null;



            }
        });




        ImageButton cancleButtonn=findViewById(R.id.cancelButton);
        cancleButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReplyLayout();
                PostId=null;
                quotedMessagePos=-1;
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

                    String quote = snapshot.child("quote").getValue(String.class);
                    String nameOfOther = snapshot.child("nameOfOther").getValue(String.class);
                    String PostId=snapshot.child("postId").getValue(String.class);
                    String imageOfPost=snapshot.child("imageUrl").getValue(String.class);
                    String dateOfMessage=snapshot.child("date").getValue(String.class);
                    int quotePose=snapshot.child("quotePos").getValue(Integer.class);
                    int type=snapshot.child("typeOfMessage").getValue(Integer.class);
                    int timeLabelId=snapshot.child("timeLabelId").getValue(Integer.class);
                        ListForChatting.add(new ChatMessage(chatMessage,ID,uidOfOther,ts,quote,quotePose,type,nameOfOther,PostId,imageOfPost,dateOfMessage,timeLabelId));




                }else
                {

                }
                chatListRecyclerView.setAdapter(new FeedAdapterForChat(ListForChatting,ChatApplication.this::onListItemClick,ChatApplication.this::onQuoteClick,ChatApplication.this));


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


//        if(ListForChatting.size()!=0)
//        {
//            Long tsLong = System.currentTimeMillis()/1000;
//            String ts = tsLong.toString();
//            String quote=textView.getText().toString();
//
//            Toast.makeText(ChatApplication.this,"time",Toast.LENGTH_SHORT).show();
//            Calendar smsTime = Calendar.getInstance();
//            smsTime.setTimeInMillis(Long.parseLong(ListForChatting.get(ListForChatting.size()-1).getTs()));
//
//            Calendar now = Calendar.getInstance();
//
//            final String timeFormatString = "h:mm aa";
//            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
//            final long HOURS = 60 * 60 * 60;
//            ChatMessage chatMessageTime=new ChatMessage("",myUid,uidOfOther,ts,quote,quotedMessagePos,Name);
//            chatMessageTime.setPostId(PostId);
//            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
//                Toast.makeText(ChatApplication.this, "fcgfgh", Toast.LENGTH_SHORT).show();
//                chatMessageTime.setMessage("Today " + DateFormat.format(timeFormatString, smsTime));
//                chatMessageTime.setTypeOfMessage(-90);
////                            databaseReference1.child(ts+"1").setValue(chatMessageTime);
////                            databaseReference2.child(ts+"1").setValue(chatMessageTime);
//
//
//            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
//                Toast.makeText(ChatApplication.this, "4", Toast.LENGTH_SHORT).show();
//                chatMessageTime.setTypeOfMessage(-90);
//                chatMessageTime.setMessage("Yesterday " + DateFormat.format(timeFormatString, smsTime));
////                            databaseReference1.child(ts).setValue(chatMessageTime);
////                            databaseReference2.child(ts).setValue(chatMessageTime);
//
//
//            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
//                Toast.makeText(ChatApplication.this, "3", Toast.LENGTH_SHORT).show();
//                chatMessageTime.setMessage(DateFormat.format(dateTimeFormatString, smsTime).toString());
//                chatMessageTime.setTypeOfMessage(-90);
////                            databaseReference1.child(ts).setValue(chatMessageTime);
////                            databaseReference2.child(ts).setValue(chatMessageTime);
//
//
//            } else {
//                Toast.makeText(ChatApplication.this, "fjhbjhghjgyjgjhgjhgjhgjh", Toast.LENGTH_SHORT).show();
//                chatMessageTime.setMessage(DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString());
//                chatMessageTime.setTypeOfMessage(-90);
//                databaseReference1.child(ts).setValue(chatMessageTime);
//                databaseReference2.child(ts).setValue(chatMessageTime);
//
//            }
//
//
//
//        }


        setUpItemTouchHelper();

    }

    private void hideReplyLayout() {
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        ImageView i=findViewById(R.id.replyPhoto);
        ResizeAnim resizeAnim=new ResizeAnim(constraintLayout,4,0);
        resizeAnim.setDuration(ANIMATION_DURATION);

        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        constraintLayout.requestLayout();
                        constraintLayout.forceLayout();
                        constraintLayout.setVisibility(View.GONE);
                        i.setVisibility(View.GONE);
                    }
                },ANIMATION_DURATION-50);

        constraintLayout.startAnimation(resizeAnim);
        resizeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
                params.height = 0;
                constraintLayout.setLayoutParams(params);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




        constraintLayout.setVisibility(View.GONE);
    }

    private void setUpItemTouchHelper() {




        MessageSwipeController messageSwipeController=new MessageSwipeController(chatListRecyclerView,this,

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

    private void showQuotedMessageAsStatus(DonorInfo donorInfo)
    {
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.VISIBLE);


        textView.setText("MainCourse=> "+donorInfo.getDonorMainCourse());

        ImageView i=findViewById(R.id.replyPhoto);
        i.getLayoutParams().height = 100;
        ImageUrl=donorInfo.getFoodPhotoUrl();
        Picasso.get().load(donorInfo.getFoodPhotoUrl()).into(i);
        quotedMessagePos=-3;
        PostId=donorInfo.getPostID();
    }

    private void showQuotedMessageAsSellStatus(SellerInfo sellerInfo)
    {
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.VISIBLE);


        textView.setText("Seller Name=> "+sellerInfo.getSellerName());

        ImageView i=findViewById(R.id.replyPhoto);
        i.getLayoutParams().height = 100;
        ImageUrl=sellerInfo.getFoodPhotoUrl();
        Picasso.get().load(sellerInfo.getFoodPhotoUrl()).into(i);
        quotedMessagePos=-33;
        PostId=sellerInfo.getPostID();
    }

    private void showQuotedMessageAsRaiseFoodStatus(FoodRaiseInfo foodRaiseInfo)
    {
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.VISIBLE);


        textView.setText("Raiser Name=> "+foodRaiseInfo.getRaiserName());

        ImageView i=findViewById(R.id.replyPhoto);
        i.getLayoutParams().height = 100;
        ImageUrl=foodRaiseInfo.getFoodPhotoUrl();
        Picasso.get().load(foodRaiseInfo.getFoodPhotoUrl()).into(i);
        quotedMessagePos=-333;
        PostId=foodRaiseInfo.getPostID();
    }

    private void showQuotedMessage(ChatMessage chatMessage) {
//        typedMessage.requestFocus();
//        InputMathodManager inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager?.showSoftInput(edit_message, InputMethodManager.SHOW_IMPLICIT)
        ConstraintLayout constraintLayout=findViewById(R.id.reply_layout);
        constraintLayout.setVisibility(View.VISIBLE);
        ImageView i=findViewById(R.id.replyPhoto);
        i.setVisibility(View.GONE);


        textView.setText(chatMessage.getMessage());
        int height=textView.getLineHeight();
        int startHeight=0;
//        if (height != startHeight) {
//
//            if (constraintLayout.getVisibility() == View.GONE)
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        constraintLayout.setVisibility(View.VISIBLE);
//                    }
//                },50);
//
//
//
//            int targetHeight = height - startHeight;
//
//            ResizeAnim resizeAnim =new
//                    ResizeAnim(
//                            constraintLayout,
//                            startHeight,
//                            targetHeight
//                    );
//
//            resizeAnim.setDuration(ANIMATION_DURATION);
//            constraintLayout.startAnimation(resizeAnim);
//
//
////            mainActivityViewModel.currentMessageHeight = height;
//
//        }


//        Toast.makeText(ChatApplication.this,chatMessage.getMessage(),Toast.LENGTH_SHORT).show();




    }


    @Override
    public void onListItemClick(int position) {

    }


    @Override
    public void onQuoteClick(int position) {
        if(position==0)
        {
            chatListRecyclerView.scrollToPosition(position);
        }
        else
        { chatListRecyclerView.scrollToPosition(position-1);}

//         FeedAdapterForChat feedAdapterForChat= (FeedAdapterForChat) chatListRecyclerView.getAdapter();
         ((FeedAdapterForChat) chatListRecyclerView.getAdapter()).blinkItem(position);
//         feedAdapterForChat.blinkItem(position);
    }

//    @Override
//    public void showReplyUI(int position) {
//
//    }
}