package com.krypt.Hubble.Users.Staff.Driver;

import static com.krypt.Hubble.utils.Urls.URL_ASSIGNED_ITEMS;
import static com.krypt.Hubble.utils.Urls.URL_ISSUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Driver.Adaptor.AdptAssignItems;
import com.krypt.Hubble.Users.Staff.Driver.Model.AssignedItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssgnDetails extends AppCompatActivity {

    private TextView txv_bookingID, txv_name, txv_county,
            txv_dateToDeliver, txv_location;

    private ProgressBar progressBar,
            progressBarSubmit;
    private String bookingID;
    private Button btn_submit;
    private AdptAssignItems adapterItems;
    private List<AssignedItems> list;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assgn_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_name = findViewById(R.id.txv_clientName);
        txv_county = findViewById(R.id.txv_county);
        txv_location = findViewById(R.id.txv_location);
        txv_dateToDeliver = findViewById(R.id.txv_dateToDeliver);
        btn_submit = findViewById(R.id.btn_submit);
        recyclerView = findViewById(R.id.recyclerView);
        btn_submit.setText("Completed");

        progressBarSubmit = findViewById(R.id.progressBarSubmit);
        progressBar = findViewById(R.id.progressBar);

        progressBarSubmit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);

        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");
        txv_bookingID.setText("Service ID " + in.getStringExtra("bookingID"));
        txv_name.setText(in.getStringExtra("clientName"));
        txv_county.setText("County " + in.getStringExtra("county"));
        txv_location.setText(in.getStringExtra("location"));
        txv_dateToDeliver.setText("Date to deliver " + in.getStringExtra("dateToDeliver"));

        if (in.getStringExtra("bookingStatus").equals("Shipping")) {
            btn_submit.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                issue();
            }
        });
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        getItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getItems() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ASSIGNED_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("RESPONSE ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);

                                    String itemName = jsn.getString("itemsName");
                                    String quantity = jsn.getString("quantity");

                                    AssignedItems assignedItems = new AssignedItems(itemName, quantity);
                                    list.add(assignedItems);

                                }

                                adapterItems = new AdptAssignItems(getApplicationContext(), list);
                                recyclerView.setAdapter(adapterItems);
                                progressBar.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {
                            e.toString();
                            e.printStackTrace();
                            Log.e("E", " " + e);

                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Log.e("ERROR", " " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                Log.e("PARAMS", " " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void issue() {
        btn_submit.setVisibility(View.GONE);
        progressBarSubmit.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ISSUE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                btn_submit.setVisibility(View.VISIBLE);
                                progressBarSubmit.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.toString();
                            Log.e("E", " " + e);

                            btn_submit.setVisibility(View.VISIBLE);
                            progressBarSubmit.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                btn_submit.setVisibility(View.VISIBLE);
                progressBarSubmit.setVisibility(View.GONE);

                Log.e("E", " " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}