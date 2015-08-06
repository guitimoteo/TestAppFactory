package br.com.teste.testeappfactory.controller;

import android.app.Dialog;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.gson.GsonObjectParser;

import br.com.teste.testeappfactory.BuildConfig;
import br.com.teste.testeappfactory.R;
import br.com.teste.testeappfactory.model.Post;

public class MapsController extends FragmentActivity implements GpsStatus.Listener, LocationListener {

    private static final String TAG = "MapController";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager location;
    private Post post;
    private double longi;
    private double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        post = new Post();
        location = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        getRestPost();
        location = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        int lat =0;
        int longi = 0;
        if(location != null) {
            location.addGpsStatusListener(this);
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi)).title("Marker"));
    }

     private void getRestPost(){
         Ion.with(this).load(BuildConfig.URL+"posts/1").asJsonObject().setCallback(new GetCallback());
     }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(mMap != null){
            lat =location.getLatitude();
            longi =location.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,longi)));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "provider enable: "+ provider);

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetCallback implements com.koushikdutta.async.future.FutureCallback<com.google.gson.JsonObject> {
        @Override
        public void onCompleted(Exception e, JsonObject result) {
            if(result != null) {
                post = new Gson().fromJson(result, Post.class);
            showMessage(post);
            }
        }

    }

    private void showMessage(Post post) {
        final Post mPost = post;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MapsController.this)
                        .setCancelable(true)
                        .setTitle(mPost.getTitle())
                        .setTitle(mPost.getId())
                        .setMessage(mPost.getBody());
            }
        });
    }
}
