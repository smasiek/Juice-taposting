package com.mmomo.cenypaliw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
        String[] cities=getResources().getStringArray(R.array.cities);
        final AutoCompleteTextView cityText = findViewById(R.id.citySearch);
        //Connect array with text box
        ArrayAdapter<String> adapterCity= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,cities);
        cityText.setAdapter(adapterCity);

        //Access to String array with names of stations
        String[] names=getResources().getStringArray(R.array.names);
        final AutoCompleteTextView nameText = findViewById(R.id.nameSearch);
        //Connect array with text box
        ArrayAdapter<String> adapterName= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,names);
        nameText.setAdapter(adapterName);

        //Confirm input
        Button addYourStationButton = (Button) findViewById(R.id.addYourStationButton);
        addYourStationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(cityText.getText()==null && nameText.getText()!=null){
                    //Ensure there is any input
                    Toast.makeText(getApplicationContext(), "Choose station name and city from list", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Connect with database
                YourStationDatabase db= new YourStationDatabase(AddYourStationActivity.this);
                /* TODO: There should be a text view with addresses of stations available in chosen city.
                Before choosing city other textboxes shouldn't be clickable

                Other way to do this is to connect names and street of station in one text view
                 */
                try {
                    //Create instance of yourGastStation using name and city
                    YourGasStation yourGasStation = new YourGasStation(cityText.getText().toString(), nameText.getText().toString(), getApplicationContext());
                    db.addYourStation(yourGasStation,getApplicationContext());
                } catch (RuntimeException e){
                    Toast.makeText(AddYourStationActivity.this, "There's no such station in database blabla", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}