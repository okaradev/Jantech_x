package com.krypt.Hubble.Users.Staff.Store_man.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Store_man.Model.StaffReturnModel;
import com.krypt.Hubble.Users.Staff.Store_man.ReturnedItems;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;

public class AdptStaffReturns extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<StaffReturnModel> items;

    private final Context ctx;


    private SessionHandler session;
    private UserModel user;


    public AdptStaffReturns(Context context, List<StaffReturnModel> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_staff_returns, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final StaffReturnModel o = items.get(position);

            view.txv_name.setText("Returned by " + o.getName());
            view.txv_bookingID.setText("Booking No " + o.getBookingNo());
            view.txv_phoneNo.setText("Contact " + o.getPhoneNo());
            view.txv_returnStatus.setText("Status " + o.getReturnStatus());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, ReturnedItems.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("staffName", o.getName());
                    in.putExtra("bookingID", o.getBookingNo());
                    in.putExtra("returnStatus", o.getReturnStatus());
                    in.putExtra("phoneNo", o.getPhoneNo());
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

        public TextView txv_bookingID, txv_name, txv_returnStatus, txv_phoneNo;


        public OriginalViewHolder(View v) {
            super(v);

            txv_name = v.findViewById(R.id.txv_name);
            txv_bookingID = v.findViewById(R.id.txv_bookingID);
            txv_returnStatus = v.findViewById(R.id.txv_returnStatus);
            txv_phoneNo = v.findViewById(R.id.txv_phoneNo);

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