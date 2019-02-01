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

public class DoctorLogin extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Button signupBtn,loginBtn;
    private EditText doctorEmail,doctorPassword;

    ProgressDialog loginProgress;
    FirebaseAuth loginAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

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
                    Toast.makeText(DoctorLogin.this,"BMI CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(),BmiCalculator.class);
                    startActivity(intent1);
                }

                else if(id==R.id.menuDiabetesbtnId)
                {
                    Toast.makeText(DoctorLogin.this,"Diabetes CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),DiabetesCalculator.class);
                    startActivity(intent2);
                }
                else if(id==R.id.menuLoginbtnId)
                {
                    Toast.makeText(DoctorLogin.this,"login CLICKED",Toast.LENGTH_SHORT).show();
                    Intent intent3 = getIntent();
                    finish();
                    startActivity(intent3);
                }
                else if(id==R.id.menuAboutusbtnId)
                {
                    Toast.makeText(DoctorLogin.this,"About Us CLICKED",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                else if(id==R.id.menuExitbtnId)
                {
                    Toast.makeText(DoctorLogin.this,"Exit CLICKED",Toast.LENGTH_SHORT).show();
                    finish();
                    moveTaskToBack(true);
                }
                else if(id == R.id.menuEmergencybtnId)
                {
                    startActivity(new Intent(getApplicationContext(),EmergencyMapsActivity.class));
                }
                return true;
            }
        });


        signupBtn = (Button) findViewById(R.id.signupBtnId);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorSignup.class));
            }
        });


        doctorEmail = (EditText) findViewById(R.id.doctorEmailId);
        doctorPassword = (EditText) findViewById(R.id.doctorPasswordId);
        loginProgress = new ProgressDialog(this);

        loginBtn = (Button) findViewById(R.id.loginBtnId);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLogin();
                loginProgress.setMessage("Login in progress");
                loginProgress.show();
            }
        });

        loginAuth = FirebaseAuth.getInstance();
    }

    void startLogin()
    {
        String email = doctorEmail.getText().toString().trim();
        String password = doctorPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Incomplete Input",Toast.LENGTH_SHORT).show();
        }
        else {
            loginAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loginProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),DoctorProfile.class));
                    }
                    else {
                        loginProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
            startActivity(new Intent(getApplicationContext(),UserCatagory.class));
            //super.onBackPressed();
        }
    }
}
