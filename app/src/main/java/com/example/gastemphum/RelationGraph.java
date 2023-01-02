package com.example.gastemphum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
public class RelationGraph extends AppCompatActivity {


    String hrValue;
    String hrValueTimestamp;

    ArrayList<String> array2; //array for tmpHr value
    ArrayList<String> array7; //array for hrValueTimestamp
    LineGraphSeries<DataPoint> series;
    int x = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation_graph);
        DatabaseReference mPostReference;
        mPostReference = FirebaseDatabase.getInstance().getReference("Data");
       final GraphView graph =  findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        array2 = new ArrayList<String>(); //array for tmpHR
        array7 = new ArrayList<>(); //array for hrValueTimestamp
        mPostReference.child("PfPCc9312INByZYVzERwFCmV0bi1Readings")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        hrValue = dataSnapshot.child("temperature").getValue(String.class);
                        hrValueTimestamp = dataSnapshot.child("gas").getValue(String.class);
                        //int timest = Integer.parseInt(hrValueTimestamp);
                        Log.i("timestamp value", "timestamp value " + hrValueTimestamp);
                        Log.i("hr value", "hr value " + hrValue);
                        array2.add(hrValue);
                        array7.add(hrValueTimestamp);
                        x = x + 1;

                        DataPoint point = new DataPoint(x, Double.parseDouble(hrValue));
                        series.appendData(point, false, 1000);
                        graph.setTitle("Relation Graph");
                        graph.setCursorMode(true);
                        graph.setTitleColor(Color.RED);
                        graph.setTitleTextSize(100);
                        graph.setBackgroundColor(Color.WHITE);
                        graph.addSeries(series);
                        graph.getGridLabelRenderer()
                                .setLabelFormatter(new DefaultLabelFormatter() {
                                    @Override
                                    public String formatLabel(double value, boolean isValueX) {
                                        if (isValueX) {
                                            return hrValueTimestamp;
                                        } else {
                                            return super.formatLabel(value, isValueX);

                                        }


                                    }

                                });
                        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
                        graph.getGridLabelRenderer().setNumHorizontalLabels(10);
                        graph.getGridLabelRenderer().setNumVerticalLabels(8);

                    }


                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RelationGraph.this, CardViewChart.class));
        finish();
    }
}