package com.koushik.health_kit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfile extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView doctorname,doctorage,doctorarea,doctorcatagory,doctoremail,doctorgender,doctorphone,doctorworkplace,doctordegree;


    private FirebaseAuth doctorAuth;
    private FirebaseAuth.AuthStateListener doctorAuthListener;
    private DatabaseReference doctorReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        drawerLayout = (DrawerLayout) findViewById(R.id.dpDrawerId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.dpNavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.menuReservationScheduleId)
                {
                    Toast.makeText(DoctorProfile.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DoctorScheduleDaySelection.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(DoctorProfile.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuLogoutbtnId)
                {
                    Toast.makeText(DoctorProfile.this,"Log Out Clicked",Toast.LENGTH_SHORT).show();
                    finish();
                    doctorAuth.signOut();
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.

        doctorAuth = FirebaseAuth.getInstance();
        String CurrentUser = doctorAuth.getCurrentUser().getUid();
        doctorReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(CurrentUser);
        String doctorUid  = doctorReference.getKey();
        doctorReference.child("doctorUid").setValue(doctorUid);

        doctorname = (TextView) findViewById(R.id.doctorName);
        doctorage = (TextView) findViewById(R.id.doctorAge);
        doctorgender = (TextView) findViewById(R.id.doctorGender);
        doctorarea = (TextView) findViewById(R.id.doctorArea);
        doctorcatagory = (TextView) findViewById(R.id.doctorCatagory);
        doctorphone = (TextView) findViewById(R.id.doctorPhone);
        doctoremail = (TextView) findViewById(R.id.doctorEmail);
        doctorworkplace = (TextView) findViewById(R.id.doctorWorkplace);
        doctordegree = (TextView) findViewById(R.id.doctorDegree);

        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myname = dataSnapshot.child("name").getValue().toString();
                    String myage = dataSnapshot.child("age").getValue().toString();
                    String mygender = dataSnapshot.child("gender").getValue().toString();
                    String mycatagory = dataSnapshot.child("catagory").getValue().toString();
                    String myarea = dataSnapshot.child("area").getValue().toString();
                    String myphone = dataSnapshot.child("phone").getValue().toString();
                    String myemail = dataSnapshot.child("email").getValue().toString();
                    String myworkplace = dataSnapshot.child("workplace").getValue().toString();
                    String mydegree = dataSnapshot.child("degree").getValue().toString();

                    doctorname.setText("Name:\t\t"+myname);
                    doctorage.setText("Age:\t\t"+myage);
                    doctorcatagory.setText("Catagory:\t\t"+mycatagory);
                    doctorgender.setText("Gender:\t\t"+mygender);
                    doctorarea.setText("Area:\t\t"+myarea);
                    doctorphone.setText("Phone No:\t\t"+myphone);
                    doctoremail.setText("Email Address:\t\t"+myemail);
                    doctorworkplace.setText("Workplaces:\t\t"+myworkplace);
                    doctordegree.setText("Degrees:\t\t"+mydegree);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        doctorAuthListener = new FirebaseAuth.AuthStateListener() {
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
        doctorAuth.addAuthStateListener(doctorAuthListener);
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
            //super.onBackPressed();
        }
    }
}