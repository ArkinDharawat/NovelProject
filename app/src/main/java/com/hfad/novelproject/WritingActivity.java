package com.hfad.novelproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WritingActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button sendText;
    private Button saveToUser;
    private EditText storyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        final String userName = (String) getIntent().getSerializableExtra("userName");
        final String storyId = (String) getIntent().getSerializableExtra("id");
        final String storyTitle = (String) getIntent().getSerializableExtra("title");

        setTitle(storyTitle);
        final DatabaseReference dbref = database.getReference("Story");

        dbref.child(storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView stroyContent = (TextView) findViewById(R.id.textView3);
                stroyContent.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendText = (Button) findViewById(R.id.button2);
        storyText = (EditText) findViewById(R.id.editText3);

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storyText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "LoL, you did not enter any text", Toast.LENGTH_SHORT).show();
                } else {

                    TextView stroyContent = (TextView) findViewById(R.id.textView3);
                    String updateStroyText = stroyContent.getText() + ". " + storyText.getText().toString();
                    System.out.println("You entered this " + updateStroyText);

                    HashMap<String,Object> updateStroy = new HashMap<String,Object>();
                    updateStroy.put(storyId,updateStroyText);
                    dbref.updateChildren(updateStroy);
                    storyText.setText("");
                }

            }
        });

        saveToUser = (Button) findViewById(R.id.button5);

        saveToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storyText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "LoL, you did not enter any text", Toast.LENGTH_SHORT).show();
            } else {
                     final DatabaseReference userRef = database.getReference("users");
                     userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             ArrayList<String> newSavedBooks = new ArrayList<String>();
                             String number = "";

                             HashMap<String,Object>  userData = (HashMap<String, Object>) dataSnapshot.getValue();

                             for (String s : userData.keySet()){
                                 HashMap<String,Object>  user = (HashMap<String,Object>) userData.get(s);

                                 if(user.get("name").equals(userName)){
                                     try {
                                         newSavedBooks.addAll((ArrayList<String>)user.get("savedBooks"));
                                     } catch (NullPointerException e) {
                                         //change to log
                                         System.out.println("NULL POINTER EXCEPTION !!!");
                                     }
                                     number = s;
                                 }
                             }
                             newSavedBooks.add(storyId);



                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
                }
        }});



    }
}
