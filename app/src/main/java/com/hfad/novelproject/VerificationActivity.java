package com.hfad.novelproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    private Button verifyCode;
    private EditText userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
       final String verificationCode = (String) getIntent().getSerializableExtra("verificationCode");

        verifyCode = (Button) findViewById(R.id.verify_me__button);
        userCode = (EditText) findViewById(R.id.user_input);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userCode.getText().toString().equals(verificationCode))
                    Toast.makeText(getApplicationContext(), "You're In!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Please enter the correct code", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
