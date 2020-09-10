package com.mmomo.cenypaliw;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
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
                        YourStationListActivity.class);
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
        currentLocation = getLastKnownLocation();

//        LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions currLocationMarker = new MarkerOptions();
        //Set curr location to Jasło as emulator doesn't receive current location info
        LatLng currLocation = new LatLng(49.74506, 21.47252);
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

        DatabaseAccess stationsDatabase = DatabaseAccess.getInstance(getApplicationContext());
        List<GasStation> stationList = stationsDatabase.getStationList();

        setMarkers(stationList);


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
                    TextView t2_stationRON95Price = (TextView) row.findViewById(R.id.stationRON95PriceMarkerValue);
                    TextView t3_stationRON98Price = (TextView) row.findViewById(R.id.stationRON98PriceMarkerValue);
                    TextView t4_stationONPrice = (TextView) row.findViewById(R.id.stationONPriceMarkerValue);
                    TextView t5_stationLPGPrice = (TextView) row.findViewById(R.id.stationLPGPriceMarkerValue);
                    TextView t6_stationStreet = (TextView) row.findViewById(R.id.stationStreetMarkerView);
                    TextView t7_stationCity = (TextView) row.findViewById(R.id.stationCityMarkerView);

                    LatLng ll = marker.getPosition();
                    //Get location position marker and then update infoWindow
                    //TODO: geocoderem przerobić LatLng na adres i po adresie znaleźć nazwe w bazie danych

                    //Pattern "Name;RON95;RON98;ON;LPG;Street;City"
                    String[] markerInfo=marker.getTitle().split(";");

                    t1_stationName.setText(markerInfo[0]);
                    t2_stationRON95Price.setText(markerInfo[1]);
                    t3_stationRON98Price.setText(markerInfo[2]);
                    t4_stationONPrice.setText(markerInfo[3]);
                    t5_stationLPGPrice.setText(markerInfo[4]);
                    t6_stationStreet.setText(markerInfo[5]);
                    t7_stationCity.setText(markerInfo[6]);

                    // t5_stationCountry.setText(getCountryFromLatLng(ll.latitude, ll.longitude));
                    return row;
                }
            });
        }

    }

    private void setMarkers(List<GasStation> stationList) {
        for (GasStation gasStation : stationList) {
            MarkerOptions locationMarker = new MarkerOptions();

            LatLng latLng = new LatLng(gasStation.getLat(), gasStation.getLng());
            locationMarker.position(latLng);

            //Pattern "Name;RON95;RON98;ON;LPG;Street;City"
            String markerInfoToSend = gasStation.getName() + ";" + gasStation.getRON95() + ";" + gasStation.getRON98() + ";" + gasStation.getON() + ";" + gasStation.getLPG() + ";" + gasStation.getStreet() + ";" + gasStation.getCity() + ";";
            //Use marker's title to send info to InfoWindowAdapter
            locationMarker.title(markerInfoToSend);

            /*TODO:For now ID is useless but in the future it will be used to add station to
            YourStationList by clicking button
            * */
            locationMarker.snippet(String.valueOf(gasStation.getID()));
            mMap.addMarker(locationMarker);
        }

        //locationMarker.snippet(stationLatLang[1]);
        // mMap.addMarker(locationMarker);
    }

    private Location getLastKnownLocation() {
        Location l = null;
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

                if (location != null) {
                    Toast.makeText(getApplicationContext(), "cos", Toast.LENGTH_SHORT).show();
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() +
                            "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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
                String[] tempString = completeAddress.split(",", 10);
                address = tempString[0];
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
                String[] tempString = completeAddress.split(",", 10);
                address = tempString[0];
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private static void getAddress(final List<GasStation> stationList, final Context context, final Handler handler) {
        /*Creates marker using <String>address

        May be  usefull during implementation of Data base with addresses of stations
        or just as a normal search engine in app
         */

        ArrayList<String[]> addresses = new ArrayList<String[]>();


        for (GasStation gasStation : stationList) {
            /* Build String to look for specific station using Geocoder
             *
             * if station is found - place marker
             * */

            //Pattern: {ID,Name,City+Street}
            String[] address;
            if (gasStation.getCity().equals(gasStation.getStreet())) {
                address = new String[]{"" + gasStation.getID(), gasStation.getName(), gasStation.getCity()};
            } else {
                address = new String[]{"" + gasStation.getID(), gasStation.getName(), gasStation.getCity() + " " + gasStation.getStreet()};
            }

            addresses.add(address);

            //Creating array of station addresses
        }

        final ArrayList<String[]> finalAddresses = addresses;
        //Making it final

        final Thread thread = new Thread() {
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                //Store latitude and longitude in array

                //String[] result = new String[4];
                ArrayList<String> addressesToSend = new ArrayList<>();
                //Create another array to store prepared Addresses with Lat Lng

                try {
                    /*TODO: check if u use full company name, geocoder is able to return shortened name

                   eg. FHU WOJTEK SZUDY ORLEN address.getXXX returns simple ORLEN
                    *
                    */

                    //for (int i = 0; i < stationList.size(); i++) {
                    for (int i = 5000; i < 9379; i++) {
                        //for loop using i to control portion of data sent do database
                        //smaller portions are calculated faster which is useful while debugging
                        List<Address> addressList = geocoder.getFromLocationName(String.valueOf(finalAddresses.get(i)[2]), 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);

                            int bias = 5000;
                            //Necessary to get specific station without using index out of array

                            //Pattern: {ID,Name,Lat,Lng}
                            addressesToSend.add(finalAddresses.get(i)[0] + ";" + finalAddresses.get(i)[1] + ";" + String.valueOf(address.getLatitude()) + ";" + String.valueOf(address.getLongitude()));
                            //Single address prepared to send, added as String to arrayList
                            Log.d("AddressToSend string " + stationList.size() + " ", addressesToSend.get(i - bias));

                        } else {
                            addressesToSend.add(finalAddresses.get(i)[0] + ";" + finalAddresses.get(i)[1] + ";1;1");
                        }
                    }
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    Message message=Message.obtain();
                    message.setTarget(handler);
                    //Set connection with handler
                    if(addressesToSend!=null){
                        message.what=1;
                        Bundle bundle=new Bundle();
                        bundle.putStringArrayList("address", addressesToSend);
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


    private class GeoHandler extends Handler {
        //Handles data from getAddress() method
        @Override
        public void handleMessage(@NonNull Message msg) {
            ArrayList<String> address;

            if (msg.what == 1) {
                //Receive message from method
                Bundle bundle = msg.getData();
                address = bundle.getStringArrayList("address");
            } else {
                address = null;
            }

            if (address != null) {
                DatabaseAccess stationsDatabase = DatabaseAccess.getInstance(getApplicationContext());
                for (String s : address) {
                    String[] stationLatLng = s.split(";");
                    //Returned: {ID,Name,Lat,Lng}


                    //if(stationLatLng[1].contains("IWOPOL"))
                    stationsDatabase.insertStationCoords(Integer.parseInt(stationLatLng[0]), stationLatLng[1], Double.parseDouble(stationLatLng[2]), Double.parseDouble(stationLatLng[3]));

                }
                stationsDatabase.closeConnection();
                // MarkerOptions locationMarker = new MarkerOptions();
                //locationMarker.position(locationLatLng);

                //locationMarker.snippet(stationLatLang[1]);
                // mMap.addMarker(locationMarker);

            }
        }
    }
}
