package com.krypt.Hubble.Users.Staff.Store_man;

import static com.krypt.Hubble.utils.Urls.RETURNS_HISTORY;

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
import com.krypt.Hubble.Users.Staff.Store_man.Adaptor.AdptStaffReturns;
import com.krypt.Hubble.Users.Staff.Store_man.Model.StaffReturnModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmedReturns extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<StaffReturnModel> list;
    private AdptStaffReturns adptStaffReturns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_returns);

        getSupportActionBar().setSubtitle("Return history");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);


        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        returnedItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnedItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RETURNS_HISTORY,
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
                                    String staffName = jsn.getString("staffName");
                                    String returnStatus = jsn.getString("returnStatus");
                                    String phoneNo = jsn.getString("phoneNo");

                                    StaffReturnModel b = new StaffReturnModel(staffName, bookingID,
                                            returnStatus, phoneNo);
                                    list.add(b);
                                }
                                adptStaffReturns = new AdptStaffReturns(getApplicationContext(), list);
                                recyclerView.setAdapter(adptStaffReturns);
                                progressBar.setVisibility(View.GONE);
                            } else if (status.equals("0")) {
                                String msg = jsonObject.getString("message");
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