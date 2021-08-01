package com.shamlatech.fragment;

        import android.app.Activity;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.res.ColorStateList;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.NestedScrollView;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.text.InputType;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.prolificinteractive.materialcalendarview.CalendarDay;
        import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
        import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
        import com.shamlatech.adapter.ClassPositionAdapter;
        import com.shamlatech.adapter.JournalsAdapter;
        import com.shamlatech.api_response.Res_Stud_Attendance;
        import com.shamlatech.api_response.Res_Stud_Journal;
        import com.shamlatech.api_response.Result_AbsentNote_List;
        import com.shamlatech.api_response.Result_Stud_Journal;
        import com.shamlatech.pojo.PojoParentWithClassList;
        import com.shamlatech.school_management.R;
        import com.shamlatech.utils.API_Calls;
        import com.shamlatech.utils.API_Code;
        import com.shamlatech.utils.Vars;
        import com.vanniktech.emoji.EmojiImageView;
        import com.vanniktech.emoji.EmojiManager;
        import com.vanniktech.emoji.EmojiPopup;
        import com.vanniktech.emoji.emoji.Emoji;
        import com.vanniktech.emoji.google.GoogleEmojiProvider;
        import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
        import com.vanniktech.emoji.listeners.OnEmojiClickListener;
        import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
        import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
        import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
        import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;
        import com.wang.avi.AVLoadingIndicatorView;

        import java.io.Serializable;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;

        import retrofit2.Response;

        import static com.shamlatech.utils.API_Calls.getRetro_Call;
        import static com.shamlatech.utils.API_Calls.onPojoBuilder;
        import static com.shamlatech.utils.API_Calls.service;
        import static com.shamlatech.utils.Support.ConvertToFloat;
        import static com.shamlatech.utils.Support.ConvertToInteger;
        import static com.shamlatech.utils.Support.FormatDateForShow;
        import static com.shamlatech.utils.Support.PrepareSeatForStudent;
        import static com.shamlatech.utils.Vars.Refresh_Forum_T_P;
        import static com.shamlatech.utils.Vars.Refresh_Forum_T_T;
        import static com.shamlatech.utils.Vars.Role_Parent;
        import static com.shamlatech.utils.Vars.Role_Principal;
        import static com.shamlatech.utils.Vars.Role_Teacher;
        import static com.shamlatech.utils.Vars.staStudentInfo;
        import static com.shamlatech.utils.Vars.staUserInfo;

         /**
         * Created by Martin Mundia Mugambi on 11-03-2020.
         */
public class SJournalFrag extends Fragment implements View.OnClickListener {

    View view;
    Activity activity;
    Dialog pick_Dialog;
    FloatingActionButton fabEditAttendance;
    EmojiPopup emojiPopup;
    String StudId = "0";
    NestedScrollView scrollContent;
    View InnerProgress;
    Res_Stud_Journal studJournalInfo;
    RecyclerView recyclerJournals;
    ArrayList<Res_Stud_Journal> listJournals = new ArrayList<>();
    JournalsAdapter mAdapter;
    MaterialCalendarView calendarJournal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_journal, container, false);
        fabEditAttendance = view.findViewById(R.id.fabEditAttendance);
        fabEditAttendance.setOnClickListener(this);
        recyclerJournals = view.findViewById(R.id.recyclerJournals);

        listJournals = new ArrayList<>();

        mAdapter = new JournalsAdapter(activity, listJournals , this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerJournals.setLayoutManager(mLayoutManager);
        recyclerJournals.setItemAnimator(new DefaultItemAnimator());
        recyclerJournals.setAdapter(mAdapter);

        calendarJournal = view.findViewById(R.id.calendarJournal);

        calendarJournal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                getStudentJournal(date.getDate(),StudId);
            }
        });

        prepareBundle();
        return view;
    }

    public void getStudentJournal(Date jDate , String StudentId){
        listJournals.clear();
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String dateAddedISO = sdf.format(jDate);
        String dateAdded = DateFormat.getDateInstance().format(jDate);

        getRetro_Call(activity, service.getJournalListDate(dateAddedISO, StudentId),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Journal mPojo_Journals = onPojoBuilder(objectResponse, Result_Stud_Journal.class);
                        if (mPojo_Journals != null) {
                            if (mPojo_Journals.getStatus().equals(API_Code.Success)) {
                                listJournals.addAll(mPojo_Journals.getJournals());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onFailure(String message){
                    }
                });

    }

     public void InitializeProgress(View view) {
         scrollContent = view.findViewById(R.id.scrollContent);
         InnerProgress = view.findViewById(R.id.ProgressInner);
         InnerProgress.setVisibility(View.VISIBLE);
         scrollContent.setVisibility(View.INVISIBLE);
         String indicator = activity.getIntent().getStringExtra("indicator");
         AVLoadingIndicatorView avi = view.findViewById(R.id.avi);
         avi.setIndicator(indicator);
         avi.show();
     }
     public void showProgress() {
                 InitializeProgress(view);
     }

    public void prepareBundle() {
                 Bundle bundle = getArguments();
                 if (bundle != null) {
                     if (bundle.containsKey("stud_id")) {
                         StudId = bundle.getString("stud_id");
                     }
                 }
    }


         private void PrepareLoadData(Serializable jList) {
             InnerProgress.setVisibility(View.GONE);
             scrollContent.setVisibility(View.VISIBLE);
             ArrayList tmplist =   (ArrayList<Res_Stud_Journal>) jList;
             listJournals.addAll(tmplist);
             mAdapter.notifyDataSetChanged();

         }

             public void loadData(Bundle bundle) {
                 if (bundle != null) {
                     if (bundle.containsKey("stud_id")) {
                         StudId = bundle.getString("stud_id");
                         if (bundle.getSerializable("journal_info") != null) {
                             PrepareLoadData(bundle.getSerializable("journal_info"));
                         }
                     }
                 }
             }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabEditAttendance:
                showCreateJournal();
                break;
        }
    }

    private void showCreateJournal() {
        if (pick_Dialog == null) {
            // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
            EmojiManager.install(new GoogleEmojiProvider());

            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_create_journal);

            final EditText edtTopic;
            final DatePicker edtAdatePick;
            RelativeLayout relativeSend;
            final Spinner spinnerViewers;
            RelativeLayout relativeTo;

            edtTopic = pick_Dialog.findViewById(R.id.edtTopic);
            edtAdatePick = pick_Dialog.findViewById(R.id.edtAdate);

            relativeSend = pick_Dialog.findViewById(R.id.relativeSend);
            spinnerViewers = pick_Dialog.findViewById(R.id.spinnerViewers);
            relativeTo = pick_Dialog.findViewById(R.id.mainLayout);

            final RadioGroup EmojiRadioGroup;

            EmojiRadioGroup = pick_Dialog.findViewById(R.id.radiogroup);
            edtTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            EmojiRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioEmojiButton;
                    radioEmojiButton = (RadioButton) EmojiRadioGroup.findViewById(checkedId);
                    int count = EmojiRadioGroup.getChildCount();
                    ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
                    for (int i=0;i<count;i++) {
                        View o = EmojiRadioGroup.getChildAt(i);
                        if (o instanceof RadioButton) {
                            RadioButton tmpRadio =  (RadioButton)o;
                            if( tmpRadio.getText().toString().trim().equals(radioEmojiButton.getText().toString().trim())){
                                radioEmojiButton.setBackgroundResource(R.drawable.button_green);
                            }else{
                                tmpRadio.setBackgroundResource(R.drawable.flag_transparent);
                            }
                        }
                    }
                }
            });



            relativeSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioEmojiButton;
                    int selectedId = EmojiRadioGroup.getCheckedRadioButtonId();
                    radioEmojiButton = (RadioButton) EmojiRadioGroup.findViewById(selectedId);

                    String explanation = edtTopic.getText().toString().trim();
                    String journal_date =   edtAdatePick.getYear()+ "-" + (edtAdatePick.getMonth() + 1) + "-"+ (edtAdatePick.getDayOfMonth());
                    String user_id = staUserInfo.getId() ;
                    String role_id =  staUserInfo.getRole();
                    String emoji = radioEmojiButton.getText().toString();

                    getRetro_Call(activity, service.getCreateJournal(user_id, role_id,emoji, explanation, journal_date,StudId),
                            false, new API_Calls.OnApiResult() {
                                @Override
                                public void onSuccess(Response<Object> objectResponse) {
                                    Toast.makeText(activity, "Journal Saved!", Toast.LENGTH_LONG).show();
                                    listJournals.clear();
                                    Result_Stud_Journal mPojo_Journals = onPojoBuilder(objectResponse, Result_Stud_Journal.class);
                                    if (mPojo_Journals != null) {
                                        if (mPojo_Journals.getStatus().equals(API_Code.Success)) {
                                            listJournals.addAll(mPojo_Journals.getJournals());
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    pick_Dialog.dismiss();
                                }
                                @Override
                                public void onFailure(String message) {

                                }
                            });
                    Toast.makeText(activity, "Sending Response...Please wait..", Toast.LENGTH_LONG).show();
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showCreateJournal();
        } else {
            pick_Dialog = null;
            showCreateJournal();
        }
    }


}

