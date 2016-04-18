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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.uidmadapp.urgenceidentification.handler.DatabaseHandler;
import com.uidmadapp.urgenceidentification.model.User;

import org.json.JSONObject;
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

public class LoginActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton fbLoginButton;
    private Button signInButton;
    private Button registerLinkButton;
    private CoordinatorLayout coordinatorLayout;

    //form value
    private EditText edittext_email;
    private EditText edittext_password;
    private String email;
    private String password;
    private String fb_id;
    private String fb_nom;
    private String fb_email;
    private String fb_gender;
    //end form value
    private CallbackManager callbackManager;
    private DatabaseHandler db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.loginLayout);

        db = new DatabaseHandler(this);
        // test if user is connected
        List<User> users = db.getAllUser();
        if (users.size() > 0) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        callbackManager = CallbackManager.Factory.create();
        info = (TextView) findViewById(R.id.info);
        edittext_email = (EditText) findViewById(R.id.edittext_email);
        edittext_password = (EditText) findViewById(R.id.edittext_password);
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions("public_profile", "email", "user_friends");
        signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edittext_email.getText().toString();
                password = edittext_password.getText().toString();
                new NetCheckAndLogin().execute();
            }
        });
        registerLinkButton = (Button) findViewById(R.id.register_link_button);
        registerLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbLoginButton.setVisibility(View.INVISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    fb_id = object.getString("id");
                                    fb_nom = object.getString("name");
                                    fb_email = object.getString("email");
                                    String gender = object.getString("gender");
                                    if (gender.equals("male")) {
                                        fb_gender = "1";
                                    } else {
                                        fb_gender = "0";
                                    }
                                    new HttpRequestLoginFBTask().execute();
                                    //User user = new User();
                                    /*user.setId_patient(object.getInt("id_patient"));
                                    user.setEmail(object.getString("email").toString());
                                    user.setNom(object.getString("nom").toString());
                                    user.setSexe(object.getInt("sexe"));*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_wrong_fblogin), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class HttpRequestLoginTask extends AsyncTask<Void, Void, User> {
        private ProgressDialog nDialog;

        @Override
        protected User doInBackground(Void... params) {
            try {
                MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                mvm.add("email", email);
                mvm.add("password", password);

                final String url = getResources().getString(R.string.ws_login_url);
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
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_wrong_infologin), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                return null;
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            nDialog.dismiss();
            if (user != null) {
                db.addUser(user);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
    }

    private class HttpRequestLoginFBTask extends AsyncTask<Void, Void, User> {
        private ProgressDialog nDialog;

        @Override
        protected User doInBackground(Void... params) {
            try {
                MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                mvm.add("fb_id", fb_id);
                mvm.add("nom", fb_nom);
                mvm.add("email", fb_email);
                mvm.add("sexe", fb_gender);

                final String url = getResources().getString(R.string.ws_loginfb_url);
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
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.msg_wrong_fblogin), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.msg_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                return null;
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            nDialog.dismiss();
            if (user != null) {
                db.addUser(user);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setMessage(getResources().getString(R.string.msg_loading));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.show();
        }
    }

    private class NetCheckAndLogin extends AsyncTask<String, Void, Void> {
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
                    //System.out.println("RESPONSECODE : "+urlc.getResponseCode());
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
                new HttpRequestLoginTask().execute();
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
            nDialog = new ProgressDialog(LoginActivity.this);
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
