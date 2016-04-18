package com.uidmadapp.urgenceidentification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uidmadapp.urgenceidentification.handler.DatabaseHandler;
import com.uidmadapp.urgenceidentification.model.InformationComplet;
import com.uidmadapp.urgenceidentification.model.Maladie;
import com.uidmadapp.urgenceidentification.model.User;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    MultiAutoCompleteTextView maladies;
    static List<String> listeMaladies = new ArrayList();
    EditText edittext_nom;
    EditText edittext_prenom;
    RadioGroup radio_register_sex;
    EditText edittext_datenaissance;
    EditText edittext_adresse;
    EditText edittext_contact;
    EditText edittext_email_register;
    EditText edittext_password_register1;
    EditText edittext_password_register2;
    TextView probleme_exemple;
    static String exemple = "(*) Epilepsie, Hypoglycémie, Problème cardiaque, Alzheimer, Asthme, Diabète";

    private CoordinatorLayout coordinatorLayout;
    private DatabaseHandler db;

    String nom;
    String prenom;
    String sexe;
    String datenaissance;
    String adresse;
    String contact;
    String mail;
    String motdepasse;
    String[] listeM;

    Button signUp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        db = new DatabaseHandler(this);


        maladies = (MultiAutoCompleteTextView) findViewById(R.id.multiAutocompleteMaladie);
        signUp = (Button) findViewById(R.id.sign_up_button);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.registerLayout);

        listeMaladies.add("Epilepsie");
        listeMaladies.add("Hypoglycémie");
        listeMaladies.add("Problème cardiaque");
        listeMaladies.add("Alzheimer");
        listeMaladies.add("Asthme");
        listeMaladies.add("Diabète");

        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listeMaladies);
        maladies.setAdapter(adapter);
        maladies.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        probleme_exemple = (TextView) findViewById(R.id.probleme_exemple);
        probleme_exemple.setText(exemple);

        edittext_nom = (EditText) findViewById(R.id.edittext_nom);
        edittext_prenom = (EditText) findViewById(R.id.edittext_prenom);
        radio_register_sex = (RadioGroup) findViewById(R.id.radio_register_sex);
        edittext_datenaissance = (EditText) findViewById(R.id.edittext_datenaissance);
        edittext_adresse = (EditText) findViewById(R.id.edittext_adresse);
        edittext_contact = (EditText) findViewById(R.id.edittext_contact);
        edittext_email_register = (EditText) findViewById(R.id.edittext_email_register);
        edittext_password_register1 = (EditText) findViewById(R.id.edittext_password_register1);
        edittext_password_register2 = (EditText) findViewById(R.id.edittext_password_register2);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = edittext_nom.getText().toString();
                prenom = edittext_prenom.getText().toString();
                int selectedId = radio_register_sex.getCheckedRadioButtonId();
                if (selectedId == R.id.radioMale)
                    sexe = "1";
                else
                    sexe = "0";
                datenaissance = edittext_datenaissance.getText().toString();
                adresse = edittext_adresse.getText().toString();
                contact = edittext_contact.getText().toString();
                mail = edittext_email_register.getText().toString();
                String m1 = edittext_password_register1.getText().toString();
                String m2 = edittext_password_register2.getText().toString();

                if (nom.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_name), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_nom.requestFocus();
                    return;
                }
                if (prenom.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_firstname), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_prenom.requestFocus();
                    return;
                }
                if (datenaissance.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_datenaissance), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_datenaissance.requestFocus();
                    return;
                }
                if (adresse.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_adresse), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_adresse.requestFocus();
                    return;
                }
                if (mail.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_mail), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_email_register.requestFocus();
                    return;
                }
                if (m1.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_password), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_password_register1.requestFocus();
                    return;
                }
                if (m2.isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_password), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_password_register2.requestFocus();
                    return;
                }
                if (!m1.equals(m2)) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_verify_password), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    edittext_password_register2.requestFocus();
                    return;
                }
                motdepasse = m1;
                if (maladies.getText().toString().isEmpty()) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_maladie), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    maladies.requestFocus();
                    return;
                }
                listeM = maladies.getText().toString().split(",");
                List<String> tenaIz = new ArrayList<String>();
                for(int k = 0;k<listeM.length;k++){
                    if(listeM[k].trim().length() > 0)
                        tenaIz.add(listeM[k].trim());
                }
                listeM = new String[tenaIz.size()];
                tenaIz.toArray(listeM);
                for (String item : listeM) {
                    if (!listeMaladies.contains(item)) {
                        Snackbar.make(coordinatorLayout, item+" "+getResources().getString(R.string.msg_maladies_notexist), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                        maladies.requestFocus();
                        return;
                    }
                }
                new NetCheckAndRegister().execute();
            }
        });
    }

    private class NetCheckAndRegister extends AsyncTask<String, Void, Void> {
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
                new HttpRequestRegisterTask().execute();
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
            nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class HttpRequestRegisterTask extends AsyncTask<Void, Void, User> {
        private ProgressDialog nDialog;

        @Override
        protected User doInBackground(Void... params) {
            try {
                MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                mvm.add("nom", nom);
                mvm.add("prenom", prenom);
                mvm.add("sexe", sexe);
                mvm.add("datenaissance", datenaissance);
                mvm.add("adresse", adresse);
                mvm.add("contact", contact);
                mvm.add("email", mail);
                mvm.add("motdepasse", motdepasse);
                mvm.add("taille",String.valueOf(listeM.length));
                for(int i=0;i<listeM.length;i++){
                    mvm.add("maladie"+i, listeM[i]);
                }

                final String url = getResources().getString(R.string.ws_register_url);
                RestTemplate restTemplate = new RestTemplate();
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                messageConverters.add(new FormHttpMessageConverter());
                messageConverters.add(new StringHttpMessageConverter());
                messageConverters.add(new MappingJackson2HttpMessageConverter());
                restTemplate.setMessageConverters(messageConverters);
                User[] u = restTemplate.postForObject(url, mvm, User[].class);
                if (u.length > 0) {
                    return u[0];
                }
                return null;
            } catch (Exception e) {
                Log.e("RegisterActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            nDialog.dismiss();
            if (user != null) {
                db.addUser(user);
                Intent intent = new Intent(RegisterActivity.this, AttachTagActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
    }
}
