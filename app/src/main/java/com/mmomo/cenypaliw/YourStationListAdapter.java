package com.mmomo.cenypaliw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

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
        TextView stationPrice=(TextView) convertView.findViewById(R.id.stationPetrolPriceValue);
        TextView stationStreet=(TextView) convertView.findViewById(R.id.stationStreeTextView);
        TextView stationCity=(TextView) convertView.findViewById(R.id.stationCityTextView);

        if(station.getName()!=null){
            stationName.setText(station.getName());
        } else {
            stationName.setText("Station name error");
        }

        stationPrice.setText(String.valueOf(station.getRON95()));
        //TODO:More text views for other type of petrol must be added
        stationStreet.setText(station.getStreet());
        stationCity.setText(station.getCity());

        return convertView;
    }
}
