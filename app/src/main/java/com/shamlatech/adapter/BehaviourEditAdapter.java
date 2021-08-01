package com.shamlatech.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.shamlatech.activity.teacher.UpdateBehaviourActivity;
import com.shamlatech.api_response.Res_Look_BehaviourContent;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class BehaviourEditAdapter extends RecyclerView.Adapter<BehaviourEditAdapter.MyViewHolder> {

    private ArrayList<Res_Look_BehaviourContent> listBehaviour;
    Activity act;
    Context mContext;
    String[] arrayReport = {"Yes", "No"};
    String[] arrayReportDeviant = {"No", "Yes"};
    private static final String[] deviantValues = new String[] {"21","22","25","26","27","28","24"};
    String[] arrayBehaviourAction = {};
    String behaviour_selected;
    Dialog pick_Dialog;
    public String strTopic, action = "";
    public EditText edtTopic;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeBehaviours, relativeBehavioursAction;
        TextView txtBehaviour;
        Spinner spinnerYesNo, spinnerAction;

        public MyViewHolder(View view) {
            super(view);
            relativeBehaviours = view.findViewById(R.id.relativeBehaviours);
            relativeBehavioursAction = view.findViewById(R.id.relativeBehavioursAction);
            txtBehaviour = view.findViewById(R.id.txtBehaviour);
            spinnerYesNo = view.findViewById(R.id.spinnerYesNo);
            spinnerAction = view.findViewById(R.id.spinnerAction);
        }
    }

    public BehaviourEditAdapter(Activity act, ArrayList<Res_Look_BehaviourContent> listBehaviour, String l) {
        this.act = act;
        this.listBehaviour = listBehaviour;
        behaviour_selected = l;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_behaviour_edit_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position ) {
        final Res_Look_BehaviourContent list = listBehaviour.get(position);

        arrayBehaviourAction = list.getActions().split(",");

        holder.txtBehaviour.setText(list.getContent_name());

        ArrayAdapter adapterAction;
        if ( ArrayUtils.contains( deviantValues  , behaviour_selected ) ) {
             adapterAction = new ArrayAdapter(act, R.layout.simple_spinner_item, arrayReportDeviant);
        }else{
             adapterAction = new ArrayAdapter(act, R.layout.simple_spinner_item, arrayReport);
        }

       // ArrayAdapter adapterAction = new ArrayAdapter(act, R.layout.simple_spinner_item, arrayReportDeviant);
        adapterAction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerYesNo.setAdapter(adapterAction);

        ArrayAdapter adapterBehaviourAction = new ArrayAdapter(act, R.layout.simple_spinner_item_10, arrayBehaviourAction);
        adapterBehaviourAction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerAction.setAdapter(adapterBehaviourAction);

        holder.spinnerYesNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 1) {
                    holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorRed));
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.RED);
                    holder.relativeBehavioursAction.setVisibility(View.VISIBLE);
                } else {
                    holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorDarkGray));
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    holder.relativeBehavioursAction.setVisibility(View.GONE);
                }
                updateList(list.getId(), holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        holder.spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.RED);
                updateList(list.getId(), holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        for (int i = 0; i < arrayBehaviourAction.length; i++) {
            if (list.getAction_taken().equalsIgnoreCase(arrayBehaviourAction[i])) {
                holder.spinnerAction.setSelection(i);
            }
        }


        if ( ArrayUtils.contains( deviantValues  , behaviour_selected ) ) {

            if (list.getReport().equalsIgnoreCase("yes")) {
                holder.spinnerYesNo.setSelection(1);
             } else {
                holder.spinnerYesNo.setSelection(0);
            }


        }else{
            if (list.getReport().equalsIgnoreCase("no")) {
                holder.spinnerYesNo.setSelection(1);
            } else {
                holder.spinnerYesNo.setSelection(0);
            }
        }


    }


    private String showCustomAction(final String id, final String report, String action2, final Spinner spinnerAction) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(mContext);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_custom_action);

            RelativeLayout relativeSend;

            edtTopic = pick_Dialog.findViewById(R.id.edtTopic);
            relativeSend = pick_Dialog.findViewById(R.id.relativeSend);

            relativeSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     action = edtTopic.getText().toString().trim();
                    String myreport;
                    if ( ArrayUtils.contains( deviantValues  , behaviour_selected ) ) {
                         myreport = "Yes";
                    }else{
                         myreport = "No";
                    }
                    int currentSize = arrayBehaviourAction.length;
                    int newSize = currentSize + 1;
                    String[] tempArray = new String[ newSize ];
                    for (int i=0; i < currentSize; i++)
                    {
                        tempArray[i] = arrayBehaviourAction [i];
                    }
                    tempArray[newSize- 1] = action;
                    arrayBehaviourAction = tempArray;
                    ArrayAdapter<String> adapte =  new ArrayAdapter(act, R.layout.simple_spinner_item_10, arrayBehaviourAction);
                    spinnerAction.setAdapter(adapte);
                    int spinnerPosition = adapte.getPosition(action);
                    spinnerAction.setSelection(spinnerPosition);
                    ((UpdateBehaviourActivity) mContext).onUpdateEditList(id, myreport, action);
                    pick_Dialog.dismiss();
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
        } else {
            pick_Dialog = null;
        }
        return strTopic;
    }


    public void updateList(String id, final MyViewHolder holder) {
        pick_Dialog = null;
        String report = "";

        if ( ArrayUtils.contains( deviantValues  , behaviour_selected ) ) {
            report = arrayReportDeviant[holder.spinnerYesNo.getSelectedItemPosition()];
            if (report.equalsIgnoreCase("yes")){
                action = arrayBehaviourAction[holder.spinnerAction.getSelectedItemPosition()];
            } else {
                action = "";
            }
        }else{
            report = arrayReport[holder.spinnerYesNo.getSelectedItemPosition()];
            if (report.equalsIgnoreCase("no")){
                action = arrayBehaviourAction[holder.spinnerAction.getSelectedItemPosition()];
              } else {
                action = "";
            }
        }

        if( action.trim().equalsIgnoreCase("Any other (explain)")){
            Toast.makeText(mContext,"Please type explanation..",Toast.LENGTH_SHORT).show();
            showCustomAction(id, report, action,holder.spinnerAction);
        }else{
            ((UpdateBehaviourActivity) mContext).onUpdateEditList(id, report, action);
        }



    }

    @Override
    public int getItemCount() {
        return listBehaviour.size();
    }

}









