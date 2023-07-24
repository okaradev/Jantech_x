package com.krypt.Hubble.Users.Staff;

import static com.krypt.Hubble.utils.Urls.URL_ACCEPT_BOOKING;
import static com.krypt.Hubble.utils.Urls.URL_BOOKING_ITEMS;
import static com.krypt.Hubble.utils.Urls.URL_REJECT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.krypt.Hubble.Users.Staff.Shipping.Supers;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsSup extends AppCompatActivity {
    private TextView txv_bookingID, txv_cash, txv_bookingDate,
            txv_mpesaCode, txv_bookingStatus, txv_dateToDeliver;
    private ProgressBar progressBar, progressBarSubmit;
    private RecyclerView recyclerView;
    private Button btn_approve, btn_reject, btn_assign;
    private String bookingID;
    private List<PaymentItemsModal> list;
    private AdapterItems adapterItems;
    private EditText edt_remark;
    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("More details");
        items();

        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_cash = findViewById(R.id.txv_cash);
        txv_bookingDate = findViewById(R.id.txv_bookingDate);
        txv_mpesaCode = findViewById(R.id.txv_mpesaCode);
        txv_bookingStatus = findViewById(R.id.txv_bookingStatus);
        txv_dateToDeliver = findViewById(R.id.txv_dateToDeliver);
        progressBar = findViewById(R.id.progressBar);
        progressBarSubmit = findViewById(R.id.progressBarSubmit);
        btn_approve = findViewById(R.id.btn_approve);
        btn_reject = findViewById(R.id.btn_reject);
        recyclerView = findViewById(R.id.recyclerView);
        edt_remark = findViewById(R.id.edt_remarks);
        btn_assign = findViewById(R.id.btn_assign);

        session = new SessionHandler(this);
        user = session.getUserDetails();

        list = new ArrayList<>();

        progressBarSubmit.setVisibility(View.GONE);


        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");
        txv_bookingID.setText("Order No " + in.getStringExtra("bookingID"));
        txv_cash.setText("Cash KES " + in.getStringExtra("bookingCost"));
        txv_mpesaCode.setText("Mpesa Code " + in.getStringExtra("mpesaCode"));
        txv_dateToDeliver.setText("Date to deliver " + in.getStringExtra("dateToDeliver"));
        txv_bookingDate.setText("Ordering Date " + in.getStringExtra("bookingDate"));
        txv_bookingStatus.setText("Status " + in.getStringExtra("bookingStatus"));

        btn_reject.setVisibility(View.GONE);
        edt_remark.setVisibility(View.GONE);
        btn_approve.setVisibility(View.GONE);
        btn_assign.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);


        if (in.getStringExtra("bookingStatus").equals("Pending approval")) {
            btn_reject.setVisibility(View.VISIBLE);
            edt_remark.setVisibility(View.VISIBLE);
            btn_approve.setVisibility(View.VISIBLE);
        }
        if (in.getStringExtra("bookingStatus").equals("Pending delivery")
                & user.getUser_type().equals("Service manager")) {
            btn_assign.setVisibility(View.VISIBLE);
        }


        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertApprove();
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject();
            }
        });


        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Supers.class);
                in.putExtra("bookingID", bookingID);
                startActivity(in);

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

    private void items() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BOOKING_ITEMS,
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
                                    String price = jsn.getString("price");

                                    String subTotal = jsn.getString("subTotal");
                                    String capacity = jsn.getString("capacity");

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


    public void approveBooking() {

        progressBarSubmit.setVisibility(View.VISIBLE);
        btn_approve.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ACCEPT_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 500);
                                toast.show();
                                finish();
                            } else {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                            progressBarSubmit.setVisibility(View.GONE);
                            btn_approve.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
                progressBarSubmit.setVisibility(View.GONE);
                btn_approve.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                Log.e("PARAMS", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void reject() {
        progressBarSubmit.setVisibility(View.VISIBLE);
        btn_approve.setVisibility(View.GONE);

        final String remarks = edt_remark.getText().toString().trim();
        if (TextUtils.isEmpty(remarks)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please write a remark why booking is rejected", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 500);
            toast.show();
            progressBarSubmit.setVisibility(View.GONE);
            btn_approve.setVisibility(View.VISIBLE);

            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                finish();
                            } else {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                progressBarSubmit.setVisibility(View.GONE);
                                btn_approve.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                            progressBarSubmit.setVisibility(View.GONE);
                            btn_approve.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                params.put("remarks", remarks);
                Log.e("PARAMS", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertApprove() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Approve");
        alertDialog.setMessage("Approve service now?");
        alertDialog.setCancelable(false);
        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approveBooking();
                return;
            }
        });
        alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                return;
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRestart() {

        super.onRestart();
        finish();  //  close activity on return

    }

}