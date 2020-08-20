package com.mmomo.cenypaliw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    public static final String GAS_STATIONS_TABLE = "Stations";
    public static final String YOUR_GAS_STATIONS_TABLE = "YourStations";

    //Private constructor to avoid usig it from outside the class
    private DatabaseAccess(Context context) {
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
    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    //Close connection with database
    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public List<GasStation> getStationList() {
        List<GasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        open();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            int i = 0;

            do {
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
                    Double[] petrol=new Double[5];
                    for(int j=7;j<12;j++){
                        if(c.getString(j).equals("")){
                            petrol[j-7]=0.0;
                        }else{
                            petrol[j-7]=Double.parseDouble(c.getString(j));
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
            } while (c.moveToNext());

            i = 0;
        }
        close();
        return result;
    }


    public List<GasStation> getStationListFromCity(String city) {
        List<GasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;
        open();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            int i = 0;

            do {
                if( c.getString(3).equals(city)) {
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
        close();
        return result;
    }
}
