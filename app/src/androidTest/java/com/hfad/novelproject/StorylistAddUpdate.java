package com.hfad.novelproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by arkin on 28/11/16.
 */

public class StorylistAddUpdate {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref = database.getReference("Genre");

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hfad.novelproject", appContext.getPackageName());


    }

    @Test
    public void putInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        Map<String, StoryItem> stories = new HashMap<String,StoryItem>();

        stories.put("The Seventh Son",new StoryItem("horror","123456789"));
        stories.put("Tale in the Servants",new StoryItem("comedy","1357911131"));
        stories.put("Bare Sword",new StoryItem("action","2456789011"));

        dbref.setValue(stories).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
