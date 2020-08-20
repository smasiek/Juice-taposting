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
    //Adapter creating rows in Your Station List
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
        //Get access to TextViews to modify them
        TextView stationName=(TextView) convertView.findViewById(R.id.stationNameTextView);
        TextView stationPrice=(TextView) convertView.findViewById(R.id.stationPetrolPriceValue);
        TextView stationStreet=(TextView) convertView.findViewById(R.id.stationStreeTextView);
        TextView stationCity=(TextView) convertView.findViewById(R.id.stationCityTextView);
        ImageView stationImage=(ImageView) convertView.findViewById(R.id.stationIconView);

        if(station.getName()!=null){
            stationName.setText(station.getName());
        } else {
            stationName.setText("Station name error");
        }

        stationPrice.setText(String.valueOf(station.getRON95()));
        //TODO:More text views for other type of petrol must be added
        stationStreet.setText(station.getStreet());
        stationCity.setText(station.getCity());

        if(station.getName().contains("ORLEN")) {
            //Set station icon using its name and enum
            stationImage.setImageResource(gasStationIcons[GasStationNames.getPosition(ORLEN)]);
        } else if(station.getName().contains("LOTOS")){
            stationImage.setImageResource(gasStationIcons[GasStationNames.getPosition(LOTOS)]);
        } else if(station.getName().contains("GROSAR")){
            stationImage.setImageResource(gasStationIcons[GasStationNames.getPosition(GROSAR)]);
        } else if(station.getName().contains("BP")){
            stationImage.setImageResource(gasStationIcons[GasStationNames.getPosition(BP)]);
        } else {
            stationImage.setImageResource(GasStationIcons.gasStationIcons[GasStationNames.getPosition(NONE)]);
        }
        return convertView;
    }
}
