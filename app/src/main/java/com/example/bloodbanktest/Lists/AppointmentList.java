package com.example.bloodbanktest.Lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bloodbanktest.Adapter.AppointmentAdapter;
import com.example.bloodbanktest.Adapter.UserAdapter;
import com.example.bloodbanktest.Appointment.Appointment;
import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentList extends AppCompatActivity {

    RecyclerView appointmentlist;
    DatabaseReference appointmentRef;

    private List<Appointment> appointmentList;
//    private List<User> userList;
    private AppointmentAdapter appointmentAdapter;

    AppointmentAdapter adapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        appointmentlist =findViewById(R.id.appointmentlist);
        appointmentRef = FirebaseDatabase.getInstance().getReference().child("appointments");
        appointmentlist.setHasFixedSize(true);
        appointmentlist.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
//        userList = new ArrayList<>();

        appointmentAdapter = new AppointmentAdapter(AppointmentList.this, appointmentList);

        appointmentlist.setAdapter(appointmentAdapter);
        progressBar = findViewById(R.id.progressbar);
        adapter = new AppointmentAdapter(this, appointmentList);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("appointments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    appointmentList.add(appointment);
                }

                appointmentAdapter.notifyDataSetChanged();

                if (appointmentList.isEmpty()){
                    Toast.makeText(AppointmentList.this, "No Appointments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}