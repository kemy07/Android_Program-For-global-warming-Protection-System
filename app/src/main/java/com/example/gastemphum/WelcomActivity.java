package com.example.gastemphum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomActivity extends AppCompatActivity {
// android --> gastemperature.apk
    //ios --> ios
    //windows -- > win
    // lin
    private int DELAY_MILLIS = 2000; //2.0s
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;

    private  FirebaseUser firebaseUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user != null) {
                    // User is signed in

                    startActivity(new Intent(WelcomActivity.this, HomePage.class));
                    finish();

                }
                else
                {
                    startActivity(new Intent(WelcomActivity.this, SignIn.class));
                    finish();
                }


            }


        }, DELAY_MILLIS);
    }
}


