package com.koushik.health_kit;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DoctorSignup extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText emailvalue,passwordvalue,namevalue,gendervalue,agevalue,workplacevalue,degreevalue,catagoryvalue,areavalue,phonevalue;
    private Button signUp,gotoLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth doctorAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);

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
                    Toast.makeText(DoctorSignup.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }
                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getApplicationContext(),PatientLogin.class);
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutustnId)
                {
                    Toast.makeText(DoctorSignup.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(DoctorSignup.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                return true;
            }
        });

        ///Drawer & NavigationBar ends.

        emailvalue = (EditText) findViewById(R.id.doctorEditEmailId);
        passwordvalue = (EditText) findViewById(R.id.doctorEditPasswordId);
        namevalue = (EditText) findViewById(R.id.doctorEditNameId);
        gendervalue =(EditText) findViewById(R.id.doctorEditGenderId);
        agevalue= (EditText) findViewById(R.id.doctorEditAgeId);
        catagoryvalue = (EditText) findViewById(R.id.doctorEditCatagoryId);
        workplacevalue = (EditText) findViewById(R.id.doctorEditWorkplaceId);
        degreevalue = (EditText) findViewById(R.id.doctorEditDegreeId);
        areavalue = (EditText) findViewById(R.id.doctoreditAreaId);
        phonevalue = (EditText) findViewById(R.id.doctorEditphoneId);

        signUp = (Button) findViewById(R.id.doctorSignUpBtnId);
        gotoLogin = (Button) findViewById(R.id.gotoLoginBtnId);
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDoctor();
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),DoctorLogin.class));
            }
        });


        /// Firebase
        doctorAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors");

    }

    void registerDoctor()
    {
        String email = emailvalue.getText().toString().trim();
        String password=passwordvalue.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Incomplete Input",Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            doctorAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_SHORT).show();
                        addDoctorInfo();
                        finish();
                        startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Email in Use",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    void addDoctorInfo(){
        String name = namevalue.getText().toString().trim();
        String gender = gendervalue.getText().toString().trim();
        String age = agevalue.getText().toString().trim();
        String catagory = catagoryvalue.getText().toString().trim();
        catagory = catagory.toLowerCase();
        String area = areavalue.getText().toString().trim();
        area = area.toLowerCase();
        String phone = phonevalue.getText().toString().trim();
        String email = emailvalue.getText().toString().trim();
        String workplace = workplacevalue.getText().toString().trim();
        String degree = degreevalue.getText().toString().trim();
        String filter = catagory+area;



        DoctorInfo doctorInfo = new DoctorInfo(name,gender,age,area,phone,email,catagory,workplace,degree,filter);

        FirebaseUser patient = doctorAuth.getCurrentUser();
        databaseReference.child(patient.getUid()).setValue(doctorInfo);
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
            startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
            //super.onBackPressed();
        }
    }

}
