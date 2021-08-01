package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.shamlatech.activity.parent.ParentsDashboard;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;

import java.util.ArrayList;

/**
 * Created by SUN on 30-03-2017.
 */

public class MyChildrenAdapter extends PagerAdapter {

    public ImageView imgStudent, imgSchoolLogo, imgRightArrow, imgLeftArrow, imgCallToClassTeacher, imgMessageToClassTeacher, imgMailToClassTeacher;
    public TextView txtStudentName, txtAdmissionNumber, txtSchoolName, txtGrade, txtAvgGrade, txtAvgPosition, txtBehaviourStatus, txtClassTeacherName;
    public SimpleRatingBar rattingOverAllBehaviour;
    RelativeLayout relativeClassTeacher;
    ArrayList<Res_Student_Info> listChildren = new ArrayList<>();
    Activity mContext;
    ParentsDashboard parentsDashboard;

    LayoutInflater mLayoutInflater;


    public MyChildrenAdapter(Activity activity, ArrayList<Res_Student_Info> listChildren, ParentsDashboard parentsDashboard) {
        mContext = activity;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listChildren = listChildren;
        this.parentsDashboard = parentsDashboard;
    }

    @Override
    public int getCount() {
        return listChildren.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.list_my_children, container, false);

        txtAdmissionNumber = itemView.findViewById(R.id.txtAdmissionNumber);
        imgStudent = itemView.findViewById(R.id.imgStudent);
        imgSchoolLogo = itemView.findViewById(R.id.imgSchoolLogo);
        imgRightArrow = itemView.findViewById(R.id.imgRightArrow);
        imgLeftArrow = itemView.findViewById(R.id.imgLeftArrow);
        imgCallToClassTeacher = itemView.findViewById(R.id.imgCallToClassTeacher);
        imgMessageToClassTeacher = itemView.findViewById(R.id.imgMessageToClassTeacher);
        imgMailToClassTeacher = itemView.findViewById(R.id.imgMailToClassTeacher);

        relativeClassTeacher = itemView.findViewById(R.id.relativeClassTeacher);

        txtStudentName = itemView.findViewById(R.id.txtStudentName);
        txtSchoolName = itemView.findViewById(R.id.txtSchoolName);
        txtGrade = itemView.findViewById(R.id.txtGrade);
        txtAvgGrade = itemView.findViewById(R.id.txtAvgGrade);
        txtAvgPosition = itemView.findViewById(R.id.txtAvgPosition);
        txtBehaviourStatus = itemView.findViewById(R.id.txtBehaviourStatus);
        rattingOverAllBehaviour = itemView.findViewById(R.id.rattingOverAllBehaviour);
        txtClassTeacherName = itemView.findViewById(R.id.txtClassTeacherName);

        txtAdmissionNumber.setText("Admission Number: " + listChildren.get(position).getAdmission_number());
        txtStudentName.setText(listChildren.get(position).getFirst_name());
        txtSchoolName.setText(listChildren.get(position).getSchool_details().getSchool_name());
        txtGrade.setText(listChildren.get(position).getClass_details().getClass_name() + " " +
                listChildren.get(position).getClass_details().getSection_name());
        if (listChildren.get(position).getAvg_grade() != null && !listChildren.get(position).getAvg_grade().isEmpty())
            txtAvgGrade.setText(listChildren.get(position).getAvg_grade());
        else
            txtAvgGrade.setText("-");
        if (listChildren.get(position).getAvg_position() != null && !listChildren.get(position).getAvg_position().isEmpty())
            txtAvgPosition.setText(listChildren.get(position).getAvg_position());
        else
            txtAvgPosition.setText("-");
        txtBehaviourStatus.setText(listChildren.get(position).getOverall_behaviour());
        txtClassTeacherName.setText(listChildren.get(position).getClass_teacher().getFirst_name());

        rattingOverAllBehaviour.setRating(Support.ConvertToFloat(listChildren.get(position).getBehaviour_ratting()));

        Support.ShowImage(mContext, imgStudent, listChildren.get(position).getImage());
        Support.ShowImage(mContext, imgSchoolLogo, listChildren.get(position).getSchool_details().getSchool_logo());

        if (!listChildren.get(position).getClass_teacher().getId().equals("")) {
            relativeClassTeacher.setVisibility(View.VISIBLE);
        } else {
            relativeClassTeacher.setVisibility(View.GONE);
        }

        imgRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsDashboard.onNextClicked(position);
            }
        });

        imgLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsDashboard.onPrevClicked(position);
            }
        });


        imgCallToClassTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsDashboard.onClickCallTeacher(position);
            }
        });

        imgMessageToClassTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsDashboard.onClickMessageTeacher(position);
            }
        });
        imgMailToClassTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsDashboard.onClickMailTeacher(position);
            }
        });

        if (position == 0) {
            imgLeftArrow.setVisibility(View.GONE);
        }

        if (position == listChildren.size() - 1) {
            imgRightArrow.setVisibility(View.GONE);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
}