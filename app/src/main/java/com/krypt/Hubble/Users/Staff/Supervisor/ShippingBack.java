package com.krypt.Hubble.Users.Staff.Supervisor;

import static com.krypt.Hubble.utils.Urls.URL_SHIPPING_BACK;

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
import com.krypt.Hubble.Users.Staff.Supervisor.Adaptor.AdptReturn;
import com.krypt.Hubble.Users.Staff.Supervisor.Model.ReturnModal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShippingBack extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ReturnModal> list;
    private AdptReturn adptReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_back);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Shipping back");

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHIPPING_BACK,
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
                                    String bookingStatus = jsn.getString("bookingStatus");
                                    String county = jsn.getString("county");
                                    String town = jsn.getString("town");

                                    ReturnModal b = new ReturnModal(bookingID, name, bookingStatus,
                                            county, town);
                                    list.add(b);
                                }
                                adptReturn = new AdptReturn(getApplicationContext(), list);
                                recyclerView.setAdapter(adptReturn);
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
//        startActivity(getIntent());
    }

}