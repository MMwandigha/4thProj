package com.example.bloodbanktest.Appointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import com.example.bloodbanktest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class BookingFragment extends Fragment {

    private TextInputEditText hospitalName, hospitalLocation;

    private FirebaseAuth mAuth;
    private Button btnBook;
    private ProgressDialog loader;
    private EditText title2, message2;


    private TextView disablePastDate, time;

    private DatabaseReference userDatabaseRef;
    private Toolbar toolbar;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("appointments");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        hospitalName = view.findViewById(R.id.hospitalName);
        hospitalLocation = view.findViewById(R.id.hospitalLocation);
        disablePastDate = view.findViewById(R.id.disable_past_date);

        time = view.findViewById(R.id.time);
        btnBook = view.findViewById(R.id.btnBook);
        mAuth = FirebaseAuth.getInstance();

        title2 = view.findViewById(R.id.title2);
        message2 = view.findViewById(R.id.message2);
        loader  = new ProgressDialog(getContext());

        hospitalLocation.addTextChangedListener(TextView);
        hospitalName.addTextChangedListener(TextView);
        time.addTextChangedListener(TextView);
        disablePastDate.addTextChangedListener(TextView);


        //onclick listener for the time picker


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String ttime = format.format(c.getTime());
                        time.setText(ttime);
                    }
                }, hours, minutes, false);
                timePickerDialog.show();

            }
        });

        //Initializing the calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        disablePastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        month = month +1;
                        String sDate = dayOfMonth + "/" +month +"/" + year;
                        disablePastDate.setText(sDate);

                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String HospitalLocation = hospitalLocation.getText().toString().trim();
                final String HospitalName = hospitalName.getText().toString().trim();
                final String Time = time.getText().toString().trim();
                final String DisablePastDate = disablePastDate.getText().toString().trim();

                if (TextUtils.isEmpty(HospitalLocation)) {
                    hospitalLocation.setError("Value is required!");
                    return;
                }
                if (TextUtils.isEmpty(HospitalName)) {
                    hospitalName.setError("Value is required!");
                    return;
                } else {

                    loader.setMessage("Booking Appointment...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String currentUserId = mAuth.getCurrentUser().getUid();
                    userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("appointments").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    HashMap userInfo = new HashMap();

                    userInfo.put("id", currentUserId);
                    userInfo.put("hospitalName", HospitalName);
                    userInfo.put("location", HospitalLocation);
                    userInfo.put("time", Time);
                    userInfo.put("date", DisablePastDate);

                    userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Appointment Set Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            Navigation.findNavController(view).navigate(R.id.action_bookingFragment_to_viewAppointmentActivity);
                            loader.dismiss();
                        }
                    });

                }

            }
        });

        return view;

    }


    private final TextWatcher TextView = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String HospitalLocation = hospitalLocation.getText().toString().trim();
            String HospitalName = hospitalName.getText().toString().trim();
            String Time = time.getText().toString().trim();
            String DisablePastDate = disablePastDate.getText().toString().trim();

            btnBook.setEnabled(!HospitalName.isEmpty() && !HospitalLocation.isEmpty() &&
                    !DisablePastDate.isEmpty() &&!Time.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}