package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shamlatech.api_response.Res_Media;
import com.shamlatech.fragment.SMediaVideoFrag;
import com.shamlatech.school_management.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    private ArrayList<Res_Media> listVideos;
    Activity act;
    String user_type;
    Context mContext;
    SMediaVideoFrag frag;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgVideos;

        public MyViewHolder(View view) {
            super(view);
            imgVideos = view.findViewById(R.id.imgVideos);
        }
    }

    public VideoListAdapter(Activity act, ArrayList<Res_Media> listVideos, String user_type, SMediaVideoFrag frag) {
        this.act = act;
        this.frag = frag;
        this.user_type = user_type;
        this.listVideos = listVideos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_video_show, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Media list = listVideos.get(position);

        ShowImage(holder.imgVideos, list.getThumb());

        holder.imgVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag.onVideoClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    private void ShowImage(ImageView img_View, String img_URL) {
        if (img_URL != null && !img_URL.equals(""))
            Picasso.with(act).load(img_URL).fit().into(img_View);
    }
}









