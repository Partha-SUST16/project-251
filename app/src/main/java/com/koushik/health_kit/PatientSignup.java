package com.koushik.health_kit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PatientSignup extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText emailvalue,passwordvalue,namevalue,gendervalue,agevalue,bloodvalue,areavalue,phonevalue;
    private Button signUp,gotoLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth patientAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        ///Drawer & NavigationBar starts

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.NavigationViewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.menuBMIbtnId)
                {
                    Toast.makeText(PatientSignup.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(PatientSignup.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(PatientSignup.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getApplicationContext(),PatientLogin.class);
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(PatientSignup.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(PatientSignup.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.

        emailvalue = (EditText) findViewById(R.id.patientEditEmailId);
        passwordvalue = (EditText) findViewById(R.id.patientEditPasswordId);
        namevalue = (EditText) findViewById(R.id.patientEditNameId);
        gendervalue =(EditText) findViewById(R.id.parientEditGenderId);
        agevalue= (EditText) findViewById(R.id.patientEditAgeId);
        bloodvalue = (EditText) findViewById(R.id.patientEditBloodId);
        areavalue = (EditText) findViewById(R.id.patienteditAreaId);
        phonevalue = (EditText) findViewById(R.id.patientEditphoneId);

        signUp = (Button) findViewById(R.id.patientSignUpBtnId);
        gotoLogin = (Button) findViewById(R.id.gotoLoginBtnId);
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPatient();
            }
        });
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),PatientLogin.class));
            }
        });

        /// Firebase
        patientAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients");


    }

    void registerPatient(){
        String email = emailvalue.getText().toString().trim();
        String password=passwordvalue.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Incomplete Input",Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            patientAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_SHORT).show();
                        addPatientInfo();
                        finish();
                        startActivity(new Intent(getApplicationContext(),PatientLogin.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Email in Use",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    void addPatientInfo(){
        String name = namevalue.getText().toString().trim();
        String gender = gendervalue.getText().toString().trim();
        String age = agevalue.getText().toString().trim();
        String blood = bloodvalue.getText().toString().trim();
        String area = areavalue.getText().toString().trim();
        String phone = phonevalue.getText().toString().trim();
        String email = emailvalue.getText().toString().trim();
        String presctiptionNumber = "1";


        PatientInfo patientInfo = new PatientInfo(name,gender,age,blood,area,phone,email,presctiptionNumber);

        FirebaseUser patient = patientAuth.getCurrentUser();
        databaseReference.child(patient.getUid()).setValue(patientInfo);
        Toast.makeText(getApplicationContext(),"Info added",Toast.LENGTH_SHORT).show();

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
            startActivity(new Intent(getApplicationContext(),PatientLogin.class));
            //super.onBackPressed();
        }
    }
}
