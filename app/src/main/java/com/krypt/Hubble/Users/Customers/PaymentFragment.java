package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_BOOKINGS;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Customers.Adaptors.AdapterPayment;
import com.krypt.Hubble.Users.Customers.Model.ModalBook;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFragment extends Fragment {


    private SessionHandler session;
    private UserModel user;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ModalBook> list;
    private AdapterPayment adapterBookings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bookings_fragment, container, false);

        session = new SessionHandler(getContext());
        user = session.getUserDetails();

        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        bookings();

        return root;
    }

    private void bookings() {
        user = session.getUserDetails();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("RESPONSE ", " " + response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String bookingID = jsn.getString("bookingID");
                                    String bookingCost = jsn.getString("bookingCost");
                                    String place = jsn.getString("place");
//                                    String place = "place";
                                    String names =user.getFirstname()+' '+user.getLastname();
                                    String mpesaCode = jsn.getString("mpesaCode");
                                    String bookingDate = jsn.getString("bookingDate");
                                    String dateToDeliver = jsn.getString("dateToDeliver");
                                    String bookingStatus = jsn.getString("bookingStatus");
                                    String deliveryCost = jsn.getString("deliveryCost");
                                    String bookingRemarks = jsn.getString("bookingRemarks");

                                    ModalBook modalBook = new ModalBook(names,place,bookingID, bookingCost,
                                            mpesaCode, bookingDate, bookingStatus,
                                            deliveryCost, dateToDeliver, bookingRemarks);
                                    list.add(modalBook);
                                }
                                adapterBookings = new AdapterPayment(getContext(), list);
                                recyclerView.setAdapter(adapterBookings);
                                progressBar.setVisibility(View.GONE);
                            } else if (status.equals("0")) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AbstractMethodError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}