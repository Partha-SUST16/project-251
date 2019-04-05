package com.koushik.health_kit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileFromPatient extends AppCompatActivity {

    private TextView doctorname,doctorage,doctorarea,doctorcatagory,doctoremail,doctorgender,doctorphone,doctorworkplace,doctordegree;

    private static String TAG = "DoctorProfileFromPatient";
    private DatabaseReference doctorReference;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button reserveSchedulebtn;
    String DoctorUID;

    private FirebaseAuth patientAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_from_patient);


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
                    Toast.makeText(getApplicationContext(),"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id == R.id.menuPrescriptionbtnId)
                {
                    startActivity(new Intent(getApplicationContext(),MyPrescriptionList.class));
                }
                else if(id == R.id.menuSearchbtnId)
                {
                    Intent intent = new Intent(getApplicationContext(),SearchDoctor.class);
                    startActivity(intent);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(getApplicationContext(),"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(getApplicationContext(),"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(getApplicationContext(),"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    patientAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                else if(id == R.id.recentButtonId)
                {
                    startActivity(new Intent(getApplicationContext(),RecentDoctors.class));
                }

                return true;
            }
        });

        ///Drawer & NavigationBar ends.

        patientAuth = FirebaseAuth.getInstance();


        doctorname = (TextView) findViewById(R.id.doctorNameId);
        doctorage = (TextView) findViewById(R.id.doctorAgeId);
        doctorgender = (TextView) findViewById(R.id.doctorGenderId);
        doctorarea = (TextView) findViewById(R.id.doctorAreaId);
        doctorcatagory = (TextView) findViewById(R.id.doctorCatagoryId);
        doctorphone = (TextView) findViewById(R.id.doctorPhoneId);
        doctoremail = (TextView) findViewById(R.id.doctorEmailId);
        doctorworkplace = (TextView) findViewById(R.id.doctorWorkplaceId);
        doctordegree = (TextView) findViewById(R.id.doctorDegreeId);


        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        String email = (String) bundle.get("doctorEmail");
        Log.d(TAG, "getExtra: "+email);
        doctorReference = FirebaseDatabase.getInstance().getReference();
        Query doctorEmail = doctorReference.child("Doctors")
                .orderByChild("email")
                .equalTo(email);

        doctorEmail.addListenerForSingleValueEvent(valueEventListener);


        reserveSchedulebtn = (Button)findViewById(R.id.reserveScheduleBtnId);
        reserveSchedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DoctorScheduleWeek.class);
                intent.putExtra("doctorUID",DoctorUID);
                startActivity(intent);

            }
        });


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DoctorUID = snapshot.getKey();
                    Log.d(TAG, "Current Node: "+snapshot.getKey());
                    //SearchDoctorCardview doctor = snapshot.getValue(SearchDoctorCardview.class);
                    //doctorList.add(doctor);
                    String myname = snapshot.child("name").getValue().toString();
                    String myage = snapshot.child("age").getValue().toString();
                    String mygender = snapshot.child("gender").getValue().toString();
                    String mycatagory = snapshot.child("catagory").getValue().toString();
                    String myarea = snapshot.child("area").getValue().toString();
                    String myphone = snapshot.child("phone").getValue().toString();
                    String myemail = snapshot.child("email").getValue().toString();
                    String myworkplace = snapshot.child("workplace").getValue().toString();
                    String mydegree = snapshot.child("degree").getValue().toString();

                    doctorname.setText("Name:\t\t"+myname);
                    doctorage.setText("Age:\t\t"+myage);
                    doctorcatagory.setText("Catagory:\t\t"+mycatagory);
                    doctorgender.setText("Gender:\t\t"+mygender);
                    doctorarea.setText("Area:\t\t"+myarea);
                    doctorphone.setText("Phone No:\t\t"+myphone);
                    doctoremail.setText("Email Address:\t\t"+myemail);
                    doctorworkplace.setText("Workplaces:\t\t"+myworkplace);
                    doctordegree.setText("Degrees:\t\t"+mydegree);

                    Log.d(TAG, "Current Data: "+myname);
                    Log.d(TAG, "Current Data: "+myage);
                    Log.d(TAG, "Current Data: "+mycatagory);
                    Log.d(TAG, "Current Data: "+myemail);
                    Log.d(TAG, "Current Data: "+myarea);


                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


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
            startActivity(new Intent(getApplicationContext(),SearchDoctor.class));
            //super.onBackPressed();
        }
    }


}
