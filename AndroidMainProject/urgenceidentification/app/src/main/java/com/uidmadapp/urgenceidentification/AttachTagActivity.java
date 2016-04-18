package com.uidmadapp.urgenceidentification;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class AttachTagActivity extends AppCompatActivity {

    private DatabaseHandler db;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_tag);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.attachTagLayout);

        final Button button_continue = (Button) findViewById(R.id.button_continue);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttachTagActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        db = new DatabaseHandler(this);
        List<User> users = db.getAllUser();
        currentUser = users.get(0);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        //nfc
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
        //nfc
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String s = action + "\n\n" + tag.toString();

        //retrieving tag uiid information
        Tag myTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        tag_uiid = Tools.bytesToHex(myTag.getId());

        new NetCheckAndSendTagInfo().execute();
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
            nDialog = new ProgressDialog(AttachTagActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class HttpRequestTagScanned extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                mvm.add("idtag", tag_uiid);
                mvm.add("iduser", String.valueOf(currentUser.getId_patient()));

                final String url = getResources().getString(R.string.ws_registertag_url);
                RestTemplate restTemplate = new RestTemplate();
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                messageConverters.add(new FormHttpMessageConverter());
                messageConverters.add(new StringHttpMessageConverter());
                messageConverters.add(new MappingJackson2HttpMessageConverter());
                restTemplate.setMessageConverters(messageConverters);
                String info = restTemplate.postForObject(url, mvm, String.class);
                return info;
            } catch (Exception e) {
                Log.e("AttachActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String info) {
            info = info.replace("\"", "");
            if(info.equals("ALREADY_USED")){
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_tag_alreadyused), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
            else if(info.equals("OK")){
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_tag_ok), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        }
    }
}
