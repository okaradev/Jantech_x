package com.krypt.Hubble.Users.Staff.Shipping;

import static com.krypt.Hubble.utils.Urls.URL_APPROVED_BOOKINGS;
import static com.krypt.Hubble.utils.Urls.URL_PENDING_DELIVERY;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Adaptor.AdptBookings;
import com.krypt.Hubble.Users.Staff.Modal.BookingModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PendingShippingTechnician extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<BookingModal> list;
    private AdptBookings adptBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_shipping);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Pending delivery");

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        bookings();
        //if (isNetworkAvailable()) {
        //    bookings();
//        } else {
//            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.GONE);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void bookings() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PENDING_DELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("RESPONSE ", " " + response);
                            String status = jsonObject.getString("status");

                            String msg = jsonObject.getString("message");
                            Toast.makeText(PendingShippingTechnician.this, msg, Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Volley Error", error.toString());
                // Show a Toast or dialog to inform the user about the error
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorMessage = new String(error.networkResponse.data);
                    Toast.makeText(getApplicationContext(), "Volley Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Volley Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        // Set the retry policy for the request
        int socketTimeout = 30000; // 30 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void loadProgrammes() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_APPROVED_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject jsonObject = array.getJSONObject(i);

                                //adding the product to product list
                                list.add(new BookingModal(
                                        jsonObject.getString("bookingID"),

                                jsonObject.getString("name"),
                                jsonObject.getString("bookingCost"),
                                jsonObject.getString("mpesaCode"),
                                jsonObject.getString("bookingDate"),
                                jsonObject.getString("bookingStatus"),

                                jsonObject.getString("deliveryCost"),
                                jsonObject.getString("dateToDeliver"),
                                jsonObject.getString("county"),
                                jsonObject.getString("town"),


                                jsonObject.getString("bookingRemarks"),
                                        jsonObject.getString("status"),


                                        jsonObject.getString("message")

                                ));
                            }
                            adptBookings = new AdptBookings(PendingShippingTechnician.this, list);
                            //creating adapter object and setting it to recyclerview
                           // adptBookings adapter = new incomingAdapter(getActivity(), productList);
                            recyclerView.setAdapter(adptBookings);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(PendingShippingTechnician.this).add(stringRequest);
    }

//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
//        startActivity(getIntent());
    }
}
