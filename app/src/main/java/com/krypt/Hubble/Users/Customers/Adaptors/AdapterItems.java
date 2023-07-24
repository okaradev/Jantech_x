package com.krypt.Hubble.Users.Customers.Adaptors;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Customers.Model.PaymentItemsModal;


import java.util.List;

public class AdapterItems extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "Orders adapter";
    ProgressDialog progressDialog;
    private final List<PaymentItemsModal> items;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //
    private final Context ctx;



    public AdapterItems(Context context, List<PaymentItemsModal> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_booking_items, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final PaymentItemsModal o = items.get(position);


            view.txv_itemName.setText(o.getQuantity() + " " + o.getItemName());
            view.txv_price.setText(o.getPrice() + " Kshs ");
            //  view.txv_days.setText("No of Days " + "55");
            view.txv_subTotal.setText("Subtotal KES " + o.getSubTotal());
            view.txv_capacity.setText("Description " + o.getCapacity());



        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_itemName, txv_subTotal;
        public TextView txv_price, txv_days, txv_capacity;


        public OriginalViewHolder(View v) {
            super(v);


            txv_subTotal = v.findViewById(R.id.txv_subTotal);
            txv_itemName = v.findViewById(R.id.txv_itemName);
            txv_price = v.findViewById(R.id.txv_price);

            txv_capacity = v.findViewById(R.id.txv_capacity);

        }
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, ProductModal obj, int pos);
//    }
//
//    public interface OnMoreButtonClickListener {
//        void onItemClick(View view, ProductModal obj, MenuItem item);
//    }


}