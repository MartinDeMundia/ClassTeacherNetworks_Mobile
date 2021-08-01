package com.shamlatech.adapter;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.shamlatech.api_response.Res_Absnote_List;
import com.shamlatech.api_response.Res_Class_info;
import com.shamlatech.api_response.Res_ParentStudent_List;
import com.shamlatech.api_response.Res_Subjects;
import com.shamlatech.api_response.Result_Student_Class;
import com.shamlatech.api_response.Result_Subjects;
import com.shamlatech.fragment.SAbsentFrag;
import com.shamlatech.school_management.ParentsFillActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;

/**
 * Created by Martin Mundia on 05-MAY-2021.
 */
public class ParentStudentsAdapter extends RecyclerView.Adapter<ParentStudentsAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    ParentsFillActivity frag;
    private ArrayList<Res_ParentStudent_List> listParentStudents;
    Dialog class_Dialog;
    RecyclerView recyclerView;
    ArrayList<Res_Class_info> listStudentClass = new ArrayList<>();
    ClassAdapter mAdapter;
    ArrayList<String> streams  = new ArrayList<String>();
    ArrayList<String> classes = new ArrayList<String>();
    ArrayAdapter<String> streamadapter,classadapter;
    MaterialAutoCompleteTextView edtcStream ,edtcClass ;


    public ParentStudentsAdapter(Activity act, ArrayList<Res_ParentStudent_List> listParentStudents) {
        this.act = act;
        this.listParentStudents = listParentStudents;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.studlist_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_ParentStudent_List list = listParentStudents.get(position);
        holder.studname.setText(list.getStudentname());
        holder.studclass.setText(list.getClassname());

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                getRetro_Call(act, service.deleteStudent(list.getStudentid()),
                                        false, new API_Calls.OnApiResult() {
                                            @Override
                                            public void onSuccess(retrofit2.Response<Object> objectResponse) {
                                                holder.itemHolder.setVisibility(View.GONE);
                                            }
                                            @Override
                                            public void onFailure(String message) {
                                            }
                                        });
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setMessage("Are you sure you want to delete "+list.getStudentname()+" ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //create dialog to add the subjects
                class_Dialog = null;
                class_Dialog = new Dialog(act);
                class_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                class_Dialog.setCancelable(true);
                class_Dialog.setCanceledOnTouchOutside(true);
                class_Dialog.setContentView(R.layout.pop_add_class);

               Button buttonSaveSubject = (Button) class_Dialog.findViewById(R.id.buttonSaveStreamClass);
                buttonSaveSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                        EditText edtStream = (EditText) class_Dialog.findViewById(R.id.edtStream);
                        EditText edtClass = (EditText) class_Dialog.findViewById(R.id.edtClass);

                        //save
                        getRetro_Call(act, service.saveStudentClasses(list.getStudentid(),edtStream.getText().toString(),edtClass.getText().toString()),
                                false, new API_Calls.OnApiResult() {
                                    @Override
                                    public void onSuccess(retrofit2.Response<Object> objectResponse) {

                                        Result_Student_Class mPojo_Classes = onPojoBuilder(objectResponse,Result_Student_Class.class);
                                        ArrayList<Res_Class_info>  class_list = mPojo_Classes.getStudentClasslist();
                                        //set to the adapter here
                                        mAdapter = new ClassAdapter(act, class_list);
                                        recyclerView.setAdapter(mAdapter);

                                        //set the autocomplete
                                        ArrayList<Res_Class_info> streamlist = mPojo_Classes.getStreamlist();
                                        for (int i = 0; i < streamlist.size(); i++) {
                                            streams.add(streamlist.get(i).getClass_name());
                                        }
                                        streamadapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1 ,streams);
                                        edtcStream.setAdapter(streamadapter);

                                        ArrayList<Res_Class_info> classlist = mPojo_Classes.getClasslist();
                                        for (int i = 0; i < classlist.size(); i++) {
                                            classes.add(classlist.get(i).getClass_name());
                                        }
                                        classadapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1 ,classes);
                                        edtcClass.setAdapter(classadapter);

                                        holder.studname.setText(list.getStudentname());
                                        holder.studclass.setText(class_list.get(0).getClass_name()+" - "+ class_list.get(0).getStream_name());

                                        class_Dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(String message) {

                                    }



                                });



                    }
                });





                edtcStream = class_Dialog.findViewById(R.id.edtStream);
                edtcClass = class_Dialog.findViewById(R.id.edtClass);

                recyclerView = (RecyclerView) class_Dialog.findViewById(R.id.recycler_studentstream);
                listStudentClass = new ArrayList<>();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                //fetch and fill adapter
                getRetro_Call(act, service.getStudentClass(list.getStudentid()),
                        false, new API_Calls.OnApiResult() {
                            @Override
                            public void onSuccess(retrofit2.Response<Object> objectResponse) {

                                Result_Student_Class mPojo_Classes = onPojoBuilder(objectResponse,Result_Student_Class.class);
                                ArrayList<Res_Class_info>  class_list = mPojo_Classes.getStudentClasslist();
                                //set to the adapter here
                                mAdapter = new ClassAdapter(act, class_list);
                                recyclerView.setAdapter(mAdapter);

                                //set the autocomplete
                                ArrayList<Res_Class_info> streamlist = mPojo_Classes.getStreamlist();
                                for (int i = 0; i < streamlist.size(); i++) {
                                    streams.add(streamlist.get(i).getClass_name());
                                }
                                streamadapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1 ,streams);
                                edtcStream.setAdapter(streamadapter);


                                ArrayList<Res_Class_info> classlist = mPojo_Classes.getClasslist();
                                for (int i = 0; i < classlist.size(); i++) {
                                    classes.add(classlist.get(i).getClass_name());
                                }
                                classadapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1 ,classes);
                                edtcClass.setAdapter(classadapter);


                            }
                            @Override
                            public void onFailure(String message) {

                            }
                        });


                class_Dialog.show();



            }
        });









    }

    @Override
    public int getItemCount() {
        return listParentStudents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studname,studclass;
        ImageView iv;
        ImageButton close;
        LinearLayout itemHolder;

        public MyViewHolder(View view) {
            super(view);
            itemHolder = (LinearLayout) itemView.findViewById(R.id.itemHolder);
            close = (ImageButton) itemView.findViewById(R.id.close);
            studname = (TextView) itemView.findViewById(R.id.tv);
            studname.setGravity(Gravity.CENTER);
            studclass = (TextView) itemView.findViewById(R.id.labelbottom);
            studclass.setGravity(Gravity.CENTER);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

}









