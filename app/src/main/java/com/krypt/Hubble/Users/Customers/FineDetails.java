package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_PAY_FINE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FineDetails extends AppCompatActivity {

    private TextView txv_bookingID, txv_fine, txv_mpesaCode,
            txv_status, txv_remarks, txv_receipt;
    private EditText edt_mpesaCode, edt_amount;
    private ProgressBar progressBar;
    private Button btn_submit;
    private ImageView img_print;
    private CardView card_payment;

    private String penaltyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txv_bookingID = findViewById(R.id.txv_bookingID);
        txv_fine = findViewById(R.id.txv_fine);
        txv_status = findViewById(R.id.txv_fineStatus);
        txv_remarks = findViewById(R.id.txv_remarks);
        txv_mpesaCode = findViewById(R.id.txv_mpesaCode);
        txv_receipt = findViewById(R.id.txv_receipt);
        edt_amount = findViewById(R.id.edt_amount);
        edt_mpesaCode = findViewById(R.id.edt_mpesaCode);
        progressBar = findViewById(R.id.progressBar);
        btn_submit = findViewById(R.id.btn_submit);
        img_print = findViewById(R.id.img_print);
        card_payment = findViewById(R.id.card_payment);

        progressBar.setVisibility(View.GONE);

        Intent in = getIntent();
        penaltyID = in.getStringExtra("penaltyID");
        txv_bookingID.setText("Booking ID " + in.getStringExtra("bookingID"));
        txv_fine.setText("KES " + in.getStringExtra("amount"));
        txv_status.setText("Status " + in.getStringExtra("fineStatus"));
        txv_remarks.setText("Remarks " + in.getStringExtra("remarks"));
        txv_mpesaCode.setText("Mpesa code " + in.getStringExtra("mpesaCode"));

        edt_amount.setFocusable(false);
        edt_amount.setText(in.getStringExtra("amount"));
        edt_mpesaCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        if (in.getStringExtra("mpesaCode").equals("NONE")) {
            txv_mpesaCode.setVisibility(View.GONE);
            img_print.setVisibility(View.GONE);
            txv_receipt.setVisibility(View.GONE);
        } else {
            card_payment.setVisibility(View.GONE);
        }

        btn_submit.setOnClickListener(v -> submit());

        img_print.setOnClickListener(v -> print());
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
        final String amount = edt_amount.getText().toString().trim();
        final String mpesaCode = edt_mpesaCode.getText().toString().trim();

        if (TextUtils.isEmpty(mpesaCode)) {
            Toast.makeText(getApplicationContext(), "Enter mpesa code", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (mpesaCode.length() > 10 || mpesaCode.length() < 10) {
            Toast.makeText(getApplicationContext(), "Mpesa code  should contain 10 digits", Toast.LENGTH_SHORT).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!mpesaCode.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            Toast.makeText(getApplicationContext(), "Mpesa code should contain letters and digits",
                    Toast.LENGTH_LONG).show();
            btn_submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PAY_FINE,
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                btn_submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("penaltyID", penaltyID);
                params.put("amount", amount);
                params.put("mpesaCode", mpesaCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    private void print() {
        img_print.setVisibility(View.GONE);
        txv_receipt.setVisibility(View.VISIBLE);
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        PrintHelper photoPrinter = new PrintHelper(this); // Asume that 'this' is your activity
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("print", bitmap);

        img_print.setVisibility(View.VISIBLE);
//        txv_receipt.setVisibility(View.GONE);
    }


//    @Override
//    public void onRestart()
//    {
//        super.onRestart();
//        finish();
//        startActivity(getIntent());
//    }
}