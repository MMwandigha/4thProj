package com.example.bloodbanktest.Booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.bloodbanktest.MainActivity;
import com.example.bloodbanktest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAppointmentActivity extends Fragment {

    private TextView  userName2, userEmail2, phoneNumber2, bloodGroup2, type2, appHospital, appLocation, appDate, appTime ;
    Button btnBackHome;
    private Toolbar toolbar;
    private CircleImageView userProfileImage2;

    private DatabaseReference userRef;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_appointment_activity, container, false);

//        userName = getActivity().getIntent().getStringExtra("userName");
//        userEmail = getActivity().getIntent().getStringExtra("userEmail");
//        phoneNumber = getActivity().getIntent().getStringExtra("phoneNumber");
//        bloodGroup = getActivity().getIntent().getStringExtra("bloodGroup");
//        type = getActivity().getIntent().getStringExtra("type");
//        userProfileImage = getActivity().getIntent().getStringExtra("userProfileImage");

        appHospital = view.findViewById(R.id.appHospital);
        appLocation = view.findViewById(R.id.appLocation);
        appDate = view.findViewById(R.id.appDate);
        appTime = view.findViewById(R.id.appTime);
        btnBackHome = view.findViewById(R.id.btnBackHome);
//
        userName2 = view.findViewById(R.id.userName2);
        userEmail2 = view.findViewById(R.id.userEmail2);
        phoneNumber2 = view.findViewById(R.id.phoneNumber2);
        bloodGroup2 = view.findViewById(R.id.bloodGroup2);
        type2 = view.findViewById(R.id.type2);
        userProfileImage2 = view.findViewById(R.id.userProfileImage2);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("name").getValue().toString();
                    String Email = snapshot.child("email").getValue().toString();
                    String BloodGroup = snapshot.child("bloodgroup").getValue().toString();
                    String Type = snapshot.child("type").getValue().toString();
                    String PhoneNumber = snapshot.child("phonenumber").getValue().toString();
                    userName2.setText(Name);
                    userEmail2.setText(Email);
                    phoneNumber2.setText(PhoneNumber);
                    bloodGroup2.setText(BloodGroup);
                    type2.setText(Type);

                    if (snapshot.hasChild("profilepictureurl")){
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(userProfileImage2);
                    } else {
                        userProfileImage2.setImageResource(R.drawable.user);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("appointments").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String AppHospital = snapshot.child("hospitalName").getValue().toString();
                    String AppLocation = snapshot.child("location").getValue().toString();
                    String AppDate = snapshot.child("date").getValue().toString();
                    String AppTime = snapshot.child("time").getValue().toString();
                    appHospital.setText(AppHospital);
                    appLocation.setText(AppLocation);
                    appDate.setText(AppDate);
                    appTime.setText(AppTime);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


//
////        Picasso.get().load(Uri.parse(userProfileImage)).into(userProfileImage2);
//
//        userName2.setText(userName);
//        userEmail2.setText(userEmail);
//        phoneNumber2.setText(phoneNumber);
//        bloodGroup2.setText(bloodGroup);
//        type2.setText(type);
//
//        String firstLetter = userName.substring(0, 1);
//        String remainingLetters = userName.substring(1, userName.length());
//        firstLetter = firstLetter.toUpperCase();
//        // join the two substrings
//        userName = firstLetter + remainingLetters;
//
//        toolbar = view.findViewById(R.id.toolbar);




        return view;
    }
}