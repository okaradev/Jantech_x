package com.krypt.Hubble.Users.Staff.Driver.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Driver.AssgnDetails;
import com.krypt.Hubble.Users.Staff.Driver.Model.AssignModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;

public class AdptAssignments extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AssignModal> items;

    private final Context ctx;


    private SessionHandler session;
    private UserModel user;


    public AdptAssignments(Context context, List<AssignModal> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_assigned, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final AssignModal o = items.get(position);

            view.txv_name.setText(o.getClientName());
            view.txv_bookingID.setText("Service No " + o.getBookingID());
            view.txv_dateToDeliver.setText("Delivery date " + o.getDateToDeliver());
            view.txv_county.setText("County " + o.getCounty() + " - " + o.getTown());
            view.txv_location.setText(o.getLocation());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, AssgnDetails.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("clientName", o.getClientName());
                    in.putExtra("bookingID", o.getBookingID());
                    in.putExtra("county", o.getCounty() + " " + o.getTown());
                    in.putExtra("location", o.getLocation());
                    in.putExtra("location", o.getLocation());
                    in.putExtra("dateToDeliver", o.getDateToDeliver());
                    in.putExtra("bookingStatus", o.getBookingStatus());
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

        public TextView txv_bookingID, txv_name, txv_county,
                txv_dateToDeliver, txv_location;


        public OriginalViewHolder(View v) {
            super(v);

            txv_name = v.findViewById(R.id.txv_clientName);
            txv_location = v.findViewById(R.id.txv_location);
            txv_county = v.findViewById(R.id.txv_county);
            txv_bookingID = v.findViewById(R.id.txv_bookingID);
            txv_dateToDeliver = v.findViewById(R.id.txv_dateToDeliver);

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