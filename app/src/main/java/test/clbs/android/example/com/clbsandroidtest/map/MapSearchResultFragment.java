package test.clbs.android.example.com.clbsandroidtest.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Config;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pub.devrel.easypermissions.EasyPermissions;
import test.clbs.android.example.com.clbsandroidtest.R;
import test.clbs.android.example.com.clbsandroidtest.base.BaseFragment;
import test.clbs.android.example.com.clbsandroidtest.databinding.MapSearchResultFragmentBinding;
import test.clbs.android.example.com.clbsandroidtest.model.AjaxResponse;
import test.clbs.android.example.com.clbsandroidtest.model.AjaxResponseModel;
import test.clbs.android.example.com.clbsandroidtest.model.Geometry;
import test.clbs.android.example.com.clbsandroidtest.model.LocationDBAdapter;
import test.clbs.android.example.com.clbsandroidtest.model.LocationList;
import test.clbs.android.example.com.clbsandroidtest.model.LocationListViewModel;
import test.clbs.android.example.com.clbsandroidtest.model.LocationModel;
import test.clbs.android.example.com.clbsandroidtest.services.ServiceAPI;
import test.clbs.android.example.com.clbsandroidtest.util.DialogUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class MapSearchResultFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks, OnMapReadyCallback {

    //Global Variable
    //View
    private View view;

    //View Binding
    private MapSearchResultFragmentBinding binding;

    //Database
    LocationDBAdapter locationDBAdapter;

    //Classes
    private DialogUtil dialogUtil;
    private ServiceAPI serviceAPI;
    AjaxResponseModel<LocationList> responseLocationList;

    LocationListViewModel locationListViewModel;

    //AsyncTask Management
    AsyncTask<String, Void, String> asyncLocationLis;

    private String latitude; private String longitude; private String radius;

    private final String apiKey = "AIzaSyCZ1BCe4Q7YL1nCa_ovtet4Bjn52tT20T8";

    private String api_url;

    //List: Database
    LocationList getLocationList;

    static List<LocationListViewModel> saveListState;

    static List<LocationListViewModel> getLocationListViewModels;
    List<LocationListViewModel> locationListViewModels = new ArrayList<>();

    //List: JSON
    List<LocationModel> locationListFromJSON;


    //Location Manager
    private LocationManager locationManager;
    private LocationListener listener;

    //Get User Current Location
    double getUserLatitude;
    double getUserLongitude;

    private GoogleMap mMap;

    public static MapSearchResultFragment newInstance() {
        MapSearchResultFragment fragment = new MapSearchResultFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Check View id duplicate or not first
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            //Fragment View Reference
            binding = DataBindingUtil.inflate(
                    inflater, R.layout.map_search_result_fragment, container, false);
            view = binding.getRoot();
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        //Initial View & Data
        initial();
        prepareList();
        initViews();

        return view;
    }

    //Initial Setup
    private void initial() {

        //Classes
        //location = new Location();
        locationListViewModel = new LocationListViewModel();

        //Class Service
        serviceAPI = new ServiceAPI();

        //Loading Dialog
        this.dialogUtil = DialogUtil.getInstance();

        //List
        locationListFromJSON = new ArrayList<>();

        //Init Database
        locationDBAdapter = new LocationDBAdapter(getActivity());

        //Set View OnClickListener
        binding.btnMapSearch.setOnClickListener(onClickListener);

        //LocationManager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getUserLatitude = location.getLatitude();
                getUserLongitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        //Initial Get User Location Service
        getLocation();

    }

    //Prepare Data
    private void prepareList() {

        //Initial DatabaseAdapter
        openDB();

    }

    //Initial View Setup
    private void initViews() {

        //Set Layout Visibility
        binding.relativeMapSearchResultFeildLayout.setVisibility(LinearLayout.GONE); //Set INVISIBLE at first time

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(ListViewSearchResultFragment.getLocationListViewModel() != null) {

            getLocationListViewModels = new ArrayList<>();

            getLocationListViewModels = ListViewSearchResultFragment.getLocationListViewModel();

            //Add Marker from previous fragment
            for (LocationListViewModel item : ListViewSearchResultFragment.getLocationListViewModel()) {

                LatLng previousLocation = new LatLng(item.getLocationLatitude(), item.getLocationLongitude());
                mMap.addMarker(new MarkerOptions().position(previousLocation).snippet(item.getLocationDistance()).title(item.getLocationName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(previousLocation));
            }
        }

    }


    //Method OnClickListener: View
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {

                case R.id.btnMapSearch:
                    //DO something
                    showLayoutView(v);
            }

        }
    };


    //Display Layout Animation
    public void showLayoutView(View view) {

        binding.relativeMapSearchResultFeildLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.clbs_listview_fade_in);
        binding.relativeMapSearchResultFeildLayout.setAnimation(animation);
        binding.relativeMapSearchResultFeildLayout.animate();
        animation.start();

        final String[] latitudeInputField = new String[1];
        final String[] longitudeInputField = new String[1];

        //Checking data from database first
        //latitudeInputField
        this.binding.latitudeSearchResultField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //Clear Object First
                locationListViewModels.clear();
                locationListViewModel = null;

                if (mMap != null) {
                    mMap.clear();
                }

                latitudeInputField[0] = binding.latitudeSearchResultField.getText().toString();
                latitude = latitudeInputField[0]; //Assign Value to Global Variable

                Cursor cusrorLat = locationDBAdapter.getLocationDataByLatitude(latitudeInputField[0]);

                //Manage lifetime of the cursor
                getActivity().startManagingCursor(cusrorLat);

                //If Location already exist in database
                if (cusrorLat.moveToFirst()) {

                    locationListViewModel = new LocationListViewModel();

                    locationListViewModel.setLocationName(cusrorLat.getString(LocationDBAdapter.COL_LOCATION_NAME));
                    locationListViewModel.setLocationLatitude(cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE));
                    locationListViewModel.setLocationLongitude(cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                    //Get Location distance from the user current location
                    String distance = getDistance(getUserLatitude,
                            getUserLongitude,
                            cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE),
                            cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                    locationListViewModel.setLocationDistance(distance);

                    locationListViewModels.add(locationListViewModel);


                    //Add Marker into Map
                    LatLng latitudeSearchResultField = new LatLng(cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE), cusrorLat.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));
                    mMap.addMarker(new MarkerOptions().position(latitudeSearchResultField).title(cusrorLat.getString(LocationDBAdapter.COL_LOCATION_NAME)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latitudeSearchResultField));


                } else {
                }

//                cusrorLat.close();

            }
        });


        //longitudeInputField
        this.binding.longitudeSearchResultField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //Clear Object First
                locationListViewModels.clear();
                locationListViewModel = null;

                if (mMap != null) {
                    mMap.clear();
                }

                longitudeInputField[0] = binding.longitudeSearchResultField.getText().toString();
                longitude = longitudeInputField[0]; //Assign Value to Global Variable

                Cursor cusrorLng = locationDBAdapter.getLocationDataByLongitude(longitudeInputField[0]);

                //Manage lifetime of the cursor
                getActivity().startManagingCursor(cusrorLng);

                //If Location already exist in database
                if (cusrorLng.moveToFirst()) {

                    locationListViewModel = new LocationListViewModel();

                    locationListViewModel.setLocationName(cusrorLng.getString(LocationDBAdapter.COL_LOCATION_NAME));
                    locationListViewModel.setLocationLatitude(cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE));
                    locationListViewModel.setLocationLongitude(cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                    //Get Location distance from the user current location
                    String distance = getDistance(getUserLatitude,
                            getUserLongitude,
                            cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE),
                            cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                    locationListViewModel.setLocationDistance(distance);

                    locationListViewModels.add(locationListViewModel);

                    //Add Marker into Map
                    LatLng longitudeSearchResultField = new LatLng(cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE), cusrorLng.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));
                    mMap.addMarker(new MarkerOptions().position(longitudeSearchResultField).title(cusrorLng.getString(LocationDBAdapter.COL_LOCATION_NAME)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(longitudeSearchResultField));

                } else {
                }

//                cusrorLng.close();

            }
        });


        //Method to Process Search Submit Button
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                //Set Search Button OnClickListener
                binding.mapSearchResultSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //Validation Radious
                        final String checkRadiusInput = binding.radiusSearchResultField.getText().toString();
                        if (!isValidRadius(checkRadiusInput)) {
                            //Get User Current Location
                            getLocation();

                            if(getLocationListViewModels!=null){
                                getLocationListViewModels.clear();
                            }

                            radius = binding.radiusSearchResultField.getText().toString();

                            //Setup API URL
                            api_url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude +
                                    "," + longitude + "&radius=" + radius + "&key=" + apiKey;

                            //execute API
                            executeAPIServices();

                            binding.relativeMapSearchResultFeildLayout.setVisibility(LinearLayout.GONE); //Set Layout GONE

                        }
                        else{

                            binding.radiusSearchResultField.setError("Radius Should be 0 - 50000");
                        }

                    }
                });
            }
        });

    }


    //Validating Radius
    private boolean isValidRadius(String radius) {
        if (radius != null && Double.parseDouble(radius) <= 0 || Double.parseDouble(radius) >= 50000) {
            return true;
        }
        return false;
    }


    //Method to save LocationMapSearch
    public static List<LocationListViewModel> getLocationMapSearchModel() {
        return getLocationListViewModels;
    }

    private void executeAPIServices() {

        dialogUtil.showLoadingDialog(getActivity());

        //Start Thread to handle asyncTask status
        final Thread waitingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Invalidate Object First
                    locationListFromJSON.clear();

                    responseLocationList = new AjaxResponseModel<>();
                    getLocationList = new LocationList();

                    //Execute JSON from URL: Wealth
                    asyncLocationLis = serviceAPI.getLocationList(api_url, new AjaxResponse<LocationList>() {
                        @Override
                        public void callback(int responseCode, AjaxResponseModel<LocationList> response) {
                            if (responseCode == 200) {

                                //Data Mapping
                                getLocationList = response.getItem();
                                locationListFromJSON = getLocationList.getLocations();

                            } else {

                            }
                        }
                    });


                    //Check AsyncTask Status Condition
                    while (asyncLocationLis.getStatus() != AsyncTask.Status.FINISHED) {
                        TimeUnit.SECONDS.sleep(1);
                    }

                    //Check Status is finish or not
                    Log.e("Status", String.valueOf(asyncLocationLis.getStatus()));

                    //Present Data
                    processData(locationListFromJSON);

                    //Close loading dialog
                    if (dialogUtil != null) {
                        dialogUtil.closeLoadingDialog();
                    }

                } catch (InterruptedException e) {
                    // TODO log
                }
            }
        });

        waitingThread.start();

    }

    //Method to Process Data from JSON
    private void processData(List<LocationModel> locationListFromJSON) {

        if(locationListViewModels!=null){
            locationListViewModels.clear();
        }

        locationListViewModel = null;

        //Assign Data into ListView
        locationListViewModel = new LocationListViewModel();

        //Checking Location already exist or not
        for (LocationModel item : locationListFromJSON) {


            Cursor jCursor = locationDBAdapter.checkLocationDataByLatLng(item.getGeometry().getGeoLocation().getLatitude(),
                    item.getGeometry().getGeoLocation().getLongitude());

            //Manage lifetime of the cursor
            getActivity().startManagingCursor(jCursor);

            //If Location already exist in database
            if (jCursor.moveToFirst()) {


                locationListViewModel.setLocationName(jCursor.getString(LocationDBAdapter.COL_LOCATION_NAME));
                locationListViewModel.setLocationLatitude(jCursor.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE));
                locationListViewModel.setLocationLongitude(jCursor.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                //Get Location distance from the user current location
                String distance = getDistance(getUserLatitude,
                        getUserLongitude,
                        jCursor.getDouble(LocationDBAdapter.COL_LOCATION_LATITUDE),
                        jCursor.getDouble(LocationDBAdapter.COL_LOCATION_LONGITUDE));

                locationListViewModel.setLocationDistance(distance);

                locationListViewModels.add(locationListViewModel);

            } else {

                //Add New Data from JSON into ListViewModel
                locationListViewModels.add(new LocationListViewModel(
                        item.getName(),
                        Double.parseDouble(item.getGeometry().getGeoLocation().getLatitude()),
                        Double.parseDouble(item.getGeometry().getGeoLocation().getLongitude()),
                        getDistance(getUserLatitude,
                                getUserLongitude,
                                Double.parseDouble(item.getGeometry().getGeoLocation().getLatitude()),
                                Double.parseDouble(item.getGeometry().getGeoLocation().getLongitude())),
                        0.0
                ));

            }

//            jCursor.close();

            //For Loop to pin all markerList
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (LocationListViewModel item : locationListViewModels) {

                            //Add ALL Marker into Map
                            LatLng searchResultField = new LatLng(item.getLocationLatitude(), item.getLocationLongitude());
                            mMap.addMarker(new MarkerOptions().position(searchResultField).title(item.getLocationName()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(searchResultField));

                        }
                    }catch (ConcurrentModificationException e){}
                }
            });
        }

        this.getLocationListViewModels =locationListViewModels;
    }


    //Distance Calculation
    public String getDistance(final double userLat, final double userLng, final double lat2, final double lon2) {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + userLat + "," + userLng + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] = distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parsedDistance[0];
    }


    //Initial Database Service
    private void openDB() {

        //Database Open
        locationDBAdapter.open();

    }


    private void getLocation() {

        //First check for permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        //Update location
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Permission Management
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //When Permissions Granted
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("Accept", "onPermissionsGranted:");

        switch (requestCode) {
            case 10:
                getLocation();
                break;
            default:
                break;
        }

    }

    //When Permissions Denied
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e("Denied", "onPermissionsDenied:");
    }

    //Life Cycle
    //Close DB : avoid lacking of memory
    private void closeDB() {
        locationDBAdapter.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }


}
