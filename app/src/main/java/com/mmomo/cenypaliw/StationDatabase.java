package com.mmomo.cenypaliw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class StationDatabase extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION=1;
    protected static final String  DATABASE_NAME="GasStationDatabase";

    private static final String TABLE_NAME="Stations";

    protected static final String KEY_ID="ID_station";
    protected static final String KEY_NAME="Name";
    protected static final String KEY_STREET="Street";
    protected static final String KEY_CITY="City";
    protected static final String KEY_POSTAL_CODE="Postal_code";
    protected static final String KEY_PROVINCE="Province";
    protected static final String KEY_COUNTY="County";
    protected static final String KEY_RON95="RON95";
    protected static final String KEY_RON98="RON98";
    protected static final String KEY_ON="[ON]";
    protected static final String KEY_LPG="LPG";
    protected static final String KEY_CNG="CNG";

    public StationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE= String.format("CREATE TABLE %s (%s INTEGER PRIMARY " +
                "KEY AUTOINCREMENT UNIQUE NOT NULL, %s VARCHAR (250) NOT NULL," +
                " %s VARCHAR (250), %s VARCHAR (250) NOT NULL, %s VARCHAR (250)," +
                " %s VARCHAR (250), %s VARCHAR (250), %s DOUBLE (10, 2), %s DOUBLE (10, 2)," +
                " %s DOUBLE (10, 2), %s DOUBLE (10, 2), %s DOUBLE (10, 2))",
                TABLE_NAME, KEY_ID, KEY_NAME, KEY_STREET, KEY_CITY, KEY_POSTAL_CODE,
                KEY_PROVINCE, KEY_COUNTY, KEY_RON95, KEY_RON98, KEY_ON, KEY_LPG, KEY_CNG);
        db.execSQL(CREATE_STATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //And create new one
        onCreate(db);
    }

    public void addStation(GasStation gasStation){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
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

    public List<GasStation> getStationList(){
        List<GasStation> result=new ArrayList<>();
        String selectQuery="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                GasStation gasStation=new GasStation(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getString(6),
                        Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)),
                        Integer.parseInt(cursor.getString(9)),
                        Integer.parseInt(cursor.getString(10)),
                        Integer.parseInt(cursor.getString(11)));
            }while(cursor.moveToNext());
        }
        db.close();
        return result;
    }
}
