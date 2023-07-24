package com.krypt.Hubble.Users.Staff.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.Users.Staff.ItemsSup;
import com.krypt.Hubble.Users.Staff.Modal.BookingModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AdptSupers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookingModal> items;
    private final List<BookingModal> searchView;

    private final Context ctx;


    private SessionHandler session;
    private UserModel user;


    public AdptSupers(Context context, List<BookingModal> items) {
        this.items = items;
        this.searchView = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_client_bookings, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final BookingModal o = items.get(position);

            view.txv_name.setText(o.getName());
            view.txv_bookingID.setText("ordering No " + o.getBookingNo());
            view.txv_cash.setText("Cash " + o.getBookingCost() + " KES");
            view.txv_mpesaCode.setText("Mpesa code " + o.getMpesaCode());
            view.txv_bookingDate.setText("Date " + o.getBookingDate());
            view.txv_bookingStatus.setText("Status " + o.getBookingStatus());
            view.txv_dateToDeliver.setText("Delivery date " + o.getDateToDeliver());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, ItemsSup.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("Name", o.getName());
                    in.putExtra("bookingID", o.getBookingNo());
                    in.putExtra("bookingCost", o.getBookingCost());
                    in.putExtra("mpesaCode", o.getMpesaCode());
                    in.putExtra("bookingDate", o.getBookingDate());
                    in.putExtra("bookingStatus", o.getBookingStatus());
                    in.putExtra("dateToDeliver", o.getDateToDeliver());
                    in.putExtra("deliveryCost", o.getDeliveryCost());

                    ctx.startActivity(in);


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //    public interface OnItemClickListener {
//        void onItemClick(View view, ProductModal obj, int pos);
//    }
//
//    public interface OnMoreButtonClickListener {
//        void onItemClick(View view, ProductModal obj, MenuItem item);
//    }
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    items = searchView;
                } else {

                    ArrayList<BookingModal> filteredList = new ArrayList<>();

                    for (BookingModal androidVersion : items) {

                        if (androidVersion.getName().toLowerCase().contains(charString)) {

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
                items = (ArrayList<BookingModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_bookingID, txv_name, txv_cash, txv_bookingDate;
        public TextView txv_mpesaCode, txv_bookingStatus, txv_dateToDeliver;


        public OriginalViewHolder(View v) {
            super(v);

            txv_name = v.findViewById(R.id.txv_name);
            txv_mpesaCode = v.findViewById(R.id.txv_mpesaCode);
            txv_bookingDate = v.findViewById(R.id.txv_bookingDate);
            txv_bookingID = v.findViewById(R.id.txv_bookingID);
            txv_bookingStatus = v.findViewById(R.id.txv_bookingStatus);
            txv_cash = v.findViewById(R.id.txv_cash);
            txv_dateToDeliver = v.findViewById(R.id.txv_dateToDeliver);

        }
    }


}