package com.hfad.novelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private Button saveToUser;
    private EditText storyText;
    private String userName;
    private String storyId;
    private String storyTitle;
    private String phoneNumber;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("wasUserName",userName);
        savedInstanceState.putString("wasId", storyId);
        savedInstanceState.putString("wasTitle",storyTitle );
        savedInstanceState.putString("wasNumber",phoneNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        userName = (String) getIntent().getSerializableExtra("userName");
        storyId = (String) getIntent().getSerializableExtra("id");
        storyTitle = (String) getIntent().getSerializableExtra("title");
        phoneNumber = (String) getIntent().getSerializableExtra("Phone Number");

        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("wasUserName");
            storyId = savedInstanceState.getString("wasId");
            storyTitle = savedInstanceState.getString("wasTitle");
            phoneNumber = savedInstanceState.getString("wasNumber");
        }

        setTitle(storyTitle);
        final DatabaseReference dbref = database.getReference("Story");

        dbref.child(storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView stroyContent = (TextView) findViewById(R.id.textView3);
                stroyContent.setText((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button sendText = (Button) findViewById(R.id.button2);
        storyText = (EditText) findViewById(R.id.editText3);

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storyText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "LoL, you did not enter any text", Toast.LENGTH_SHORT).show();
                } else {

                    TextView storyContent = (TextView) findViewById(R.id.textView3);
                    String updateStoryText = storyContent.getText() + ". " + storyText.getText().toString();
//                    System.out.println("You entered this " + updateStoryText);

                    HashMap<String,Object> updateStroy = new HashMap<String,Object>();
                    updateStroy.put(storyId,updateStoryText);
                    dbref.updateChildren(updateStroy);
                    storyText.setText("");
                }

            }
        });

        Button saveToUser = (Button) findViewById(R.id.button5);

        saveToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference someUser = database.getReference("users");

                someUser.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userVals = (HashMap<String, Object>) dataSnapshot.getValue();

                        ArrayList<String> newSavedBooks = new ArrayList<String>();
                        try {

                            boolean present = false;
                            newSavedBooks.addAll((ArrayList<String>)userVals.get("savedBooks"));

                            for(String s:newSavedBooks) {
                                if(s.equals(storyId)){
                                    present = true;
                                    break;
                                }
                            }

                            if (!present)
                                newSavedBooks.add(storyId);

                        } catch (NullPointerException e) {
                            //Change this to log
                            Log.e(WritingActivity.this.getClass().getSimpleName(),"Null pointer exception");
                            newSavedBooks.add(storyId);
                        }

//                        System.out.println(newSavedBooks);

                        HashMap<String,Object> userUpdate = new HashMap<String, Object>();
                        userUpdate.put("savedBooks",newSavedBooks);

                        someUser.child(phoneNumber).updateChildren(userUpdate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        Button toChat = (Button) findViewById(R.id.button6);

        toChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WritingActivity.this,ChatActivity.class);
                intent.putExtra("title",storyTitle);
                intent.putExtra("id",storyId);
                intent.putExtra("userName",userName);
                intent.putExtra("Phone Number",phoneNumber);
                startActivity(intent);
            }
        });

    }
}
