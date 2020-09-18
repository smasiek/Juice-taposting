package com.mmomo.cenypaliw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class YourStationListAdapter extends ArrayAdapter<YourGasStation> {
    //Adapter creating rows in Your Station List

    public static YourStationListAdapterHandler adapterHandler;

    public YourStationListAdapter(Context context, List<YourGasStation> stations) {
        super(context, 0, stations);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final YourGasStation station = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.station_list_row, parent, false);
        }
        //Get access to TextViews to modify them
        TextView stationName = convertView.findViewById(R.id.stationNameTextView);
        TextView fuelPreference = convertView.findViewById(R.id.stationFuelPriceTextView);
        TextView stationPrice = convertView.findViewById(R.id.stationFuelPriceValue);
        TextView stationStreet = convertView.findViewById(R.id.stationStreeTextView);
        TextView stationCity = convertView.findViewById(R.id.stationCityTextView);
        ImageView stationImage = convertView.findViewById(R.id.stationIconView);
        ImageButton stationDeleteButton = convertView.findViewById(R.id.stationDeleteButton);

        stationDeleteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                YourStationDatabase yourStationDatabase=new YourStationDatabase(getContext());
                yourStationDatabase.deleteYourStation(station);
                if (YourStationListAdapter.adapterHandler != null) {
                    YourStationListAdapter.adapterHandler.deleteRow();
                }
                Toast.makeText(getContext(), "Station deleted from your list", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO:If signing in already implemented add showing station prices using user preferences

        //For now hardcoded:
        String preferableFuel = "ON95";

        switch (preferableFuel) {
            case "ON98":
                stationPrice.setText(String.valueOf(station.getRON98()));
                fuelPreference.setText("Cena ON98: ");
                break;
            case "ON":
                stationPrice.setText(String.valueOf(station.getON()));
                fuelPreference.setText("Cena ON: ");
                break;
            case "LPG":
                stationPrice.setText(String.valueOf(station.getLPG()));
                fuelPreference.setText("Cena LPG: ");
                break;
            case "CNG":
                stationPrice.setText(String.valueOf(station.getCNG()));
                fuelPreference.setText("Cena CNG: ");
                break;
            default:
                stationPrice.setText(String.valueOf(station.getRON95()));
                fuelPreference.setText("Cena ON95: ");
                break;
        }

        stationStreet.setText(station.getStreet());
        stationCity.setText(station.getCity());

        if (station.getName() != null) {

            MapsActivity.setStationNameImage(station.getName(), stationImage, stationName);

        } else {
            stationName.setText("Station name error");
        }

        return convertView;
    }

    public abstract static class YourStationListAdapterHandler
    {
        public void deleteRow() {}
    }


}
