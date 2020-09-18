package com.mmomo.cenypaliw;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class YourGasStation extends GasStation {
    //Gas station added to Your Station List, stored in separate database with its specific ID
    private int ID_your_station;

    public YourGasStation(int ID_your_station, int ID, String name, String street, String city, String postalCode, String province, String county, double RON95, double RON98, double ON, double LPG, double CNG, double lat, double lng) {
        super(ID, name, street, city, postalCode, province, county, RON95, RON98, ON, LPG, CNG, lat, lng);
        this.ID_your_station = ID_your_station;
    }


    public YourGasStation(String city, String name, String street, Context context) {
        super();
        //TODO:Probably this will need updating: there will be more String in constructor in order to distinguish stations

        DatabaseAccess stationsDatabase = DatabaseAccess.getInstance(context);
        //Get list of stations from the city
        List<GasStation> stationListFromCity = stationsDatabase.getStationListFromCity(city);

        for (GasStation gasStation : stationListFromCity) {
            if (gasStation.getName().contains(name)) {
                if (gasStation.getStreet().equals(street)) {
                    this.setID(gasStation.getID());
                    this.setName(gasStation.getName());
                    this.setStreet(gasStation.getStreet());
                    this.setCity(city);
                    this.setPostalCode(gasStation.getPostalCode());
                    this.setProvince(gasStation.getProvince());
                    this.setCounty(gasStation.getCounty());
                    this.setRON95(gasStation.getRON95());
                    this.setRON98(gasStation.getRON98());
                    this.setON(gasStation.getON());
                    this.setLPG(gasStation.getLPG());
                    this.setCNG(gasStation.getCNG());
                    this.setLat(gasStation.getLat());
                    this.setLng(gasStation.getLng());
                    return;
                }
            }
        }
        Toast.makeText(context, "There's no such station found", Toast.LENGTH_SHORT).show();
    }

    public int getID_your_station() {
        return ID_your_station;
    }
}
