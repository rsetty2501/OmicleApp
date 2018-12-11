package com.example.android.sircleapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;

public class RouteMap extends AppCompatActivity {

    // text view
    TextView textView;
    MapView mapView = null;


    ArrayList<LocationCoord> locationCoords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //inflate and create the map
        setContentView(R.layout.activity_mapview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getLocationCoordsList();

        uppsalaMapView();
        boolean flag = settingMarkerinMap();
        scaleBar();

        // Create route from one location to another
        createRoute(flag);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void getLocationCoordsList() {
        // Get the list of coordinates for the cluster
        locationCoords = (ArrayList<LocationCoord>) getIntent().getSerializableExtra(SircleAppConstants.LOCATIONCOORD_LIST);
        locationCoords.add(locationCoords.get(0));
    }

    private void uppsalaMapView() {

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(14);
        GeoPoint startPoint = new GeoPoint(locationCoords.get(0).getLatitude(), locationCoords.get(0).getLongitude());
        mapController.setCenter(startPoint);

    }

    private boolean settingMarkerinMap() {

        ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();

        // Check if there is at least 2 entry data in the database to make the route
        if (locationCoords.size() > 1) {

            for (int i = 0; i < locationCoords.size(); i++) {
                if (i == locationCoords.size() - 1) {
                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(locationCoords.get(i).getLatitude(), locationCoords.get(i).getLongitude()));
                    marker.setIcon(this.getResources().getDrawable(R.drawable.depot));
                    marker.setTitle("Ragn-Sells");
                    mapView.getOverlays().add(marker);
                } else {
                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(locationCoords.get(i).getLatitude(), locationCoords.get(i).getLongitude()));
                    marker.setIcon(this.getResources().getDrawable(R.drawable.users));
                    marker.setTitle(String.valueOf(i));
                    mapView.getOverlays().add(marker);

                }

//                overlayItems.add(new OverlayItem("Ragn-sells","Testing Location",
//                        new GeoPoint(locationCoords.get(i).getLatitude(), locationCoords.get(i).getLongitude())));

            }

//            ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay
//                    = new ItemizedOverlayWithFocus<OverlayItem>(this, overlayItems, null);
//            anotherItemizedIconOverlay.setFocusItemsOnTap(true);
//            mapView.getOverlays().add(anotherItemizedIconOverlay);


            return true;
        } else {
            return false;
        }
    }

    private void scaleBar() {
        //Add Scale Bar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(mapView);
        mapView.getOverlays().add(myScaleBarOverlay);
    }

    private void createRoute(boolean flag) {
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        GeoPoint startPoint;
        GeoPoint endPoint;

        if (flag) {
            for (int i = 0; i < locationCoords.size(); i++) {

                if ((i + 1) == locationCoords.size()) {
                    break;
                } else {
                    startPoint = new GeoPoint(locationCoords.get(i).getLatitude(), locationCoords.get(i).getLongitude());
                    endPoint = new GeoPoint(locationCoords.get(i + 1).getLatitude(), locationCoords.get(i + 1).getLongitude());

                    waypoints.add(startPoint);
                    waypoints.add(endPoint);
                    new RoadMap().execute(waypoints);
                }
            }

        } else {
            textView.setText("No route available");
        }

    }

    private class RoadMap extends AsyncTask<ArrayList<GeoPoint>, Integer, Polyline> {


        @SafeVarargs
        @Override
        protected final Polyline doInBackground(ArrayList<GeoPoint>... waypoints) {

//            RoadManager roadManager = new MapQuestRoadManager("lAAGx5jEeYKdwHBSDGr08PL4zXOHz5uo");
            RoadManager roadManager = new OSRMRoadManager(getApplicationContext());
            Road road = null;

            try{
                road = roadManager.getRoad(waypoints[0]);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            assert road != null;
            if(road.mStatus != Road.STATUS_OK){
                Log.e("Route","Error!");
            }

            return RoadManager.buildRoadOverlay(road);
        }

        @Override
        protected void onPostExecute(Polyline result) {
            mapView.getOverlays().add(result);
            mapView.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
