package com.shamlatech.adapter;



import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Absnote_List;
import com.shamlatech.fragment.SAbsentFrag;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
/**
 * Created by Martin Mundia on 28-AUG-2019.
 */
public class AbsentNoteAdapter extends RecyclerView.Adapter<AbsentNoteAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    SAbsentFrag frag;
    private ArrayList<Res_Absnote_List> listAbsentNote;


    public AbsentNoteAdapter(Activity act, ArrayList<Res_Absnote_List> listAbsentNote, SAbsentFrag frag) {
        this.act = act;
        this.listAbsentNote = listAbsentNote;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_absentnote_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Absnote_List list = listAbsentNote.get(position);

        holder.txtTopic.setText(list.getAdate());
        holder.txtCreatorAndDate.setText(list.getAuserid());
        holder.txtSendTo.setText(list.getAnote());
/*        holder.txtCreatorAndDate.setText("POSTED BY " + list.getPosted_name() + " | " + FormatDateTimeForShow(list.getPosted_on(), Vars.DatePattern1, ""));
        if (list.getTo().equals(Vars.Role_Teacher) || list.getTo().equals(Vars.Role_Principal)) {
            holder.txtSendTo.setText("SEND TO " + list.getClass_name() + list.getSection_name() + " Teacher");
        } else {
            holder.txtSendTo.setText("SEND TO " + list.getClass_name() + list.getSection_name() + " Parent");
        }
        if (list.getTo().equals(Vars.Role_Teacher) && list.getFrom().equals(Vars.Role_Teacher)) {
            holder.txtSendTo.setVisibility(View.GONE);
        } else if (list.getTo().equals(Vars.Role_Principal) && list.getFrom().equals(Vars.Role_Principal)) {
            holder.txtSendTo.setVisibility(View.GONE);
        } else {
            holder.txtSendTo.setVisibility(View.VISIBLE);
        }
        holder.txtComments.setText(list.getTotal_comments() + " Comments");
        if (list.getIs_fav().equals("1")) {
            holder.imgStar.setVisibility(View.VISIBLE);
        } else {
            holder.imgStar.setVisibility(View.GONE);
        }

        if (list.getIs_open().equals("1")) {
            holder.txtTopic.setPaintFlags(holder.txtTopic.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        } else {
            holder.txtTopic.setPaintFlags(holder.txtTopic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.linearForums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.onClickForum(list);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listAbsentNote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearForums;
        TextView txtTopic, txtCreatorAndDate, txtComments, txtSendTo;
        ImageView imgStar;

        public MyViewHolder(View view) {
            super(view);
            linearForums = view.findViewById(R.id.linearForums);
            txtTopic = view.findViewById(R.id.txtTopic);
            txtCreatorAndDate = view.findViewById(R.id.txtCreatorAndDate);
            txtSendTo = view.findViewById(R.id.txtSendTo);
            //txtComments = view.findViewById(R.id.txtComments);
            //imgStar = view.findViewById(R.id.imgStar);
        }
    }

}









