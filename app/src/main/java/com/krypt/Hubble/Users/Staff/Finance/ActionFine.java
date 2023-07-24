package com.krypt.Hubble.Users.Staff.Finance;

import static com.krypt.Hubble.utils.Urls.URL_APPROVE_FINE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActionFine extends AppCompatActivity {
    private TextView txv_bookingID, txv_fine, txv_mpesaCode,
            txv_status, txv_remarks, txv_receipt;
    private ProgressBar progressBar;
    private Button btn_submit;
    private String penaltyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_fine);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_fine = findViewById(R.id.txv_fine);
        txv_status = findViewById(R.id.txv_fineStatus);
        txv_remarks = findViewById(R.id.txv_remarks);
        txv_mpesaCode = findViewById(R.id.txv_mpesaCode);
        txv_receipt = findViewById(R.id.txv_receipt);
        progressBar = findViewById(R.id.progressBar);
        btn_submit = findViewById(R.id.btn_submit);

        progressBar.setVisibility(View.GONE);

        Intent in = getIntent();
        penaltyID = in.getStringExtra("penaltyID");
        txv_bookingID.setText("Booking ID " + in.getStringExtra("bookingID"));
        txv_fine.setText("KES " + in.getStringExtra("amount"));
        txv_status.setText("Status " + in.getStringExtra("fineStatus"));
        txv_remarks.setText("Remarks " + in.getStringExtra("remarks"));
        txv_mpesaCode.setText("Mpesa code " + in.getStringExtra("mpesaCode"));

        btn_submit.setOnClickListener(v -> submit());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {


        btn_submit.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_APPROVE_FINE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "is" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();
                                btn_submit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                btn_submit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.printStackTrace();
             //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                btn_submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("penaltyID", penaltyID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}