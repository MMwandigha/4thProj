package com.example.bloodbanktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.example.bloodbanktest.Lists.AppointmentList;
import com.example.bloodbanktest.Lists.DonorList;
import com.example.bloodbanktest.Lists.RecipientList;
import com.example.bloodbanktest.aob.AboutSection;
import com.example.bloodbanktest.reports.AppointmentReports;
import com.example.bloodbanktest.reports.BloodStockReports;
import com.example.bloodbanktest.reports.DonorReports;
import com.example.bloodbanktest.reports.NotificationsReport;
import com.example.bloodbanktest.reports.RecipientReports;
import com.example.bloodbanktest.reports.UserReports;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    CardView Donor, Recipient, BloodCamps, BloodBank, Appointments, About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Donor=findViewById(R.id.Donor);
        Recipient=findViewById(R.id.Recipient);
        BloodCamps=findViewById(R.id.BloodCamps);
        BloodBank=findViewById(R.id.BloodBank);
        Appointments = findViewById(R.id.Appointments);
        About = findViewById(R.id.About);

        Donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, DonorList.class);
                startActivity(intent);
            }
        });

        Recipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, RecipientList.class);
                startActivity(intent);

            }
        });

        BloodCamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminBloodCamp.class);
                startActivity(intent);

            }
        });

        BloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminBloodBank.class);
                startActivity(intent);

            }
        });

        Appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AppointmentList.class);
                startActivity(intent);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AboutSection.class);
                startActivity(intent);
            }
        });

        nav = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.sentEmail:
                        Intent intent2 = new Intent(AdminMainActivity.this, SentEmailActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.BloodCamps:
                        Intent intent3 = new Intent(AdminMainActivity.this, AdminBloodCamp.class);
                        startActivity(intent3);
                        break;

                    case R.id.BloodBank:
                        Intent intent4 = new Intent(AdminMainActivity.this, AdminBloodBank.class);
                        startActivity(intent4);
                        break;

                    case R.id.notifications:
                        Intent intent5 = new Intent(AdminMainActivity.this, NotificationActivity.class);
                        startActivity(intent5);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent6 = new Intent(AdminMainActivity.this, LoginActivity.class);
                        startActivity(intent6);
                        break;

                    case R.id.Donor:
                        Intent intent7 = new Intent(AdminMainActivity.this,DonorList.class);
                        startActivity(intent7);
                        break;

                    case R.id.Recipient:
                        Intent intent8 = new Intent(AdminMainActivity.this, RecipientList.class);
                        startActivity(intent8);
                        break;

                    case R.id.profile:
                        Intent intent9 = new Intent(AdminMainActivity.this, AdminUpdateProfile.class);
                        startActivity(intent9);
                        break;

                    case R.id.about:
                        Intent intent10 = new Intent(AdminMainActivity.this, AboutSection.class);
                        startActivity(intent10);
                        break;

                    case R.id.userreport:
                        Intent intent99 = new Intent(AdminMainActivity.this, UserReports.class);
                        startActivity(intent99);
                        break;

                    case R.id.appointmentreport:
                        Intent intent98 = new Intent(AdminMainActivity.this, AppointmentReports.class);
                        startActivity(intent98);
                        break;


                    case R.id.donorreport:
                        Intent intent96 = new Intent(AdminMainActivity.this, DonorReports.class);
                        startActivity(intent96);

                    case R.id.recipientreport:
                        Intent intent95 = new Intent(AdminMainActivity.this, RecipientReports.class);
                        startActivity(intent95);


                    case R.id.share:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent11 = new Intent(Intent.ACTION_SEND);
                        intent11.setType("text/plain");
                        String Body = "Download this App";
                        intent11.putExtra(Intent.EXTRA_TEXT,Body);
                        startActivity(intent11.createChooser(intent11,"Share Using"));
                        break;
                }

                return true;
            }
        });
    }
}