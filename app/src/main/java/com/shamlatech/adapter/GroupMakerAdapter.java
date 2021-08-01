package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Group;
import com.shamlatech.fragment.teacher.GroupMakerFragment;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class GroupMakerAdapter extends RecyclerView.Adapter<GroupMakerAdapter.MyViewHolder> {

    private ArrayList<Res_Group> listGroupMaker;
    GroupMakerFragment frag;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearGroupMaker;
        TextView txtGroupName, txtSubjectClass, txtGroupMember;
        ImageView imgMenu;

        public MyViewHolder(View view) {
            super(view);
            linearGroupMaker = view.findViewById(R.id.linearGroupMaker);
            txtGroupName = view.findViewById(R.id.txtGroupName);
            txtSubjectClass = view.findViewById(R.id.txtSubjectClass);
            txtGroupMember = view.findViewById(R.id.txtGroupMember);
            imgMenu = view.findViewById(R.id.imgMenu);
        }
    }

    public GroupMakerAdapter(Activity act, ArrayList<Res_Group> listGroupMaker, GroupMakerFragment frag) {
        this.act = act;
        this.listGroupMaker = listGroupMaker;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group_maker, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_Group list = listGroupMaker.get(position);

        holder.txtGroupName.setText(list.getName());
        holder.txtSubjectClass.setText(list.getSubject_name() + ", " + list.getClass_name() + list.getSection_name());

        List<String> listStudent = new ArrayList();
        for (int i = 0; i < list.getGroup_members().size(); i++) {
            listStudent.add(list.getGroup_members().get(i).getName());
        }

        holder.txtGroupMember.setText("Group Member : " + android.text.TextUtils.join(",", listStudent));
        holder.linearGroupMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag.onGroupMakerViewClick(position);
            }
        });

        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.imgMenu);
                //inflating menu from xml resource
                popup.inflate(R.menu.group_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_Edit:
                                frag.onGroupMakerEditClick(position);
                                break;
                            case R.id.item_Delete:
                                frag.onGroupMakerDeleteClick(position);
                                //handle menu2 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listGroupMaker.size();
    }

}









