package com.krypt.Hubble.Users.Staff.Store_man;

import static com.krypt.Hubble.utils.Urls.ADD_STOCK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddStock extends AppCompatActivity {

    private TextView txv_stock, txv_itemName;
    private Button btn_add;
    private ProgressBar progressBar;
    private String stockID;
    private EditText edt_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        btn_add = findViewById(R.id.btn_add);
        txv_stock = findViewById(R.id.txv_stock);
        txv_itemName = findViewById(R.id.txv_itemName);
        edt_stock = findViewById(R.id.edt_stock);


        progressBar.setVisibility(View.GONE);

        Intent in = getIntent();
        stockID = in.getStringExtra("stockID");
        txv_stock.setText("Available stock " + in.getStringExtra("stock"));
        txv_itemName.setText(in.getStringExtra("itemName"));


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addStock();
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

    private void addStock() {

        btn_add.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        final String stock = edt_stock.getText().toString().trim();
        if (TextUtils.isEmpty(stock)) {
            Toast.makeText(getApplicationContext(), "Enter first name", Toast.LENGTH_SHORT).show();
            btn_add.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_STOCK,
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
                                btn_add.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                btn_add.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            btn_add.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                btn_add.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("stockID", stockID);
                params.put("stock", stock);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}