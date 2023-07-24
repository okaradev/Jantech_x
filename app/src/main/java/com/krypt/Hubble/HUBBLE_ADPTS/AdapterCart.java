package com.krypt.Hubble.HUBBLE_ADPTS;


import static com.krypt.Hubble.utils.Urls.ROOT_URL_IMAGES;
import static com.krypt.Hubble.utils.Urls.URL_REMOVE_ITEM;
import static com.krypt.Hubble.utils.Urls.URL_UPDATE_CART;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.HUBBLEMODELS.CartModel;
import com.krypt.Hubble.HUBBLEMODELS.ProductModal;
import com.krypt.Hubble.MainActivity;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "Item_adapter";
    public LinearLayout layout_bottom;
    ProgressDialog progressDialog;
    private final List<CartModel> items;
    private final Context ctx;

    //
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;
    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String productID = "";
    private String itemID = "";

    private String qty;
    private EditText quantity, days;

    public AdapterCart(Context context, List<CartModel> items) {
        this.items = items;
        ctx = context;

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final CartModel p = items.get(position);


            String url = ROOT_URL_IMAGES;
            Picasso.with(ctx)
                    .load(url + p.getImgUrl())
                    .fit()
                    .centerCrop()
                    .into(view.image);
            view.title.setText(p.getQuantity() + " " + p.getProductName());
            view.txv_stock.setText(p.getStock() + " in stock");

            view.price.setText("Kshs "+p.getPrice());
            view.txv_subTotal.setText("Subtotal: Kshs " + p.getSubToatl());
            session = new SessionHandler(ctx);
            user = session.getUserDetails();
            try {
                clientId = user.getUserID();

            } catch (RuntimeException e) {
                e.printStackTrace();

            }

            view.txv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productID = p.getProductID();
                    itemID = p.getItemID();

                    qty = p.getQuantity();
                    qtyPrompts(v);


                }
            });

            view.txv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        itemID = p.getItemID();
                        getAlertRemove(v);

                        items.remove(position);
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    public void getAlertRemove(final View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setTitle("Please confirm");
        builder1.setIcon(R.drawable.ic_notifications);
        builder1.setMessage("Are sure you want to remove? ");
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "No", null);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressShow(view);

                        removeItem();
                        dialog.dismiss();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void qtyPrompts(View v) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(ctx);
        View promptsView = li.inflate(R.layout.cart_prompt, null);
        //Create the AlertDialog with a reference to edit it later
        final AlertDialog dialog = new AlertDialog.Builder(v.getContext())
//                .setView(v)
                .setCancelable(false)
                .setPositiveButton("Update cart", null) //Set to null. We override the onclick
                .setNegativeButton("Close", null)
                .create();
        dialog.setView(promptsView);

        quantity = promptsView.findViewById(R.id.edt_quantity);
      //  days = promptsView.findViewById(R.id.edt_days);

        quantity.setText(qty);
        //days.setText(NoDays);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(quantity.getText().toString())) {
                            quantity.setError("Enter item quantity");

//
                        } else {
                            dialog.dismiss();
//                            progressShow(view);
                            updateCart();

                        }


                    }
                });
            }
        });
        dialog.show();

    }

    public void updateCart() {
        progressDialog=new ProgressDialog(ctx.getApplicationContext());
        final String itemQty = quantity.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("response ", response);
                            String msg = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                notifyDataSetChanged();

                            } else {
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "error1 " + msg);
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx, "error" + e, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "error2 " + e);
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "error3 " + error);
                progressDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", clientId);
                params.put("productID", productID);
                params.put("itemID", itemID);
                params.put("quantity", itemQty);
                Log.e(TAG, "params is " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    public void removeItem() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REMOVE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("tagconvertstrrev", "["+response+"]");
                            Log.e("response ", response);
                            String msg = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                notifyDataSetChanged();

                                // check if shopping cart is empty
                                if (getItemCount() == 0) {
                                    Toast.makeText(ctx, "Shopping cart is empty", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(ctx, MainActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    ctx.startActivity(in);

                                }


                            } else {
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "error1 " + msg);
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx, "error" + e, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "error2 " + e);
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "error3 " + error);
                progressDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("itemID", itemID);
                Log.e(TAG, "params is " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    public void progressShow(View view) {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage(" Please wait...");
        progressDialog.setTitle("Action");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public int getItemCount() {
        return items.size();

    }


    public interface OnItemClickListener {
        void onItemClick(View view, ProductModal obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ProductModal obj, MenuItem item);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, txv_stock;
        public TextView price, txv_subTotal,
                txv_days, txv_update, txv_remove;


        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image_c);
            title = v.findViewById(R.id.txv_title);
            txv_stock = v.findViewById(R.id.txv_stock);
            price = v.findViewById(R.id.txv_price_c);
            txv_subTotal = v.findViewById(R.id.txv_subTotal);
           // txv_days = v.findViewById(R.id.txv_days);
            txv_update = v.findViewById(R.id.txv_update);
            txv_remove = v.findViewById(R.id.txv_remove);

        }


    }


}