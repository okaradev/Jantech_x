package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_GET_SERVICES;

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
import com.krypt.Hubble.HUBBLEMODELS.ProductModal;
import com.krypt.Hubble.HUBBLE_ADPTS.AdapterServices;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Services extends AppCompatActivity {

    private SessionHandler session;
   UserModel user;
    private List<ProductModal> list;
    private AdapterServices adapterProducts;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Services");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_services);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
        list = new ArrayList<>();

        getServices();
    }

    public void getServices() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String productID = jsn.getString("productID");
                                    String productName = jsn.getString("productName");
                                    String price = jsn.getString("price");
                                    String stock = jsn.getString("stock");
                                    String image = jsn.getString("image");
                                    String desc = jsn.getString("desc");

                                    ProductModal productModal = new ProductModal(productID, productName, stock, price, image, desc);
                                    list.add(productModal);
                                }
                                progressBar.setVisibility(View.GONE);
                                adapterProducts = new AdapterServices(Services.this, list);
                                recyclerView.setAdapter(adapterProducts);
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
                Toast.makeText(Services.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            session = new SessionHandler(getApplicationContext());
            user = session.getUserDetails();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
finish();
    }
}