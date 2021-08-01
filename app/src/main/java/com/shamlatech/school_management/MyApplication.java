package com.shamlatech.school_management;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shamlatech.utils.Vars;

/**
 * Created by  Martin Mundia Mugambi  on 11-03-2020.
 */

public class MyApplication extends Application {

    private static final String firebaseURL = Vars.FirebaseURL;
    private static DatabaseReference firebaseRef;
    private static MyApplication ourInstance;
    public static DatabaseReference getFirebaseRef() {
        return firebaseRef;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

        FirebaseApp.initializeApp(this);
        firebaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        auth.signInWithEmailAndPassword(Vars.FirebaseUsername, Vars.FirebasePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("fb conn state : " + task.isSuccessful());
                if (!task.isSuccessful()) {
                    System.out.println("fb conn state failed : " + task.getException());
                }else{
                    System.out.println("Authenticated!");
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}