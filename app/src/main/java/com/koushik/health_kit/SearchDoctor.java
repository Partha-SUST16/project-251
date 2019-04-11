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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchDoctor extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    private FirebaseAuth patientAuth;
    private FirebaseAuth.AuthStateListener patientAuthListener;

    List<SearchDoctorCardview> doctorList;

    //the recyclerview
    RecyclerView recyclerView;
    SearchDoctorCardviewadapter adapter;
    DatabaseReference doctorDatabaseReference;


    private EditText searchByCatagory,searchByarea;
    private Button searchByCatagorybtn,searchbyAreabtn,refreshbtn,filterbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);


        drawerLayout = (DrawerLayout) findViewById(R.id.ppDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.ppNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(SearchDoctor.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
                }
                else if(id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(),SearchDoctor.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(SearchDoctor.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(SearchDoctor.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(SearchDoctor.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    patientAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    //Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    Toast.makeText(getApplicationContext(),"See you Soon!!",Toast.LENGTH_SHORT).show();

                    //startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });

        ///Drawer & NavigationBar ends.





        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing the productlist
        doctorList = new ArrayList<>();

        //creating recyclerview adapter
        adapter = new SearchDoctorCardviewadapter(this, doctorList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        doctorDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctors");
        //doctorDatabaseReference.addListenerForSingleValueEvent(valueEventListener);

        searchByCatagory = (EditText) findViewById(R.id.searchByCatagoryEdittextId);
        searchByarea = (EditText) findViewById(R.id.searchByAreaEdittextId);
        searchByCatagorybtn = (Button) findViewById(R.id.searchByCatagoryBtnId);
        searchbyAreabtn = (Button) findViewById(R.id.searchByAreaBtnId);
        refreshbtn = (Button) findViewById(R.id.RefreshBtnId);
        filterbtn = (Button) findViewById(R.id.FilterBtnId);


        doctorList.clear();
        doctorDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctors");
        doctorDatabaseReference.addListenerForSingleValueEvent(valueEventListener);


        searchByCatagorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String catagory = searchByCatagory.getText().toString().trim();
                catagory=catagory.toLowerCase();
                if(catagory!=null)
                {
                    Query catagorySearch = FirebaseDatabase.getInstance().getReference("Doctors")
                            .orderByChild("catagory")
                            .startAt(catagory)
                            .endAt(catagory+"\uf8ff");
                    catagorySearch.addListenerForSingleValueEvent(valueEventListener);
                }
                else
                    Toast.makeText(getApplicationContext(),"Input field Null",Toast.LENGTH_SHORT).show();
            }
        });

        searchbyAreabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String area = searchByarea.getText().toString().trim();
                area = area.toLowerCase();
                if(area!=null)
                {
                    Query areaSearch = FirebaseDatabase.getInstance().getReference("Doctors")
                            .orderByChild("area")
                            .startAt(area)
                            .endAt(area+"\uf8ff");
                    areaSearch.addListenerForSingleValueEvent(valueEventListener);
                }
                else
                    Toast.makeText(getApplicationContext(),"Input field Null",Toast.LENGTH_SHORT).show();
            }
        });


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doctorList.clear();
                doctorDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctors");
                doctorDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });



        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catagory = searchByCatagory.getText().toString().trim().toLowerCase();
                String area = searchByarea.getText().toString().trim().toLowerCase();

                String filterSearch = catagory+area;
                filterSearch.trim();
                if(!catagory.equals("") && !area.equals(""))
                {
                    Query filtersearch = FirebaseDatabase.getInstance().getReference("Doctors")
                            .orderByChild("filter")
                            .startAt(filterSearch)
                            .endAt(filterSearch+"\uf8ff");
                    filtersearch.addListenerForSingleValueEvent(valueEventListener);
                }

            }
        });

        patientAuth = FirebaseAuth.getInstance();

        patientAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(getApplicationContext(),UserCatagory.class));
                }
            }
        };


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            doctorList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchDoctorCardview doctor = snapshot.getValue(SearchDoctorCardview.class);
                    doctorList.add(doctor);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


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
            //startActivity(new Intent(getApplicationContext(),PatientProfile.class));
            super.onBackPressed();
        }
    }


}