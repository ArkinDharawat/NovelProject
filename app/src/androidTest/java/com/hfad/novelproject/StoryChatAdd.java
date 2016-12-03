package com.hfad.novelproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by arkin on 29/11/16.
 */

public class StoryChatAdd {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("Story");
    private DatabaseReference dbrefG = database.getReference("Genre");

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hfad.novelproject", appContext.getPackageName());


    }

    @Test
    public void putInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        dbrefG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object jlang = dataSnapshot.getValue();
                HashMap<String,HashMap<String,String>> randomKeys = (HashMap<String,HashMap<String,String>>) jlang;
                Map<String,String> stories = new HashMap<>();

                String [] firstLines = {"She had made a poor job of hiding the damage","His voice had never sounded so cold","He knew he must keep very still while he waited",
                "She stood out from the crowd because","He knew he must keep very still while he waited","They'd had a lot of freedom back then and not just because their environment was safer - few children had been allowed to roam as much as they",
                "The horse came back alone","Why had no-one ever mentioned Mum's twin?","He sat her down and held her close before telling her the terrible news",
                "After five years, he just happened to be walking down her street?","The little boy's idea of heaven was"};
                int i = 0;

                Set<String> keySet = randomKeys.keySet();
                for (String s: keySet) {
                    stories.put(randomKeys.get(s).get("storyID"),firstLines[i]);
                    dbref.setValue(stories).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
                    i+=1;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        writeSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void readStringInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object jObj = dataSnapshot.getValue();
                System.out.println("jlnag "+jObj.getClass());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}


