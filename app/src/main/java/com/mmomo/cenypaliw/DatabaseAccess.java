package com.mmomo.cenypaliw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    public static final String GAS_STATIONS_TABLE = "Stations";
    public static final String YOUR_GAS_STATIONS_TABLE = "YourStations";
    public static final String GAS_STATIONS_COORD_TABLE = "Coordinates";

    //Private constructor to avoid usig it from outside the class
    DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //Get single instance of database
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    //Open connection with database
    public void openConnection() {
        this.db = openHelper.getWritableDatabase();
    }

    //Close connection with database
    public void closeConnection() {
        if (db != null) {
            this.db.close();
        }
    }

    public List<GasStation> getStationList() {
        List<GasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        String selectQueryLatLng = "SELECT * FROM " + GAS_STATIONS_COORD_TABLE;
        openConnection();
        Cursor c1 = db.rawQuery(selectQuery, null);
        Cursor c2 = db.rawQuery(selectQueryLatLng, null);

        if (c1.moveToFirst() && c2.moveToFirst()) {
            int i = 0;

            do {
                if (c1.getString(2) == null) {
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
                    i++;
                } else if (c1.getString(4) == null) {
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
                    i++;
                } else {
                    //Avoidence of strings =="" caused by 0 in database table
                    Double[] petrol = new Double[5];
                    for (int j = 7; j < 12; j++) {
                        if (c1.getString(j).equals("")) {
                            petrol[j - 7] = 0.0;
                        } else {
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
                    i++;
                }
            } while (c1.moveToNext() && c2.moveToNext());

            i = 0;
        }
        closeConnection();
        return result;
    }


    public List<GasStation> getStationListFromCity(String city) {
        List<GasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            int i = 0;

            do {
                if (c.getString(3).equals(city)) {
                    if (c.getString(2) == null) {
                        GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                                c.getString(1), c.getString(3), c.getString(3),
                                c.getString(4), c.getString(5), c.getString(6),
                                Double.parseDouble(c.getString(7)),
                                Double.parseDouble(c.getString(8)),
                                Double.parseDouble(c.getString(9)),
                                Double.parseDouble(c.getString(10)),
                                Double.parseDouble(c.getString(11)));
                        result.add(gasStation);
                        i++;
                    } else if (c.getString(4) == null) {
                        GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                                c.getString(1), c.getString(3), c.getString(3),
                                " ", c.getString(5), c.getString(6),
                                Double.parseDouble(c.getString(7)),
                                Double.parseDouble(c.getString(8)),
                                Double.parseDouble(c.getString(9)),
                                Double.parseDouble(c.getString(10)),
                                Double.parseDouble(c.getString(11)));
                        result.add(gasStation);
                        i++;
                    } else {
                        //Avoidence of strings =="" caused by 0 in database table
                        Double[] petrol = new Double[5];
                        for (int j = 7; j < 12; j++) {
                            if (c.getString(j).equals("")) {
                                petrol[j - 7] = 0.0;
                            } else {
                                petrol[j - 7] = Double.parseDouble(c.getString(j));
                            }
                        }
                        GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                                c.getString(1), c.getString(2), c.getString(3),
                                c.getString(4), c.getString(5), c.getString(6),
                                petrol[0],
                                petrol[1],
                                petrol[2],
                                petrol[3],
                                petrol[4]);
                        result.add(gasStation);
                        i++;
                    }
                }
            } while (c.moveToNext());

            i = 0;
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
