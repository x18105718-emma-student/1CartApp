package com.example.cartproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private EditText registerName;
    private EditText registerPassword;
    private Button registerButton;

    public static Credentials cred;

    //make a reference to shared preferences
    SharedPreferences sharedPref;

    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerName = findViewById(R.id.regName);
        registerPassword = findViewById(R.id.regPassword);
        registerButton = findViewById(R.id.btnReg);

        sharedPref = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE); // CredentialsDB is the file name values will be stored in
        sharedPreferencesEditor = sharedPref.edit();

        //system checks creds when register button is clicked
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String registeredName = registerName.getText().toString();
                String registeredPassword = registerPassword.getText().toString();

                // check the inputs are valid
                if (validate(registeredName, registeredPassword)) {
                    //add the users username and password to the database
                    cred = new Credentials(registeredName, registeredPassword);

                    //this is how credentials are stored to shared preferences
                    sharedPreferencesEditor.putString("Username", registeredName);
                    sharedPreferencesEditor.putString("Password", registeredPassword);
                    sharedPreferencesEditor.apply();

                    Toast.makeText(Register.this, "Success! Welcome to 1Cart", Toast.LENGTH_SHORT).show();

                    //redirect to login
                    startActivity(new Intent(Register.this, MainActivity.class));
                }
            }
        });
    }

    boolean validate(String name, String password) {
        //password validation
        if (name.isEmpty() || (password.length() < 6)) {
            Toast.makeText(this, "Password must be longer than 6 characters ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}