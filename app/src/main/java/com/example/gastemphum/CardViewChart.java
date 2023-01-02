package com.example.gastemphum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CardViewChart extends AppCompatActivity {

    CardView tempcard , gascard , relationcard ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_chart);

        tempcard = (CardView) findViewById(R.id.Temperature_cardview);
        gascard = (CardView) findViewById(R.id.Gas_card_view);
        relationcard = (CardView) findViewById(R.id.relation_card_view);

        tempcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardViewChart.this, TemperatureGraph.class));
                finish();
            }
        });
        gascard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardViewChart.this, GasGraph.class));
                finish();
            }
        });
        relationcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardViewChart.this, RelationGraph.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CardViewChart.this, HomePage.class));
        finish();

    }
}