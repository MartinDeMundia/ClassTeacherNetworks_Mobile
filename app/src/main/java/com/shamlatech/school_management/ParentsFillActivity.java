package com.shamlatech.school_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.shamlatech.adapter.ParentStudentsAdapter;
import com.shamlatech.api_response.Res_ParentStudent_List;
import com.shamlatech.api_response.Res_SI_School_Details;
import com.shamlatech.api_response.Result_Forget_Password;
import com.shamlatech.api_response.Result_ParentStudent_List;
import com.shamlatech.api_response.Result_Parent_Student_Details;
import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.services.AchieveMessageService;
import com.shamlatech.services.BGLookupService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.AccessUserInfo;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.UpdateProfileInfo;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ParentsFillActivity extends AppCompatActivity {

    String strNemis = "", strRole = "";
    EditText txtfname , txtlname , txtphone1 , txtphone2 , txtemail , txtrofession,edtPassword;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;

    ParentStudentsAdapter mAdapter;
    ArrayList<Res_ParentStudent_List> listParentStudents = new ArrayList<>();
    Dialog pick_Dialog,spick_Dialog;
    Context mContext;
    public EditText edtNemis,edtSchoolName,edtAddress,edtCounty,edtTelephone,edtEmail,edtWebsite,edtNemisNo,edtFirstName,edtSecondName,edtLastName,edtGender,edtBirthday,edtStudentCode,edtCheckPassword;
    public String strTopic,strParentId = "0" , strNemisNumber = "";
    DBAdapter db;
    ArrayList<String> schools = new ArrayList<String>();
    MaterialAutoCompleteTextView schoolsSelect;
    String eschool, eschooladdress , eschoolcounty , eschooltelephone , eschoolemail , eschoolwebsite;
    ArrayAdapter<String> sadapter;
    String studnemis,studfirstname,studsecondname,studlastname,studgender,studbirthday,studentcode;
    TextView txtLabel1;
    ConstraintLayout recyclerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentsfill);
        mContext = getApplicationContext();
        txtfname = findViewById(R.id.edtFirstname);
        txtlname = findViewById(R.id.edtLastname);
        txtphone1 = findViewById(R.id.edtPhone1);
        txtphone2 = findViewById(R.id.edtPhone2);
        txtemail = findViewById(R.id.edtEmailaddress);
        txtrofession = findViewById(R.id.edtprofession);
        edtPassword =  findViewById(R.id.edtPassword);

        try {
            db = new DBAdapter(this, this.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        PreparePage();

        //strNemis = "0722625532";

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        listParentStudents = new ArrayList<>();
        mAdapter = new ParentStudentsAdapter(ParentsFillActivity.this, listParentStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        txtLabel1 = (TextView)findViewById(R.id.txtLabel1);
        recyclerlayout = (ConstraintLayout) findViewById(R.id.recyclerlayout);



        Button addStudent = findViewById(R.id.buttonAdd);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addStudentNemis();
            }
        });
        Button save = findViewById(R.id.buttonOk);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean Error = false;

                if (txtfname.getText().toString().equals("")) {
                    txtfname.setError("Please enter valid Parent's First Name");
                    Error = true;
                }else if(txtlname.getText().toString().equals("")){
                    txtlname.setError("Please enter a valid Last Name");
                    Error = true;
                }else if(txtphone1.getText().toString().equals("")){
                    txtphone1.setError("Please enter a Guardian Contact (Phone 1)");
                    Error = true;
                }else if(txtphone2.getText().toString().equals("")){
                    txtphone2.setError("Please enter a Guardian Contact (Phone 2)");
                    Error = true;
                }else if(txtemail.getText().toString().equals("")){
                    txtemail.setError("Please enter a valid Email address");
                    Error = true;
                }else if(txtrofession.getText().toString().equals("")){
                    txtrofession.setError("Please enter Guardians profession");
                    Error = true;
                }else if(edtPassword.getText().toString().equals("")){
                    edtPassword.setError("Please enter a valid Password");
                    Error = true;
                }

                if (Error) {
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }else{
                    //action save parents details
                    getRetro_ASaveParentDetails(strNemis,strParentId,txtfname.getText().toString().trim(),txtlname.getText().toString().trim(),txtphone1.getText().toString().trim(),txtphone2.getText().toString().trim(),txtemail.getText().toString().trim(),txtrofession.getText().toString().trim() ,edtPassword.getText().toString().trim());
                }


            }
        });
        fillparentStudent(strNemis);
    }

    private void updateData(ArrayList<Res_ParentStudent_List> parentstudents, Result_ParentStudent_List mPojo_ParentStudentList){

        ArrayList<Res_SI_School_Details> schoolslist = mPojo_ParentStudentList.getSchoolslist();
        for (int i = 0; i < schoolslist.size(); i++) {
            schools.add(schoolslist.get(i).getSchool_name());
        }

        boolean NewList = true;
        if (listParentStudents.size() > 0) {
            NewList = false;
        }
             /*           progress_LoadMore.setVisibility(View.GONE);
                        if (forum.size() >= 20)
                            ListLoading = true;
                        else {
                            ListLoading = false;
                        }*/

        ArrayList<Res_ParentStudent_List> TempNearBy = new ArrayList();
        TempNearBy.addAll(listParentStudents);
        listParentStudents.clear();

        if(parentstudents != null){
            for (int i = 0; i < parentstudents.size(); i++) {
                boolean Exist = false;
                for (int j = 0; j < TempNearBy.size(); j++) {
                    if (parentstudents.get(i).getStudentid().equals(TempNearBy.get(j).getStudentid())) {
                        TempNearBy.set(j, parentstudents.get(i));
                        Exist = true;
                    }
                }
                if (!Exist) {
                    TempNearBy.add(parentstudents.get(i));
                }
            }
            listParentStudents.addAll(TempNearBy);
        }

                       /* if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }*/
        if (NewList) {
            mAdapter = new ParentStudentsAdapter(ParentsFillActivity.this, listParentStudents);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        setformData(mPojo_ParentStudentList);
    }


    private void setformData(Result_ParentStudent_List myobject ) {
        txtfname.setText(myobject.getFirstname());
        txtlname.setText(myobject.getLastname());

        if (myobject.getPhone1().equals("")) {
            txtphone1.setText(strNemis);
        } else {
            txtphone1.setText(myobject.getPhone1());
        }

        txtphone2.setText(myobject.getPhone2());
        txtemail.setText(myobject.getEmail());
        txtrofession.setText(myobject.getOccupation());
        strParentId = myobject.getParentid();
        edtPassword.setText("");

        if(myobject != null) {

        if (!myobject.getPassword().toString().equals("")) {
            final Dialog setPasswordDialog;
            setPasswordDialog = new Dialog(ParentsFillActivity.this);
            setPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setPasswordDialog.setCancelable(false);
            setPasswordDialog.setCanceledOnTouchOutside(false);
            setPasswordDialog.setContentView(R.layout.pop_check_password);
            setPasswordDialog.show();
            edtCheckPassword = setPasswordDialog.findViewById(R.id.edtCheckPassword);
            Button buttonCheckPassword = setPasswordDialog.findViewById(R.id.buttonCheckPassword);
            buttonCheckPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtCheckPassword.getText().toString().equals("")) {
                        edtCheckPassword.setError("Please enter your Password!");
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                    } else {
                        getRetro_Call(ParentsFillActivity.this, service.checkPassword(strNemis, edtCheckPassword.getText().toString()),
                                false, new API_Calls.OnApiResult() {
                                    @Override
                                    public void onSuccess(Response<Object> objectResponse) {
                                        Result_Forget_Password mPojo_Teacher = onPojoBuilder(objectResponse, Result_Forget_Password.class);
                                        if (mPojo_Teacher.getStatus().toString().equals("true")) {
                                            edtPassword.setText(edtCheckPassword.getText().toString());
                                            setPasswordDialog.dismiss();
                                        } else {
                                            edtCheckPassword.setError("Invalid password entered!");
                                            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                            vib.vibrate(500);
                                        }
                                    }

                                    @Override
                                    public void onFailure(String message) {
                                    }
                                });

                    }

                }
            });
        }
    }




    }

    public void fillparentStudent(String studid){
        getRetro_Call(ParentsFillActivity.this, service.getParentStudent(studid),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(retrofit2.Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        ArrayList<Res_ParentStudent_List> parentstudents = mPojo_ParentStudentList.getParentstudentlist();
                        updateData(parentstudents, mPojo_ParentStudentList);
                        if(parentstudents.size() <= 0){
                            txtLabel1.setVisibility(View.GONE);
                            recyclerlayout.setVisibility(View.GONE);
                        }else{
                            txtLabel1.setVisibility(View.VISIBLE);
                            recyclerlayout.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private String addStudentNemis() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(ParentsFillActivity.this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_student_number);


            edtNemisNo = pick_Dialog.findViewById(R.id.edtNemisNo);
            edtFirstName = pick_Dialog.findViewById(R.id.edtFirstName);
            edtSecondName = pick_Dialog.findViewById(R.id.edtSecondName);
            edtLastName = pick_Dialog.findViewById(R.id.edtLastName);
            edtGender = pick_Dialog.findViewById(R.id.edtGender);
            edtBirthday = pick_Dialog.findViewById(R.id.edtBirthday);
            edtStudentCode = pick_Dialog.findViewById(R.id.edtStudentCode);

            schoolsSelect = pick_Dialog.findViewById(R.id.edtSchool);
            sadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , schools);
            schoolsSelect.setAdapter(sadapter);
            final LinearLayout layoutholder = pick_Dialog.findViewById(R.id.linearRegister);
            Button  saveStudent = pick_Dialog.findViewById(R.id.buttonOk);
            saveStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                  final String schoolname = schoolsSelect.getEditableText().toString();
                  if(schools.contains(schoolname)){
                      //do inputs validations
                      //add the student in the students database and link to the parent
                      studnemis = edtNemisNo.getEditableText().toString().trim();
                      studfirstname = edtFirstName.getEditableText().toString().trim();
                      studsecondname = edtSecondName.getEditableText().toString().trim();
                      studlastname = edtLastName.getEditableText().toString().trim();
                      studgender = edtGender.getEditableText().toString().trim();
                      studbirthday = edtBirthday.getEditableText().toString().trim();
                      studentcode = edtStudentCode.getEditableText().toString().trim();

                      boolean Error = false;
                      if (studnemis.equals("")) {
                          edtNemisNo.setError("Please enter a valid NEMIS number");
                          Error = true;
                      }else if(studfirstname.equals("")){
                          edtFirstName.setError("Please enter a valid First Name");
                          Error = true;
                      }else if(studsecondname.equals("")){
                          edtSecondName.setError("Please enter a valid Second Name");
                          Error = true;
                      }else if(studlastname.equals("")){
                          edtLastName.setError("Please enter a valid Last Name");
                          Error = true;
                      }else if(studgender.equals("")){
                          edtGender.setError("Please enter a valid Gender");
                          Error = true;
                      }else if(studbirthday.equals("")){
                          edtBirthday.setError("Please enter a valid Birth Date");
                          Error = true;
                      }else if(studentcode.equals("")){
                          edtStudentCode.setError("Please enter a valid Student Code/No.");
                          Error = true;
                      }

                      if (Error) {
                          Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                          vib.vibrate(500);
                      }else{
                          getRetro_ASaveStudentDetails(strNemis,schoolname,studnemis,studfirstname,studsecondname,studlastname,studgender,studbirthday,studentcode);
                      }

                  }else {
                      if (schoolsSelect.getText().toString().equals("")) {
                          schoolsSelect.setError("Please enter valid School Name");
                          Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                          vib.vibrate(500);
                      }else {

                      Snackbar snackbar = Snackbar
                              .make(layoutholder, "School does not exist in our database!", Snackbar.LENGTH_LONG);
                      snackbar.show();
                      AlertDialog.Builder builder = new AlertDialog.Builder(ParentsFillActivity.this);

                      DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              spick_Dialog = null;
                              switch (which) {
                                  case DialogInterface.BUTTON_POSITIVE:
                                      spick_Dialog = new Dialog(ParentsFillActivity.this);
                                      spick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                      spick_Dialog.setCancelable(true);
                                      spick_Dialog.setCanceledOnTouchOutside(true);
                                      spick_Dialog.setContentView(R.layout.pop_add_school);

                                      edtSchoolName = spick_Dialog.findViewById(R.id.edtSchoolName);
                                      edtAddress = spick_Dialog.findViewById(R.id.edtAddress);
                                      edtCounty = spick_Dialog.findViewById(R.id.edtCounty);
                                      edtTelephone = spick_Dialog.findViewById(R.id.edtTelephone);
                                      edtEmail = spick_Dialog.findViewById(R.id.edtEmail);
                                      edtWebsite = spick_Dialog.findViewById(R.id.edtWebsite);
                                      Button saveSchool = spick_Dialog.findViewById(R.id.buttonSaveSchool);
                                      edtSchoolName.setText(schoolname);
                                      spick_Dialog.show();
                                      saveSchool.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              eschool = edtSchoolName.getEditableText().toString().trim();
                                              eschooladdress = edtAddress.getEditableText().toString().trim();
                                              eschoolcounty = edtCounty.getEditableText().toString().trim();
                                              eschooltelephone = edtTelephone.getEditableText().toString().trim();
                                              eschoolemail = edtEmail.getEditableText().toString().trim();
                                              eschoolwebsite = edtWebsite.getEditableText().toString().trim();

                                              boolean Error = false;
                                              if (eschool.equals("")) {
                                                  edtSchoolName.setError("Please enter School Name");
                                                  Error = true;
                                              } else if (eschooladdress.equals("")) {
                                                  edtAddress.setError("Please enter School Address");
                                                  Error = true;
                                              } else if (eschoolcounty.equals("")) {
                                                  edtCounty.setError("Please enter County where located");
                                                  Error = true;
                                              } else if (eschooltelephone.equals("")) {
                                                  edtTelephone.setError("Please enter School Telephone");
                                                  Error = true;
                                              } else if (eschoolemail.equals("")) {
                                                  edtEmail.setError("Please enter School Email");
                                                  Error = true;
                                              } else if (eschoolwebsite.equals("")) {
                                                  edtWebsite.setError("Please enter School Website (N/A if does not have)");
                                                  Error = true;
                                              }
                                              if (Error) {
                                                  Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                                  vib.vibrate(500);
                                              } else {
                                                  getRetro_ASaveSchoolDetails(eschool, eschooladdress, eschoolcounty, eschooltelephone, eschoolemail, eschoolwebsite);
                                                  spick_Dialog.dismiss();
                                              }

                                          }
                                      });
                                      break;
                                  case DialogInterface.BUTTON_NEGATIVE:
                                      dialog.dismiss();
                                      break;
                              }
                          }
                      };
                      builder.setMessage("Do you wish to add " + schoolname + " in our database?").setPositiveButton("Yes", dialogClickListener)
                              .setNegativeButton("No", dialogClickListener).show();

                  }


                  }

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


    public void getRetro_AddNemisNumber(String nemisno,String nemisno_new){
        getRetro_Call(ParentsFillActivity.this, service.getAddNemis(nemisno, nemisno_new),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        ArrayList<Res_ParentStudent_List> parentstudents = mPojo_ParentStudentList.getParentstudentlist();
                        if(mPojo_ParentStudentList.getStatus() == "1"){
                        }else{
                            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
                            Snackbar snackbar = Snackbar
                                    .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        updateData(parentstudents,mPojo_ParentStudentList);
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_ASaveStudentDetails(String parentsPhone, String schoolname, String studnemis , String studfirstname, String studsecondname , String studlastname,String studgender ,String studbirthday , String studentcode ){
        getRetro_Call(ParentsFillActivity.this, service.getSaveStudentDetails(parentsPhone,schoolname,studnemis,studfirstname,studsecondname,studlastname,studgender,studbirthday,studentcode),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        ArrayList<Res_ParentStudent_List> parentstudents = mPojo_ParentStudentList.getParentstudentlist();
                        if(mPojo_ParentStudentList.getStatus() == "1"){
                        }else{
                            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
                            Snackbar snackbar = Snackbar
                                    .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        updateData(parentstudents,mPojo_ParentStudentList);

                        if(parentstudents.size() <= 0){
                            txtLabel1.setVisibility(View.GONE);
                            recyclerlayout.setVisibility(View.GONE);
                        }else{
                            txtLabel1.setVisibility(View.VISIBLE);
                            recyclerlayout.setVisibility(View.VISIBLE);
                        }
                        pick_Dialog.dismiss();
                    }
                    @Override
                    public void onFailure(String message){
                    }
                });
    }


    public void getRetro_ASaveSchoolDetails(final String school, String schooladdress, String schoolcounty , String schooltelephone, String schoolemail , String schoolwebsite ){
        getRetro_Call(ParentsFillActivity.this, service.getSaveSchoolDetails(school,schooladdress,schoolcounty,schooltelephone,schoolemail,schoolwebsite),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        schools.add(school);
                        sadapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getRetro_ASaveParentDetails(String nemisno, String parentid, String firstname , String lastname, final String phone1 , String phone2, String email, String occupation , final String passwd ){
        getRetro_Call(ParentsFillActivity.this, service.getSaveParentDetails(nemisno,parentid, firstname , lastname,  phone1 ,  phone2,  email, occupation ,passwd),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);

                        if(listParentStudents.size() <= 0){
                            Snackbar snackbar = Snackbar
                                    .make(relativeLayout, "Add your student using the plus button!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            getRetro_Login(phone1,passwd,"phone", "1");
                        }

                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("nemisnumber")) {
                strNemis = bundle.getString("nemisnumber");
                strRole = bundle.getString("role");
            }
        }
    }
    public void getRetro_Login(final String name, final String password, final String source, final String role) {
        getRetro_Call(ParentsFillActivity.this, service.getLogin(name, password, source, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                if (mPojo_Userinfo != null) {
                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
                        UpdateProfileInfo(ParentsFillActivity.this, mPojo_Userinfo.getUser_info());
                        AccessUserInfo(ParentsFillActivity.this);

                        final String user_id = staUserInfo.getId();
                        final String role_id = staUserInfo.getRole();

                            getRetro_Call(ParentsFillActivity.this, service.getAccessParentDetails(user_id, role_id), false, new API_Calls.OnApiResult() {
                                @Override
                                public void onSuccess(Response<Object> objectResponse) {
                                    Result_Parent_Student_Details mPojo_ParentInfo = onPojoBuilder(objectResponse, Result_Parent_Student_Details.class);
                                    if (mPojo_ParentInfo != null) {
                                        if (mPojo_ParentInfo.getStatus().equals(API_Code.Success)) {
                                            db.ClearTable("parent_student");
                                            db.InsertParentStudent(mPojo_ParentInfo.getStudents());
                                            RedirectUser();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(String message) {
                                }
                            });


                    } else {
                        ShowError(ParentsFillActivity.this, "Error", mPojo_Userinfo.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(String message) {

            }
        });
    }
    public void RedirectUser() {
        startService(new Intent(ParentsFillActivity.this, AchieveMessageService.class));
        getRetro_AccessParentDetails(staUserInfo.getId(), staUserInfo.getRole());
    }
    public void getRetro_AccessParentDetails(final String user_id, String role_id) {
        getRetro_Call(ParentsFillActivity.this, service.getAccessParentDetails(user_id, role_id), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Parent_Student_Details mPojo_ParentInfo = onPojoBuilder(objectResponse, Result_Parent_Student_Details.class);
                if (mPojo_ParentInfo != null) {
                    if (mPojo_ParentInfo.getStatus().equals(API_Code.Success)) {

                        db.ClearTable("parent_student");
                        db.InsertParentStudent(mPojo_ParentInfo.getStudents());

                        if (mPojo_ParentInfo.getExpired().equals('1')) {

                            Intent in = new Intent(ParentsFillActivity.this, MPESAExpressActivity.class);
                            in.putExtra("id", user_id);
                            startActivity(in);

                        }else {
                            Intent in = new Intent(ParentsFillActivity.this, HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(ParentsFillActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

}





