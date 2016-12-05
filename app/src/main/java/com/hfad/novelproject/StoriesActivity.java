package com.hfad.novelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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

public class StoriesActivity extends AppCompatActivity {
    private SimpleAdapter storyTitleDetailAdapter;
    private ListView storiesList;
    private String userName;
    private String phoneNumber;
    private ArrayList<HashMap<String, String>> titleGenre = new ArrayList<HashMap<String, String>>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("Genre");

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("wasUser", userName);
        savedInstanceState.putString("wasNumber", phoneNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        storiesList = (ListView) findViewById(R.id.story_list);

        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("wasUser");
            phoneNumber = savedInstanceState.getString("wasNumber");
        }
        userName = (String) getIntent().getSerializableExtra("Username");
        phoneNumber = (String) getIntent().getSerializableExtra("Phone Number");
        setTitle("Welcome " + userName + " !");


        //Initially Build the story list
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    HashMap<String, String> idGen = (HashMap<String, String>) player.getValue();
                    HashMap<String, String> nameGen = new HashMap<String, String>();
                    nameGen.put("title", player.getKey());
                    nameGen.put("genre", idGen.get("genre"));
                    titleGenre.add(nameGen);
                }

                storyTitleDetailAdapter = new SimpleAdapter(getApplicationContext(), titleGenre, R.layout.story_detail,
                        new String[]{"title", "genre"}, new int[]{R.id.textView, R.id.textView2});

                storiesList.setAdapter(storyTitleDetailAdapter);

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        //search query
        final EditText searchBox = (EditText) findViewById(R.id.editText);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //search term
                final String searchQ = searchBox.getText().toString();

                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap<String, String>> newSearchList = new ArrayList<HashMap<String, String>>();

                        for (DataSnapshot player : dataSnapshot.getChildren()) {
                            HashMap<String, String> idGen = (HashMap<String, String>) player.getValue();
                            HashMap<String, String> nameGen = new HashMap<String, String>();

                            //search by genre
                            if (searchQ.contains("g:")) {
                                if (idGen.get("genre").contains(searchQ.substring(2))) {
                                    nameGen.put("title", player.getKey());
                                    nameGen.put("genre", idGen.get("genre"));
                                    newSearchList.add(nameGen);
                                }

                            }
                            //search by title
                            else if (player.getKey().contains(searchQ)) {
                                nameGen.put("title", player.getKey());
                                nameGen.put("genre", idGen.get("genre"));
                                newSearchList.add(nameGen);

                            }
                        }

                        storyTitleDetailAdapter = new SimpleAdapter(getApplicationContext(), newSearchList, R.layout.story_detail,
                                new String[]{"title", "genre"}, new int[]{R.id.textView, R.id.textView2});

                        storiesList.setAdapter(storyTitleDetailAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });


            }
        });


        //go to writing
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
//            System.out.println(searchBox.getText() + " and " + position);

                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String searchQ = searchBox.getText().toString();
                        ArrayList<HashMap<String, String>> newSearchList = new ArrayList<HashMap<String, String>>();

                        for (DataSnapshot player : dataSnapshot.getChildren()) {
                            HashMap<String, String> idGen = (HashMap<String, String>) player.getValue();
                            HashMap<String, String> nameGen = new HashMap<String, String>();

                            //search by genre
                            if (searchQ.contains("g:")) {
                                if (idGen.get("genre").contains(searchQ.substring(2))) {
                                    nameGen.put("title", player.getKey());
                                    //nameGen.put("genre",idGen.get("genre"));
                                    nameGen.put("id", idGen.get("storyID"));
                                    newSearchList.add(nameGen);
                                }

                            }
                            //search by title
                            else if (player.getKey().contains(searchQ)) {
                                nameGen.put("title", player.getKey());
                                //nameGen.put("genre",idGen.get("genre"));
                                nameGen.put("id", idGen.get("storyID"));
                                newSearchList.add(nameGen);

                            }
                        }

                        // getting the title we need !!!
                        Intent intent = new Intent(StoriesActivity.this, WritingActivity.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("id", newSearchList.get(position).get("id"));
                        intent.putExtra("title", newSearchList.get(position).get("title"));
                        intent.putExtra("Phone Number", phoneNumber);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });

            }
        });
        // go to saved stories
        Button savedStories = (Button) findViewById(R.id.button3);

        savedStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference userRef = database.getReference("users/" + phoneNumber + "/savedBooks");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Intent intent = new Intent(StoriesActivity.this, SavedStoriesActivity.class);
                            intent.putExtra("userName", userName);
                            intent.putExtra("Phone Number", phoneNumber);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "You got NO Saved Stories, mate", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
