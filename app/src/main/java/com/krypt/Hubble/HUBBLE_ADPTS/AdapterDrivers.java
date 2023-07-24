package com.krypt.Hubble.HUBBLE_ADPTS;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.HUBBLEMODELS.DriversModel;
import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.Shipping.AssignDrivers;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;


public class AdapterDrivers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "Orders";
    ProgressDialog progressDialog;
    private final List<DriversModel> items;
    private final List<DriversModel> searchView;


    //
    private final Context ctx;
    private SessionHandler session;
    private UserModel user;
    private final String driverID = "";
    private final String orderID = "";
    private EditText quantity, days;


    public AdapterDrivers(Context context, List<DriversModel> items) {
        this.items = items;
        this.searchView = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drivers, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final DriversModel p = items.get(position);


            view.txv_driverName.setText(p.getDriverName());

            view.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, AssignDrivers.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("driverName", p.getDriverName());
                    in.putExtra("bookingID", p.getOrderID());
                    in.putExtra("driverID", p.getDriverID());

                    Toast.makeText(ctx, p.getDriverName(), Toast.LENGTH_SHORT).show();

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

        public TextView txv_driverName;


        public OriginalViewHolder(View v) {
            super(v);
            txv_driverName = v.findViewById(R.id.txv_driverName);


        }
    }


}