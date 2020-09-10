package com.mmomo.cenypaliw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.mmomo.cenypaliw.GasStationNames.*;
import static com.mmomo.cenypaliw.GasStationIcons.*;

public class YourStationListActivity extends AppCompatActivity {

    ListView listView;

    List<YourGasStation> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        //Set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton addStationButton = findViewById(R.id.addStationButton);
        addStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addStationButton.getContext(), AddYourStationActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.stationListView);

        //TODO: ZOBACZYC GDZIE SĄ PRZECHOWYWANE BAZY DANYCH

        stations = new YourStationDatabase(this).getYourStationList();
        YourStationListAdapter yourStationListAdapter = new YourStationListAdapter(this, stations);

        if (stations.size() != 0) {
            //Handle exception when stations are empty and adapter can't be set
            listView.setAdapter(yourStationListAdapter);
        } else {
            Toast.makeText(this, "Station list is empty!\nAdd some stations to your list :)", Toast.LENGTH_SHORT).show();
        }
    }

}
