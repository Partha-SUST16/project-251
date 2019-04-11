package com.koushik.health_kit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ID_Preview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<SenderInfo> senderInfoList;
    private id_previewAdapter adapter;
    private DatabaseReference patient,doctor;
    private String doctoruid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id__preview);

        recyclerView = (RecyclerView) findViewById(R.id.id_preview_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        senderInfoList = new ArrayList<>();

       // adapter = new id_previewAdapter(this,senderInfoList);
        recyclerView.setAdapter(adapter);

        senderInfoList.clear();
        patient = FirebaseDatabase.getInstance().getReference().child("Patients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child("Recents");


    }
}
