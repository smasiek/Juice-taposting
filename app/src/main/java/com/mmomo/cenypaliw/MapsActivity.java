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
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
//Main activity containing map and markers
    private GoogleMap mMap;
    //Provide access to current location
    public FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    //Typical request code for write permission
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Set custom toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create roll-down menu in top right corner
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rolldown_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Handle roll-down menu item click
        switch (item.getItemId()) {
            case R.id.twojeStacje:
                Intent intent = new Intent(this,
                        YourStationListActivity.class);
                startActivity(intent);
                return true;
            case R.id.dodajNowaStacje:
                Intent intent2 = new Intent(this, AddNewStationActivity.class);
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

        LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions currLocationMarker = new MarkerOptions();

        //Set curr location to Jasło as emulator doesn't receive current location info
        currLocation=new LatLng(49.74506, 21.47252);
        //Set marker at current location
        currLocationMarker.position(currLocation);
        currLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.your_marker));
        mMap.addMarker(currLocationMarker);
        //Set zoom at your current location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 13), 5000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Create instance of external storage with stations info
        DatabaseAccess stationsDatabase=DatabaseAccess.getInstance(getApplicationContext());

        List<GasStation>stationList=stationsDatabase.getStationList();
        for (GasStation gasStation : stationList) {
            /* Build String to look for specific station using Geocoder
            *
            * if station is found - place marker
            * */
            StringBuilder address=new StringBuilder();
            address.append(gasStation.getCity()).append(" ");
            address.append(gasStation.getStreet());

            if(gasStation.getCity().equals("JASŁO")){
                //Tighten search scope to one city  to save CPU resources
                getAddress(address.toString(),getApplicationContext(),new GeoHandler());
            }
        }
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
                    //Get access to TextViews to modify them
                    TextView t1_stationName = (TextView) row.findViewById(R.id.stationNameTextMarkerView);
                    TextView t2_stationPetrolPrice = (TextView) row.findViewById(R.id.stationPetrolPriceMarkerValue);
                    TextView t3_stationStreet = (TextView) row.findViewById(R.id.stationStreetMarkerView);
                    TextView t4_stationCity = (TextView) row.findViewById(R.id.stationCityMarkerView);

                    LatLng ll = marker.getPosition();
                    //Get specific marker position and update infoWindow
                    //TODO: geocoderem przerobić LatLng na adres i po adresie znaleźć nazwe w bazie danych
                    t1_stationName.setText("Tymczasowa nazwa");//Stworzyć pobieranie nazwy z bazy
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
                //Save last location
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l == null) {
                //???
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                //If l has better accutacy than bestLocation then update bestLocation
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    //Probably this method may be deleted in future, i don't know if getLastKnownLocation works without it
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Check for necessary permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener((new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //If last location found, change current location
                if(location!=null){
                    currentLocation=location;
                }
            }
        }));
    }

    private String getStreetFromLatLng(double Latitude, double Longtitude) {
        //Return the name of street using latitude and longitude
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                //Get full address line
                String completeAddress = "" + returnAddress.getAddressLine(0);
                //Separate street name from other data
                String[] tempString=completeAddress.split(",",10);
                //Street is in the beginning of address line
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
        //Return city using latitude and longitude
        String address = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                //Connect postal code and name of city in one String
                String stringReturnAddress = "" + returnAddress.getPostalCode() + " " +
                        returnAddress.getLocality();
                address = stringReturnAddress;
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private static void getAddress(final String locationAddress, final Context context, final Handler handler){
        //Creates marker using <String>address
        Thread thread = new Thread(){
            public void run(){
                Geocoder geocoder = new Geocoder(context,Locale.getDefault());
                //Store latitude and longitude in array
                double[] result = new double[2];
                try{
                    List<Address> addressList=geocoder.getFromLocationName(locationAddress,1);
                    if (addressList!=null && addressList.size()>0){
                        Address address = addressList.get(0);
                        result[0]=address.getLatitude();
                        result[1]=address.getLongitude();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    //Send result using pipe
                    Message message=Message.obtain();
                    message.setTarget(handler);
                    //Set connection with handler
                    if(result!=null){
                        message.what=1;
                        Bundle bundle=new Bundle();
                        bundle.putString("adress", result[0] +" "+result[1]);
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
                String[] latLang = address.split(" ", 2);

                LatLng locationLatLang = new LatLng(Double.parseDouble(latLang[0]), Double.parseDouble(latLang[1]));
                MarkerOptions locationMarker = new MarkerOptions();
                locationMarker.position(locationLatLang);
                mMap.addMarker(locationMarker);

            }
        }
    }
}