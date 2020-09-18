package com.mmomo.cenypaliw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class YourStationDatabase extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "GasStationDatabase";
    private static final String TABLE_NAME = "YourStations";
    protected static final String KEY_YOUR_ID = "ID_your_station";
    protected static final String KEY_ID = "ID_station";
    protected static final String KEY_NAME = "Name";
    protected static final String KEY_STREET = "Street";
    protected static final String KEY_CITY = "City";
    protected static final String KEY_POSTAL_CODE = "Postal_code";
    protected static final String KEY_PROVINCE = "Province";
    protected static final String KEY_COUNTY = "County";
    protected static final String KEY_RON95 = "RON95";
    protected static final String KEY_RON98 = "RON98";
    protected static final String KEY_ON = "[ON]";
    protected static final String KEY_LPG = "LPG";
    protected static final String KEY_CNG = "CNG";
    protected static final String KEY_LAT = "Latitude";
    protected static final String KEY_LNG = "Longitude";

    public YourStationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "CREATE TABLE YourStations (" +
                "    ID_your_station INTEGER        PRIMARY KEY AUTOINCREMENT" +
                "                                   UNIQUE" +
                "                                   NOT NULL," +
                "    ID_station      INTEGER        UNIQUE" +
                "                                   NOT NULL" +
                "                                   REFERENCES Stations (ID_station) ON DELETE CASCADE" +
                "                                                                    ON UPDATE CASCADE," +
                "    Name            VARCHAR (250)  NOT NULL," +
                "    Street          VARCHAR (250)," +
                "    City            VARCHAR (250)  NOT NULL," +
                "    Postal_code     VARCHAR (250)," +
                "    Province        VARCHAR (250)," +
                "    County          VARCHAR (250)," +
                "    RON95           DOUBLE (10, 2)," +
                "    RON98           DOUBLE (10, 2)," +
                "    [ON]            DOUBLE (10, 2)," +
                "    LPG             DOUBLE (10, 2)," +
                "    CNG             DOUBLE (10, 2)," +
                "    Latitude             DOUBLE," +
                "    Longitude             DOUBLE " +
                ");";
        db.execSQL(CREATE_STATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create new one
        onCreate(db);
    }

    public void addYourStation(YourGasStation gasStation, Context context) {
        //Open connection with database
        SQLiteDatabase db = this.getWritableDatabase();
        //Prepare tuple with values
        ContentValues values = new ContentValues();
        values.put(KEY_YOUR_ID, (String) null);
        values.put(KEY_ID, gasStation.getID());
        values.put(KEY_NAME, gasStation.getName());
        values.put(KEY_STREET, gasStation.getStreet());
        values.put(KEY_CITY, gasStation.getCity());
        values.put(KEY_PROVINCE, gasStation.getProvince());
        values.put(KEY_COUNTY, gasStation.getCounty());
        values.put(KEY_RON95, gasStation.getRON95());
        values.put(KEY_RON98, gasStation.getRON98());
        values.put(KEY_ON, gasStation.getON());
        values.put(KEY_LPG, gasStation.getLPG());
        values.put(KEY_CNG, gasStation.getCNG());
        values.put(KEY_LAT, gasStation.getLat());
        values.put(KEY_LNG, gasStation.getLng());


        db.insert(TABLE_NAME, null, values);
        Toast.makeText(context, "Station added to your list!", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void deleteYourStation(YourGasStation station) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_YOUR_ID + "=? ", new String[]{String.valueOf(station.getID_your_station())});
        db.close();
    }

    public List<YourGasStation> getYourStationList() {
        List<YourGasStation> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                YourGasStation gasStation = new YourGasStation(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        Double.parseDouble(cursor.getString(8)),
                        Double.parseDouble(cursor.getString(9)),
                        Double.parseDouble(cursor.getString(10)),
                        Double.parseDouble(cursor.getString(11)),
                        Double.parseDouble(cursor.getString(12)),
                        Double.parseDouble(cursor.getString(13)),
                        Double.parseDouble(cursor.getString(14)));
                result.add(gasStation);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}
