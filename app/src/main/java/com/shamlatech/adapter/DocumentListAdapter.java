package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Media;
import com.shamlatech.fragment.SMediaDocumentFrag;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.MyViewHolder> {

    Activity act;
    String user_type;
    Context mContext;
    SMediaDocumentFrag frag;
    private ArrayList<Res_Media> listDocument;


    public DocumentListAdapter(Activity act, ArrayList<Res_Media> listDocument, String user_type, SMediaDocumentFrag frag) {
        this.act = act;
        this.listDocument = listDocument;
        this.user_type = user_type;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_document, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Media list = listDocument.get(position);

        holder.txtDocument.setText(list.getTitle());
        holder.txtDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.showViewDocument(list);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag.onDeleteClicked(position);
            }
        });


        if (list.getUploader_id().equals(staUserInfo.getId()) && staUserInfo.getRole().equals(Role_Teacher)) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else if (list.getUploader_id().equals(staUserInfo.getId()) && staUserInfo.getRole().equals(Role_Principal)) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listDocument.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocument;
        ImageView imgDelete;

        public MyViewHolder(View view) {
            super(view);
            txtDocument = view.findViewById(R.id.txtDocument);
            imgDelete = view.findViewById(R.id.imgDelete);
        }
    }

}









