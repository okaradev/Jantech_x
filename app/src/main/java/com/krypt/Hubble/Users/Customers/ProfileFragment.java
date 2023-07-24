package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_TECHNICIAN_ORDER;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.LoginActivity;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private SessionHandler session;
    private UserModel user;
    private TextView txv_name, txv_phoneNo, txv_email,
            txv_username;
    private Button btn_logout, btn_fine,myservives;
    EditText det;
    EditText mpesacode;

    String clientId="";


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        txv_name = root.findViewById(R.id.txv_name);
        txv_username = root.findViewById(R.id.txv_username);
        txv_phoneNo = root.findViewById(R.id.txv_phoneNo);
        txv_email = root.findViewById(R.id.txv_email);
        btn_logout = root.findViewById(R.id.btn_logout);
        //  btn_fine = root.findViewById(R.id.btn_fine);
//                myservives= root.findViewById(R.id.btn_services);


        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();
            clientId = user.getUserID();

            txv_email.setText(user.getEmail());
            txv_name.setText(user.getFirstname() + " " + user.getLastname());
            txv_phoneNo.setText("Phone No " + user.getPhoneNo());
            txv_username.setText(user.getUsername());
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Toast.makeText(getActivity(), "You have logged out successfully", Toast.LENGTH_LONG).show();
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
            }
        });

      /*  btn_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*//*
                Intent in = new Intent(getActivity(), MyFines.class);
                startActivity(in);*//*

                placeService();


            }
        });*/

//       myservives.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent in = new Intent(getContext(),Services.class);
//                startActivity(in);
//            }});


        return root;
    }

    private void placeService() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.technician_dialog, null);
        //Create the AlertDialog with a reference to edit it later
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
//                .setView(v)
                .setCancelable(false)
                .setPositiveButton("submit", null) //Set to null. We override the onclick
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.setView(promptsView);

        det = promptsView.findViewById(R.id.edt_quantity);
     //    mpesacode = promptsView.findViewById(R.id.mpesacodeservice);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(det.getText().toString())) {
                            det.setError("Enter your problem");

//
                        }

                        else {
                            dialog.dismiss();
                           // progressShow();
                            AddTofinance();


                        }


                    }
                });
            }
        });
        dialog.show();


    }

    private void AddTofinance() {
        final String itemQty = det.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TECHNICIAN_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("response ", response);
                            String msg = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                               // progressDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "error1 " + msg);
                            //    progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "error" + e, Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "error2 " + e);
                        //    progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "error3 " + error);
             //   progressDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", clientId );

                params.put("request", itemQty);

                Log.e("params", "params is " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}