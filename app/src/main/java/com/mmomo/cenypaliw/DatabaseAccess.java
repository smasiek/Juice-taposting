package com.mmomo.cenypaliw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    //Provide access to external storage - our Stations Database
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    public static final String GAS_STATIONS_TABLE = "Stations";
    public static final String YOUR_GAS_STATIONS_TABLE = "YourStations";

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
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;

        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                if (c.getString(2) == null) {
                    //Handle null Street input when small town has none
                    GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                            c.getString(1), c.getString(3), c.getString(3),
                            c.getString(4), c.getString(5), c.getString(6),
                            Double.parseDouble(c.getString(7)),
                            Double.parseDouble(c.getString(8)),
                            Double.parseDouble(c.getString(9)),
                            Double.parseDouble(c.getString(10)),
                            Double.parseDouble(c.getString(11)));
                    result.add(gasStation);
                } else if (c.getString(4) == null) {
                    //Handle null Postal code input when uknown
                    GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                            c.getString(1), c.getString(3), c.getString(3),
                            " ", c.getString(5), c.getString(6),
                            Double.parseDouble(c.getString(7)),
                            Double.parseDouble(c.getString(8)),
                            Double.parseDouble(c.getString(9)),
                            Double.parseDouble(c.getString(10)),
                            Double.parseDouble(c.getString(11)));
                    result.add(gasStation);
                } else {
                    //Handle strings equal to "" caused by 0 in database table
                    //Size=5 because there are 5 types of petrol
                    Double[] petrol=new Double[5];
                    for(int j=7;j<12;j++){
                        if(c.getString(j).equals("")){
                            //Replace "" with 0.0
                            petrol[j-7]=0.0;
                        }else{
                            //Get actual value
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
                }
            } while (c.moveToNext());
        }
        closeConnection();
        return result;
    }


    public List<GasStation> getStationListFromCity(String city) {
        //Return list of stations from specific city
        List<GasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GAS_STATIONS_TABLE;

        openConnection();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if( c.getString(3).equals(city)) {
                    //First get stations from city then handle exceptional cases
                    if (c.getString(2) == null) {
                        //Handle null Street input when small town has none
                        GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                                c.getString(1), c.getString(3), c.getString(3),
                                c.getString(4), c.getString(5), c.getString(6),
                                Double.parseDouble(c.getString(7)),
                                Double.parseDouble(c.getString(8)),
                                Double.parseDouble(c.getString(9)),
                                Double.parseDouble(c.getString(10)),
                                Double.parseDouble(c.getString(11)));
                        result.add(gasStation);
                    } else if (c.getString(4) == null) {
                        //Handle null Postal code input when uknown
                        GasStation gasStation = new GasStation(Integer.parseInt(c.getString(0)),
                                c.getString(1), c.getString(3), c.getString(3),
                                " ", c.getString(5), c.getString(6),
                                Double.parseDouble(c.getString(7)),
                                Double.parseDouble(c.getString(8)),
                                Double.parseDouble(c.getString(9)),
                                Double.parseDouble(c.getString(10)),
                                Double.parseDouble(c.getString(11)));
                        result.add(gasStation);
                    } else {
                        //Handle strings equal to "" caused by 0 in database table
                        //Size=5 because there are 5 types of petrol
                        Double[] petrol = new Double[5];
                        for (int j = 7; j < 12; j++) {
                            if (c.getString(j).equals("")) {
                                //Replace "" with 0.0
                                petrol[j - 7] = 0.0;
                            } else {
                                //Get actual value
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
                    }
                }
            } while (c.moveToNext());
        }
        closeConnection();
        return result;
    }
}
