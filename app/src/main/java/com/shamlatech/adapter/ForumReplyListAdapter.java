package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Forum_Comments;
import com.shamlatech.pojo.PojoForumReplyList;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.FormatDateTimeForShow;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class ForumReplyListAdapter extends RecyclerView.Adapter<ForumReplyListAdapter.MyViewHolder> {

    private ArrayList<Res_Forum_Comments> listReply;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtReplyComment, txtReplyByAndDate;
        LinearLayout linearReply;

        public MyViewHolder(View view) {
            super(view);
            linearReply = view.findViewById(R.id.linearReply);
            txtReplyComment = view.findViewById(R.id.txtReplyComment);
            txtReplyByAndDate = view.findViewById(R.id.txtReplyByAndDate);
        }
    }

    public ForumReplyListAdapter(Activity act, ArrayList<Res_Forum_Comments> listReply) {
        this.act = act;
        this.listReply = listReply;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_forum_reply_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Forum_Comments list = listReply.get(position);

        holder.txtReplyComment.setText(list.getComment());
        holder.txtReplyByAndDate.setText("Replied By " + list.getComment_by_name() + " | " + FormatDateTimeForShow(list.getComment_on(), Vars.DatePattern1, "") );

        if (position % 2 == 0) {
            holder.linearReply.setBackgroundColor(act.getResources().getColor(R.color.colorListBlueAlternate1));
        } else {
            holder.linearReply.setBackgroundColor(act.getResources().getColor(R.color.colorListBlueAlternate2));
        }
    }

    @Override
    public int getItemCount() {
        return listReply.size();
    }

}









