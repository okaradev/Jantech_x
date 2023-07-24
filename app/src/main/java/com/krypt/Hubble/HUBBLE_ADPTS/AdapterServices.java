package com.krypt.Hubble.HUBBLE_ADPTS;


import static com.krypt.Hubble.utils.Urls.ROOT_URL_IMAGES;
import static com.krypt.Hubble.utils.Urls.URL_ADD_CARTSCERV;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.HUBBLEMODELS.ProductModal;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "Item_adapter";
    ProgressDialog progressDialog;
    private List<ProductModal> items;
    private final List<ProductModal> searchView;
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;


    private OnMoreButtonClickListener onMoreButtonClickListener;
    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String itemID = "";
    private String price = "";
    private final int count = 1;
    private EditText quantity;
    private TextView title;




    public AdapterServices(Context context, List<ProductModal> items) {
        this.items = items;
        this.searchView = items;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stock, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            session = new SessionHandler(ctx);
            user = session.getUserDetails();

            final ProductModal p = items.get(position);
            String url = ROOT_URL_IMAGES;
            Picasso.with(ctx)
                    .load(url + p.getImgUrl())
                    .fit()
                    .centerCrop()
                    .into(view.image);
            view.title.setText(p.getProductName());
//            view.txv_capacity.setText("Details: " + p.getDesc());
//            view.txv_stock.setText("In stock :" + p.getStock());
            view.price.setText("Ksh :" + p.getPrice());
            session = new SessionHandler(ctx);
            user = session.getUserDetails();
            try {
                clientId = user.getUserID();
            } catch (RuntimeException e) {

            }
//            view.add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    count = count+1;
//                    Log.e("count", "count "+ count);
//                    view.num.setText( Integer.toString(count));
//                }
//            });
//            view.minus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(count>1){
//                        count = count-1;
//                        view.num.setText( Integer.toString(count));
//                    }
//                }
//            });

            view.img_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "yea", Toast.LENGTH_SHORT).show();
                }
            });
              view.img_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session = new SessionHandler(ctx);
                    user = session.getUserDetails();


                        cartPrompts();
                      itemID = p.getProductID();
                    price = p.getPrice();




                }
            });
        }
    }




    public void cartPrompts() {



        // get prompts.xml view
        //  LayoutInflater li = LayoutInflater.from(ctx);
        View promptsView = LayoutInflater.from(ctx).inflate(R.layout.cart_prompt,null);
        //Create the AlertDialog with a reference to edit it later
        final AlertDialog dialog = new AlertDialog.Builder(ctx)


                .setCancelable(false)
                .setPositiveButton(R.string.sendservice, null) //Set to null. We override the onclick
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.setView(promptsView);

        quantity = promptsView.findViewById(R.id.edt_quantity);
        title = promptsView.findViewById(R.id.price_Text);
        promptsView.findViewById(R.id.edt_quantity).setVisibility(View.INVISIBLE);
        promptsView.findViewById(R.id.price_Text).setVisibility(View.INVISIBLE);


       dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        addToCart();



                    }
                });
            }
        });
       dialog.show();

    }



    public void addToCart() {
        final String itemQty = quantity.getText().toString().trim();
     //   Toast.makeText(ctx,"is"+itemQty+"is"+itemID+"is"+clientId , Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_CARTSCERV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("tagconvertstradd","["+response+"]");
                            Log.e("response ", response);
                            String msg = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
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
                params.put("itemID", itemID);
                params.put("quantity", itemQty);
                params.put("price", price);

                Log.e(TAG, "params is " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    items = searchView;
                } else {

                    ArrayList<ProductModal> filteredList = new ArrayList<>();

                    for (ProductModal androidVersion : items) {

                        if (androidVersion.getProductName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    items = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (ArrayList<ProductModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductModal obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ProductModal obj, MenuItem item);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, img_cart;
        public TextView title, txv_stock, txv_capacity;
        public TextView price;


        public OriginalViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.image_c);
            img_cart = v.findViewById(R.id.img_cart);

            title = v.findViewById(R.id.txv_title);
//            txv_stock = v.findViewById(R.id.txv_stock);
//            txv_capacity = v.findViewById(R.id.txv_capacity);
            price = v.findViewById(R.id.txv_price_c);

        }
    }


}