package com.krypt.Hubble.Users.Staff.Driver;

import static com.krypt.Hubble.utils.Urls.MARK_RETURN_BACK;
import static com.krypt.Hubble.utils.Urls.URL_ASSIGNED_ITEMS;

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

public class ReturningItems extends AppCompatActivity {
    private TextView txv_bookingID, txv_name, txv_county;
    private ProgressBar progressBar;
    private Button btn_submit;
    private AdptAssignItems adapterItems;
    private List<AssignedItems> list;
    private String bookingID;
    private RecyclerView recyclerView;
    private ProgressBar progressBarSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returning_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Items");

        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_name = findViewById(R.id.txv_clientName);
        txv_county = findViewById(R.id.txv_county);

        btn_submit = findViewById(R.id.btn_submit);
        recyclerView = findViewById(R.id.recyclerView);

        progressBarSubmit = findViewById(R.id.progressBarSubmit);
        progressBar = findViewById(R.id.progressBar);

        progressBarSubmit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);

        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");
        txv_bookingID.setText("Booking ID " + in.getStringExtra("bookingID"));
        txv_name.setText(in.getStringExtra("clientName"));
        txv_county.setText("County " + in.getStringExtra("county"));

        if (in.getStringExtra("returnStatus").equals("Pending return")) {
            btn_submit.setVisibility(View.VISIBLE);
            btn_submit.setText("Returned back");
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack();
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


    private void returnBack() {
        btn_submit.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, MARK_RETURN_BACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();
                                progressBar.setVisibility(View.GONE);

                            } else if (status.equals("0")) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
//                                btn_submit.setVisibility(View.VISIBLE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}