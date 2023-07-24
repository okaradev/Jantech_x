package com.krypt.Hubble.Users.Staff.Supervisor;

import static com.krypt.Hubble.utils.Urls.ASSIGN_RETURN_DRIVERS;
import static com.krypt.Hubble.utils.Urls.RETURN_FINE;
import static com.krypt.Hubble.utils.Urls.URL_ASSIGNED_ITEMS;
import static com.krypt.Hubble.utils.Urls.URL_RETURN_DRIVERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ReturnItems extends AppCompatActivity {

    private TextView txv_bookingID, txv_name, txv_county;
    private ProgressBar progressBar;
    private Button btn_submit;
    private AdptAssignItems adapterItems;
    private List<AssignedItems> list;
    private String bookingID;
    private RecyclerView recyclerView;
    private ProgressBar progressBarSubmit;

    private View prompt;
    private android.app.AlertDialog dialog;
    private EditText edt_username, edt_fine, edt_remarks;
    private ProgressBar progressBarPopup;
    private Button btn_fine, btn_no_fine;
    private LinearLayout layout_fine;
    private RadioGroup radioGroup;
    private RadioButton rb_no_fine, rb_fine;
    private String driver;
    private ArrayList<String> drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_items);

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

        drivers = new ArrayList<>();

        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");
        txv_bookingID.setText("Payment ID " + in.getStringExtra("bookingID"));
        txv_name.setText(in.getStringExtra("clientName"));
        txv_county.setText("County " + in.getStringExtra("county"));

        if (in.getStringExtra("bookingStatus").equals("Pending return")) {
            btn_submit.setVisibility(View.VISIBLE);
            btn_submit.setText("Return");
        }

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrompts();
            }
        });


        getItems();
        getDrivers();
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

    public void getPrompts() {
        android.app.AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);
        alertDialog.setNegativeButton("Close", null);
        prompt = getLayoutInflater().inflate(R.layout.prompt_return, null);
        edt_username = prompt.findViewById(R.id.edt_username);
        edt_fine = prompt.findViewById(R.id.edt_fine);
        edt_remarks = prompt.findViewById(R.id.edt_remarks);
        progressBarPopup = prompt.findViewById(R.id.progressBarPopup);
        btn_fine = prompt.findViewById(R.id.btn_fine);
        btn_no_fine = prompt.findViewById(R.id.btn_no_fine);
        layout_fine = prompt.findViewById(R.id.layout_fine);
        rb_fine = prompt.findViewById(R.id.rb_fine);
        rb_no_fine = prompt.findViewById(R.id.rb_no_fine);
        radioGroup = prompt.findViewById(R.id.rbtn_group);
        layout_fine.setVisibility(View.GONE);
        progressBarPopup.setVisibility(View.GONE);

        edt_username.setFocusable(false);
        radioGroup.clearCheck();

//        radioGroup.setVisibility(View.GONE);


        rb_no_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_fine.setVisibility(View.GONE);
                btn_no_fine.setVisibility(View.VISIBLE);
            }
        });

        rb_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_fine.setVisibility(View.VISIBLE);
                btn_no_fine.setVisibility(View.GONE);
            }
        });

        edt_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertDrivers(v);
            }
        });

        alertDialog.setView(prompt);
        dialog = alertDialog.create();
        dialog.show();


        btn_no_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                returnNoFine();
            }
        });

        btn_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                returnWithFine();
            }
        });
    }

    public void getDrivers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RETURN_DRIVERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String username = jsn.getString("username");
                                    drivers.add(username);
                                }
//                                btn_submit.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getAlertDrivers(View v) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Choose a driver ");
        final String[] array = drivers.toArray(new String[drivers.size()]);
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_username.setText(array[i]);
                dialogInterface.dismiss();

            }
        });
        builder.show();

    }

    private void returnNoFine() {
        btn_no_fine.setVisibility(View.GONE);
        progressBarPopup.setVisibility(View.VISIBLE);

        final String username = edt_username.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            btn_no_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Please select a return driver", Toast.LENGTH_SHORT).show();

            return;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            btn_no_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Please select fine or no fine", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ASSIGN_RETURN_DRIVERS,
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
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    private void returnWithFine() {
        btn_fine.setVisibility(View.GONE);
        progressBarPopup.setVisibility(View.VISIBLE);

        final String username = edt_username.getText().toString().trim();
        final String fine = edt_fine.getText().toString().trim();
        final String remarks = edt_remarks.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            btn_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Please select a return driver", Toast.LENGTH_SHORT).show();

            return;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            btn_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Please select fine or no fine", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(fine)) {
            btn_no_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Please enter a fine", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(remarks)) {
            btn_no_fine.setVisibility(View.VISIBLE);
            progressBarPopup.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Please write a fin remark", Toast.LENGTH_SHORT).show();

            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RETURN_FINE,
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
                params.put("username", username);
                params.put("fine", fine);
                params.put("remarks", remarks);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}