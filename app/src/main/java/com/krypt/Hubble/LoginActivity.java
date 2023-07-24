package com.krypt.Hubble;

import static com.krypt.Hubble.utils.Urls.URL_LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.krypt.Hubble.Users.Staff.Dashboard;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText edt_username, edt_password, edt_select;
    private TextView txv_forget, txv_register;
    private ProgressBar progressBar;
    private SessionHandler session;
    private UserModel user;

    private String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  progressBar = findViewById(R.id.progressBar);
        edt_password = findViewById(R.id.edt_password);
        edt_username = findViewById(R.id.edt_username);
        edt_select = findViewById(R.id.edt_select);
        txv_register = findViewById(R.id.txv_register);
       // txv_forget = findViewById(R.id.txv_forget);
        btn_login = findViewById(R.id.login_btn);

        progressBar.setVisibility(View.GONE);

        edt_select.setFocusable(false);

        session = new SessionHandler(LoginActivity.this);
        user = session.getUserDetails();

        txv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(LoginActivity.this, Register.class);
                startActivity(in);
            }
        });
        edt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select(v);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });


    }

    public void select(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"Customer", "Finance", "Service manager", "Driver", "Stock manager","Technician","Supplier"};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_select.setText(array[i]);
                Toast.makeText(LoginActivity.this, array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }

    public void login() {
        progressBar.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        final String select = edt_select.getText().toString().trim();
        final String username = edt_username.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();

        if (TextUtils.isEmpty(select)) {
            Toast.makeText(LoginActivity.this, "Select an option", Toast.LENGTH_SHORT).show();
            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String clientID = jsn.getString("clientID");
                                    String firstname = jsn.getString("firstname");
                                    String lastname = jsn.getString("lastname");
                                    String username = jsn.getString("username");
                                    String phoneNo = jsn.getString("phoneNo");
                                    String email = jsn.getString("email");
                                    String dateCreated = jsn.getString("dateCreated");
                                    user_type = jsn.getString("user");
                                    session.loginUser(clientID, firstname, lastname, username, phoneNo, email, dateCreated, user_type);
                                }

                                if (user_type.equals("Client")) {

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                    startActivity(intent);
                                    LoginActivity.this.onBackPressed();
                                }
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                LoginActivity.this.onBackPressed();

                            } else if (status.equals("2")) {

                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            } else if (status.equals("0")) {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("select", select);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}