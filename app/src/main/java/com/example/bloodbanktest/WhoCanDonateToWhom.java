package com.example.bloodbanktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class WhoCanDonateToWhom extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView image;
    private Spinner spinner;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_can_donate_to_whom);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enter Blood Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        spinner=findViewById (R.id.spinner);
        image=findViewById (R.id.image);
        text=findViewById (R.id.text);

        String [] BloodGroup= {"Select Your Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, BloodGroup);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                switch (i) {
                    case 1:text.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.blood_a_p);
                    break;

                    case 2:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_a_n);
                        break;
                    case 3:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_b_p);
                        break;
                    case 4:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_b_n);
                        break;
                    case 5:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_ab_p);
                        break;
                    case 6:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_ab_n);
                        break;
                    case 7:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_o_p);
                        break;
                    case 8:
                        text.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.blood_o_n);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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