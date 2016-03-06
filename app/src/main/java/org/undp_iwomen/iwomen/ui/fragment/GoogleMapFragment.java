package org.undp_iwomen.iwomen.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.makeramen.RoundedImageView;

import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.Rating;
import org.smk.model.TLGTownship;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.TlgProfileActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 5/17/15.
 */
public class GoogleMapFragment extends Fragment {//

    private MapFragment mapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double coordinates[] = new double[2];

    private Context mContext;
    private ArrayList<TLGTownship> tlgArraylist;
    //List<TlgProfileItem> tlgList;
    private String mstr_lang;
    SharedPreferences sharePrefLanguageUtil;
    private StorageUtil storageUtil;
    private Menu menu;
    private Rating avgRatings;

    private CustomTextView txtName;
    private CustomTextView txtBody;
    private RoundedImageView profileImg;
    //private ProgressBar profileProgressbar;
    private CustomTextView txtAuthorTitle;

    private final String Location_READ_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    boolean locationPermissionAccepted = false;

    private final String READ_CONTACTS_PERMISSION = "android.permission.READ_CONTACTS";
    boolean readcontactPermissionAccepted = false;

    //Try request code between 1 to 255
    private static final int INITIAL_REQUEST = 1;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 2;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST + 1;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;


    public GoogleMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tlg_map, container, false);
        mContext = getActivity().getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        init(rootView);
        getReview();
        return rootView;
    }

    private void init(View rootView) {
        txtName = (CustomTextView) rootView.findViewById(R.id.map_mdk_name);
        txtAuthorTitle = (CustomTextView) rootView.findViewById(R.id.map_mdy_network);
        txtBody = (CustomTextView) rootView.findViewById(R.id.map_about_mdk);


        profileImg = (RoundedImageView) rootView.findViewById(R.id.map_profilePic_rounded);
        //profileProgressbar = (ProgressBar) rootView.findViewById(R.id.map_progressBar_profile_item);
        tlgArraylist = new ArrayList<TLGTownship>();
        tlgArraylist = (ArrayList<TLGTownship>) storageUtil.ReadArrayListFromSD("TlgArrayList");


        txtBody.setText(getResources().getString(R.string.may_doe_kabar));
        if (!isGooglePlayServicesAvailable()) {
            Log.e("Google Map not ok", "==>");
            //getActivity().finish();
            Toast.makeText(mContext, "Google Map not support", Toast.LENGTH_LONG).show();
            BeTogetherFragment beTogetherFragment = new BeTogetherFragment();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_map_frame, beTogetherFragment).commit();

        } else {
            Log.e("tlgArrSize", "" + tlgArraylist.size());
            setUpMapIfNeeded();


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {


                    for (TLGTownship tlgTownship : tlgArraylist) {
                        Log.i("Map TLG", marker.getTitle() + "=>" + tlgTownship.getTlgGroupName());
                        if (marker.getTitle().equals(tlgTownship.getTlgGroupName())) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), TlgProfileActivity.class);////DetailActivity

                            intent.putExtra("tlgObj", new Gson().toJson(tlgTownship));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                }
            });
        }

        /*mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.tlg_map_detail);*/

        // mapFragment.getMapAsync(getActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case LOCATION_REQUEST:

                locationPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (locationPermissionAccepted) {
                    setUpMapIfNeeded();
                }

                break;
            case CONTACTS_REQUEST:

                readcontactPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (locationPermissionAccepted) {
                    setUpMapIfNeeded();
                }

                break;

        }
    }


    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (!isGooglePlayServicesAvailable()) {
            //getActivity().finish();
            //Toast.makeText(mcontext, Config.MSG_GOOGLE_PLAY_NOT_OK, Toast.LENGTH_LONG).show();
        } else {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.tlg_map_detail)).getMap();

                //check whether there is permission for READ_STORAGE_PERMISSION
                /*else if (!hasPermission(READ_CONTACTS_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {READ_CONTACTS_PERMISSION};



                    requestPermissions(perms, CONTACTS_REQUEST);
                    Log.e("<<<else if>>>", "===>");

                }*/
                if (!hasPermission(Location_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {Location_READ_PERMISSION};


                    requestPermissions(perms, LOCATION_REQUEST);
                    Log.e("<<<if>>>", "===>");

                } else {
                    // Check if we were successful in obtaining the map.
                    Log.e("<<<else>>>", "===>");
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    if (mMap != null) {
                        setUpMap();

                    }
                }


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
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (Connection.isOnline(mContext)) {

            //Log.e("<<< calling map api >>>","===>");
            NetworkEngine.getInstance().getTLGTownship(new Callback<List<TLGTownship>>() {
                @Override
                public void success(List<TLGTownship> tlgTownships, Response response) {
                    tlgArraylist.clear();
                    tlgArraylist.addAll(tlgTownships);

                    /*TLGTownshipSpinnerAdapter adapter = new TLGTownshipSpinnerAdapter((AppCompatActivity) getActivity(), tlgTownshipArrayList);
                    spnTLG.setAdapter(adapter);tlgTownshipArrayList.get(position).getId().toString();*/

                    //Log.e("Success tlgList", "==>" + tlgArraylist.size());
                    storageUtil.SaveArrayListToSD("TlgArrayList", tlgArraylist);

                    setmapData(tlgArraylist);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RetrofitError", "==>" + error);
                }
            });


        } else {

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }


    }

    private void setmapData(ArrayList<TLGTownship> tlgTownshipArrayList) {
        for (int i = 0; i < tlgTownshipArrayList.size(); i++) {

            if (tlgTownshipArrayList.get(i).getTlgGroupLatAddress() != "null") {
                double lat = Double.parseDouble(tlgTownshipArrayList.get(i).getTlgGroupLatAddress()); //Nearby[i][0];//
                double lng = Double.parseDouble(tlgTownshipArrayList.get(i).getTlgGroupLngAddress());// Nearby[i][1];
                LatLng pick_up = new LatLng(lat, lng);

                //LatLng pick_up = new LatLng(16.779602, 96.151679);
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMap.addMarker(new MarkerOptions()

                        .title(tlgTownshipArrayList.get(i).getTlgGroupName())

                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon))
                        .snippet(tlgTownshipArrayList.get(i).getTlgGroupAddress())
                        .position(pick_up));

                //CameraPosition cameraPosition = new CameraPosition.Builder().target(pick_up).zoom(14.0f).build();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pick_up).zoom(5.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.moveCamera(cameraUpdate);
            }
        }
    }

    private void setmapData() {
        for (int i = 0; i < tlgArraylist.size(); i++) {

            if (tlgArraylist.get(i).getTlgGroupLatAddress() != "null") {
                double lat = Double.parseDouble(tlgArraylist.get(i).getTlgGroupLatAddress()); //Nearby[i][0];//
                double lng = Double.parseDouble(tlgArraylist.get(i).getTlgGroupLngAddress());// Nearby[i][1];
                LatLng pick_up = new LatLng(lat, lng);

                //LatLng pick_up = new LatLng(16.779602, 96.151679);
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMap.addMarker(new MarkerOptions()

                        .title(tlgArraylist.get(i).getTlgGroupName())

                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon))
                        .snippet(tlgArraylist.get(i).getTlgGroupAddress())
                        .position(pick_up));

                //CameraPosition cameraPosition = new CameraPosition.Builder().target(pick_up).zoom(14.0f).build();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pick_up).zoom(5.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.moveCamera(cameraUpdate);
            }
        }
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            //l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());//MapsActivity.this
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            //GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);

        this.menu = menu;
        this.menu.findItem(R.id.action_rating).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                setUpMapIfNeeded();
                return true;
            case R.id.action_rating:
                showReviewDetailDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReview() {
        NetworkEngine.getInstance().getReview("Be Together", new Callback<Rating>() {


            @Override
            public void success(Rating arg0, Response response) {
                menu.findItem(R.id.action_rating).setVisible(true);
                menu.findItem(R.id.action_rating).setIcon(BaseActionBarActivity.getRatingIcon(arg0.getTotalRatings()));
                avgRatings = arg0;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void showReviewDetailDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = View.inflate(getActivity(), R.layout.dialog_reviews_detial, null);
        TextView txt_avg_title = (TextView) convertView.findViewById(R.id.txt_avg_title);
        TextView txt_total_rating = (TextView) convertView.findViewById(R.id.txt_total_rating);
        RatingBar avg_ratings = (RatingBar) convertView.findViewById(R.id.avg_ratings);
        TextView txt_avg_ratings = (TextView) convertView.findViewById(R.id.txt_avg_ratings);
        TextView txt_rating_desc = (TextView) convertView.findViewById(R.id.txt_rating_desc);
        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        alertDialog.setView(convertView);

        txt_avg_title.setText(getResources().getString(R.string.str_overall_rating_be_together));
        txt_total_rating.setText(avgRatings.getTotalRatings() + "");
        avg_ratings.setRating(avgRatings.getTotalRatings().floatValue());
        txt_rating_desc.setText(getRatingDesc(avgRatings.getTotalRatings()));
        txt_avg_ratings.setText(avgRatings.getTotalUsers() + " " + getResources().getString(R.string.str_total));

        final AlertDialog ad = alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public String getRatingDesc(Double ratings) {
        String ratingOfDesc = "";
        if (ratings > 0 && ratings <= 1.5) {
            ratingOfDesc = getResources().getString(R.string.str_poor);
        }
        if (ratings > 1.5 && ratings <= 2.5) {
            ratingOfDesc = getResources().getString(R.string.str_fair);
        }
        if (ratings > 2.5 && ratings <= 3.5) {
            ratingOfDesc = getResources().getString(R.string.str_good);
        }
        if (ratings > 3.5 && ratings <= 4.5) {
            ratingOfDesc = getString(R.string.str_very_good);
        }
        if (ratings > 4.5) {
            ratingOfDesc = getResources().getString(R.string.str_excellent);
        }
        return ratingOfDesc;
    }

    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    /**
     * Modifies the standard behavior to allow results to be delivered to fragments.
     * This imposes a restriction that requestCode be <= 0xffff.
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode != -1 && (requestCode & 0xffff0000) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
        super.startActivityForResult(intent, requestCode);
    }


}
