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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.shamlatech.adapter.TeacherClassesAdapter;
import com.shamlatech.api_response.Res_Class_info;
import com.shamlatech.api_response.Res_ParentStudent_List;
import com.shamlatech.api_response.Res_SI_School_Details;
import com.shamlatech.api_response.Result_Classes;
import com.shamlatech.api_response.Result_Forget_Password;
import com.shamlatech.api_response.Result_ParentStudent_List;
import com.shamlatech.api_response.Result_Parent_Student_Details;
import com.shamlatech.api_response.Result_TeacherInfo;
import com.shamlatech.api_response.Result_Teacher_Info;
import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.services.AchieveMessageService;
import com.shamlatech.services.BGLookupService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.AccessUserInfo;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.UpdateProfileInfo;
import static com.shamlatech.utils.Vars.staUserInfo;

public class TeachersFillActivity extends AppCompatActivity {

    String strNemis = "", strRole = "";

    private ProgressDialog pDialog;
    private RecyclerView recyclerView;

    TeacherClassesAdapter mAdapter;
    ArrayList<Res_Class_info> listClasses = new ArrayList<>();
    Dialog pick_Dialog,spick_Dialog;
    Context mContext;

    public EditText edtFirstname,edtMiddlename,edtLastname,edtTscNumber,edtGender,edtEmailaddress,edtPassword,edtTeachersCode,edtCheckPassword;
    EditText eSchools,eAddres,eCountry,eTelephone,eEmail,eWebsite;
    RelativeLayout relativeLayout;
    MaterialAutoCompleteTextView edtSchoolName;
    public String strTopic,strParentId = "0" , strNemisNumber = "";
    DBAdapter db;

    ArrayList<String> schools= new ArrayList<String>();
    ArrayList<String> streams  = new ArrayList<String>();
    ArrayList<String> classes = new ArrayList<String>();

    MaterialAutoCompleteTextView schoolsSelect;
    String eschool, eschooladdress , eschoolcounty , eschooltelephone , eschoolemail , eschoolwebsite;
    ArrayAdapter<String> sadapter,streamadapter,classadapter;
    String cStrStream , cStrClass;
    MaterialAutoCompleteTextView edtcSchool,edtcStream ,edtcClass ;
    String accounttype = "teacher";
    RelativeLayout lineallay;
    ConstraintLayout recyclerconstraintlayout;
    TextView txtLabel2,txtLabel1;
    RadioButton radioTeacher , radioPrincipal;
    RadioButton radioMale , radioFemale;
    String gendertype = "Male";
    Spinner title;
    String[] titlevalues = { "Mr.", "Miss.", "Mrs." , "Dr."};

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioTeacher:
                if (checked)
                    accounttype = "teacher";
                    txtLabel2.setText("Fill in Teachers Details");
                    lineallay.setVisibility(View.VISIBLE);
                    recyclerconstraintlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.radioPrincipal:
                if (checked)
                    accounttype = "principal";
                    txtLabel2.setText("Fill in School Principals Details");
                    lineallay.setVisibility(View.GONE);
                    recyclerconstraintlayout.setVisibility(View.GONE);
                break;

            case R.id.radioMale:
                if (checked)
                    gendertype = "Male";
                break;
            case R.id.radioFemale:
                if (checked)
                    gendertype = "Female";
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachersfill);
        mContext = getApplicationContext();

       //////// edtInitial = findViewById(R.id.edtInitial);
        title = (Spinner)findViewById(R.id.edtInitial);


       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               // R.array.title_array, android.R.layout.simple_spinner_item);

        final List<String> initialList = new ArrayList<>(Arrays.asList(titlevalues));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_list,initialList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(spinnerArrayAdapter);

        edtFirstname = findViewById(R.id.edtFirstname);
        edtMiddlename = findViewById(R.id.edtMiddlename);
        edtLastname = findViewById(R.id.edtLastname);
        edtTscNumber = findViewById(R.id.edtTscNumber);
        edtEmailaddress = findViewById(R.id.edtteachersEmail);
        //edtGender = findViewById(R.id.edtGender);
        edtPassword = findViewById(R.id.edtPassword);
        edtTeachersCode = findViewById(R.id.edtTeachersCode);
        edtSchoolName = findViewById(R.id.edtSchool);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);

        lineallay = findViewById(R.id.lineallay);
        recyclerconstraintlayout = findViewById(R.id.recyclerconstraintlayout);
        txtLabel2 = findViewById(R.id.txtLabel2);
        txtLabel1 = findViewById(R.id.txtLabel1);

        radioTeacher = findViewById(R.id.radioTeacher);
        radioPrincipal = findViewById(R.id.radioPrincipal);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        try {
            db = new DBAdapter(this, this.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }
        PreparePage();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        listClasses = new ArrayList<>();

        mAdapter = new TeacherClassesAdapter(TeachersFillActivity.this, listClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        //strNemis = "0725000000";

        Button addStudent = findViewById(R.id.buttonAdd);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String schoolname = edtSchoolName.getEditableText().toString();
               //fill drop downs stream and classes
                getRetro_Call(TeachersFillActivity.this, service.getSchoolInfo(schoolname),
                        false, new API_Calls.OnApiResult() {
                            @Override
                            public void onSuccess(retrofit2.Response<Object> objectResponse) {
                                Result_Classes mPojo_Classes = onPojoBuilder(objectResponse, Result_Classes.class);
                                addTeachersClasses(mPojo_Classes);
                            }
                            @Override
                            public void onFailure(String message) {

                            }
                        });

            }
        });
        Button save = findViewById(R.id.buttonOk);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action save teachers details
                final String schoolname = edtSchoolName.getEditableText().toString();

                if(schools.contains(schoolname)) {
                    boolean Error = false;

                    if (title.getSelectedItem().toString().equals("")) {
                        //title.setError("Please enter valid Title (Mr./Mrs./Miss/Dr./Prof.)");
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "Please enter valid Title (Mr./Mrs./Miss/Dr./Prof.)", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Error = true;
                    }else if(edtFirstname.getText().toString().equals("")){
                        edtFirstname.setError("Please enter valid First Name");
                        Error = true;
                    }else if(edtMiddlename.getText().toString().equals("")){
                        edtMiddlename.setError("Please enter valid Middle Name");
                        Error = true;
                    }else if(edtLastname.getText().toString().equals("")){
                        edtLastname.setError("Please enter valid Last Name");
                        Error = true;
                    }else if(edtTscNumber.getText().toString().equals("")){
                        edtTscNumber.setError("Please enter valid TSC Number");
                        Error = true;
                    }else if(edtEmailaddress.getText().toString().equals("")){
                        edtEmailaddress.setError("Please enter valid Email address");
                        Error = true;
                    }else if(edtPassword.getText().toString().equals("")){
                        edtPassword.setError("Please enter valid Password");
                        Error = true;
                    }else if(edtTeachersCode.getText().toString().equals("")){
                        edtTeachersCode.setError("Please enter valid Teachers Code");
                        Error = true;
                    }else if(edtSchoolName.getText().toString().equals("")){
                        edtSchoolName.setError("Please enter valid School Name");
                        Error = true;
                    }
                    if (Error) {
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                    }else{
                        //asdad
                       getRetro_ASaveTeacherDetails(accounttype,strNemis,title.getSelectedItem().toString().trim(),edtFirstname.getText().toString().trim(),edtMiddlename.getText().toString().trim(),edtLastname.getText().toString().trim(),edtTscNumber.getText().toString().trim(),gendertype.toString().trim(),edtEmailaddress.getText().toString().trim(),edtPassword.getText().toString().trim(),edtTeachersCode.getText().toString().trim(),schoolname);

                    }

                }else{

                    if (edtSchoolName.getText().toString().equals("")) {
                        edtSchoolName.setError("Please enter valid School Name");
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);

                    }else {

                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "School does not exist in our database!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(TeachersFillActivity.this);

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spick_Dialog = null;
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        spick_Dialog = new Dialog(TeachersFillActivity.this);
                                        spick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        spick_Dialog.setCancelable(true);
                                        spick_Dialog.setCanceledOnTouchOutside(true);
                                        spick_Dialog.setContentView(R.layout.pop_add_school);

                                        eSchools = spick_Dialog.findViewById(R.id.edtSchoolName);
                                        eAddres = spick_Dialog.findViewById(R.id.edtAddress);
                                        eCountry = spick_Dialog.findViewById(R.id.edtCounty);
                                        eTelephone = spick_Dialog.findViewById(R.id.edtTelephone);
                                        eEmail = spick_Dialog.findViewById(R.id.edtEmail);
                                        eWebsite = spick_Dialog.findViewById(R.id.edtWebsite);
                                        Button saveSchool = spick_Dialog.findViewById(R.id.buttonSaveSchool);
                                        eSchools.setText(schoolname);
                                        spick_Dialog.show();
                                        saveSchool.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                eschool = eSchools.getEditableText().toString().trim();
                                                eschooladdress = eAddres.getEditableText().toString().trim();
                                                eschoolcounty = eCountry.getEditableText().toString().trim();
                                                eschooltelephone = eTelephone.getEditableText().toString().trim();
                                                eschoolemail = eEmail.getEditableText().toString().trim();
                                                eschoolwebsite = eWebsite.getEditableText().toString().trim();

                                                boolean Error = false;
                                                if (eschool.equals("")) {
                                                    edtSchoolName.setError("Please enter School Name");
                                                    Error = true;
                                                }else if(eschooladdress.equals("")){
                                                    eAddres.setError("Please enter School Address");
                                                    Error = true;
                                                }else if(eschoolcounty.equals("")){
                                                    eCountry.setError("Please enter County where located");
                                                    Error = true;
                                                }else if(eschooltelephone.equals("")){
                                                    eTelephone.setError("Please enter School Telephone");
                                                    Error = true;
                                                }else if(eschoolemail.equals("")){
                                                    eEmail.setError("Please enter School Email");
                                                    Error = true;
                                                }else if(eschoolwebsite.equals("")){
                                                    eWebsite.setError("Please enter School Website (N/A if does not have)");
                                                    Error = true;
                                                }
                                                if (Error) {
                                                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                                    vib.vibrate(500);
                                                }else{
                                                    getRetro_ASaveSchoolDetails(eschool, eschooladdress , eschoolcounty , eschooltelephone , eschoolemail , eschoolwebsite);
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

        fillTeachersInfo(strNemis);
    }

    private void updateData(ArrayList<Res_Class_info> teachers_classes, Result_Teacher_Info mPojo_Teacher){
        ArrayList<Res_SI_School_Details> schoolslist = mPojo_Teacher.getSchoolslist();
        for (int i = 0; i < schoolslist.size(); i++) {
            schools.add(schoolslist.get(i).getSchool_name());
        }
        boolean NewList = true;
        if (listClasses.size() > 0) {
            NewList = false;
        }
        listClasses.clear();
        if(teachers_classes != null){
            listClasses.addAll(teachers_classes);
        }
        if (NewList) {
            mAdapter = new TeacherClassesAdapter(TeachersFillActivity.this, listClasses);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
       setformData(mPojo_Teacher);
    }

    private void setformData(Result_Teacher_Info myobject ){

           int pos = new ArrayList<String>(Arrays.asList(titlevalues)).indexOf(myobject.getInitial());

           if(pos > 0) title.setSelection(pos);

            edtFirstname.setText(myobject.getName());
            edtMiddlename.setText(myobject.getMiddle_name());
            edtLastname.setText(myobject.getLast_name());
            edtTscNumber.setText(myobject.getTsc_number());
            //edtGender.setText(myobject.getSex());
            edtEmailaddress.setText(myobject.getEmail());
            edtPassword.setText("");
            edtTeachersCode.setText(myobject.getTeacherscode());
            strParentId = myobject.getTeacher_id();
            edtSchoolName.setText(myobject.getSchool_name());
            sadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,schools);
            edtSchoolName.setAdapter(sadapter);

        if(myobject != null) {
            if (!myobject.getPassword().toString().equals("")) {
                final Dialog setPasswordDialog;
                setPasswordDialog = new Dialog(TeachersFillActivity.this);
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
                            getRetro_Call(TeachersFillActivity.this, service.checkPassword(strNemis, edtCheckPassword.getText().toString()),
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

        if(myobject != null) {

            if (myobject.getTeacherscode().equals("principal")) {
                accounttype = "principal";
                radioPrincipal.setChecked(true);
                radioTeacher.setChecked(false);
                lineallay.setVisibility(View.GONE);
                recyclerconstraintlayout.setVisibility(View.GONE);
                txtLabel2.setText("FILL IN PRINCIPAL DETAILS");

            } else {
                accounttype = "teacher";
                radioPrincipal.setChecked(false);
                radioTeacher.setChecked(true);
                lineallay.setVisibility(View.VISIBLE);
                recyclerconstraintlayout.setVisibility(View.VISIBLE);
                //txtLabel1.setVisibility(View.GONE);
                txtLabel2.setVisibility(View.VISIBLE);

            }

            if (myobject.getSex().equals("Male")){
                radioMale.setChecked(true);
                radioFemale.setChecked(false);
                gendertype = "Male";
            }else if (myobject.getSex().equals("Female")){
                radioMale.setChecked(false);
                radioFemale.setChecked(true);
                gendertype = "Female";
            }else{
                radioMale.setChecked(false);
                radioFemale.setChecked(false);
            }

        }


    }

    private  void fillTeachersInfo(String phonenumber){
        getRetro_Call(TeachersFillActivity.this, service.getTeachersInfo(phonenumber),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(retrofit2.Response<Object> objectResponse) {
                        Result_Teacher_Info mPojo_Teacher = onPojoBuilder(objectResponse, Result_Teacher_Info.class);
                        ArrayList<Res_Class_info>  teachersclasses = mPojo_Teacher.getClasslist();
                        if(teachersclasses.size() <=0){
                            lineallay.setVisibility(View.VISIBLE);
                            recyclerconstraintlayout.setVisibility(View.GONE);
                            txtLabel1.setVisibility(View.GONE);
                            txtLabel2.setVisibility(View.VISIBLE);
                        }else{
                            lineallay.setVisibility(View.VISIBLE);
                            recyclerconstraintlayout.setVisibility(View.VISIBLE);
                        }
                        updateData(teachersclasses,mPojo_Teacher);
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private String addTeachersClasses(Result_Classes mPojo_Classes) {
        pick_Dialog = null;
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(TeachersFillActivity.this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_class_stream);


           edtcSchool = pick_Dialog.findViewById(R.id.edtcSchool);
           edtcStream = pick_Dialog.findViewById(R.id.edtcStream);
           edtcClass = pick_Dialog.findViewById(R.id.edtcClass);

           sadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , schools);
           edtcSchool.setAdapter(sadapter);
           edtcStream.requestFocus();

           ArrayList<Res_Class_info> streamlist = mPojo_Classes.getStreamlist();
           for (int i = 0; i < streamlist.size(); i++) {
               streams.add(streamlist.get(i).getClass_name());
           }
           streamadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,streams);
           edtcStream.setAdapter(streamadapter);

           ArrayList<Res_Class_info> classlist = mPojo_Classes.getClasslist();
           for (int i = 0; i < classlist.size(); i++) {
               classes.add(classlist.get(i).getClass_name());
           }
           classadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,classes);
           edtcClass.setAdapter(classadapter);

           final LinearLayout layoutholder = pick_Dialog.findViewById(R.id.linearRegister);
           Button  saveTeachersClass  = pick_Dialog.findViewById(R.id.buttonOk);
           edtcSchool.setText(edtSchoolName.getEditableText().toString());
            saveTeachersClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    final String schoolname = edtcSchool.getEditableText().toString();
                    if(schools.contains(schoolname)){
                         //assign the class to the teacher
                        cStrStream = edtcStream.getEditableText().toString().trim();
                        cStrClass = edtcClass.getEditableText().toString().trim();

                        boolean Error = false;
                        if (cStrStream.equals("")) {
                            edtcStream.setError("Please enter a valid Stream name");
                            Error = true;
                        }else if(cStrClass.equals("")){
                            edtcClass.setError("Please enter a valid Class name");
                            Error = true;
                        }
                        if (Error) {
                            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(500);
                        }else{
                            getRetro_ASaveClassDetails(strNemis,schoolname,cStrStream,cStrClass);
                        }

                    }else{
                        Snackbar snackbar = Snackbar
                                .make(layoutholder,"School does not exist in our database!", Snackbar.LENGTH_LONG);
                        snackbar.show();
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

    public void getRetro_ASaveClassDetails(String phone, String schoolname, String stream , String classname ){
        getRetro_Call(TeachersFillActivity.this, service.getSaveClasses(phone,schoolname,stream,classname),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Teacher_Info mPojo_Teacher = onPojoBuilder(objectResponse, Result_Teacher_Info.class);
                        ArrayList<Res_Class_info>  teachersclasses = mPojo_Teacher.getClasslist();
                        updateData(teachersclasses,mPojo_Teacher);
                        pick_Dialog.dismiss();
                    }
                    @Override
                    public void onFailure(String message){
                    }
                });
    }

    public void getRetro_ASaveSchoolDetails(final String school, String schooladdress, String schoolcounty , String schooltelephone, String schoolemail , String schoolwebsite ){
        getRetro_Call(TeachersFillActivity.this, service.getSaveSchoolDetails(school,schooladdress,schoolcounty,schooltelephone,schoolemail,schoolwebsite),
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


    public void getRetro_ASaveTeacherDetails(final String accounttype, final String phonenumber, String initial , String firstname, final String middlename , String lastname, String tscnumber, String gender , String emailaddress , final String passwd , String teacherscode , String schoolname ){
        getRetro_Call(TeachersFillActivity.this, service.getSaveTeacherDetails(accounttype,phonenumber,initial,firstname,middlename,lastname,tscnumber,gender,emailaddress,passwd,teacherscode,schoolname),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_ParentStudent_List mPojo_ParentStudentList = onPojoBuilder(objectResponse, Result_ParentStudent_List.class);
                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);

                        if(listClasses.size() <= 0){
                                if(accounttype.toString().equals("teacher")){
                                    Snackbar snackbar = Snackbar
                                            .make(relativeLayout, "Add your class and stream using the plus button!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    getRetro_Login(phonenumber,passwd,"phone", "2");
                                }

                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(relativeLayout, mPojo_ParentStudentList.getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            getRetro_Login(phonenumber,passwd,"phone", "2");
                        }

                    }
                    @Override
                    public void onFailure(String message){
                    }
                });
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("phonenumber")) {
                strNemis = bundle.getString("phonenumber");
                strRole = bundle.getString("role");
            }
        }
    }
    public void getRetro_Login(final String name, final String password, final String source, final String role) {
        getRetro_Call(TeachersFillActivity.this, service.getLogin(name, password, source, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                if (mPojo_Userinfo != null) {
                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
                        UpdateProfileInfo(TeachersFillActivity.this, mPojo_Userinfo.getUser_info());
                        AccessUserInfo(TeachersFillActivity.this);

                       final String user_id = staUserInfo.getId();
                       final String role_id = staUserInfo.getRole();

                        getRetro_Call(TeachersFillActivity.this, service.getAccessTeachersDetails(user_id, role_id), false, new API_Calls.OnApiResult() {
                            @Override
                            public void onSuccess(Response<Object> objectResponse) {
                                Result_TeacherInfo mPojo_TeacherInfo = onPojoBuilder(objectResponse, Result_TeacherInfo.class);
                                if (mPojo_TeacherInfo != null) {
                                    if (mPojo_TeacherInfo.getStatus().equals(API_Code.Success)) {
                                        db.ClearTable("teachers_class");
                                        db.ClearTable("teachers_student");
                                        db.ClearTable("teachers_timetable");
                                        db.ClearTable("student_marks");
                                        db.ClearTable("sub_subject_marks");
                                        db.InsertTeachersTimeTable(mPojo_TeacherInfo.getTimetable());
                                        db.InsertTeachersTeachingSubject(mPojo_TeacherInfo.getTeaching_subjects());
                                        db.InsertTeachersClass(mPojo_TeacherInfo.getMy_class());
                                        ////db.InsertTeachersClass(mPojo_TeacherInfo.getSubject_class());
                                        db.InsertStudentMarks(mPojo_TeacherInfo.getStudentMarks(),user_id,role_id);
                                        db.InsertStudentPaperMarks(mPojo_TeacherInfo.getStudentpapermarks(),user_id,role_id);
                                        RedirectUser();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(String message) {
                                Log.e("Error", message);
                            }
                        });



                    } else {
                        ShowError(TeachersFillActivity.this, "Error", mPojo_Userinfo.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(String message) {

            }
        });
    }
    public void RedirectUser() {
        startService(new Intent(TeachersFillActivity.this, AchieveMessageService.class));
        getRetro_AccessParentDetails(staUserInfo.getId(), staUserInfo.getRole());
    }
    public void getRetro_AccessParentDetails(final String user_id, String role_id) {
        getRetro_Call(TeachersFillActivity.this, service.getAccessParentDetails(user_id, role_id), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Parent_Student_Details mPojo_ParentInfo = onPojoBuilder(objectResponse, Result_Parent_Student_Details.class);
                if (mPojo_ParentInfo != null) {
                    if (mPojo_ParentInfo.getStatus().equals(API_Code.Success)) {

                        db.ClearTable("parent_student");
                        db.InsertParentStudent(mPojo_ParentInfo.getStudents());

                        if (mPojo_ParentInfo.getExpired().equals('1')) {

                            Intent in = new Intent(TeachersFillActivity.this, MPESAExpressActivity.class);
                            in.putExtra("id", user_id);
                            startActivity(in);

                        }else {
                            Intent in = new Intent(TeachersFillActivity.this, HomeActivity.class);
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
        pDialog = new ProgressDialog(TeachersFillActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

}





