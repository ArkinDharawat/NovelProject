package com.hfad.novelproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mMatchmaker = database.getReference("message");
    private DatabaseReference mMatchmaker1 = database.getReference("message/users");


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hfad.novelproject", appContext.getPackageName());


    }

    @Test
    public void putStringInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        mMatchmaker.setValue("exp").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void putInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        DatabaseReference dbref = mMatchmaker.child("users");

        dbref.child("user_1").setValue(new User("Lsdf","Lddsd")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });
        writeSignal.await(10, TimeUnit.SECONDS);
    }



    @Test
    public void readStringInRealtimeDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);


        mMatchmaker1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.v("Value",dataSnapshot.getValue(User.class).toString());
                writeSignal.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}

