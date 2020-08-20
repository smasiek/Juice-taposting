package com.mmomo.cenypaliw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.mmomo.cenypaliw.GasStationNames.*;
import static com.mmomo.cenypaliw.GasStationIcons.*;

public class StationListActivity extends AppCompatActivity {

    ListView listView;
    List<String> stationName = new ArrayList<>();
    List<String> stationStreet = new ArrayList<>();
    List<String> stationCity = new ArrayList<>();
    List<String> stationPrice = new ArrayList<>();
    List<Integer> stationImage = new ArrayList<>();

    List<YourGasStation> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton addStationButton = findViewById(R.id.addStationButton);
        addStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addStationButton.getContext(), AddYourStation.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.stationListView);

        //TODO: ZOBACZYC GDZIE SĄ PRZECHOWYWANE BAZY DANYCH I CZEMU SIENIE TWORZY UZYWAJĄC onCreate

        stations = new YourStationDatabase(this).getYourStationList();
        YourStationListAdapter yourStationListAdapter = new YourStationListAdapter(this, stations);
        for (YourGasStation station : stations) {
            stationName.add(station.getName());
            stationStreet.add(station.getStreet());
            stationCity.add(station.getCity());
            stationPrice.add(String.valueOf(station.getRON95()));
        }

        if (stations.size() != 0) {
            listView.setAdapter(yourStationListAdapter);
        } else {
            Toast.makeText(this, "Station list is empty!\nAdd some stations to your list :)", Toast.LENGTH_SHORT).show();
        }
    }

}