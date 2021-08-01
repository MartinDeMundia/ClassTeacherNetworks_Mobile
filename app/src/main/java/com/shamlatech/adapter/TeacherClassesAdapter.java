
package com.shamlatech.adapter;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Vibrator;
        import android.support.design.widget.Snackbar;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import com.shamlatech.api_response.Res_Class_info;
        import com.shamlatech.api_response.Res_SI_School_Details;
        import com.shamlatech.api_response.Res_Subjects;
        import com.shamlatech.api_response.Result_Classes;
        import com.shamlatech.api_response.Result_Subjects;
        import com.shamlatech.school_management.ParentsFillActivity;
        import com.shamlatech.school_management.R;
        import com.shamlatech.school_management.TeachersFillActivity;
        import com.shamlatech.utils.API_Calls;

        import java.util.ArrayList;
        import static com.facebook.FacebookSdk.getApplicationContext;
        import static com.shamlatech.utils.API_Calls.getRetro_Call;
        import static com.shamlatech.utils.API_Calls.onPojoBuilder;
        import static com.shamlatech.utils.API_Calls.service;

/**
 * Created by Martin Mundia on 04-MAY-2021.
 */
public class TeacherClassesAdapter extends RecyclerView.Adapter<TeacherClassesAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    ParentsFillActivity frag;
    private ArrayList<Res_Class_info> listClassesinfo;
    Dialog subject_Dialog;
    RecyclerView recyclerView;
    ArrayList<Res_Subjects> listSubjects = new ArrayList<>();
    SubjectsAdapter mAdapter;



    public TeacherClassesAdapter(Activity act, ArrayList<Res_Class_info> listClassesinfo) {
        this.act = act;
        this.listClassesinfo = listClassesinfo;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classlist_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_Class_info list = listClassesinfo.get(position);
        holder.studname.setText(list.getClass_name());
        holder.studclass.setText(list.getStream_name());

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                getRetro_Call(act, service.deleteClassStream(list.getStream_id(),list.getClass_id(),list.getTeacher_id()),
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
                builder.setMessage("Are you sure you want to delete "+list.getStream_name()+","+list.getClass_name()+" ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //create dialog to add the subjects
                subject_Dialog = new Dialog(act);
                subject_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                subject_Dialog.setCancelable(true);
                subject_Dialog.setCanceledOnTouchOutside(true);
                subject_Dialog.setContentView(R.layout.pop_add_subjects);

                Button buttonSaveSubject = (Button) subject_Dialog.findViewById(R.id.buttonSaveSubject);
                buttonSaveSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        EditText edtSubject = (EditText) subject_Dialog.findViewById(R.id.edtSubject);

                        if (edtSubject.getText().toString().equals("")) {
                            edtSubject.setError("Subject name cannot be empty!");
                            Vibrator vib = (Vibrator) act.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(500);
                        }else{
                                //save
                                getRetro_Call(act, service.saveTeachersSubjects(list.getStream_id(),list.getClass_id(),list.getTeacher_id(),edtSubject.getText().toString()),
                                        false, new API_Calls.OnApiResult() {
                                            @Override
                                            public void onSuccess(retrofit2.Response<Object> objectResponse) {
                                                Result_Subjects mPojo_Subjects = onPojoBuilder(objectResponse,Result_Subjects.class);
                                                ArrayList<Res_Subjects>  subject_list = mPojo_Subjects.getSubjects();
                                                mAdapter = new SubjectsAdapter(act, subject_list);
                                                recyclerView.setAdapter(mAdapter);
                                                mAdapter.notifyDataSetChanged();

                                                TextView  txtselectyoursubjects  = (TextView) subject_Dialog.findViewById(R.id.txtselectyoursubjects);
                                                TextView  txtaddsubjects  = (TextView) subject_Dialog.findViewById(R.id.txtaddsubjects);
                                                if(subject_list.size() <=0){
                                                    txtselectyoursubjects.setText("");
                                                    txtselectyoursubjects.setVisibility(View.GONE);
                                                    txtaddsubjects.setText("ADD A SUBJECT FOR " +list.getStream_name()+ " , "+list.getClass_name());
                                                }else{
                                                    txtselectyoursubjects.setText("SELECT/UNSELECT SUBJECT");
                                                    txtselectyoursubjects.setVisibility(View.VISIBLE);
                                                    txtaddsubjects.setText("OR ADD A SUBJECT FOR " +list.getStream_name()+ " , "+list.getClass_name());
                                                }
                                            }
                                            @Override
                                            public void onFailure(String message) {

                                            }
                                        });
                        }
                    }
                });




                recyclerView = (RecyclerView) subject_Dialog.findViewById(R.id.recycler_subjects);
                listSubjects = new ArrayList<>();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                //fetch and fill adapter
                getRetro_Call(act, service.getTeachersSubjects(list.getStream_id(),list.getClass_id(),list.getTeacher_id()),
                        false, new API_Calls.OnApiResult() {
                            @Override
                            public void onSuccess(retrofit2.Response<Object> objectResponse) {
                                Result_Subjects mPojo_Subjects = onPojoBuilder(objectResponse,Result_Subjects.class);
                                ArrayList<Res_Subjects>  subject_list = mPojo_Subjects.getSubjects();
                               //set to the adapter here
                                mAdapter = new SubjectsAdapter(act, subject_list);
                                recyclerView.setAdapter(mAdapter);
                                //mAdapter.notifyDataSetChanged();
                                TextView  txtselectyoursubjects  = (TextView) subject_Dialog.findViewById(R.id.txtselectyoursubjects);
                                TextView  txtaddsubjects  = (TextView) subject_Dialog.findViewById(R.id.txtaddsubjects);
                                if(subject_list.size() <=0){
                                    txtselectyoursubjects.setText("");
                                    txtselectyoursubjects.setVisibility(View.GONE);
                                    txtaddsubjects.setText("ADD A SUBJECT FOR " +list.getStream_name()+ " , "+list.getClass_name());
                                }else{
                                    txtselectyoursubjects.setText("SELECT/UNSELECT SUBJECT");
                                    txtselectyoursubjects.setVisibility(View.VISIBLE);
                                    txtaddsubjects.setText("OR ADD A SUBJECT FOR " +list.getStream_name()+ " , "+list.getClass_name());
                                }
                            }
                            @Override
                            public void onFailure(String message) {

                            }
                        });
                subject_Dialog.show();



            }
        });






    }

    @Override
    public int getItemCount() {
        return listClassesinfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studname,studclass;
        ImageView iv;
        RecyclerView recycler_subjects;
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
            mAdapter = new SubjectsAdapter(act, listSubjects);

        }
    }

}









