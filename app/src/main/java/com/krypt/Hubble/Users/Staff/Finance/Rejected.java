package com.krypt.Hubble.Users.Staff.Finance;

import static com.krypt.Hubble.utils.Urls.URL_REJECTED_BOOKINGS;

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
import com.krypt.Hubble.Users.Staff.Adaptor.AdptBookings;
import com.krypt.Hubble.Users.Staff.Modal.BookingModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rejected extends AppCompatActivity {
    private SessionHandler session;
    private UserModel user;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<BookingModal> list;
    private AdptBookings adptBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("REJECTED");

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REJECTED_BOOKINGS,
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
                                    String bookingID = jsn.getString("bookingID");
                                    String name = jsn.getString("name");
                                    String bookingCost = jsn.getString("bookingCost");
                                    String mpesaCode = jsn.getString("mpesaCode");
                                    String bookingDate = jsn.getString("bookingDate");
                                    String dateToDeliver = jsn.getString("dateToDeliver");
                                    String bookingStatus = jsn.getString("bookingStatus");
                                    String deliveryCost = jsn.getString("deliveryCost");
                                    String county = jsn.getString("county");
                                    String town = jsn.getString("town");
                                    String bookingRemarks = jsn.getString("bookingRemarks");

                                    BookingModal b = new BookingModal(bookingID, name, bookingCost,
                                            mpesaCode, bookingDate, bookingStatus, deliveryCost, dateToDeliver,
                                            county, town, bookingRemarks);
                                    list.add(b);
                                }
                                adptBookings = new AdptBookings(getApplicationContext(), list);
                                recyclerView.setAdapter(adptBookings);
                                progressBar.setVisibility(View.GONE);
                            } else if (status.equals("0")) {
                                progressBar.setVisibility(View.GONE);

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
        });
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