package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shamlatech.api_response.Res_Media;
import com.shamlatech.fragment.SMediaPhotoFrag;
import com.shamlatech.school_management.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyViewHolder> {

    private ArrayList<Res_Media> listPhotos;
    Activity act;
    String user_type;
    Context mContext;
    SMediaPhotoFrag frag;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhotos;

        public MyViewHolder(View view) {
            super(view);
            imgPhotos = view.findViewById(R.id.imgPhotos);
        }
    }

    public PhotoListAdapter(Activity act, ArrayList<Res_Media> listPhotos, String user_type, SMediaPhotoFrag frag) {
        this.act = act;
        this.frag = frag;
        this.user_type = user_type;
        this.listPhotos = listPhotos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_photo_show, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_Media list = listPhotos.get(position);

        ShowImage(holder.imgPhotos, list.getThumb());

        holder.imgPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.onPhotoClicked(position, holder.imgPhotos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhotos.size();
    }

    private void ShowImage(ImageView img_View, String img_URL) {
        if (img_URL != null && !img_URL.equals(""))
            Picasso.with(act).load(img_URL).fit().into(img_View);
    }
}









