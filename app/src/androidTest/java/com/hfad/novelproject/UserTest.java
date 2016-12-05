package com.hfad.novelproject;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by arkin on 04/12/16.
 */

public class UserTest {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("users");

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hfad.novelproject", appContext.getPackageName());


    }

    @Test
    public void putInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,Object> userNumbers = (HashMap<String,Object>) dataSnapshot.getValue();
                Set<String> keySet = userNumbers.keySet();
                for (String s: keySet) {
                    if (s.equals("+11234567891")){
                        HashMap<String,String> user = (HashMap<String,String>) userNumbers.get(s);
                       assertEquals("Doe",user.get("name"));
                    } else if(s.equals("+11234567890")){
                        HashMap<String,String> user = (HashMap<String,String>) userNumbers.get(s);
                        assertEquals("Joe",user.get("name"));

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
