package com.hfad.novelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {

    private Button verifyCode;
    private EditText userCode;
    private EditText userName;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);




        final String verificationCode = (String) getIntent().getSerializableExtra("verificationCode");
        final String phoneNumber = (String) getIntent().getSerializableExtra("phoneNo");

        verifyCode = (Button) findViewById(R.id.verify_me__button);
        userCode = (EditText) findViewById(R.id.user_input);
        userName = (EditText) findViewById(R.id.editText4);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                if(userCode.getText().toString().equals(verificationCode)) {

                    Map<String, User> users = new HashMap<String, User>();
                    users.put(phoneNumber,new User(userName.getText().toString()));

                   dbref.child(phoneNumber).setValue(new User(userName.getText().toString()));

                    Toast.makeText(getApplicationContext(), "You're In!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(VerificationActivity.this,StoriesActivity.class);
                    intent.putExtra("Username",userName.getText().toString());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Please enter the correct code", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
