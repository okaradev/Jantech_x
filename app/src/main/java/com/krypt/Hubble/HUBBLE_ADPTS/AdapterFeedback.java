package com.krypt.Hubble.HUBBLE_ADPTS;



import static com.krypt.Hubble.HUBBLE_ADPTS.AdapterFeedbackService.myfb;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.krypt.Hubble.HUBBLEMODELS.FeedbackModel;
import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

import java.util.List;

public class AdapterFeedback extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "Events";
    ProgressDialog progressDialog;
    private final List<FeedbackModel> items;
    private final Context ctx;
    private SessionHandler session;
    private UserModel user;
    public static String finid="";

    public AdapterFeedback(Context context, List<FeedbackModel> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_feedback, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final FeedbackModel p = items.get(position);
            finid=p.getId();
            myfb=p.getId();

            view.txv_comment.setText(p.getComment());
            view.txv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(ctx, "Adapterfeedback  "+finid+" "+myfb, Toast.LENGTH_SHORT).show();

                }
            });
            view.txv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "Adapterfeedback"+finid+""+myfb, Toast.LENGTH_SHORT).show();

                }
            });

            if (p.getReply().equals("0")) {
                view.txv_reply.setVisibility(View.GONE);
            } else {
                view.txv_reply.setText(p.getReply());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_comment, txv_reply;

        public OriginalViewHolder(View v) {
            super(v);

            txv_comment = v.findViewById(R.id.txv_comment);
            txv_reply = v.findViewById(R.id.txv_reply);

            session = new SessionHandler(ctx);
            user = session.getUserDetails();

        }
    }
}