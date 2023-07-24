package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_DELIVERY_DETAILS;
import static com.krypt.Hubble.utils.Urls.URL_GET_COUNTIES;
import static com.krypt.Hubble.utils.Urls.URL_GET_TOWNS;
import static com.krypt.Hubble.utils.Urls.URL_SUBMIT_SHIPPING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnterShipping extends AppCompatActivity {

    private EditText edt_county, edt_town,
            edt_address, edt_location;
    private ProgressBar progressBar, progressBarTown, progressBar1;
    private Button btn_submit;
    private CardView card_form;

    private ArrayList<String> arrayCounties;
    private ArrayList<String> arrayTowns;

    private String countyName;
    private String countyID;
    private String townName;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_shipping);
        getSupportActionBar().setTitle("Shipping details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_submit = findViewById(R.id.btn_submit);
        edt_address = findViewById(R.id.edt_Address);
        edt_county = findViewById(R.id.edt_county);
        edt_town = findViewById(R.id.edt_town);
        edt_location = findViewById(R.id.edt_location);
        progressBar = findViewById(R.id.progressBar);
        progressBarTown = findViewById(R.id.progressBarTown);
        progressBar1 = findViewById(R.id.progressBar1);
        card_form = findViewById(R.id.card_form);

        progressBar.setVisibility(View.GONE);
        progressBarTown.setVisibility(View.GONE);
        card_form.setVisibility(View.GONE);

        session = new SessionHandler(this);
        user = session.getUserDetails();

        edt_county.setFocusable(false);
        edt_town.setFocusable(false);

        arrayCounties = new ArrayList<>();
        arrayTowns = new ArrayList<>();
        edt_county.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarTown.setVisibility(View.VISIBLE);
                edt_town.setVisibility(View.GONE);
                edt_town.setText("");
                countiesAlert(v);
            }
        });
        edt_town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String checkCounty = edt_county.getText().toString().trim();
                if (TextUtils.isEmpty(checkCounty)) {
                    Toast.makeText(getApplicationContext(), "Select county to continue", Toast.LENGTH_SHORT).show();
                } else {
                    townsAlert(v);

                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        counties();
        delivery();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void submit() {
        btn_submit.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final String county = edt_county.getText().toString().trim();
        final String town = edt_town.getText().toString().trim();
        final String address = edt_address.getText().toString().trim();
        final String location = edt_location.getText().toString().trim();


        if (TextUtils.isEmpty(county)) {
            Toast.makeText(getApplicationContext(), "Select county", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (TextUtils.isEmpty(town)) {
            Toast.makeText(getApplicationContext(), "Select town", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(), "Enter address", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getApplicationContext(), "Enter location", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT_SHIPPING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "is" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(getApplicationContext(), CheckOut.class);
                                startActivity(in);
                                finish();
                                btn_submit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                btn_submit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                btn_submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                params.put("countyID", countyID);
                params.put("town", town);
                params.put("address", address);
                params.put("location", location);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void delivery() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELIVERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String county_ID = jsn.getString("county_ID");
                                    String county = jsn.getString("county");
                                    String town = jsn.getString("town");
                                    String ship_address = jsn.getString("ship_address");
                                    String location = jsn.getString("location");

                                    edt_county.setText(county);
                                    edt_town.setText(town);
                                    edt_address.setText(ship_address);
                                    edt_location.setText(location);
                                    countyID = county_ID;

                                    card_form.setVisibility(View.VISIBLE);
                                    progressBar1.setVisibility(View.GONE);


                                }

                            } else if (status.equals("0")) {
//                                String msg=jsonObject.getString("message");
//                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                                card_form.setVisibility(View.VISIBLE);
                                progressBar1.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void countiesAlert(View v) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Your county ");
        final String[] array = arrayCounties.toArray(new String[arrayCounties.size()]);
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_county.setText(array[i]);
                dialogInterface.dismiss();
                countyName = array[i];

                town();


            }
        });
        builder.show();

    }

    public void townsAlert(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(" Towns in  " + countyName);
        final String[] array = arrayTowns.toArray(new String[arrayTowns.size()]);

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_town.setText(array[i]);
                dialogInterface.dismiss();
                townName = array[i];
                builder.setNegativeButton("Close", null);

                town();


            }
        });
        builder.show();

    }

    public void counties() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_COUNTIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response", " " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("counties");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String county = jsn.getString("countyName");
                                    arrayCounties.add(county);

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void town() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TOWNS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("towns");
                                arrayTowns.clear();
                                progressBarTown.setVisibility(View.GONE);
                                edt_town.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String towns = jsn.getString("townName");
                                    String ID = jsn.getString("countyID");
                                    arrayTowns.add(towns);

                                    countyID = ID;
                                }
                            } else {
                                String msg = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("countyName", countyName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}