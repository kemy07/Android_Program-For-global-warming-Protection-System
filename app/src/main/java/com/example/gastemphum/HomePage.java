package com.example.gastemphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    Button log_out, chart_graph;
    TextView temperature , humidity , gass_;
    private FirebaseAuth mAuth;
    private LineChart Temp_linechart , Intersection_linechart, Gas_linechart;
    ArrayList<Entry> tempData;
    ArrayList<Entry> intersectionData;
    ArrayList<Entry> gasData;
    DatabaseReference mPostReference;

    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        temperature = (TextView) findViewById(R.id.temperature_info);
        //humidity = (TextView) findViewById(R.id.humidity_info);
        gass_ = (TextView) findViewById(R.id.gas_info);

        mAuth = FirebaseAuth.getInstance();
        //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        chart_graph = (Button) findViewById(R.id.graph_btn);
        chart_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CardViewChart.class));
                finish();
            }
        });
        log_out = (Button) findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mAuth.signOut();
                Toast.makeText(Settings.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.this, SignInAsStudent.class));
                finish();*/
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomePage.this);
                alertBuilder.setCancelable(false).setMessage("Do you want to log out of the application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Toast.makeText(HomePage.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomePage.this, SignIn.class));
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setTitle("Do you want to go to the login page?");
                alertDialog.show();
            }
        });


        mPostReference = FirebaseDatabase.getInstance().getReference("Data");
        mPostReference.child("PfPCc9312INByZYVzERwFCmV0bi1Readings").addValueEventListener(valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            /*    String tempp = dataSnapshot.child("temperature").getValue().toString();
                String humm = dataSnapshot.child("humidity").getValue().toString();
                String gass = dataSnapshot.child("gas").getValue().toString();

                temperature.setText(tempp);
                humidity.setText(humm);
                gas.setText(gass);*/

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String SV = ds.child("temperature").getValue().toString();
                    temperature.setText(SV);
                }
               /* for (DataSnapshot ds : dataSnapshot.getChildren()){
                    i=i+1;
                    String hum = ds.child("humidity").getValue().toString();
                    Float SensorValuehum = Float.parseFloat(hum);
                    intersectionData.add(new Entry(i,SensorValuehum));
                    //humidity.setText(hum);

                } */
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String gas = ds.child("gas").getValue().toString();
                    gass_.setText(gas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false).setMessage("Do you want to exit the program?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Exit the app");
        alertDialog.show();
    }
}