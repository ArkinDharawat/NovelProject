package com.hfad.novelproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private String userName;
    private String storyId;
    private String storyTitle;
    private SimpleAdapter chatListAdapter;
    private ListView chatMessages;
    private Button sendMessage;
    private EditText message;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbrefC = database.getReference("Chat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userName = (String) getIntent().getSerializableExtra("userName");
        storyId = (String) getIntent().getSerializableExtra("id");
        storyTitle = (String) getIntent().getSerializableExtra("title");

        chatMessages = (ListView) findViewById(R.id.chat_list);

        setTitle(storyTitle);



        dbrefC.child(storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<HashMap<String,String>> chatContent = new ArrayList<HashMap<String, String>>();

                ArrayList<HashMap<String,String>>  posToChat =  new ArrayList<HashMap<String, String>>();

                posToChat = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();

                chatListAdapter = new SimpleAdapter(getApplicationContext(),posToChat,R.layout.chat_detail,
                        new String[]{"line","author"},new int[]{R.id.textView4,R.id.textView5});

                chatMessages.setAdapter(chatListAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendMessage = (Button) findViewById(R.id.button2);
        message = (EditText) findViewById(R.id.editText3);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "LoL, you did not enter any text", Toast.LENGTH_SHORT).show();
                } else {
                    dbrefC.child(storyId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            ArrayList<HashMap<String,String>> chatContent = new ArrayList<HashMap<String, String>>();

                            ArrayList<HashMap<String,String>>  posToChat =  new ArrayList<HashMap<String, String>>();

                            posToChat = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();

                            HashMap<String,String> newMessage = new HashMap<String, String>();
                            newMessage.put("author",userName);
                            newMessage.put("line",message.getText().toString());
                            posToChat.add(newMessage);

                            HashMap<String,Object> chatUpdate = new HashMap<String, Object>();
                            chatUpdate.put(storyId,posToChat);
                            dbrefC.updateChildren(chatUpdate);
                            message.setText("");

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });


    }
}
