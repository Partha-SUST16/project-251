package com.koushik.health_kit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPrescriptionList extends AppCompatActivity {


    List<String>presList=new ArrayList<>();
    private ListView mList;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseAuth patientAuth;
    private FirebaseAuth.AuthStateListener patientAuthListener;
    private DatabaseReference patientReference;
    private String pMax;
    private int MAX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription_list);

        drawerLayout = (DrawerLayout) findViewById(R.id.ppDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        mList = findViewById(R.id.listview);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.ppNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menuBMIbtnId) {
                    Toast.makeText(MyPrescriptionList.this, "BMI CLICKED", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), BmiCalculator.class);
                    startActivity(intent1);
                }
                else if (id == R.id.menuPrescriptionbtnId)
                {
                    startActivity(new Intent(getApplicationContext(), MyPrescriptionList.class));
                }
                else if (id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(), SearchDoctor.class);
                    startActivity(intent);
                }
                else if (id == R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(MyPrescriptionList.this, "Diabetes CLICKED", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if (id == R.id.menuAboutustnId)
                {
                    Toast.makeText(MyPrescriptionList.this, "About Us CLICKED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AboutUs.class));
                }
                else if (id == R.id.menuLogoutbtnId)
                {
                    Toast.makeText(MyPrescriptionList.this, "Log Out Clicked", Toast.LENGTH_SHORT).show();
                    finish();
                    patientAuth.signOut();
                }
                else if (id == R.id.menuEmergencybtnId)
                {
                    startActivity(new Intent(getApplicationContext(), EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.

        patientAuth = FirebaseAuth.getInstance();
        String CurrentUser = patientAuth.getCurrentUser().getUid();
        patientReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(CurrentUser);
        patientReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pMax = dataSnapshot.child("prescriptionNumber").getValue(String.class);
                Log.d("My Prescription ",pMax);
                MAX=Integer.parseInt(pMax);
                for(int i=1;i<=MAX;i++)
                {
                    String idx = Integer.toString(i);
                    presList.add(idx);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,presList);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                /*Intent i = new Intent(More.this, NextActvity.class);
                //If you wanna send any data to nextActicity.class you can us
                i.putExtra(String key, value.get(position));
                startActivity(i);*/

                String tempo = String.valueOf(position);

                Log.d("MyPrescriptionList", "onItemClickP: "+position);
                Intent intent = new Intent(getApplicationContext(),MyPrescription.class);
                intent.putExtra("clickedOn",tempo);
                startActivity(intent);
            }
        });

        patientAuth.getInstance();
        patientAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(getApplicationContext(),UserCatagory.class));
                }
            }
        };


    }


    @Override
    protected void onStart() {
        super.onStart();
        patientAuth.addAuthStateListener(patientAuthListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) ||super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            startActivity(new Intent(getApplicationContext(),PatientProfile.class));
            //super.onBackPressed();
        }
    }

}
