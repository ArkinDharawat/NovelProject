package com.hfad.novelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

;

/**
 * A login screen that offers login via email/password.
 */
public class StartLoginActivity extends AppCompatActivity {

    private OkHttpClient mClient = new OkHttpClient();
    private EditText phNumber;
    private Button signUp;
    private String VerificationCode;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);

        phNumber = (EditText) findViewById(R.id.editText2);
        signUp = (Button) findViewById(R.id.button);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = phNumber.getText().toString();
                //sending sms
                if (number.substring(0, 2).equals("+1") && number.length() == 12) {

                    //Check if the number already exists
                    // if it does start Story Activity

                    dbref.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                //user exists, start story activity
                                Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                                HashMap<String,String> userVals = (HashMap<String, String>) snapshot.getValue();


                                Intent intent = new Intent(StartLoginActivity.this, StoriesActivity.class);
                                intent.putExtra("Username",userVals.get("name"));
                                intent.putExtra("Phone Number",snapshot.getKey());
                                startActivity(intent);
                            } else {
                                //user does not exist, send sms
                                sendVerification();
                                Intent intent = new Intent(StartLoginActivity.this, VerificationActivity.class);
                                intent.putExtra("verificationCode", VerificationCode);
                                intent.putExtra("Phone Number", number);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter a Valid Number", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * Send verification message
     */
    void sendVerification() {
        try {
            post("http://34adbdfb.ngrok.io/sms", new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            phNumber.setText("");
                            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * make POST request to ngrok server
     *
     * @param url
     * @param callback
     * @return
     * @throws IOException
     */
    Call post(String url, Callback callback) throws IOException {
        String phoneNumber = phNumber.getText().toString();
        VerificationCode = "";
        for (int i = 0; i < 8; i += 2) {
            VerificationCode = VerificationCode + phoneNumber.substring(i + 2, i + 3);
        }
        RequestBody formBody = new FormBody.Builder()
                .add("To", phoneNumber)
                .add("Body", "" + "\n\n" + " Your Verification Code is: " + VerificationCode)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;

    }
}
