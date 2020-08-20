package com.mmomo.cenypaliw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AddYourStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_station);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String[] cities=getResources().getStringArray(R.array.cities);
        final AutoCompleteTextView cityText = findViewById(R.id.citySearch);
        ArrayAdapter<String> adapterCity= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,cities);
        cityText.setAdapter(adapterCity);

        String[] names=getResources().getStringArray(R.array.names);
        final AutoCompleteTextView nameText = findViewById(R.id.nameSearch);
        ArrayAdapter<String> adapterName= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,names);
        nameText.setAdapter(adapterName);

        Button addYourStationButton = (Button) findViewById(R.id.addYourStationButton);
        addYourStationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(cityText.getText()==null && nameText.getText()!=null){
                    Toast.makeText(getApplicationContext(), "Choose station name and city from list", Toast.LENGTH_SHORT).show();
                    return;
                }
                YourStationDatabase db= new YourStationDatabase(AddYourStation.this);
                /* TODO: There should be a text view with addresses of stations available in chosen city.
                Before choosing city other textboxes shouldn't be clickable

                Other way to do this is to connect names and street of station in one text view
                 */
                try {
                    YourGasStation yourGasStation = new YourGasStation(cityText.getText().toString(), nameText.getText().toString(), getApplicationContext());

                    db.addYourStation(yourGasStation,getApplicationContext());
                } catch (RuntimeException e){
                    Toast.makeText(AddYourStation.this, "There's no such station in database bla bla bla", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}