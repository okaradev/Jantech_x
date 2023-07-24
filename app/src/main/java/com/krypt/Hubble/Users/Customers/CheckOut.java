package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_DELIVERY_DETAILS;
import static com.krypt.Hubble.utils.Urls.URL_GET_CHECKOUT_TOTAL;
import static com.krypt.Hubble.utils.Urls.URL_MAKE_PAYMENT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.MainActivity;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOut extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    private SessionHandler session;
    private UserModel user;
    private LinearLayout layout_checkout;
    private RelativeLayout layout_bottom;
    private TextView txv_name, txv_phoneNo, txv_email;
    private TextView txv_order_cost, txv_shipping_cost, txv_totalCost;
    private EditText edt_county, edt_town, edt_address,
            edt_mpesaCode, edt_location, edt_cost, edt_deliveryDate;
    private ProgressBar progressBar, progressCheckout;
    private Button btn_checkout, btn_enter;
    private RadioGroup radioGroup;
    private RadioButton rb_no_shipping, rb_shipping;
    private ArrayList<String> arrayCounties;
    private ArrayList<String> arrayTowns;
    private String countyName;
    private String countyID;
    private String townName;
    private String o_cost;
    private String s_cost;
    private String shippingCost;
    private int cost;
    private int totalCost;
    private ImageView img_edit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);


        getSupportActionBar().setTitle("Checkout");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_checkout = findViewById(R.id.layout_checkout);
        layout_bottom = findViewById(R.id.layout_bottom);
        progressBar = findViewById(R.id.progressBar);
        progressCheckout = findViewById(R.id.progressCheckout);
        btn_checkout = findViewById(R.id.btn_order);
        btn_enter = findViewById(R.id.btn_enter);
        txv_email = findViewById(R.id.txv_email);
        txv_name = findViewById(R.id.txv_name);
        txv_phoneNo = findViewById(R.id.txv_phoneNo);
        txv_order_cost = findViewById(R.id.txv_order_cost);
        edt_mpesaCode = findViewById(R.id.edt_mpesaCode);
        edt_deliveryDate = findViewById(R.id.edt_deliveryDate);
        txv_shipping_cost = findViewById(R.id.txv_shiping_cost);
        txv_totalCost = findViewById(R.id.txv_total_cost);
        edt_county = findViewById(R.id.edt_county);
        edt_town = findViewById(R.id.edt_town);
        edt_address = findViewById(R.id.edt_Address);
        edt_town = findViewById(R.id.edt_town);
        edt_cost = findViewById(R.id.edt_cost);
        edt_location = findViewById(R.id.edt_location);
        radioGroup = findViewById(R.id.rbtn_group);
        rb_no_shipping = findViewById(R.id.rb_no_shipping);
        rb_shipping = findViewById(R.id.rb_shipping);
        img_edit = findViewById(R.id.img_edit);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        layout_bottom.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        progressCheckout.setVisibility(View.VISIBLE);
        layout_checkout.setVisibility(View.GONE);
        btn_enter.setVisibility(View.GONE);

        rb_no_shipping.setVisibility(View.GONE);
        rb_shipping.setVisibility(View.GONE);
        txv_totalCost.setVisibility(View.GONE);

        txv_name.setText("Name " + user.getFirstname() + " " + user.getLastname());
        txv_email.setText("Email " + user.getEmail());
        txv_phoneNo.setText("Phone No " + user.getPhoneNo());

        edt_county.setFocusable(false);
        edt_town.setFocusable(false);
        edt_cost.setFocusable(false);
        edt_address.setFocusable(false);
        edt_location.setFocusable(false);
        edt_deliveryDate.setFocusable(false);

        arrayCounties = new ArrayList<>();
        arrayTowns = new ArrayList<>();

        edt_mpesaCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        radioGroup.clearCheck();

//        btn_checkout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               alertOrderNow(v);
//
//            }


   //     });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EnterShipping.class);
                startActivity(in);
            }
        });
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EnterShipping.class);
                startActivity(in);
            }
        });
        edt_deliveryDate.setOnClickListener(v -> getDate(v));



//        rb_no_shipping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                cost =Integer.parseInt(o_cost);   // order cost
//                txv_totalCost.setText("Total cost Ksh "+ cost);
//                txv_shipping_cost.setText("Shipping Cost Ksh "+0);
//
//
//            }
//        });
//        rb_shipping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                cost =Integer.parseInt(s_cost)+Integer.parseInt(o_cost); // get order total cost + shipping
//                txv_totalCost.setText("Total cost Ksh "+ cost);
//                txv_shipping_cost.setText("Shipping Cost Ksh "+s_cost);
//
//            }
//        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

        delivery();
        payment();
    }

    public void getDate(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edt_deliveryDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void delivery() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELIVERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String county_ID = jsn.getString("county_ID");
                                    String county = jsn.getString("county");
                                    String town = jsn.getString("town");
                                    String ship_address = jsn.getString("ship_address");
                                    String location = jsn.getString("location");

                                    edt_county.setText(county);
                                    edt_town.setText(town);
                                    edt_address.setText(ship_address);
                                    edt_location.setText(location);
                                    countyID = county_ID;

                                    btn_enter.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    progressCheckout.setVisibility(View.GONE);
                                    layout_checkout.setVisibility(View.VISIBLE);

                                }

                            } else if (status.equals("0")) {
                                String msg = jsonObject.getString("message");

                                Intent in = new Intent(getApplicationContext(), EnterShipping.class);
                                startActivity(in);
                                btn_enter.setVisibility(View.VISIBLE);
                                progressCheckout.setVisibility(View.GONE);
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

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void payment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CHECKOUT_TOTAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("cResponse", "d " + response);
                            Log.e("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                String orderCost = jsonObject.getString("orderCost");
                                String deliveryCost = jsonObject.getString("deliveryCost");
                                String totalCost = jsonObject.getString("totalCost");
                                txv_order_cost.setText("Items cost: " + orderCost);
                                txv_shipping_cost.setText("Delivery cost Ksh: " + deliveryCost);
                                edt_cost.setText(totalCost);

                                shippingCost = deliveryCost;
                                o_cost = orderCost;
                                s_cost = deliveryCost;
                                layout_bottom.setVisibility(View.VISIBLE);
                                layout_checkout.setVisibility(View.VISIBLE);
                                progressCheckout.setVisibility(View.GONE);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                Log.e("PARAMSc", " " + params);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void submitPayment() {

        progressBar.setVisibility(View.VISIBLE);
        btn_checkout.setVisibility(View.GONE);
        final String county = edt_county.getText().toString().trim();
        final String mpesaCode = edt_mpesaCode.getText().toString().trim();
        final String address = edt_address.getText().toString().trim();
        final String townName = edt_town.getText().toString().trim();
        final String location = edt_location.getText().toString().trim();
        final String cost = edt_cost.getText().toString().trim();
        final String deliveryDate = edt_deliveryDate.getText().toString().trim();

        if (TextUtils.isEmpty(county)) {
            Toast.makeText(getApplicationContext(), "Select delivery county", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(townName)) {
            Toast.makeText(getApplicationContext(), "Select delivery town", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(), "Select address address", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.VISIBLE);
            return;
        }
//        if (radioGroup.getCheckedRadioButtonId() == -1) {
//            // no radio buttons are checked
//            progressBar.setVisibility(View.GONE);
//            btn_checkout.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(),"Please select a shipping method",Toast.LENGTH_SHORT).show();
//            return;
//        } else if(rb_shipping.isChecked()) {
//            totalCost=cost;
//
//            shippingCost=s_cost;
//
//
//        }else if(rb_no_shipping.isChecked()){
//            totalCost=cost;
//            shippingCost="0";
//        }

        if (TextUtils.isEmpty(mpesaCode)) {
            Toast.makeText(getApplicationContext(), "Please enter Mpesa code", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.VISIBLE);
            return;
        }
        if (mpesaCode.length() > 10 || mpesaCode.length() < 10) {
            Toast.makeText(getApplicationContext(), "Mpesa code  should contain 10 characters", Toast.LENGTH_SHORT).show();
            btn_checkout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!mpesaCode.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            Toast.makeText(getApplicationContext(), "Mpesa code must contain letters and digits",
                    Toast.LENGTH_LONG).show();
            btn_checkout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAKE_PAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Order placed", Toast.LENGTH_SHORT).show();
                            } else if (status.equals("0")) {
                                Toast.makeText(getApplicationContext(), "Order not placed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                btn_checkout.setVisibility(View.VISIBLE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("e", e.toString());
                            btn_checkout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Error 2 ", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_checkout.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                params.put("countyID", countyID);
                params.put("townName", townName);
                params.put("address", address);
                params.put("bookingCost", o_cost);
                params.put("shippingCost", shippingCost);
                params.put("mpesaCode", mpesaCode);
                params.put("totalCost", cost);
                params.put("location", location);
                params.put("deliveryDate", deliveryDate);
                Log.e("values", "" + params);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}