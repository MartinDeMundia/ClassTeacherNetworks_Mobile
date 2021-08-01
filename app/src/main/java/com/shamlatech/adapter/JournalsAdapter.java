package com.shamlatech.adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Absnote_List;
import com.shamlatech.api_response.Res_Stud_Journal;
import com.shamlatech.fragment.SAbsentFrag;
import com.shamlatech.fragment.SJournalFrag;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
/**
 * Created by Martin Mundia on 12-March-2020.
 */
public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.MyViewHolder> {
    Activity act;
    Context mContext;
    SJournalFrag frag;
    private ArrayList<Res_Stud_Journal> listJournals;
    public JournalsAdapter(Activity act, ArrayList<Res_Stud_Journal> listJournals, SJournalFrag frag) {
        this.act = act;
        this.listJournals = listJournals;
        this.frag = frag;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_journal_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Journal list = listJournals.get(position);
        holder.txtJDate.setText(list.getJournal_date());
        holder.txtEmoji.setText(list.getEmoji().trim());
        holder.txtExplanation.setText(list.getExplanation());
        switch(list.getEmoji().trim()) {
            case "happy":
                holder.imageJournal.setImageResource(R.drawable.smiling);
                break;
            case "excited":
                holder.imageJournal.setImageResource(R.drawable.excited);
                break;
            case "sad":
                holder.imageJournal.setImageResource(R.drawable.sad);
                break;
            case "angry":
                holder.imageJournal.setImageResource(R.drawable.angry);
                break;
            default:
                holder.imageJournal.setImageResource(R.drawable.smiling);
        }
    }

    @Override
    public int getItemCount() {
        return listJournals.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout linearJournals;
        TextView txtJDate, txtEmoji,txtExplanation;
        ImageView imageJournal;
        public MyViewHolder(View view) {
            super(view);
            linearJournals = view.findViewById(R.id.linearForums);
            txtJDate = view.findViewById(R.id.txtJDate);
            txtEmoji = view.findViewById(R.id.txtEmoji);
            txtExplanation = view.findViewById(R.id.txtExplanation);
            imageJournal = view.findViewById(R.id.imageJournal);
        }
    }

}
