package com.uidmadapp.urgenceidentification;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uidmadapp.urgenceidentification.fragment.HomeFragment;
import com.uidmadapp.urgenceidentification.fragment.ProfileFragment;
import com.uidmadapp.urgenceidentification.handler.DatabaseHandler;
import com.uidmadapp.urgenceidentification.model.InformationComplet;
import com.uidmadapp.urgenceidentification.model.User;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    private DatabaseHandler db;
    protected NavigationView navigationView;

    private TextView nav_header_username;
    private TextView nav_header_useremail;
    private User currentUser;

    private CoordinatorLayout coordinatorLayout;
    //NFC operation
    private final static String utf8 = "UTF-8";
    private final static String utf16 = "UTF-16";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private static String tag_uiid;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getResources().getString(R.string.title_activity_home));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHandler(this);
        List<User> users = db.getAllUser();
        currentUser = users.get(0);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nav_header_username = (TextView) header.findViewById(R.id.nav_header_username);
        nav_header_useremail = (TextView) header.findViewById(R.id.nav_header_useremail);
        nav_header_username.setText(currentUser.getNom() + " " + currentUser.getPrenom());
        nav_header_useremail.setText(currentUser.getEmail());

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.container_body, new HomeFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setTitle("Urgence");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.homelayout);

        //test if device support NFC technology
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, getResources().getString(R.string.msg_device_notsupportnfc), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mNfcAdapter.isEnabled()) {
            Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_verify_network), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_nfc_disable), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};

        //google api location
        LocationManager lM = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean locationServiceIsEnabled = lM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!locationServiceIsEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            //Setting Dialog Title
            alertDialog.setTitle(R.string.msg_activate_location);

            //Setting Dialog Message
            alertDialog.setMessage(R.string.msg_activate_location);

            //On Pressing Setting button
            alertDialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            //On pressing cancel button
            alertDialog.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    return;
                }
            });
            alertDialog.show();
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                            //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_urgence:
                fragment = new HomeFragment();
                title = getString(R.string.emergency_title_bar);
                break;
            case R.id.nav_account:
                fragment = new ProfileFragment();
                title = getString(R.string.account_title_bar);
                break;
            case R.id.nav_logout:
                //perform logout process
                db.deleteAllUser();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                fragment = new HomeFragment();
                title = getString(R.string.emergency_title_bar);
                break;
        }

        if (fragment != null) {
            //on change le contenu
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
            // ici on change le titre de la barre de menu
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //google api location
        mGoogleApiClient.connect();

        //nfc
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        //google api location
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        //nfc
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();

        // get message if exists
        //Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        //retrieving tag uiid information
        Tag myTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        tag_uiid = Tools.bytesToHex(myTag.getId());

        new NetCheckAndSendTagInfo().execute();
        //Toast.makeText(this, "RESULTAT = " + tag_uiid + " || LONGITUDE = " + currentLongitude + " and LATITUDE = " + currentLatitude, Toast.LENGTH_SHORT).show();
    }

    private class NetCheckAndSendTagInfo extends AsyncTask<String, Void, Void> {
        private ProgressDialog nDialog;
        private boolean test = false;

        @Override
        protected Void doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200 || urlc.getResponseCode() == 302) {
                        test = true;
                        return null;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (test == true) {
                nDialog.dismiss();
                new HttpRequestTagScanned().execute();
            }
            if (test == false) {
                nDialog.dismiss();
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_verify_network), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(HomeActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class HttpRequestTagScanned extends AsyncTask<Void, Void, InformationComplet> {
        @Override
        protected InformationComplet doInBackground(Void... params) {
            try {
                MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                mvm.add("idtag", tag_uiid);
                mvm.add("longitude", String.valueOf(currentLongitude));
                mvm.add("latitude", String.valueOf(currentLatitude));

                final String url = getResources().getString(R.string.ws_scan_tag);
                RestTemplate restTemplate = new RestTemplate();
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                messageConverters.add(new FormHttpMessageConverter());
                messageConverters.add(new StringHttpMessageConverter());
                messageConverters.add(new MappingJackson2HttpMessageConverter());
                restTemplate.setMessageConverters(messageConverters);
                String u = restTemplate.postForObject(url, mvm, String.class);
                ObjectMapper mapper = new ObjectMapper();
                if(u!=null){
                    InformationComplet info = mapper.readValue(u, InformationComplet.class);
                    return info;
                }
                else {
                    return null;
                }
            } catch (Exception e) {
                Log.e("HomeActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(InformationComplet info) {
            Intent intent = new Intent(HomeActivity.this, SuccessScanTagActivity.class);
            intent.putExtra("InfoComplet", info);
            startActivity(intent);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Connection failed ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }
}
