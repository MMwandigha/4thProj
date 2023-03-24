package com.example.bloodbanktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {

    private TextView backButton;
    private EditText AdminLoginEmail;
    private EditText AdminLoginPassword;
    private TextView AttemptsRemaining;
    private Button loginButton;
    private int counter = 5;

    String userName = "";
    String userPassword = "";

    /* Class to hold credentials */
    class Credentials
    {
        String name = "admin@gmail.com";
        String password = "123456";
    }

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        AdminLoginEmail = findViewById(R.id.AdminLoginEmail);
        AdminLoginPassword = findViewById(R.id.AdminLoginPassword);
        AttemptsRemaining = findViewById(R.id.AttemptsRemaining);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //obtain the user inputs
                userName = AdminLoginEmail.getText().toString();
                userPassword = AdminLoginPassword.getText().toString();

                if(userName.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(AdminLoginActivity.this, "Please enter name and password!", Toast.LENGTH_LONG).show();
                } else {
                    isValid = validate(userName, userPassword);
                    if (!isValid) {
                        counter--;
                        AttemptsRemaining.setText("No Of Attempts Remaining : "+ String.valueOf(counter));
                        if(counter == 0) {
                            loginButton.setEnabled(false);
                            Toast.makeText(AdminLoginActivity.this, "You have used all your attempts try again later!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Login Succesful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
                    }
                }
            }
        });
    }

    private boolean validate(String userName, String userPassword){
        Credentials credentials = new Credentials();
        if(userName.equals(credentials.name) && userPassword.equals(credentials.password))
        {
            return true;
        }

        return false;
    }
}