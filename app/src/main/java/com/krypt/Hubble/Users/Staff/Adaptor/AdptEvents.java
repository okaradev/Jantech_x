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
import com.krypt.Hubble.Users.Staff.Items;
import com.krypt.Hubble.Users.Staff.Modal.EventsModal;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AdptEvents extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<EventsModal> items;
    private final List<EventsModal> searchView;

    private final Context ctx;


    private SessionHandler session;
    private UserModel user;


    public AdptEvents(Context context, List<EventsModal> items) {
        this.items = items;
        this.searchView = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_events, parent, false);
        vh = new AdptEvents.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdptBookings.OriginalViewHolder) {
            final AdptEvents.OriginalViewHolder view = (AdptEvents.OriginalViewHolder) holder;

            final EventsModal o = items.get(position);


            view.events_Title.setText("Event " + o.getEvent_title());
            view.events_desc.setText("About " + o.getEvent_des());


            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, Items.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("Name", o.getEvent_title());
                    in.putExtra("bookingID", o.getEvent_des());


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

                    ArrayList<EventsModal> filteredList = new ArrayList<>();

                    for (EventsModal androidVersion : items) {

                        if (androidVersion.getEvent_title().toLowerCase().contains(charString)) {

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
                items = (ArrayList<EventsModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView events_Title,events_desc;



        public OriginalViewHolder(View v) {
            super(v);

            events_Title = v.findViewById(R.id.event_title);
            events_desc = v.findViewById(R.id.event_desc);


        }
    }

}
