package com.example.bloodbanktest.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbanktest.Email.JavaMailApi;
import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.R;
import com.example.bloodbanktest.ViewOtherProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context context;
    private List<User> userList;
    private List<User> listfull;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

        listfull=new ArrayList<>(userList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.user_displayed_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = userList.get(position);

        ///Addedd

        holder.itemView.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewOtherProfile.class);
                intent.putExtra("userName",user.getName());
                intent.putExtra("userEmail",user.getEmail());
                intent.putExtra("phoneNumber",user.getPhonenumber());
                intent.putExtra("bloodGroup",user.getBloodgroup());
                intent.putExtra("type",user.getType());
                intent.putExtra("userProfileImage",user.getProfilepictureurl());
                context.startActivity(intent);

                Toast.makeText(context, user.getName().toUpperCase(Locale.ROOT)+" Profile", Toast.LENGTH_SHORT).show();

            }

        });

        ///////


        holder.type.setText(user.getType());

        if (user.getType().equals("donor")){
            holder.emailNow.setVisibility(View.VISIBLE);
        }

        holder.type.setText(user.getType());
        holder.userEmail.setText(user.getEmail());
        holder.phoneNumber.setText(user.getPhonenumber());
        holder.userName.setText(user.getName());
        holder.bloodGroup.setText(user.getBloodgroup());
        Glide.with(context).load(user.getProfilepictureurl()).into(holder.userProfileImage);

        final String nameOfTheReceiver = user.getName();
        final String idOfTheReceiver = user.getId();

        //sending the email
        holder.emailNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("SEND EMAIL")
                        .setMessage("Send email to " + user.getName() + "?")
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String nameOfSender = snapshot.child("name").getValue().toString();
                                        String email = snapshot.child("email").getValue().toString();
                                        String phone = snapshot.child("phonenumber").getValue().toString();
                                        String blood = snapshot.child("bloodgroup").getValue().toString();

                                        String mEmail = user.getEmail();
                                        String mSubject = "Blood Donation";
                                        String mMessage = "Hello "+ nameOfTheReceiver+ ", "+nameOfSender+
                                                " would like to request for blood donation from you. Here's his/her details:\n"
                                                +"Name: "+nameOfSender + "\n" +
                                                "Phone Number: "+phone + "\n" +
                                                "Email: " +email +"\n" +
                                                "Blood Group: " +blood + "\n" +
                                                "Kindly reach out to him/her upon your final choice. Thank You!\n"
                                                +"BLOOD DONATION APP - Donate blood, save lives!";



                                        JavaMailApi javaMailApi = new JavaMailApi(context, mEmail, mSubject, mMessage);
                                        javaMailApi.execute();

                                        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("emails")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        senderRef.child(idOfTheReceiver).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("emails")
                                                            .child(idOfTheReceiver);
                                                    receiverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                                    addNotifications(idOfTheReceiver, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                }

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        //implement a function which replaces profile image if not set with a placeholder image

//        if (snapshot.hasChild("profilepictureurl")){
//            String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//            Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
//        } else {
//            nav_profile_image.setImageResource(R.drawable.user);
//        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView userProfileImage;
        public TextView type, userName, userEmail, phoneNumber, bloodGroup;
        public Button emailNow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            type = itemView.findViewById(R.id.type);
            userName = itemView.findViewById(R.id.name);
            userEmail = itemView.findViewById(R.id.email);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
            emailNow = itemView.findViewById(R.id.emailNow);

        }
    }

    //to add the notifications

    private void addNotifications(String receiverId, String senderId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications")
                .child(receiverId);

        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("receiverId", receiverId);
        hashMap.put("senderId", senderId);
        hashMap.put("text", "Sent you an email, kindly check it out!");
        hashMap.put("date", date);

        reference.push().setValue(hashMap);

    }

}
