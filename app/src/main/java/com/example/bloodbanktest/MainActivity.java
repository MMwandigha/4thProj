package com.example.bloodbanktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bloodbanktest.Adapter.UserAdapter;
import com.example.bloodbanktest.Booking.AppointmentActivity;
import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.aob.AboutSection;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CardView cardCampaign;
    private Button btnSetAppointment, btnSeeAvailability;

    //display user data

    private CircleImageView nav_profile_image;
    private TextView nav_fullname, nav_email, nav_bloodgroup, nav_type, textGreeting, displayName, displayGroup, displayType;

    //declare the recyclerview
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<User> userList;
    private UserAdapter userAdapter;


    private DatabaseReference userRef;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout= findViewById(R.id.drawerLayout);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_view= findViewById(R.id.nav_view);


        textGreeting = findViewById(R.id.textGreeting);
        displayName = findViewById(R.id.displayName);
        displayGroup = findViewById(R.id.displayGroup);
        displayType = findViewById(R.id.displayType);


        btnSeeAvailability = findViewById(R.id.btnSeeAvailability);
        btnSetAppointment = findViewById(R.id.btnSetAppointment);
        cardCampaign = findViewById(R.id.cardCampaign);


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 12 && hour <17){
            textGreeting.setText("Good Afternoon");
        } else if(hour >= 17 && hour < 21){
            textGreeting.setText("Good Evening");
        } else if(hour >= 21 && hour < 24){
            textGreeting.setText("Good Night");
        } else {
            textGreeting.setText("Good Morning");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        //initialize the recyclerview
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recyclerView);

        //set linear layout manager for the recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this, userList);
        recyclerView.setAdapter(userAdapter);

        //display data within the recyclerview

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if (type.equals("donor")){
                    readRecipients();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("GiveBlood");
                    displayType.setText("Locate and Email individual Recipients");

                }else {
                    readDonors();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("GiveBlood");
                    displayType.setText("Locate and Email individual Donors");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //display firebase data

        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_type = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        //fetch data from firebase

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //to check for the name and set it to the variable text
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);
                    displayName.setText(name);

                    //to check for the email and set it as the variable email
                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                    nav_bloodgroup.setText(bloodgroup);
                    displayGroup.setText(bloodgroup);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);


                    //if there are no profile images uploaded by user then a placeholder image
                    //is shown by default

                    if (snapshot.hasChild("profilepictureurl")){
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
                    } else {
                        nav_profile_image.setImageResource(R.drawable.user);
                    }

                    //change 'Donor's Email' whether you are logged in as a donor or recipient to match accordingly

                    Menu nav_menu = nav_view.getMenu();

                    if (type.equals("donor")){
                        nav_menu.findItem(R.id.sentEmail).setTitle("Received Emails");
                        nav_menu.findItem(R.id.notifications).setVisible(true);
                        nav_menu.findItem(R.id.Ideal_Donation).setVisible(true);
                        nav_menu.findItem(R.id.Ideal_Reciver).setVisible(false);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cardCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BloodCampsActivity.class));
            }
        });

        btnSeeAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GraphBloodBank.class ));
            }
        });

        btnSetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AppointmentActivity.class));
            }
        });


    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }

                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Donors", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readRecipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }

                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Recipients", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.aplus:
                Intent intent3 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent3.putExtra("group", "A+");
                startActivity(intent3);
                break;

            case R.id.aminus:
                Intent intent4 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent4.putExtra("group", "A-");
                startActivity(intent4);
                break;

            case R.id.bplus:
                Intent intent5 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent5.putExtra("group", "B+");
                startActivity(intent5);
                break;

            case R.id.bminus:
                Intent intent6 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent6.putExtra("group", "B-");
                startActivity(intent6);
                break;

            case R.id.abplus:
                Intent intent7 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent7.putExtra("group", "AB+");
                startActivity(intent7);
                break;

            case R.id.abminus:
                Intent intent8 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent8.putExtra("group", "AB-");
                startActivity(intent8);
                break;

            case R.id.oplus:
                Intent intent9 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent9.putExtra("group", "O+");
                startActivity(intent9);
                break;

            case R.id.ominus:
                Intent intent10 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent10.putExtra("group", "O-");
                startActivity(intent10);
                break;

            case R.id.compatible:
                Intent intent11 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent11.putExtra("group", "Compatible with Me");
                startActivity(intent11);
                break;

            case R.id.notifications:
                Intent intent13 = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent13);
                break;

            case R.id.sentEmail:
                Intent intent12 = new Intent(MainActivity.this, SentEmailActivity.class);
                startActivity(intent12);
                break;

            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;

            case R.id.Ideal_Donation:
                Intent intent18 = new Intent(MainActivity.this, WhoCanDonateToWhom.class);
                startActivity(intent18);
                break;

            case R.id.Ideal_Reciver:
                Intent intent19 = new Intent(MainActivity.this, WhoCanReceiveFromWhom.class);
                startActivity(intent19);
                break;


            case R.id.share:
                Intent intent17 = new Intent(Intent.ACTION_SEND);
                intent17.setType("text/plain");
                String Body = "Download this App";
                intent17.putExtra(Intent.EXTRA_TEXT,Body);
                startActivity(intent17.createChooser(intent17,"Share Using"));
                break;

            case R.id.about:
                Intent intent20 = new Intent(MainActivity.this, AboutSection.class);
                startActivity(intent20);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}