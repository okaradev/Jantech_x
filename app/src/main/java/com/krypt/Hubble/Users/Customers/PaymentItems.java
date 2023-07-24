package com.krypt.Hubble.Users.Customers;

//import static com.krypt.Hubble.Users.Customers.Adaptors.AdapterItems.subtotal;
import static com.krypt.Hubble.utils.Urls.URL_BOOKING_ITEMS;
import static com.krypt.Hubble.utils.Urls.URL_COLLECT;
import static com.krypt.Hubble.utils.Urls.URL_RECEIVED;


import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.krypt.Hubble.Users.Customers.Adaptors.AdapterItems;
import com.krypt.Hubble.Users.Customers.Model.PaymentItemsModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;
import com.krypt.Hubble.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentItems extends AppCompatActivity {

    private SessionHandler session;
    private UserModel user;

    private TextView txv_bookingID, txv_cash, txv_bookingDate,txv_nm,
            txv_mpesaCode, txv_bookingStatus,
            txv_dateToDeliver, txv_receipt,txv_place;
    private ProgressBar progressBar, progressBarSubmit;
    private RecyclerView recyclerView;
    private Button btn_submit;
    private String bookingID;
    private List<PaymentItemsModal> list;
    private AdapterItems adapterItems;
    private ImageView ic_printshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_items);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Details");
        txv_nm= findViewById(R.id.txv_names);
        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_place = findViewById(R.id.txv_placer);
        txv_cash = findViewById(R.id.txv_cash);
        String q="";
        String p="";
        String s="";

        txv_bookingDate = findViewById(R.id.txv_bookingDate);
        txv_mpesaCode = findViewById(R.id.txv_mpesaCode);
        txv_bookingStatus = findViewById(R.id.txv_bookingStatus);
        txv_dateToDeliver = findViewById(R.id.txv_dateToDeliver);
        txv_receipt = findViewById(R.id.txv_receipt);
        progressBar = findViewById(R.id.progressBar);
        progressBarSubmit = findViewById(R.id.progressBarSubmit);
        btn_submit = findViewById(R.id.btn_submit);
        ic_printshop = findViewById(R.id.img_print);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();

        progressBarSubmit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);


        Intent in = getIntent();//""
        bookingID = in.getStringExtra("bookingID");

        txv_nm.setText("Name:" + user.getFirstname()+" "+user.getLastname());
        txv_bookingID.setText("Order No " + in.getStringExtra("bookingID"));
        //txv_cash.setText("Cash KES " + Integer.parseInt(in.getStringExtra("bookingCost")));
        txv_cash.setText("Cash KES " + in.getStringExtra("bookingCost"));
        txv_mpesaCode.setText("Mpesa Code " + in.getStringExtra("mpesaCode"));
        txv_dateToDeliver.setText("Date to deliver " + in.getStringExtra("dateToDeliver"));
        txv_place.setText("Place to deliver " + in.getStringExtra("place"));
        txv_bookingDate.setText("Ordering Date " + in.getStringExtra("bookingDate"));
        txv_bookingStatus.setText("Status " + in.getStringExtra("bookingStatus"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        items();

        if (in.getStringExtra("bookingStatus").equals("Issued")) {
            btn_submit.setVisibility(View.VISIBLE);
        }

     /*   if (in.getStringExtra("bookingStatus").equals("Delivered")) {
            btn_submit.setVisibility(View.VISIBLE);
            btn_submit.setText("Ready for collection");
        }*/

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (in.getStringExtra("bookingStatus").equals("Issued")) {
                    update();
                } else if (in.getStringExtra("bookingStatus").equals("Delivered")) {

                    // collect();
                }

            }
        });

        ic_printshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void print() {
        ic_printshop.setVisibility(View.GONE);
        txv_receipt.setVisibility(View.VISIBLE);
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        PrintHelper photoPrinter = new PrintHelper(this); // Assume that 'this' is your activity
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("print", bitmap);

        ic_printshop.setVisibility(View.VISIBLE);
        txv_receipt.setVisibility(View.GONE);
    }

    private void items() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BOOKING_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("RESPONSEitem", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String quantity = jsn.getString("quantity");
                                    String itemmPrice = jsn.getString("itemPrice");
                                    String itemName = jsn.getString("itemsName");

                                    String price = jsn.getString("price");
                                    String capacity = jsn.getString("capacity");

                                    String subTotal = jsn.getString("subTotal");



                                    PaymentItemsModal paymentItemsModal = new PaymentItemsModal(itemName, price,
                                            quantity, subTotal, capacity);
                                    list.add(paymentItemsModal);

                                }

                                adapterItems = new AdapterItems(getApplicationContext(), list);
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

    private void update() {
        progressBarSubmit.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECEIVED,
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
                                btn_submit.setVisibility(View.GONE);
                                progressBarSubmit.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("E ", " " + e);
                            btn_submit.setVisibility(View.GONE);
                            progressBarSubmit.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("ERROR ", error.toString());
                btn_submit.setVisibility(View.GONE);
                progressBarSubmit.setVisibility(View.VISIBLE);
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

    private void collect() {
        progressBarSubmit.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COLLECT,
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
                                btn_submit.setVisibility(View.GONE);
                                progressBarSubmit.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("E ", " " + e);
                            btn_submit.setVisibility(View.GONE);
                            progressBarSubmit.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("ERROR ", error.toString());
                btn_submit.setVisibility(View.GONE);
                progressBarSubmit.setVisibility(View.VISIBLE);
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
//    @Override
//    public void onRestart()
//    {
//        super.onRestart();
//        finish();
//        startActivity(getIntent());
//    }
}