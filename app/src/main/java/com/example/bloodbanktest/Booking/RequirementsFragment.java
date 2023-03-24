package com.example.bloodbanktest.Booking;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.bloodbanktest.R;


public class RequirementsFragment extends Fragment {


    private RadioButton radioButton, radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6, radioButton7, radioButton8, radioButton9;
    private RadioGroup rgAge, rgHIV, rgDonate, rgIllness, rgFuture;
    private Button submitButton;
    private EditText editText;
    private Toolbar toolbar;
    private ProgressDialog loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_requirements, container, false);

        toolbar = itemView.findViewById(R.id.toolbar);

        rgAge = itemView.findViewById(R.id.rgAge);
        rgHIV = itemView.findViewById(R.id.rgHIV);
        rgDonate = itemView.findViewById(R.id.rgDonate);
        rgIllness = itemView.findViewById(R.id.rgIllness);
        rgFuture = itemView.findViewById(R.id.rgFuture);

        radioButton = itemView.findViewById(R.id.radioButton);
        radioButton1 = itemView.findViewById(R.id.radioButton1);
        radioButton2 = itemView.findViewById(R.id.radioButton2);
        radioButton3 = itemView.findViewById(R.id.radioButton3);
        radioButton4 = itemView.findViewById(R.id.radioButton4);
        radioButton5 = itemView.findViewById(R.id.radioButton5);
        radioButton6 = itemView.findViewById(R.id.radioButton6);
        radioButton7 = itemView.findViewById(R.id.radioButton7);
        radioButton8 = itemView.findViewById(R.id.radioButton8);
        radioButton9 = itemView.findViewById(R.id.radioButton9);

        submitButton = itemView.findViewById(R.id.submitButton);
        editText = itemView.findViewById(R.id.editTextNumber);

        loader = new ProgressDialog(getContext());



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String weight = editText.getText().toString().trim();

                if (TextUtils.isEmpty(weight)){
                   editText.setError("Weight is Required");
                   return;
                }
//
                if (Integer.parseInt(editText.getText().toString()) < 50){
                    editText.setError("You are underweight to donate blood");
                    return;
                }

                if(radioButton1.isChecked()){
                    Toast.makeText(getContext(), "You are underage to donate blood", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if(radioButton2.isChecked()){
                    Toast.makeText(getContext(), "You cannot donate if you are HIV Positive", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if(radioButton6.isChecked()){
                    Toast.makeText(getContext(), "You cannot donate if you have chronic illnesses", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if(!radioButton4.isChecked() && !radioButton5.isChecked()) {
                    Toast.makeText(getContext(), "Check if you have donated blood before", Toast.LENGTH_SHORT ).show();
                    return;

                }

                if(!radioButton8.isChecked() && !radioButton9.isChecked()) {
                    Toast.makeText(getContext(), "Check if you are willing to donate blood again", Toast.LENGTH_SHORT ).show();
                    return;

                }

                if(!radioButton.isChecked() || !radioButton3.isChecked() || !radioButton7.isChecked()){
                    Toast.makeText(getContext(), "You do not meet the requirements", Toast.LENGTH_LONG).show();

                }
                else {
                    loader.setMessage("Checking Requirements");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    Navigation.findNavController(view).navigate(R.id.action_requirementsFragment_to_bookingFragment);

                    loader.dismiss();
                }

            }
        });

        return itemView;
    }




}