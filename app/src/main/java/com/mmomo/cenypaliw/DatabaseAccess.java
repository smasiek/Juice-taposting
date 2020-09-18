package com.mmomo.cenypaliw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DatabaseAccess {
    //Provide access to external storage - our Stations Database
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    public static final String GAS_STATIONS_TABLE = "Stations";
    public static final String YOUR_GAS_STATIONS_TABLE = "YourStations";
    public static final String GAS_STATIONS_COORD_TABLE = "Coordinates";

    //Private constructor to avoid using it from outside the class
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        //Get single instance of database
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void openConnection() {
        //Open connection with database
        this.db = openHelper.getWritableDatabase();
    }

    public void closeConnection() {
        //Close connection with database
        if (db != null) {
            this.db.close();
        }
    }

    public List<GasStation> getStationList() {
        //Return complete list of stations from database
        List<GasStation> result = new ArrayList<>();
        //Two tables opened simultaneously to receive basic data and coords
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        String selectQueryLatLng = "SELECT * FROM " + GAS_STATIONS_COORD_TABLE;
        openConnection();
        Cursor c1 = db.rawQuery(selectQuery, null);
        Cursor c2 = db.rawQuery(selectQueryLatLng, null);

        if (c1.moveToFirst() && c2.moveToFirst()) {

            do {
                if (c1.getString(2) == null && c1.getString(4) == null) {
                    //Handle null Postal code and Street
                    GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                            c1.getString(1), c1.getString(3), c1.getString(3),
                            " ", c1.getString(5), c1.getString(6),
                            Double.parseDouble(c1.getString(7)),
                            Double.parseDouble(c1.getString(8)),
                            Double.parseDouble(c1.getString(9)),
                            Double.parseDouble(c1.getString(10)),
                            Double.parseDouble(c1.getString(11)),
                            Double.parseDouble(c2.getString(2)),
                            Double.parseDouble(c2.getString(3)));
                    result.add(gasStation);
                } else if (c1.getString(2) == null) {
                    //Handle null Street input when small town has none
                    GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                            c1.getString(1), c1.getString(3), c1.getString(3),
                            c1.getString(4), c1.getString(5), c1.getString(6),
                            Double.parseDouble(c1.getString(7)),
                            Double.parseDouble(c1.getString(8)),
                            Double.parseDouble(c1.getString(9)),
                            Double.parseDouble(c1.getString(10)),
                            Double.parseDouble(c1.getString(11)),
                            Double.parseDouble(c2.getString(2)),
                            Double.parseDouble(c2.getString(3)));
                    result.add(gasStation);
                } else if (c1.getString(4) == null) {
                    //Handle null Postal code input
                    GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                            c1.getString(1), c1.getString(2), c1.getString(3),
                            " ", c1.getString(5), c1.getString(6),
                            Double.parseDouble(c1.getString(7)),
                            Double.parseDouble(c1.getString(8)),
                            Double.parseDouble(c1.getString(9)),
                            Double.parseDouble(c1.getString(10)),
                            Double.parseDouble(c1.getString(11)),
                            Double.parseDouble(c2.getString(2)),
                            Double.parseDouble(c2.getString(3)));
                    result.add(gasStation);
                } else {
                    //Handle strings equal to "" caused by 0 in database table
                    //Size=5 because there are 5 types of petrol
                    Double[] petrol = new Double[5];
                    for (int j = 7; j < 12; j++) {
                        if (c1.getString(j).equals("")) {
                            //Replace "" with 0.0
                            petrol[j - 7] = 0.0;
                        } else {
                            //Get actual value
                            petrol[j - 7] = Double.parseDouble(c1.getString(j));
                        }
                    }
                    GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                            c1.getString(1), c1.getString(2), c1.getString(3),
                            c1.getString(4), c1.getString(5), c1.getString(6),
                            petrol[0],
                            petrol[1],
                            petrol[2],
                            petrol[3],
                            petrol[4],
                            Double.parseDouble(c2.getString(2)),
                            Double.parseDouble(c2.getString(3)));
                    result.add(gasStation);
                }
            } while (c1.moveToNext() && c2.moveToNext());
        }
        closeConnection();
        return result;
    }


    public List<GasStation> getStationListFromCity(String city) {
        //Return list of stations from specific city
        List<GasStation> result = new ArrayList<>();
        //Two tables opened simultaneously to receive basic data and coords
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        String selectQueryLatLng = "SELECT * FROM " + GAS_STATIONS_COORD_TABLE;
        openConnection();
        Cursor c1 = db.rawQuery(selectQuery, null);
        Cursor c2 = db.rawQuery(selectQueryLatLng, null);
        if (c1.moveToFirst() && c2.moveToFirst()) {
            do {
                if (c1.getString(3).equals(city)) {
                    //First get stations from city then handle exceptional cases
                    if (c1.getString(2) == null && c1.getString(4) == null) {
                        //Handle null Postal code and Street
                        GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                                c1.getString(1), c1.getString(3), c1.getString(3),
                                " ", c1.getString(5), c1.getString(6),
                                Double.parseDouble(c1.getString(7)),
                                Double.parseDouble(c1.getString(8)),
                                Double.parseDouble(c1.getString(9)),
                                Double.parseDouble(c1.getString(10)),
                                Double.parseDouble(c1.getString(11)),
                                Double.parseDouble(c2.getString(2)),
                                Double.parseDouble(c2.getString(3)));
                        result.add(gasStation);
                    } else if (c1.getString(2) == null) {
                        //Handle null Street input when small town has none
                        GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                                c1.getString(1), c1.getString(3), c1.getString(3),
                                c1.getString(4), c1.getString(5), c1.getString(6),
                                Double.parseDouble(c1.getString(7)),
                                Double.parseDouble(c1.getString(8)),
                                Double.parseDouble(c1.getString(9)),
                                Double.parseDouble(c1.getString(10)),
                                Double.parseDouble(c1.getString(11)),
                                Double.parseDouble(c2.getString(2)),
                                Double.parseDouble(c2.getString(3)));
                        result.add(gasStation);
                    } else if (c1.getString(4) == null) {
                        //Handle null Postal code input
                        GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                                c1.getString(1), c1.getString(3), c1.getString(3),
                                " ", c1.getString(5), c1.getString(6),
                                Double.parseDouble(c1.getString(7)),
                                Double.parseDouble(c1.getString(8)),
                                Double.parseDouble(c1.getString(9)),
                                Double.parseDouble(c1.getString(10)),
                                Double.parseDouble(c1.getString(11)),
                                Double.parseDouble(c2.getString(2)),
                                Double.parseDouble(c2.getString(3)));
                        result.add(gasStation);
                    } else {
                        //Handle strings equal to "" caused by 0 in database table
                        //Size=5 because there are 5 types of petrol
                        Double[] petrol = new Double[5];
                        for (int j = 7; j < 12; j++) {
                            if (c1.getString(j).equals("")) {
                                //Replace "" with 0.0
                                petrol[j - 7] = 0.0;
                            } else {
                                //Get actual value
                                petrol[j - 7] = Double.parseDouble(c1.getString(j));
                            }
                        }
                        GasStation gasStation = new GasStation(Integer.parseInt(c1.getString(0)),
                                c1.getString(1), c1.getString(2), c1.getString(3),
                                c1.getString(4), c1.getString(5), c1.getString(6),
                                petrol[0],
                                petrol[1],
                                petrol[2],
                                petrol[3],
                                petrol[4],
                                Double.parseDouble(c2.getString(2)),
                                Double.parseDouble(c2.getString(3)));
                        result.add(gasStation);
                    }
                }
            } while (c1.moveToNext() && c2.moveToNext());
        }
        closeConnection();
        return result;
    }

    public ArrayList<String> getCitiesArrayList() {
        ArrayList<String> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                if (c.getString(3) != null && !result.contains(c.getString(3))) {
                    //Handle null Street input when small town has none
                    result.add(c.getString(3));
                }
            } while (c.moveToNext());
        }
        closeConnection();
        Collections.sort(result);
        return result;
    }

    public ArrayList<String> getStationsArrayList(String city) {
        //Returns stations from provided city
        ArrayList<String> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;

        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                //Check if station is widely known/franchise like Orlen,LOTOS etc. and shorten its name
                String tempName = GasStationNames.bigStationNameShortcut(c.getString(1));
                if (c.getString(3).equals(city) && !result.contains(tempName)) {
                    result.add(tempName);
                }
            } while (c.moveToNext());
        }
        closeConnection();
        return result;
    }


    public ArrayList<String> getStreetsArrayList(String city, String name) {
        ArrayList<String> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;

        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                if (c.getString(3).equals(city) && c.getString(1).contains(name) && !result.contains(c.getString(2))) {
                    if (c.getString(2) != null) {
                        //Handle null Street input when small town has none
                        result.add(c.getString(2));
                    }
                }
            } while (c.moveToNext());
        }
        closeConnection();
        return result;
    }

    public void insertStationCoords(int id, String name, double lat, double lng) {
        //openConnection();

        this.db = openHelper.getReadableDatabase();

        ContentValues values = new ContentValues();

        //final String temp_name="Name";
        //final String temp_lat="Latitude";
        //final String temp_lng="Longitude";
        values.put("ID_station", id);
        values.put("Name", name);
        values.put("Latitude", lat);
        values.put("Longitude", lng);
        Log.i("Attempt", values.getAsString("Name") + " " + lat + " " + lng);
        db.insert(GAS_STATIONS_COORD_TABLE, null, values);
        //db.execSQL("INSERT INTO Coordinates (ID_station,Name, Latitude, Longitude) VALUES ("+id+", '"+name+"', "+lat+", "+lng+");");
        //closeConnection();
    }
}
