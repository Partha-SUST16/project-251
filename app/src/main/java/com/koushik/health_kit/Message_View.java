package com.koushik.health_kit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Message_View extends AppCompatActivity {

    private String  doctorname, doctoruid, rev_username, username;
    List<chat_message_obj> msgList;
    public static String sendername;
    private RecyclerView recyclerView;
    private EditText messageET;
    private ImageView sendButton;
    private DatabaseReference patientD, doctorD,first,secend;
    private msg_adapter ma;
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__view);
        msgList = new ArrayList<>();
        doctorname = getIntent().getStringExtra("doctorname");
        doctoruid = getIntent().getStringExtra("doctoruid");
        flag = getIntent().getExtras().getInt("flag");
        Toast.makeText(getApplicationContext(),Integer.toString(flag),Toast.LENGTH_SHORT).show();
        if(flag==0){
            first = FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .child("name");
        }
        else {
            first = FirebaseDatabase.getInstance().getReference().child("Doctors")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .child("name");
        }
        first.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendername = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), sendername, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        username = sendername + "," + doctorname;
        rev_username = doctorname + "," + sendername;
        recyclerView = (RecyclerView) findViewById(R.id.MessageRecyclerView);
        messageET = (EditText) findViewById(R.id.MessageEditText);
        sendButton = (ImageView) findViewById(R.id.MessageSendButton);

        ma = new msg_adapter(msgList,sendername);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ma);
        //recyclerView.scrollToPosition(ma.getItemCount()-1);

        if(flag==0){
            patientD = FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .child("message").child(doctoruid);
        }else {
            patientD = FirebaseDatabase.getInstance().getReference().child("Doctors")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .child("message").child(doctoruid);
        }
       if(flag==0) {
           doctorD = FirebaseDatabase.getInstance().getReference().child("Doctors")
                   .child(doctoruid).child("message")
                   .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
       }else{
           doctorD = FirebaseDatabase.getInstance().getReference().child("Patients")
                   .child(doctoruid).child("message")
                   .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
       }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageET.getText().toString().trim();
                hideKeyboardwithoutPopulate(Message_View.this);
                if (!message.equals("")) {

                    chat_message_obj chatMessage = new chat_message_obj(message, sendername);
                    patientD.push().setValue(chatMessage);
                    doctorD.push().setValue(chatMessage);
//                    msgList.add(chatMessage);
//                    ma.notifyDataSetChanged();
                }
                messageET.setText("");
            }
        });

        try {
            patientD.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    msgList.add(dataSnapshot.getValue(chat_message_obj.class));
                    ma.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){

        }

    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
