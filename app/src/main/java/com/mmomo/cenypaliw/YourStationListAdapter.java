package com.mmomo.cenypaliw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static com.mmomo.cenypaliw.GasStationIcons.gasStationIcons;
import static com.mmomo.cenypaliw.GasStationNames.*;

public class YourStationListAdapter extends ArrayAdapter<YourGasStation> {
    public YourStationListAdapter(Context context, List<YourGasStation> stations){
        super(context,0,stations);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GasStation station = getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.station_list_row,parent,false);
        }

        TextView stationName=(TextView) convertView.findViewById(R.id.stationNameTextView);
        TextView fuelPreference=(TextView) convertView.findViewById(R.id.stationFuelPriceTextView);
        TextView stationPrice=(TextView) convertView.findViewById(R.id.stationFuelPriceValue);
        TextView stationStreet=(TextView) convertView.findViewById(R.id.stationStreeTextView);
        TextView stationCity=(TextView) convertView.findViewById(R.id.stationCityTextView);
        ImageView stationImage=(ImageView) convertView.findViewById(R.id.stationIconView);


        //TODO:If signing in already implemented add showing station prices using user preferences

        //For now hardcoded:
        String preferableFuel="ON95";

        switch(preferableFuel){
            case "ON98":stationPrice.setText(String.valueOf(station.getRON98()));
                fuelPreference.setText("Cena ON98: ");
                break;
            case "ON":stationPrice.setText(String.valueOf(station.getON()));
                fuelPreference.setText("Cena ON: ");
                break;
            case "LPG":stationPrice.setText(String.valueOf(station.getLPG()));
                fuelPreference.setText("Cena LPG: ");
                break;
            case "CNG":stationPrice.setText(String.valueOf(station.getCNG()));
                fuelPreference.setText("Cena CNG: ");
                break;
            default:stationPrice.setText(String.valueOf(station.getRON95()));
                fuelPreference.setText("Cena ON95: ");
                break;
        }

        stationStreet.setText(station.getStreet());
        stationCity.setText(station.getCity());

        if(station.getName()!=null){

            MapsActivity.setStationNameImage(station.getName(), stationImage,stationName);

        } else {
            stationName.setText("Station name error");
        }

        return convertView;
    }



}
