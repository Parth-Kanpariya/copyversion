package com.example.copyversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatInitiator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatInitiator extends Fragment implements FeedAdapterForEnterChat.ListItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference databaseReference1, databaseReference2;
    DatabaseReference status1,connect1;
    String nameOfUser, profileImageUri;



    private ArrayList<ChatInitiateUtil> ListForChatting = new ArrayList<>();
    private ArrayList<ChatInitiateUtil> ListForChatting1 = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatInitiator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatInitiator.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatInitiator newInstance(String param1, String param2) {
        ChatInitiator fragment = new ChatInitiator();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_chat_initiator, container, false);

        if(ListForChatting!=null)
        {
            ListForChatting.clear();
        }
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String myUid = Auth.getCurrentUser().getUid();

        RecyclerView forChatRecyclerView = (RecyclerView) rootView.findViewById(R.id.EnterIntoChat);
        forChatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        ProgressDialog progressDialog
                = new ProgressDialog(getContext());


        TextView displayMessage=rootView.findViewById(R.id.dispalyMessageForNoChatInitiate);
        if(ListForChatting.size()==0)
        {
            progressDialog.dismiss();

            displayMessage.setVisibility(View.VISIBLE);
        }else
        {
            progressDialog.show();
            displayMessage.setVisibility(View.GONE);
        }

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

                        final long[] max = {0};
                        final String[] sd = new String[1];

                        String finalUidOfOther = uidOfOther;
                        String finalUidOfOther1 = uidOfOther;
                        db1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    for (DataSnapshot ds : snapshot.getChildren()) {
//                                                Toast.makeText(ChatInitiate.this,ds.getKey(),Toast.LENGTH_SHORT).show();
                                        long s = Long.parseLong(ds.getKey());

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

                                    if(ListForChatting.size()!=0)
                                        displayMessage.setVisibility(View.GONE);



                                    Collections.sort(ListForChatting, (o1, o2) -> (int) (o2.getTimeStamp() - o1.getTimeStamp()));
                                    forChatRecyclerView.setAdapter(new FeedAdapterForEnterChat(ListForChatting, ChatInitiator.this::onListItemClick, getContext()));
                                    progressDialog.dismiss();

                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





                    }


                } else {

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



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // do not reload anymore, unless I tell you so...
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

                    Bundle bundle=new Bundle();
                    bundle.putString("Name",nameOfUser);
                    bundle.putString("imageUrl",profileImageUri);
                    bundle.putString("uidOther",x.getOtherUid());
//                    Intent intent = new Intent(getContext(), ChatApplicationn.class);
//                    intent.putExtra("Name", nameOfUser);
//                    intent.putExtra("imagUrl", profileImageUri);
//                    intent.putExtra("uidOther", x.getOtherUid());
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_chatInitiator_to_chatApplicationn,bundle);


//                    startActivity(intent);






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }



}