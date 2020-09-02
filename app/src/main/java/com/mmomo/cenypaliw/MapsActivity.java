package com.mmomo.cenypaliw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rolldown_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.twojeStacje:
                Intent intent = new Intent(this,
                        StationListActivity.class);
                startActivity(intent);
                return true;
            case R.id.dodajNowaStacje:
                Intent intent2 = new Intent(this, AddNewStation.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Provide current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        currentLocation=getLastKnownLocation();

//        LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions currLocationMarker = new MarkerOptions();
        //Set curr location to Jasło as emulator doesn't receive current location info
        LatLng currLocation=new LatLng(49.74506, 21.47252);
        //Set marker at current location
        currLocationMarker.position(currLocation);
        currLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.your_marker));
        currLocationMarker.title("Your current location");
        mMap.addMarker(currLocationMarker);
        //mMap.addMarker(new MarkerOptions().position(currLocation).title("This is your current location"));
        // mMap.addMarker(new MarkerOptions().position(currLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.you_marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 13), 5000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        DatabaseAccess stationsDatabase=DatabaseAccess.getInstance(getApplicationContext());

        List<GasStation>stationList=stationsDatabase.getStationList();

        startAsyncTask(stationList);

       //
        // forma debuggingu:
        stationsDatabase.closeConnection();
        if (mMap != null) {

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                //Access to infoWindow
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @SuppressLint("SetTextI18n")
                @Override
                public View getInfoContents(Marker marker) {
                    //Modify content of locationMarker infoWindow

                    View row = getLayoutInflater().inflate(R.layout.custom_marker_info, null);
                    //Get access to textViews in order to modify them
                    TextView t1_stationName = (TextView) row.findViewById(R.id.stationNameTextMarkerView);
                    TextView t2_stationPetrolPrice = (TextView) row.findViewById(R.id.stationPetrolPriceMarkerValue);
                    TextView t3_stationStreet = (TextView) row.findViewById(R.id.stationStreetMarkerView);
                    TextView t4_stationCity = (TextView) row.findViewById(R.id.stationCityMarkerView);

                    /*1. marker.getSnippet

                    2. split(,)
                    3. use in info window
                    *
                    */

                    LatLng ll = marker.getPosition();
                    //Get location position marker and then update infoWindow
                    //TODO: geocoderem przerobić LatLng na adres i po adresie znaleźć nazwe w bazie danych
                    t1_stationName.setText(marker.getSnippet());//Stworzyć pobieranie nazwy z bazy
                    t2_stationPetrolPrice.setText("9.99"); // do pobrania w jakiś sposób z bazy!
                    t3_stationStreet.setText(getStreetFromLatLng(ll.latitude, ll.longitude));
                    t4_stationCity.setText(getCityFromLatLng(ll.latitude, ll.longitude));
                   // t5_stationCountry.setText(getCountryFromLatLng(ll.latitude, ll.longitude));
                    //each method uses geoCoder to get actual name of street etc.
                    return row;
                }
            });
        }

    }

    private Location getLastKnownLocation() {
        Location l=null;
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener((new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!=null){
                    Toast.makeText(getApplicationContext(),"cos",Toast.LENGTH_SHORT).show();
                    currentLocation=location;
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+
                            ""+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    //To moze bedzie mozna usunac v
                }
            }
        }));
    }

    private String getStreetFromLatLng(double Latitude, double Longtitude) {
        //Return name of street
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                String completeAddress = "" + returnAddress.getAddressLine(0);
                String[] tempString=completeAddress.split(",",10);
                address=tempString[0];
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private String getCityFromLatLng(double Latitude, double Longtitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderReturnAddress = new StringBuilder("");
                stringBuilderReturnAddress.append(returnAddress.getPostalCode()).append(" ");
                stringBuilderReturnAddress.append(returnAddress.getLocality());
                address = stringBuilderReturnAddress.toString();
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private String getCountryFromLatLng(double Latitude, double Longtitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                address = "" + returnAddress.getCountryName();
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }


    //To jest do wyjebania chyba
    private String getNameFromLatLng(double Latitude, double Longtitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                //
                String completeAddress = "" + returnAddress.getAddressLine(0);
                String[] tempString=completeAddress.split(",",10);
                address=tempString[0];
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private static void getAddress(final String locationAddress, final Context context, final Handler handler){
        /*Creates marker using <String>address

        May be  usefull during implementation of Data base with addresses of stations
        or just as a normal search engine in app
         */
        Thread thread = new Thread(){
            public void run(){
                Geocoder geocoder = new Geocoder(context,Locale.getDefault());
                //Store latitude and longitude in array
                String[] result = new String[4];
                try{
                    String [] completeAddressArray=locationAddress.split(";");
                    /*TODO: check if u use full company name, geocoder is able to return shortened name

                   eg. FHU WOJTEK SZUDY ORLEN address.getXXX returns simple ORLEN
                    *
                    */
                    List<Address> addressList=geocoder.getFromLocationName(completeAddressArray[0],1);
                    if (addressList!=null && addressList.size()>0){
                        Address address = addressList.get(0);
                        result[0]=completeAddressArray[1]; //ID
                        result[1]=completeAddressArray[2]; //Station full name
                        result[2]=String.valueOf( address.getLatitude());
                        result[3]=String.valueOf(address.getLongitude());
                    }
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    Message message=Message.obtain();
                    message.setTarget(handler);
                    //Set connection with handler
                    if(result!=null){
                        message.what=1;
                        Bundle bundle=new Bundle();
                        bundle.putString("adress", result[0] +";"+result[1]+";"+result[2]+";"+result[3]);
                        //Prepare data to send
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                    //Send data
                }
            }
        };
        thread.start();
    }


    private static class GeoHandler extends Handler {
        //Handles data from getAddress() method

        Context context;
        public GeoHandler(Context context){
            this.context=context;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;

            if (msg.what == 1) {
                //Receive message from method
                Bundle bundle = msg.getData();
                address = bundle.getString("adress");
            } else {
                address = null;
            }

            if(address!=null) {
                //This should be deleted in future. Created just to have some marker onCreate

                //?????? why delete this, i dunno
                String[] stationLatLang = address.split(";");

                LatLng locationLatLang = new LatLng(Double.parseDouble(stationLatLang[2]), Double.parseDouble(stationLatLang[3]));



               // MarkerOptions locationMarker = new MarkerOptions();
                //locationMarker.position(locationLatLang);



                DatabaseAccess stationsDatabase=DatabaseAccess.getInstance(context);
                //if(stationLatLang[1].contains("IWOPOL"))
                stationsDatabase.insertStationCoords(Integer.parseInt(stationLatLang[0]),stationLatLang[1],Double.parseDouble(stationLatLang[2]),Double.parseDouble(stationLatLang[3]));



                //locationMarker.snippet(stationLatLang[1]);
               // mMap.addMarker(locationMarker);

            }
        }
    }

    public void startAsyncTask(List<GasStation> gasStationList) {
    // method used to get lat/lng from database

        LatLangAsyncTask task=new LatLangAsyncTask(this);
        task.execute(gasStationList);
    }

    private static class LatLangAsyncTask extends AsyncTask<List<GasStation>, Integer, Integer> {

        private WeakReference<MapsActivity> activityWeakReference;

        LatLangAsyncTask(MapsActivity activity){
            activityWeakReference=new WeakReference<MapsActivity>(activity);
        }


        @Override
        protected Integer doInBackground(List<GasStation>... params) {

            MapsActivity activity=activityWeakReference.get();
            if(activity==null || activity.isFinishing()){
                return 0;
            }
            Integer result=0;

            for (GasStation gasStation : params[0]) {
                /* Build String to look for specific station using Geocoder
                 *
                 * if station is found - place marker
                 * */
                //Street,City,ID,Name,
                StringBuilder completeAddress=new StringBuilder();
                completeAddress.append(gasStation.getCity()).append(" ");
                completeAddress.append(gasStation.getStreet()).append(";");
                completeAddress.append(gasStation.getID()).append(";"); // sometimes station name includes ","
                completeAddress.append(gasStation.getName());


                /* TEMPORARILY COMMENTED CUS I WANT TO MAKE A DATABASE WITHOUT MARKERS*/

                final int MIN_FOUND_STATIONS_NUMBER=70;
                //final int MAX_FOUND_STATIONS_NUMBER=70;

                if(MIN_FOUND_STATIONS_NUMBER<=gasStation.getID()){
                    //Tighten search scope to one city  to save CPU resources
                    getAddress(completeAddress.toString(),activity.getApplicationContext(),new GeoHandler(activity.getApplicationContext()));
                    result++;
                }
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            MapsActivity activity=activityWeakReference.get();
            if(activity==null || activity.isFinishing()){
                return;
            }
            Toast.makeText(activity.getApplicationContext(), "Synchronized " + integer + " stations", Toast.LENGTH_SHORT).show();
        }
    }



}
