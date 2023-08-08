package com.coderbd.smartlocationfinder;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.coderbd.smartlocationfinder.category.Category;
import com.coderbd.smartlocationfinder.category.CategoryDatabaseHandler;
import com.coderbd.smartlocationfinder.databinding.ActivityMapsBinding;
import com.coderbd.smartlocationfinder.location.LocationData;
import com.coderbd.smartlocationfinder.location.LocationDataDatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    Button btnSignOut;
    SharedPreferences sharedpreferences;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    TextView okay_text, cancel_text, latView, lonView;
    Spinner spinnerCate;
    EditText locationName;


    // Ask for All permissions
    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isInternetPermissionGranted = false;
    private boolean isAccessNetworkStatePermissionGranted = false;
    private boolean isAccessFineLocationPermissionGranted = false;
    private boolean isAccessCoarseLocationPermissionGranted = false;


    // For Getting Current Location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected double latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    ArrayList<LatLng> locations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ask for permission
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.INTERNET) != null) {
                    isInternetPermissionGranted = result.get(Manifest.permission.INTERNET);
                }
                if (result.get(Manifest.permission.ACCESS_NETWORK_STATE) != null) {
                    isInternetPermissionGranted = result.get(Manifest.permission.ACCESS_NETWORK_STATE);
                }
                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                    isInternetPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (result.get(Manifest.permission.ACCESS_COARSE_LOCATION) != null) {
                    isInternetPermissionGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
            }
        });
        requestPermission();
        //End of Asking Permission

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //For getting Current Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        sharedpreferences = getSharedPreferences(SignupActivity.BMI_SIGNUP_PREFERENCES, Context.MODE_PRIVATE);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(SignupActivity.SIGN_IN_STATUS, "false");
                editor.commit();

                Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
LocationDataDatabaseHandler db = new LocationDataDatabaseHandler(getApplicationContext());
List<LocationData> locationDataList = db.getAllLocations();
        // Add More Markers
        for(LocationData locationData : locationDataList){
            LatLng latLng = new LatLng(Double.parseDouble(locationData.getLat()), Double.parseDouble(locationData.getLon()));
            locations.add(latLng);
            }
//        locations.add(new LatLng(22.820000, 89.550003));//Khulna
//        locations.add(new LatLng(23.999941, 90.420273));//Gazipur
//        locations.add(new LatLng(23.643999, 88.855637));//Chuadanga
//        locations.add(new LatLng(23.644480, 90.598434));//Sonargaon
//        locations.add(new LatLng(23.622641, 90.499794));//N Ganj
//        locations.add(new LatLng(25.778522, 88.897377));//Saidpur
//        locations.add(new LatLng(26.335377, 88.551697));//panchagarh



    }

    private void showDialog(String lat, String lon) {
        Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.location_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        initializeSpinnerUI(dialog);
        okay_text = dialog.findViewById(R.id.okay_text);
        cancel_text = dialog.findViewById(R.id.cancel_text);
        spinnerCate = dialog.findViewById(R.id.spinner);
        locationName = dialog.findViewById(R.id.location_name);

        latView = dialog.findViewById(R.id.lat);
        lonView = dialog.findViewById(R.id.lon);
        latView.setText("Lat: "+lat);
        lonView.setText("Lon: "+lon);


        okay_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LocationDataDatabaseHandler db =new LocationDataDatabaseHandler(getApplicationContext());
                LocationData locationData=new LocationData(locationName.getText().toString(),spinnerCate.getSelectedItem().toString().split(":")[1],lat,lon);
                db.addCategory(locationData);
                Toast.makeText(MapsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(MapsActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void initializeSpinnerUI(Dialog dialog) {
        CategoryDatabaseHandler db=new CategoryDatabaseHandler(getApplicationContext());
        spinnerCate = dialog.findViewById(R.id.spinner);
        List<Category> categories = db.getAllCategories();
        ArrayList<String> categories1 = new ArrayList<>();
        for(int i=0; i < categories.size(); i++){
            categories1.add(categories.get(i).getId()+" : "+categories.get(i).getName());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner_style, categories1);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCate.setAdapter(adapter);
    }

    public void btnGotoCate(View view) {
        Intent intent = new Intent(MapsActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    public void btnGotoLocationList(View view) {
        Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
        startActivity(intent);
    }

    private void requestPermission() {
        isInternetPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        isAccessNetworkStatePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
        isAccessFineLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        isAccessCoarseLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        List<String> permissionRequest = new ArrayList<>();
        if (!isInternetPermissionGranted) {
            permissionRequest.add(Manifest.permission.INTERNET);
        }
        if (!isAccessNetworkStatePermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!isAccessFineLocationPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!isAccessCoarseLocationPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(23.8103, 90.4125);
//        LatLng currentLocation = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Now You are Here" + String.valueOf(latitude) + " : " + String.valueOf(longitude)));
//        mMap.setMapType(1);
//        mMap.setMinZoomPreference(9);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        for (int i = 0; i < locations.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locations.get(i)).title("DMPI Marker"));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(i)));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("New Marker");
                mMap.addMarker(marker);
                String lat = String.valueOf(point.latitude);
                String lang = String.valueOf(point.longitude);
                System.out.println(point.latitude + "---" + point.longitude);
                //  Toast.makeText(MapsActivity.this,lat+" ::: "+lang,Toast.LENGTH_LONG).show();
                showDialog(lat, lang);
            }
        });
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        longitude = location.getLongitude();
        latitude = location.getLatitude();
        System.out.printf("Current Location is: " + longitude + " : " + latitude);

        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(23.8103, 90.4125);
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Now You are Here" + String.valueOf(latitude) + " : " + String.valueOf(longitude)).snippet("Website: http://www.dmpi.gov.bd"));
        mMap.setMapType(1);
        mMap.setMinZoomPreference(9);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }
}