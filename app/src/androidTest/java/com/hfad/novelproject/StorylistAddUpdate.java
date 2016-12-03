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

    public static String RandomAlphaNumericString(int size){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String ret = "";
        int length = chars.length();
        for (int i = 0; i < size; i ++){
            ret += chars.split("")[ (int) (Math.random() * (length - 1)) ];
        }
        return ret;
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hfad.novelproject", appContext.getPackageName());


    }

    @Test
    public void putInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);


//        System.out.println("NUMBER " + RandomAlphaNumericString(11));

        Map<String, StoryItem> stories = new HashMap<String,StoryItem>();

        stories.put("The Seventh Son",new StoryItem("horror",RandomAlphaNumericString(11)));
        stories.put("Tale in the Servants",new StoryItem("comedy",RandomAlphaNumericString(11)));
        stories.put("Bare Sword",new StoryItem("action",RandomAlphaNumericString(11)));
        stories.put("The Snows Sparks",new StoryItem("comedy",RandomAlphaNumericString(11)));
        stories.put("The Flight's Force",new StoryItem("horror",RandomAlphaNumericString(11)));
        stories.put("Whispering Door",new StoryItem("horror",RandomAlphaNumericString(11)));
        stories.put("The Fallen Man",new StoryItem("fantasy",RandomAlphaNumericString(11)));
        stories.put("The Silk of the Wave",new StoryItem("adventure",RandomAlphaNumericString(11)));
        stories.put("The Dreams's Weeping",new StoryItem("fantasy",RandomAlphaNumericString(11)));
        stories.put("The Absent Prince",new StoryItem("comedy",RandomAlphaNumericString(11)));


        dbref.setValue(stories).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
