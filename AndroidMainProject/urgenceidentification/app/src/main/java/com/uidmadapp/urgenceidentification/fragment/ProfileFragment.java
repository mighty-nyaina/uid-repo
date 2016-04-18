package com.uidmadapp.urgenceidentification.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uidmadapp.urgenceidentification.AttachTagActivity;
import com.uidmadapp.urgenceidentification.HomeActivity;
import com.uidmadapp.urgenceidentification.R;
import com.uidmadapp.urgenceidentification.RegisterActivity;
import com.uidmadapp.urgenceidentification.handler.DatabaseHandler;
import com.uidmadapp.urgenceidentification.model.Maladie;
import com.uidmadapp.urgenceidentification.model.MaladieUser;
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

public class ProfileFragment extends Fragment {
    private Button attachLinkButton;
    private Button modify_information;
    private CoordinatorLayout coordinatorLayout;
    private User currentUser;

    MultiAutoCompleteTextView maladies;
    static List<String> listeMaladies = new ArrayList();
    static String maladieUser;
    EditText modify_edittext_nom;
    EditText modify_edittext_prenom;
    RadioGroup modify_radio_register_sex;
    EditText modify_edittext_datenaissance;
    EditText modify_edittext_adresse;
    EditText modify_edittext_contact;
    EditText modify_edittext_email_register;
    EditText modify_edittext_password_register1;
    EditText modify_edittext_password_register2;
    TextView modify_probleme_exemple;
    static String exemple = "(*) Epilepsie, Hypoglycémie, Problème cardiaque, Alzheimer, Asthme, Diabète";

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        attachLinkButton = (Button) v.findViewById(R.id.modify_attach_tag);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.homelayout);
        attachLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.msg_advertissement);
                alertDialog.setMessage(R.string.msg_quit_window);
                alertDialog.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), AttachTagActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        db = new DatabaseHandler(getActivity());
        List<User> users = db.getAllUser();
        currentUser = users.get(0);

        maladies = (MultiAutoCompleteTextView) v.findViewById(R.id.modify_multiAutocompleteMaladie);
        modify_information = (Button) v.findViewById(R.id.modify_information);

        listeMaladies.add("Epilepsie");
        listeMaladies.add("Hypoglycémie");
        listeMaladies.add("Problème cardiaque");
        listeMaladies.add("Alzheimer");
        listeMaladies.add("Asthme");
        listeMaladies.add("Diabète");

        ArrayAdapter adapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, listeMaladies);
        maladies.setAdapter(adapter);
        maladies.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        modify_probleme_exemple = (TextView) v.findViewById(R.id.modify_probleme_exemple);
        modify_probleme_exemple.setText(exemple);

        modify_edittext_nom = (EditText) v.findViewById(R.id.modify_edittext_nom);
        modify_edittext_prenom = (EditText) v.findViewById(R.id.modify_edittext_prenom);
        modify_radio_register_sex = (RadioGroup) v.findViewById(R.id.modify_radio_register_sex);
        final RadioButton male = (RadioButton)v.findViewById(R.id.modify_radioMale);
        final RadioButton female = (RadioButton)v.findViewById(R.id.modify_radioFemale);
        modify_edittext_datenaissance = (EditText) v.findViewById(R.id.modify_edittext_datenaissance);
        modify_edittext_adresse = (EditText) v.findViewById(R.id.modify_edittext_adresse);
        modify_edittext_email_register = (EditText) v.findViewById(R.id.modify_edittext_email_register);
        modify_edittext_contact = (EditText) v.findViewById(R.id.modify_edittext_contact);
        modify_edittext_password_register1 = (EditText) v.findViewById(R.id.modify_edittext_password_register1);
        modify_edittext_password_register2 = (EditText) v.findViewById(R.id.modify_edittext_password_register2);

        modify_edittext_nom.setText(currentUser.getNom());
        modify_edittext_prenom.setText(currentUser.getPrenom());
        if(currentUser.getSexe()==1){
            male.setChecked(true);
            female.setChecked(false);
        }
        else{
            male.setChecked(false);
            female.setChecked(true);
        }
        modify_edittext_datenaissance.setText(currentUser.getDate_naissance());
        modify_edittext_adresse.setText(currentUser.getDate_naissance());
        modify_edittext_contact.setText(currentUser.getContact());
        modify_edittext_email_register.setText(currentUser.getEmail());
        modify_edittext_password_register1.setText(currentUser.getPassword());
        modify_edittext_password_register2.setText(currentUser.getPassword());

        new NetCheckAndGetMaladie().execute();

        modify_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = modify_edittext_nom.getText().toString();
                prenom = modify_edittext_prenom.getText().toString();
                int selectedId = modify_radio_register_sex.getCheckedRadioButtonId();
                if (selectedId == R.id.modify_radioMale)
                    sexe = "1";
                else
                    sexe = "0";
                datenaissance = modify_edittext_datenaissance.getText().toString();
                adresse = modify_edittext_adresse.getText().toString();
                contact = modify_edittext_contact.getText().toString();
                String m1 = modify_edittext_password_register1.getText().toString();
                String m2 = modify_edittext_password_register2.getText().toString();

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

        return v;
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class NetCheckAndRegister extends AsyncTask<String, Void, Void> {
        private ProgressDialog nDialog;
        private boolean test = false;

        @Override
        protected Void doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            nDialog = new ProgressDialog(getActivity());
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
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(getActivity());
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
    }
    private class NetCheckAndGetMaladie extends AsyncTask<String, Void, Void> {
        private ProgressDialog nDialog;
        private boolean test = false;

        @Override
        protected Void doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200 || urlc.getResponseCode() == 302) {
                        test = true;
                        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                        mvm.add("iduser", String.valueOf(currentUser.getId_patient()));

                        final String wsUrl = getResources().getString(R.string.ws_listemaladiebyid_url);
                        RestTemplate restTemplate = new RestTemplate();
                        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                        messageConverters.add(new FormHttpMessageConverter());
                        messageConverters.add(new StringHttpMessageConverter());
                        messageConverters.add(new MappingJackson2HttpMessageConverter());
                        restTemplate.setMessageConverters(messageConverters);
                        MaladieUser[] lm = restTemplate.postForObject(wsUrl, mvm, MaladieUser[].class);
                        for(int i = 0;i<lm.length;i++){
                            if(i>0){
                                maladieUser += ","+lm[i].getTitre();
                            }
                            else{
                                maladieUser = lm[i].getTitre();
                            }
                        }
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
                maladies.setText(maladieUser);
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
            nDialog = new ProgressDialog(getActivity());
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
