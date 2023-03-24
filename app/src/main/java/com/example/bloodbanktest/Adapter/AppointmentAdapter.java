package com.example.bloodbanktest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbanktest.Appointment.Appointment;
import com.example.bloodbanktest.Booking.ViewAppointmentActivity;
import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private Context context;
    private List <Appointment> appointmentList;
    private List <User> userList;
    private List <Appointment> listfull;
    private FirebaseRecyclerAdapter adapter;




    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;

        listfull = new ArrayList<>(appointmentList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate
                (R.layout.appointment_displayed_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final Appointment appointment = appointmentList.get(position);
//       final User user = userList.get(position);

        holder.appLocation3.setText(appointment.getLocation());
        holder.appHospital3.setText(appointment.getHospitalName());
        holder.appTime3.setText(appointment.getTime());
        holder.appDate3.setText(appointment.getDate());






//       holder.itemView.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//               Intent intent = new Intent(context, ViewAppointmentActivity.class);
//               intent.putExtra("userName",appointment.getName());
//               intent.putExtra("userEmail",appointment.getEmail());
//               intent.putExtra("phoneNumber",appointment.getPhonenumber());
//               intent.putExtra("bloodGroup",appointment.getBloodgroup());
////              intent.putExtra("type",appointment.getType());
//               intent.putExtra("date",appointment.getDate());
//               intent.putExtra("time",appointment.getTime());
//               intent.putExtra("hospitalName",appointment.getHospitalName());
//               intent.putExtra("userProfileImage",appointment.getProfilepictureurl());
//               intent.putExtra("location",appointment.getLocation());
//               context.startActivity(intent);
//
//               Toast.makeText(context, appointment.getName().toUpperCase(Locale.ROOT)+" Appointment Details", Toast.LENGTH_SHORT).show();
//
//           }
//       });



//        Glide.with(context).load(appointment.getProfilepictureurl()).into(holder.userProfileImage);

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public CircleImageView userProfileImage;
        public TextView email3, phoneNumber3, bloodGroup3, appDate3, appTime3, appName3, appHospital3, appLocation3;
        public Button emailNow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            appName3 = itemView.findViewById(R.id.appName);
            email3 = itemView.findViewById(R.id.email);
            appDate3 = itemView.findViewById(R.id.appDate);
            appTime3 = itemView.findViewById(R.id.appTime);
            appLocation3 = itemView.findViewById(R.id.appLocation);
            appHospital3 = itemView.findViewById(R.id.appHospital);
            phoneNumber3 = itemView.findViewById(R.id.phoneNumber);
            bloodGroup3 = itemView.findViewById(R.id.bloodGroup);

            emailNow = itemView.findViewById(R.id.emailNow);
//            addNames();

        }

//        private void addNames() {
//
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
//            userRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()){
//                        String Name = snapshot.child("name").getValue().toString();
//                        String Email = snapshot.child("email").getValue().toString();
//                        String BloodGroup = snapshot.child("bloodgroup").getValue().toString();
//                        String PhoneNumber = snapshot.child("phonenumber").getValue().toString();
//                        email3.setText(Email);
//                        phoneNumber3.setText(PhoneNumber);
//                        appName3.setText(Name);
//                        bloodGroup3.setText(BloodGroup);
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//
//        }
    }

}
