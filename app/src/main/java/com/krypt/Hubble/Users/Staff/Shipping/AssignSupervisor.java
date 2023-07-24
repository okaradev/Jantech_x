package com.krypt.Hubble.Users.Staff.Shipping;

import static com.krypt.Hubble.utils.Urls.URL_ASSIGN_SUPER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class AssignSupervisor extends AppCompatActivity {

    private Button btn_assgin;
    private TextView txv_bookingID, txv_driverName;
    private ProgressBar progressBar;
    private String driverID, bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_drivers);

        progressBar = findViewById(R.id.progressBar);
        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_driverName = findViewById(R.id.txv_driverName);
        btn_assgin = findViewById(R.id.btn_submit);

        progressBar.setVisibility(View.GONE);

        Intent in = getIntent();
        bookingID = in.getStringExtra("bookingID");
        driverID = in.getStringExtra("driverID");

        txv_driverName.setText(in.getStringExtra("driverName"));
        txv_bookingID.setText("Payment ID: " + in.getStringExtra("bookingID"));

        btn_assgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignBooking();

            }
        });
    }

    private void assignBooking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ASSIGN_SUPER,
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
                                btn_assgin.setVisibility(View.VISIBLE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btn_assgin.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_assgin.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bookingID", bookingID);
                params.put("driverID", driverID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}