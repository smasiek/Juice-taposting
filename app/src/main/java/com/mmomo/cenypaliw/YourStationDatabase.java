package com.mmomo.cenypaliw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class YourStationDatabase extends StationDatabase {

    private static final String TABLE_NAME = "YourStations";

    protected static final String KEY_YOUR_ID = "ID_your_station";

    public YourStationDatabase(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_STATIONS_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY " +
                        "KEY AUTOINCREMENT UNIQUE NOT NULL, %s INTEGER UNIQUE NOT NULL," +
                        "REFERENCES Stations (%s) ON DELETE CASCADE ON UPDATE CASCADE," +
                        " %s VARCHAR (250) NOT NULL, %s VARCHAR (250), %s VARCHAR (250) NOT NULL," +
                        " %s VARCHAR (250), %s VARCHAR (250), %s VARCHAR (250), %s DOUBLE (10, 2)," +
                        " %s DOUBLE (10, 2), %s DOUBLE (10, 2), %s DOUBLE (10, 2), %s DOUBLE (10, 2))",
                TABLE_NAME, KEY_YOUR_ID, KEY_ID,KEY_ID, KEY_NAME, KEY_STREET, KEY_CITY, KEY_POSTAL_CODE,
                KEY_PROVINCE, KEY_COUNTY, KEY_RON95, KEY_RON98, KEY_ON, KEY_LPG, KEY_CNG);

        */
        String CREATE_STATIONS_TABLE= "CREATE TABLE YourStations (" +
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
                "    CNG             DOUBLE (10, 2) " +
                ");";
        db.execSQL(CREATE_STATIONS_TABLE);

    }


    public void addYourStation(YourGasStation gasStation){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID,gasStation.getID());
        values.put(KEY_NAME,gasStation.getName());
        values.put(KEY_STREET,gasStation.getStreet());
        values.put(KEY_CITY,gasStation.getCity());
        values.put(KEY_PROVINCE,gasStation.getProvince());
        values.put(KEY_COUNTY,gasStation.getCounty());
        values.put(KEY_RON95,gasStation.getRON95());
        values.put(KEY_RON98,gasStation.getRON98());
        values.put(KEY_ON,gasStation.getON());
        values.put(KEY_LPG,gasStation.getLPG());
        values.put(KEY_CNG,gasStation.getCNG());

        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void deleteYourStation(YourGasStation station) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_YOUR_ID + " ? ", new String[]{String.valueOf(station.getID_your_station())});
        db.close();
    }

    public List<YourGasStation> getYourStationList(){
        List<YourGasStation> result=new ArrayList<>();
        String selectQuery="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();
       /* String CREATE_STATIONS_TABLE= "CREATE TABLE YourStations (" +
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
                "    CNG             DOUBLE (10, 2) " +
                ");";
        db.execSQL(CREATE_STATIONS_TABLE);*/
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                YourGasStation gasStation=new YourGasStation(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),cursor.getString(7),
                        Integer.parseInt(cursor.getString(8)),
                        Integer.parseInt(cursor.getString(9)),
                        Integer.parseInt(cursor.getString(10)),
                        Integer.parseInt(cursor.getString(11)),
                        Integer.parseInt(cursor.getString(12)));
            }while(cursor.moveToNext());
        }
        db.close();
        return result;
    }
}
