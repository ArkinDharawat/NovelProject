package com.hfad.novelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedStoriesActivity extends AppCompatActivity {
    private String userName;
    private String phoneNumber;
    private SimpleAdapter storyTitleDetailAdapter;
    private DatabaseReference storyRef;
    private  DatabaseReference userDbRef;
    private ArrayList<HashMap<String,String>> titleGenre = new ArrayList<HashMap<String, String>>();
    private ListView storiesList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);

        userName = (String) getIntent().getSerializableExtra("userName");
        phoneNumber = (String) getIntent().getSerializableExtra("Phone Number");
        storiesList = (ListView) findViewById(R.id.saved_list);
        storyRef = database.getReference("Genre");

        System.out.println(userName + phoneNumber);

        userDbRef = database.getReference("users/"+phoneNumber);

        userDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, Object> aUser = (HashMap<String, Object>) dataSnapshot.getValue();
                final ArrayList<String> savedBooks = (ArrayList<String>) aUser.get("savedBooks");

                storyRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (String sID : savedBooks) {

                            for (DataSnapshot player : dataSnapshot.getChildren()) {
                                HashMap<String, String> idGen = (HashMap<String, String>) player.getValue();
                                HashMap<String, String> nameGen = new HashMap<String, String>();

                                if (sID.equals(idGen.get("storyID"))) {
                                    nameGen.put("title", player.getKey());
                                    nameGen.put("genre", idGen.get("genre"));
                                    titleGenre.add(nameGen);
                                    break;
                                }
                            }
                        }

                        storyTitleDetailAdapter = new SimpleAdapter(getApplicationContext(), titleGenre, R.layout.story_detail,
                                new String[]{"title", "genre"}, new int[]{R.id.textView, R.id.textView2});

                        storiesList.setAdapter(storyTitleDetailAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });


                //Initially Build the story list
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        storiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * t
             * @param adapterView
             * @param view
             * @param position
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                userDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final HashMap<String,Object> aUser = (HashMap<String,Object>)dataSnapshot.getValue();
                        final ArrayList<String> savedBooks = (ArrayList<String>) aUser.get("savedBooks");

                        System.out.println(savedBooks);

                        storyRef = database.getReference("Genre");


                        //Initially Build the story list
                        storyRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot player : dataSnapshot.getChildren()) {
                                    HashMap<String,String> idGen = (HashMap<String, String>) player.getValue();
                                    HashMap<String,String> nameGen = new HashMap<String, String>();

                                    if (idGen.get("storyID").equals(savedBooks.get(position))) {

                                        Intent intent = new Intent(SavedStoriesActivity.this,WritingActivity.class);
                                        intent.putExtra("userName",userName);
                                        intent.putExtra("id",idGen.get("storyID"));
                                        intent.putExtra("title",player.getKey());
                                        intent.putExtra("Phone Number",phoneNumber);
                                        startActivity(intent);
                                    }

                            }
                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }});


    }
}
