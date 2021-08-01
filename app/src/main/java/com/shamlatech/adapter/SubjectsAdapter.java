package com.shamlatech.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.api_response.Res_Class_info;
import com.shamlatech.api_response.Res_Subjects;
import com.shamlatech.api_response.Result_Student_Class;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;

import java.util.ArrayList;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

    private ArrayList<Res_Subjects> listSubjects;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subject_name;
        CheckBox  is_active;
        LinearLayout subjectholder;

        public MyViewHolder(View view) {
            super(view);
            subject_name = view.findViewById(R.id.subject_name);
            is_active = view.findViewById(R.id.is_active);
            subjectholder = view.findViewById(R.id.subjectholder);
        }
    }
    public SubjectsAdapter(Activity act, ArrayList<Res_Subjects> listSubjects) {
        this.act = act;
        this.listSubjects = listSubjects;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_subjects_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_Subjects list = listSubjects.get(position);
        holder.subject_name.setText(list.getDescription());
        holder.is_active.setChecked(true);

        holder.is_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked()){
                    // perform logic
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    getRetro_Call(act, service.removeSubject(list.getId()),
                                            false, new API_Calls.OnApiResult() {
                                                @Override
                                                public void onSuccess(retrofit2.Response<Object> objectResponse) {
                                                    holder.subjectholder.setVisibility(View.GONE);
                                                    Toast.makeText(act,
                                                            "Removed subject " + list.getDescription(),
                                                            Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                                @Override
                                                public void onFailure(String message) {
                                                }

                                            });

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    holder.is_active.setChecked(true);
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                    builder.setMessage("Are you sure you want to remove "+list.getDescription()+" from the list ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return listSubjects.size();
    }

}










