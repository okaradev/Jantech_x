package com.krypt.Hubble.Users.Customers;

import static com.krypt.Hubble.utils.Urls.URL_GET_CART;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.krypt.Hubble.HUBBLEMODELS.CartModel;
import com.krypt.Hubble.HUBBLE_ADPTS.AdapterCart;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {


    private List<CartModel> list;
    private AdapterCart adapterCart;
    private SessionHandler session;
    private UserModel user;
    private LinearLayout layout_bottom;
    private TextView txv_cart_total, txv_success;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btn_cart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.cart_fragment, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        recyclerView = root.findViewById(R.id.recyclerView);
        txv_cart_total = root.findViewById(R.id.txv_cart_total);
        txv_success = root.findViewById(R.id.txv_success);
        layout_bottom = root.findViewById(R.id.layout_bottom);
        btn_cart = root.findViewById(R.id.btn_cart);

        layout_bottom.setVisibility(View.GONE);
        txv_success.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        list = new ArrayList<>();
   user=new UserModel();
        session = new SessionHandler(getContext());
        user = session.getUserDetails();

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CheckOut.class);
                startActivity(intent);
            }
        });


            getCartItems();

        return root;
    }


    public void getCartItems() {
        user = session.getUserDetails();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagcart", "["+response+"]");
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");


                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String itemID = jsn.getString("itemID");
                                    String productID = jsn.getString("productID");
                                    String productName = jsn.getString("productName");
                                    String price = jsn.getString("price");
                                    String quantity = jsn.getString("quantity");
                                    String image = jsn.getString("image");
                                    String subTotal = jsn.getString("subTotal");
                                    String stock = jsn.getString("stock");

                                    CartModel cartModal = new CartModel(productID, productName, quantity,
                                            price, image, itemID, subTotal, stock);
                                    list.add(cartModal);

                                }
                                progressBar.setVisibility(View.GONE);
                                layout_bottom.setVisibility(View.VISIBLE);
                                adapterCart = new AdapterCart(getContext(), list);

                                recyclerView.setAdapter(adapterCart);

                                String cartTotal = jsonObject.getString("cartTotal");

                                txv_cart_total.setText("Subtotal Ksh " + cartTotal);
                            } else if (status.equals("0")) {
                                Toast.makeText(getContext(), "Shopping cart is empty", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                txv_success.setVisibility(View.VISIBLE);

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}