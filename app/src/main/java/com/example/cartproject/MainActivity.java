package com.example.cartproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int counter = 4;
    boolean isValid = false;
    private EditText nAme;
    private EditText pAssword;
    private Button lOgin;
    private TextView aTtemptsInfo;
    private TextView rEegister;
    private CheckBox rememberMe;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nAme = findViewById(R.id.nAme);
        pAssword = findViewById(R.id.pAssword);
        lOgin = findViewById(R.id.btnLogin);
        aTtemptsInfo = findViewById(R.id.aTtempts);
        rEegister = findViewById(R.id.rEgister);
        rememberMe = findViewById(R.id.rememberMe);

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if (sharedPreferences != null) {
            String savedUsername = sharedPreferences.getString("Username", "");
            String savedPassword = sharedPreferences.getString("Password", "");

            Register.cred = new Credentials(savedUsername, savedPassword);

            if (sharedPreferences.getBoolean("RememberMeCheckbox", false)) {
                nAme.setText(savedUsername);
                pAssword.setText(savedPassword);
                rememberMe.setChecked(true);
            }
        }

        rEegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        lOgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = nAme.getText().toString();
                String userPassword = pAssword.getText().toString();

                /* Check if the user inputs are empty */
                if (userName.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter valid username and password", Toast.LENGTH_LONG).show();

                } else {

                    isValid = validate(userName, userPassword);

                    if (!isValid) {

                        counter--;


                        aTtemptsInfo.setText("Attempts Remaining: " + String.valueOf(counter));

                        //disable button after 4 attempts
                        if (counter == 0) {
                            lOgin.setEnabled(false);
                            Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_LONG).show();
                        }

                        else {
                            Toast.makeText(MainActivity.this, "Invalid login, try again", Toast.LENGTH_LONG).show();
                        }
                    }

                    else {
                        sharedPreferencesEditor.putBoolean("RememberUserCheckbox", rememberMe.isChecked());
                        sharedPreferencesEditor.apply();
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                    }

                }
            }
        });
    }

    private boolean validate(String username, String userPassword) {

        if (Register.cred != null) {
            if (username.equals(Register.cred.getUsername()) && userPassword.equals(Register.cred.getPassword())) {
                return true;
            }
        }


        return false;
    }
}