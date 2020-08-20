package com.mmomo.cenypaliw;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class YourGasStation extends GasStation {

    private int ID_your_station;

    public YourGasStation(int ID, int ID_your_station, String name, String street, String city, String postalCode, String province, String county, double RON95, double RON98, double ON, double LPG, double CNG) {
        super(ID, name, street, city, postalCode, province, county, RON95, RON98, ON, LPG, CNG);
        this.ID_your_station = ID_your_station;
    }


    public YourGasStation(String city, String name, Context context) {
        super();
        //Probably this will need updating: there will be more String in constructor in order to distinguish stations


        DatabaseAccess stationsDatabase=DatabaseAccess.getInstance(context);
        // 1.Wyłuskanie stacji z wybranego miasta
        List<GasStation>stationListFromCity=stationsDatabase.getStationListFromCity(city);

        // 2. Przejscie po liscie kilku tych stacj
        // 3. Porówywanie nazw czy znajduje się w nich wpisana nazwa

        for (GasStation gasStation : stationListFromCity) {
            if(gasStation.getName().contains(name)){
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
                return;
            }
        }
        Toast.makeText(context, "There's no such station found", Toast.LENGTH_SHORT).show();
    /*4. Zebranie danych stacji spelniajacej kryteria
            4b. Stwierdzenie ze nie da sie takich naleźć
        5. Podstawienie danych do pól stacji.

        */
    }

    public int getID_your_station() {
        return ID_your_station;
    }


}
