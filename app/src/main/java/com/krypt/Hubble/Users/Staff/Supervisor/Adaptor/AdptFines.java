package com.krypt.Hubble.Users.Staff.Supervisor.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Finance.ActionFine;
import com.krypt.Hubble.Users.Staff.Supervisor.Model.FinesModel;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;

public class AdptFines extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FinesModel> items;
    private final Context ctx;
    private SessionHandler session;
    private UserModel user;

    public AdptFines(Context context, List<FinesModel> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fine, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final FinesModel o = items.get(position);
            view.txv_name.setText(o.getClientName());
            view.txv_bookingID.setText("# " + o.getBookingID());
            view.txv_penaltyStatus.setText("Status " + o.getPenaltyStatus());
            view.txv_amount.setText("KSH  " + o.getAmount());
            view.txv_remarks.setText("Remarks  " + o.getRemarks());

            view.itemView.setOnClickListener(v -> {

                if (user.getUser_type().equals("Finance")) {

                    Intent in = new Intent(ctx, ActionFine.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("clientName", o.getClientName());
                    in.putExtra("bookingID", o.getBookingID());
                    in.putExtra("penaltyID", o.getPenaltyID());
                    in.putExtra("amount", o.getAmount());
                    in.putExtra("mpesaCode", o.getMpesaCode());
                    in.putExtra("fineStatus", o.getPenaltyStatus());
                    in.putExtra("remarks", o.getRemarks());
                    ctx.startActivity(in);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txv_bookingID, txv_name, txv_amount,
                txv_remarks, txv_penaltyStatus;

        public OriginalViewHolder(View v) {
            super(v);
            txv_name = v.findViewById(R.id.txv_name);
            txv_bookingID = v.findViewById(R.id.txv_bookingID);
            txv_penaltyStatus = v.findViewById(R.id.txv_penaltyStatus);
            txv_amount = v.findViewById(R.id.txv_cash);
            txv_remarks = v.findViewById(R.id.txv_remarks);

            session = new SessionHandler(ctx);
            user = session.getUserDetails();
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