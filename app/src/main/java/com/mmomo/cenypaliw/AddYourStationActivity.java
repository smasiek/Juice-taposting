package com.mmomo.cenypaliw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class AddYourStationActivity extends AppCompatActivity {
    /*Add new station to your Station list

    uses Autocomplete Text View to ensure correct input
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_station);

        //Set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Access to String array with cities
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        ArrayList<String> cities = databaseAccess.getCitiesArrayList();

        final AutoCompleteTextView cityText = findViewById(R.id.citySearch);

        //Connect array with text box
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cities);
        cityText.setAdapter(adapterCity);


        //Access to String array with names of stations
        final ArrayList<String>[] names = new ArrayList[]{new ArrayList<>()};
        final AutoCompleteTextView nameText = findViewById(R.id.nameSearch);
        //Connect array with text box

        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                names[0] = databaseAccess.getStationsArrayList(cityText.getText().toString());
                ArrayAdapter<String> adapterName = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names[0]);
                nameText.setAdapter(adapterName);
            }
        });

        //Access to String array with names of stations
        final ArrayList<String>[] streets = new ArrayList[]{new ArrayList<>()};
        final AutoCompleteTextView streetText = findViewById(R.id.streetSearch);
        //Connect array with text box

        streetText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                streets[0] = databaseAccess.getStreetsArrayList(cityText.getText().toString(), nameText.getText().toString());
                ArrayAdapter<String> adapterStreet = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, streets[0]);
                streetText.setAdapter(adapterStreet);
                streetText.showDropDown();
            }
        });

        //Confirm input
        Button addYourStationButton = (Button) findViewById(R.id.addYourStationButton);
        addYourStationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (cityText.getText() == null && nameText.getText() != null && streetText.getText() != null) {
                    //Ensure there is any input
                    Toast.makeText(getApplicationContext(), "Choose station city,name and street from the list", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Connect with database
                YourStationDatabase db = new YourStationDatabase(AddYourStationActivity.this);

                try {
                    //Create instance of yourGastStation using name and city
                    YourGasStation yourGasStation = new YourGasStation(cityText.getText().toString(), nameText.getText().toString(), streetText.getText().toString(), getApplicationContext());
                    db.addYourStation(yourGasStation, getApplicationContext());
                } catch (RuntimeException e) {
                    Toast.makeText(AddYourStationActivity.this, "There's no such station in database blabla", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}