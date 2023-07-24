package com.krypt.Hubble.Users.Customers.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Customers.FineDetails;
import com.krypt.Hubble.Users.Customers.Model.MyFinesModel;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;

public class AdptMyFines extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MyFinesModel> items;
    private final Context ctx;
    private SessionHandler session;
    private UserModel user;

    public AdptMyFines(Context context, List<MyFinesModel> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_fine, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final MyFinesModel o = items.get(position);
            view.txv_bookingID.setText("# " + o.getBookingID());
            view.txv_penaltyStatus.setText("Status " + o.getPenaltyStatus());
            view.txv_amount.setText("KSH  " + o.getAmount());
            view.txv_remarks.setText("Remarks  " + o.getRemarks());

            view.itemView.setOnClickListener(v -> {
                Intent in = new Intent(ctx, FineDetails.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("bookingID", o.getBookingID());
                in.putExtra("penaltyID", o.getPenaltyID());
                in.putExtra("fineStatus", o.getPenaltyStatus());
                in.putExtra("remarks", o.getRemarks());
                in.putExtra("amount", o.getAmount());
                in.putExtra("mpesaCode", o.getMpesaCode());
                ctx.startActivity(in);
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txv_bookingID, txv_amount,
                txv_remarks, txv_penaltyStatus;

        public OriginalViewHolder(View v) {
            super(v);
            txv_bookingID = v.findViewById(R.id.txv_bookingID);
            txv_penaltyStatus = v.findViewById(R.id.txv_penaltyStatus);
            txv_amount = v.findViewById(R.id.txv_cash);
            txv_remarks = v.findViewById(R.id.txv_remarks);
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