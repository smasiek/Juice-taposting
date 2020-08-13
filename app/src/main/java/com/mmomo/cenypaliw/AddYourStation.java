package com.mmomo.cenypaliw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AddYourStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_station);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String[] cities=getResources().getStringArray(R.array.cities);
        AutoCompleteTextView cityText = findViewById(R.id.citySearch);
        ArrayAdapter<String> adapterCity= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,cities);
        cityText.setAdapter(adapterCity);

        String[] names=getResources().getStringArray(R.array.names);
        AutoCompleteTextView nameText = findViewById(R.id.nameSearch);
        ArrayAdapter<String> adapterName= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,names);
        nameText.setAdapter(adapterName);
    }
}