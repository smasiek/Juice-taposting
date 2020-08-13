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

import static com.mmomo.cenypaliw.GasStationNames.*;
import static com.mmomo.cenypaliw.GasStationIcons.*;

public class StationListActivity extends AppCompatActivity {

    ListView listView;
    String[] stationName ={"Orlen","Bliska"};
    String[] stationPrice ={"4.19","4.30"};


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

        MyAdapter adapter=new MyAdapter(this,stationName,stationPrice,gasStationIcons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<position;i++) {
                    Toast.makeText(StationListActivity.this,stationName[i] + " cenka: "+ stationPrice[i], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String[] stationName;
        String[] stationPrice;
        int[] stationImage;

        MyAdapter(Context c, String[] name, String[] price, int[] img){
            super(c,R.layout.station_list_row,name);
            this.context=c;
            this.stationName=name;
            this.stationPrice=price;
            this.stationImage=img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.station_list_row,parent,false);
            ImageView images=row.findViewById(R.id.stationIconView);
            TextView stationName=row.findViewById(R.id.stationNameTextView);
            TextView stationPrice=row.findViewById(R.id.stationPetrolPriceValue);
            if(this.stationName[position].equals("Orlen")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(ORLEN)];
            }else if(this.stationName[position].equals("Lotos")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(LOTOS)];
            }else if(this.stationName[position].equals("Grosar")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(GROSAR)];
            }else if(this.stationName[position].equals("BP")){
                stationImage[position]=gasStationIcons[GasStationNames.getPosition(BP)];
            }else {
                stationImage[position] = gasStationIcons[GasStationNames.getPosition(NONE)];
            }
            images.setImageResource(stationImage[position]);
            stationName.setText(this.stationName[position]);
            stationPrice.setText(this.stationPrice[position]);


            return row;
        }
    }
}