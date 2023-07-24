package com.krypt.Hubble.Users.Staff.Driver;

import static com.krypt.Hubble.utils.Urls.URL_DELIVERY_HISTORY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Driver.Adaptor.AdptAssignments;
import com.krypt.Hubble.Users.Staff.Driver.Model.AssignModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mydeliveries extends AppCompatActivity {

    private SessionHandler session;
    private UserModel user;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<AssignModal> list;
    private AdptAssignments adptBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydeliveries);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Delivery history");

        session = new SessionHandler(this);
        user = session.getUserDetails();

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        bookings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void bookings() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELIVERY_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("RESPONSE ", " " + response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String bookingID = jsn.getString("bookingID");
                                    String clientName = jsn.getString("clientName");
                                    String dateToDeliver = jsn.getString("dateToDeliver");
                                    String bookingStatus = jsn.getString("bookingStatus");
                                    String county = jsn.getString("county");
                                    String location = jsn.getString("location");
                                    String town = jsn.getString("town");

                                    AssignModal b = new AssignModal(bookingID, clientName, dateToDeliver,
                                            location, bookingStatus, county, town);
                                    list.add(b);
                                    Log.e("RESPONSE ", " " + response);
                                }
                                adptBookings = new AdptAssignments(getApplicationContext(), list);
                                recyclerView.setAdapter(adptBookings);
                                progressBar.setVisibility(View.GONE);
                            } else if (status.equals("0")) {
                                progressBar.setVisibility(View.GONE);
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
            @Override
            protected Map<String, String> getParams() throws AbstractMethodError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", user.getUserID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


}