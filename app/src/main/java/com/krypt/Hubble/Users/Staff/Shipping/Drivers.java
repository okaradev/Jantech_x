package com.krypt.Hubble.Users.Staff.Shipping;

import static com.krypt.Hubble.utils.Urls.URL_DRIVERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.HUBBLEMODELS.DriversModel;
import com.krypt.Hubble.HUBBLE_ADPTS.AdapterDrivers;
import com.krypt.Hubble.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Drivers extends AppCompatActivity {

    String bookingID;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<DriversModel> list;
    private AdapterDrivers adapterDrivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        getSupportActionBar().setSubtitle("Drivers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");

        drivers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void drivers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DRIVERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("RESPONSE ", " " + response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);

                                    String driverName = jsn.getString("driverName");
                                    String driverID = jsn.getString("driverID");

                                    DriversModel b = new DriversModel(driverID, driverName, bookingID);
                                    list.add(b);
                                }
                                adapterDrivers = new AdapterDrivers(getApplicationContext(), list);
                                recyclerView.setAdapter(adapterDrivers);
                                progressBar.setVisibility(View.GONE);
                            } else if (status.equals("0")) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("E ", "" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR ", "" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
//        startActivity(getIntent());
    }
}