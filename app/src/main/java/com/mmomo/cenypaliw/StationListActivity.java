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
    List<String> stationName=new ArrayList<>();
    List<String>  stationStreet=new ArrayList<>();
    List<String>  stationCity=new ArrayList<>();
    List<String>  stationPrice=new ArrayList<>();


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

        listView=findViewById(R.id.stationListView);

        YourStationDatabase yourStationDatabase = new YourStationDatabase(this);
        //TODO: ZOBACZYC GDZIE SĄ PRZECHOWYWANE BAZY DANYCH I CZEMU SIENIE TWORZY UZYWAJĄC onCreate
        //List<YourGasStation> stations = new YourStationDatabase(this).getYourStationList();
        stations = new YourStationDatabase(this).getYourStationList();
        YourStationListAdapter yourStationListAdapter = new YourStationListAdapter(this,stations);
        for (YourGasStation station : stations) {
            stationName.add(station.getName());
            stationStreet.add(station.getStreet());
            stationCity.add(station.getCity());
            stationPrice.add(String.valueOf(station.getRON95()));
        }


        MyAdapter adapter=new MyAdapter(this,stationName,stationPrice,stationStreet,stationCity, gasStationIcons);
        if(stations.size()!=0){
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<position;i++) {
                    Toast.makeText(StationListActivity.this, stationName.get(i) + " cenka: "+ stationPrice.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ListView listView = (ListView) findViewById(R.id.stationListView);
        listView.setAdapter(yourStationListAdapter);
        } else{
            Toast.makeText(this, "Lista stacji pusta", Toast.LENGTH_SHORT).show();
        }
    }


    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        List<String> stationName;
        List<String>  stationStreet;
        List<String>  stationCity;
        List<String>  stationPrice;
        int[] stationImage;

        MyAdapter(Context c, List<String> name, List<String> price, List <String> street, List<String>city, int[] img){
            //Hold values we want to show in Station List row
            super(c,R.layout.station_list_row,name);
            this.context=c;
            this.stationName=name;
            this.stationPrice=price;
            this.stationStreet=street;
            this.stationCity=city;
            this.stationImage=img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //????
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.station_list_row,parent,false);
            //Access to actual row in layout
            ImageView images=row.findViewById(R.id.stationIconView);
            TextView stationName=row.findViewById(R.id.stationNameTextView);
            TextView stationPrice=row.findViewById(R.id.stationPetrolPriceValue);
            TextView stationCity=row.findViewById(R.id.stationCityTextView);
            TextView stationStreet=row.findViewById(R.id.stationStreeTextView);

            //Replace default text from layout with data stored in our Adapter
            stationName.setText(this.stationName.get(position));
            stationPrice.setText(this.stationPrice.get(position));
            stationStreet.setText(this.stationStreet.get(position));
            Toast.makeText(context, "cosik sie", Toast.LENGTH_SHORT).show();

            stationCity.setText(this.stationCity.get(position));

            if(this.stationName.get(position).contains("ORLEN")) {
                //Set station icon using its name and enum
                stationImage[position] = gasStationIcons[GasStationNames.getPosition(ORLEN)];
            } else if(this.stationName.get(position).contains("LOTOS")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(LOTOS)];
            } else if(this.stationName.get(position).contains("GROSAR")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(GROSAR)];
            } else if(this.stationName.get(position).contains("BP")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(BP)];
            } else {

                stationImage[position]=gasStationIcons[GasStationNames.getPosition(NONE)];
            }
            images.setImageResource(stationImage[position]);

            return row;
        }
    }
}