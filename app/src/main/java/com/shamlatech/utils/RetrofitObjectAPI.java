package com.shamlatech.utils;

import com.shamlatech.api_response.Res_Student_Marks;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;

/**
 * Created by  Dharmalingam Sekar  on 11/17/2016.
 */

public interface RetrofitObjectAPI {

    @FormUrlEncoded
    @retrofit2.http.POST("userToken")
    retrofit2.Call<Object> getUpdateToken(@retrofit2.http.Field("user_id") String user_id,
                                          @retrofit2.http.Field("role_id") String role_id,
                                          @retrofit2.http.Field("token") String token);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessLookups")
    retrofit2.Call<Object> getAccessLookups(@retrofit2.http.Field("user_id") String user_id,
                                            @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getUserInfo")
    retrofit2.Call<Object> getAccessUserInfo(@retrofit2.http.Field("user_id") String user_id,
                                             @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getCheckActivation")
    retrofit2.Call<Object> getCheckActivation(@retrofit2.http.Field("phone_number") String phone_number,
                                              @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getCheckNemis")
    retrofit2.Call<Object> getCheckNemis(@retrofit2.http.Field("nemisnumber") String nemisnumber,
                                              @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getActivateUser")
    retrofit2.Call<Object> getActivateUser(@retrofit2.http.Field("phone_number") String phone_number,
                                           @retrofit2.http.Field("password") String password,
                                           @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getLogin")
    retrofit2.Call<Object> getLogin(@retrofit2.http.Field("name") String name,
                                    @retrofit2.http.Field("password") String password,
                                    @retrofit2.http.Field("source") String source,
                                    @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessTeachersDetails")
    retrofit2.Call<Object> getAccessTeachersDetails(@retrofit2.http.Field("user_id") String user_id,
                                                    @retrofit2.http.Field("role_id") String role_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getTimeTableDetails")
    retrofit2.Call<Object> getTimeTableDetails(@retrofit2.http.Field("user_id") String user_id,
                                                    @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("term") String term
    );

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessTeachersGroup")
    retrofit2.Call<Object> getAccessTeachersGroup(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentInfo")
    retrofit2.Call<Object> getAccessStudentInfo(@retrofit2.http.Field("student_id") String student_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessAnnouncement")
    retrofit2.Call<Object> getAccessAnnouncement(@retrofit2.http.Field("user_id") String user_id,
                                                 @retrofit2.http.Field("role_id") String role_id,
                                                 @retrofit2.http.Field("status") String status,
                                                 @retrofit2.http.Field("last_id") String last_id,
                                                 @retrofit2.http.Field("date_from") String date_from,
                                                 @retrofit2.http.Field("date_to") String date_to);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessSingleAnnouncement")
    retrofit2.Call<Object> getAccessSingleAnnouncement(@retrofit2.http.Field("user_id") String user_id,
                                                       @retrofit2.http.Field("role_id") String role_id,
                                                       @retrofit2.http.Field("announcement_id") String announcement_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentTeacher")
    retrofit2.Call<Object> getAccessStudentTeacher(@retrofit2.http.Field("user_id") String user_id,
                                                   @retrofit2.http.Field("role_id") String role_id,
                                                   @retrofit2.http.Field("student_id") String student_id,
                                                   @retrofit2.http.Field("year") String year);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentEducation")
    retrofit2.Call<Object> getAccessStudentEducation(@retrofit2.http.Field("user_id") String user_id,
                                                     @retrofit2.http.Field("role_id") String role_id,
                                                     @retrofit2.http.Field("student_id") String student_id,
                                                     @retrofit2.http.Field("year") String year);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentBehaviour")
    retrofit2.Call<Object> getAccessStudentBehaviour(@retrofit2.http.Field("user_id") String user_id,
                                                     @retrofit2.http.Field("role_id") String role_id,
                                                     @retrofit2.http.Field("student_id") String student_id,
                                                     @retrofit2.http.Field("year") String year);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentHealth")
    retrofit2.Call<Object> getAccessStudentHealth(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id,
                                                  @retrofit2.http.Field("student_id") String student_id,
                                                  @retrofit2.http.Field("year") String year);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentAttendance")
    retrofit2.Call<Object> getAccessStudentAttendance(@retrofit2.http.Field("user_id") String user_id,
                                                      @retrofit2.http.Field("role_id") String role_id,
                                                      @retrofit2.http.Field("student_id") String student_id,
                                                      @retrofit2.http.Field("year") String year);

    @FormUrlEncoded
    @retrofit2.http.POST("getJournalList")
    retrofit2.Call<Object> getJournalList(@retrofit2.http.Field("user_id") String user_id,
                                                      @retrofit2.http.Field("role_id") String role_id,
                                                      @retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getJournalListDate")
    retrofit2.Call<Object> getJournalListDate(@retrofit2.http.Field("jDate") String jDate,
                                          @retrofit2.http.Field("StudentId") String StudentId);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentFees")
    retrofit2.Call<Object> getAccessStudentFees(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessStudentAssignmentList")
    retrofit2.Call<Object> getAccessStudentAssignmentList(@retrofit2.http.Field("user_id") String user_id,
                                                          @retrofit2.http.Field("role_id") String role_id,
                                                          @retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateStudentMark")
    retrofit2.Call<Object> getUpdateStudentMark(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("student_id") String student_id,
                                                @retrofit2.http.Field("exam_id") String exam_id,
                                                @retrofit2.http.Field("subject_id") String subject_id,
                                                @retrofit2.http.Field("marks") String marks,
                                                @retrofit2.http.Field("part1") String part1,
                                                @retrofit2.http.Field("part2") String part2,
                                                @retrofit2.http.Field("part3") String part3,
                                                @retrofit2.http.Field("comments") String comments);


    @FormUrlEncoded
    @retrofit2.http.POST("getAddUpdateAssignment")
    retrofit2.Call<Object> getAddUpdateAssignment(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id,
                                                  @retrofit2.http.Field("student_id") String student_id,
                                                  @retrofit2.http.Field("subject_id") String subject_id,
                                                  @retrofit2.http.Field("assignment_id") String assignment_id,
                                                  @retrofit2.http.Field("assignment_name") String assignment_name,
                                                  @retrofit2.http.Field("assignment_details") String assignment_details,
                                                  @retrofit2.http.Field("given_date") String given_date,
                                                  @retrofit2.http.Field("due_date") String due_date);


    @FormUrlEncoded
    @retrofit2.http.POST("getSubmitAssignment")
    retrofit2.Call<Object> getSubmitAssignment(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("student_id") String student_id,
                                               @retrofit2.http.Field("assignment_id") String assignment_id,
                                               @retrofit2.http.Field("date") String date,
                                               @retrofit2.http.Field("status") String status);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateBehaviour")
    retrofit2.Call<Object> getUpdateBehaviour(@retrofit2.http.Field("user_id") String user_id,
                                              @retrofit2.http.Field("role_id") String role_id,
                                              @retrofit2.http.Field("student_id") String student_id,
                                              @retrofit2.http.Field("behaviour") String behaviour);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateAttendance")
    retrofit2.Call<Object> getUpdateAttendance(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("student_id") String student_id,
                                               @retrofit2.http.Field("date") String date,
                                               @retrofit2.http.Field("period") String period,
                                               @retrofit2.http.Field("subject_id") String subject_id,
                                               @retrofit2.http.Field("attendance") String attendance,
                                               @retrofit2.http.Field("reason") String reason);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateLastHealthOccurrence")
    retrofit2.Call<Object> getUpdateLastHealthOccurrence(@retrofit2.http.Field("user_id") String user_id,
                                                         @retrofit2.http.Field("role_id") String role_id,
                                                         @retrofit2.http.Field("student_id") String student_id,
                                                         @retrofit2.http.Field("date") String date,
                                                         @retrofit2.http.Field("occurrence") String occurrence,
                                                         @retrofit2.http.Field("action") String action,
                                                         @retrofit2.http.Field("further_action") String further_action);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessSchoolEvents")
    retrofit2.Call<Object> getAccessSchoolEvents(@retrofit2.http.Field("user_id") String user_id,
                                                 @retrofit2.http.Field("role_id") String role_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessDocuments")
    retrofit2.Call<Object> getAccessDocuments(@retrofit2.http.Field("user_id") String user_id,
                                              @retrofit2.http.Field("role_id") String role_id,
                                              @retrofit2.http.Field("last_id") String last_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessPhotos")
    retrofit2.Call<Object> getAccessPhotos(@retrofit2.http.Field("user_id") String user_id,
                                           @retrofit2.http.Field("role_id") String role_id,
                                           @retrofit2.http.Field("last_id") String last_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getAccessVideos")
    retrofit2.Call<Object> getAccessVideos(@retrofit2.http.Field("user_id") String user_id,
                                           @retrofit2.http.Field("role_id") String role_id,
                                           @retrofit2.http.Field("last_id") String last_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getRemoveDocument")
    retrofit2.Call<Object> getRemoveDocument(@retrofit2.http.Field("user_id") String user_id,
                                             @retrofit2.http.Field("role_id") String role_id,
                                             @retrofit2.http.Field("id") String id);


    @FormUrlEncoded
    @retrofit2.http.POST("getForumsList")
    retrofit2.Call<Object> getAccessForumsList(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("type") String type,
                                               @retrofit2.http.Field("student_id") String student_id,
                                               @retrofit2.http.Field("status") String status,
                                               @retrofit2.http.Field("last_id") String last_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAbsentNoteList")
    retrofit2.Call<Object> getAccessAbsentNoteList(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("type") String type,
                                               @retrofit2.http.Field("student_id") String student_id,
                                               @retrofit2.http.Field("status") String status,
                                               @retrofit2.http.Field("last_id") String last_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getParentStudent")
    retrofit2.Call<Object> getParentStudent(@retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getTeachersInfo")
    retrofit2.Call<Object> getTeachersInfo(@retrofit2.http.Field("phonenumber") String phonenumber);


    @FormUrlEncoded
    @retrofit2.http.POST("getExams")
    retrofit2.Call<Object> getExams(@retrofit2.http.Field("user_id") String user_id,
                                                   @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getSubsubjects")
    retrofit2.Call<Object> getSubsubjects(@retrofit2.http.Field("sId") String sId);

    @FormUrlEncoded
    @retrofit2.http.POST("getPostExams")
    retrofit2.Call<Object> getPostExams(@Field("user_id") String user_id,
                                        @Field("role_id") String role_id,
                                        @Field("datasaved") String datasaved
                                       );

    @FormUrlEncoded
    @retrofit2.http.POST("getPostSubjectMarks")
    retrofit2.Call<Object> getPostSubjectMarks(@Field("user_id") String user_id,
                                        @Field("role_id") String role_id,
                                        @Field("datasaved") String datasaved
    );


    @FormUrlEncoded
    @retrofit2.http.POST("getCreateForumsList")
    retrofit2.Call<Object> getCreateForumsList(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("topic") String topic,
                                               @retrofit2.http.Field("from") String from,
                                               @retrofit2.http.Field("to") String to,
                                               @retrofit2.http.Field("type") String type,
                                               @retrofit2.http.Field("class_id") String class_id,
                                               @retrofit2.http.Field("section_id") String section_id,
                                               @retrofit2.http.Field("student_id") String student_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getCreateAbsentNoteList")
    retrofit2.Call<Object> getCreateAbsentNoteList(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("topic") String topic,
                                               @retrofit2.http.Field("adate") String from,
                                               @retrofit2.http.Field("to") String to,
                                               @retrofit2.http.Field("type") String type,
                                               @retrofit2.http.Field("class_id") String class_id,
                                               @retrofit2.http.Field("section_id") String section_id,
                                               @retrofit2.http.Field("student_id") String student_id);


    @FormUrlEncoded
    @retrofit2.http.POST("getCreateJournal")
    retrofit2.Call<Object> getCreateJournal(@retrofit2.http.Field("user_id") String user_id,
                                                   @retrofit2.http.Field("role_id") String role_id,
                                                   @retrofit2.http.Field("emoji") String emoji,
                                                   @retrofit2.http.Field("explanation") String explanation,
                                                   @retrofit2.http.Field("journal_date") String journal_date,
                                                   @retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("snoozeNotification")
    retrofit2.Call<Object> snoozeNotification(@retrofit2.http.Field("notificationid") String notificationid);


    @FormUrlEncoded
    @retrofit2.http.POST("getAddNemis")
    retrofit2.Call<Object> getAddNemis(@retrofit2.http.Field("nemisno") String nemisno,
                                                   @retrofit2.http.Field("nemisno_new") String nemisno_new);




    @FormUrlEncoded
    @retrofit2.http.POST("removeSubject")
    retrofit2.Call<Object> removeSubject(
            @retrofit2.http.Field("subject_id") String subject_id
    );


    @FormUrlEncoded
    @retrofit2.http.POST("saveStudentClasses")
    retrofit2.Call<Object> saveStudentClasses(
            @retrofit2.http.Field("student_id") String student_id,
            @retrofit2.http.Field("stream_id") String stream_id,
            @retrofit2.http.Field("class_id") String class_id
    );


    @FormUrlEncoded
    @retrofit2.http.POST("deleteStudent")
    retrofit2.Call<Object> deleteStudent(
            @retrofit2.http.Field("student_id") String student_id
    );


    @FormUrlEncoded
    @retrofit2.http.POST("deleteClassStream")
    retrofit2.Call<Object> deleteClassStream(
            @retrofit2.http.Field("stream_id") String stream_id,
            @retrofit2.http.Field("class_id") String class_id,
            @retrofit2.http.Field("teachers_id") String teachers_id
    );


    @FormUrlEncoded
    @retrofit2.http.POST("saveTeachersSubjects")
    retrofit2.Call<Object> saveTeachersSubjects(
            @retrofit2.http.Field("stream_id") String stream_id,
            @retrofit2.http.Field("class_id") String class_id,
            @retrofit2.http.Field("teachers_id") String teachers_id,
            @retrofit2.http.Field("subject") String subject
    );


    @FormUrlEncoded
    @retrofit2.http.POST("getStudentClass")
    retrofit2.Call<Object> getStudentClass(
            @retrofit2.http.Field("student_id") String student_id
    );

    @FormUrlEncoded
    @retrofit2.http.POST("getTeachersSubjects")
    retrofit2.Call<Object> getTeachersSubjects(
            @retrofit2.http.Field("stream_id") String stream_id,
            @retrofit2.http.Field("class_id") String class_id,
            @retrofit2.http.Field("teachers_id") String teachers_id
    );


    @FormUrlEncoded
    @retrofit2.http.POST("getSchoolInfo")
    retrofit2.Call<Object> getSchoolInfo(@retrofit2.http.Field("schoolname") String schoolname
    );


    @FormUrlEncoded
    @retrofit2.http.POST("checkPassword")
    retrofit2.Call<Object> checkPassword(@retrofit2.http.Field("phone") String phone,
                                          @retrofit2.http.Field("passwd") String passwd
    );



    @FormUrlEncoded
    @retrofit2.http.POST("getSaveClasses")
    retrofit2.Call<Object> getSaveClasses(@retrofit2.http.Field("phone") String phone,
                                                 @retrofit2.http.Field("schoolname") String schoolname,
                                                 @retrofit2.http.Field("stream") String stream,
                                                 @retrofit2.http.Field("classname") String classname
    );

    @FormUrlEncoded
    @retrofit2.http.POST("getSaveStudentDetails")
    retrofit2.Call<Object> getSaveStudentDetails(@retrofit2.http.Field("parentsPhone") String parentsPhone,
                                                @retrofit2.http.Field("schoolname") String schoolname,
                                                @retrofit2.http.Field("studnemis") String studnemis,
                                                @retrofit2.http.Field("studfirstname") String studfirstname,
                                                @retrofit2.http.Field("studsecondname") String studsecondname,
                                                @retrofit2.http.Field("studlastname") String studlastname,
                                                @retrofit2.http.Field("studgender") String studgender,
                                                @retrofit2.http.Field("studbirthday") String studbirthday,
                                                @retrofit2.http.Field("studentcode") String studentcode
    );


    @FormUrlEncoded
    @retrofit2.http.POST("getSaveSchoolDetails")
    retrofit2.Call<Object> getSaveSchoolDetails(@retrofit2.http.Field("school") String school,
                                                @retrofit2.http.Field("schooladdress") String schooladdress,
                                                @retrofit2.http.Field("schoolcounty") String schoolcounty,
                                                @retrofit2.http.Field("schooltelephone") String schooltelephone,
                                                @retrofit2.http.Field("schoolemail") String schoolemail,
                                                @retrofit2.http.Field("schoolwebsite") String schoolwebsite
    );



    @FormUrlEncoded
    @retrofit2.http.POST("getSaveTeacherDetails")
    retrofit2.Call<Object> getSaveTeacherDetails(
                                                @retrofit2.http.Field("accounttype") String accounttype,
                                                @retrofit2.http.Field("phonenumber") String phonenumber,
                                                @retrofit2.http.Field("initial") String initial,
                                                @retrofit2.http.Field("firstname") String firstname,
                                                @retrofit2.http.Field("middlename") String middlename,
                                                @retrofit2.http.Field("lastname") String lastname,
                                                @retrofit2.http.Field("tscnumber") String tscnumber,
                                                @retrofit2.http.Field("gender") String gender,
                                                @retrofit2.http.Field("emailaddress") String emailaddress,
                                                @retrofit2.http.Field("passwd") String passwd,
                                                @retrofit2.http.Field("teacherscode") String teacherscode,
                                                @retrofit2.http.Field("schoolname") String schoolname
    );

    @FormUrlEncoded
    @retrofit2.http.POST("getSaveParentDetails")
    retrofit2.Call<Object> getSaveParentDetails(@retrofit2.http.Field("nemisno") String nemisno,
                                                @retrofit2.http.Field("parentid") String parentid,
                                                @retrofit2.http.Field("firstname") String firstname,
                                                @retrofit2.http.Field("lastname") String lastname,
                                                @retrofit2.http.Field("phone1") String phone1,
                                                @retrofit2.http.Field("phone2") String phone2,
                                                @retrofit2.http.Field("email") String email,
                                                @retrofit2.http.Field("occupation") String occupation,
                                                @retrofit2.http.Field("passwd") String passwd
    );

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessSingleForum")
    retrofit2.Call<Object> getAccessSingleForum(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("forum_id") String forum_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateFavForum")
    retrofit2.Call<Object> getUpdateFavForum(@retrofit2.http.Field("user_id") String user_id,
                                             @retrofit2.http.Field("role_id") String role_id,
                                             @retrofit2.http.Field("forum_id") String forum_id,
                                             @retrofit2.http.Field("fav") String fav);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateOpenForum")
    retrofit2.Call<Object> getUpdateOpenForum(@retrofit2.http.Field("user_id") String user_id,
                                              @retrofit2.http.Field("role_id") String role_id,
                                              @retrofit2.http.Field("forum_id") String forum_id,
                                              @retrofit2.http.Field("open") String open);

    @FormUrlEncoded
    @retrofit2.http.POST("getReplyForum")
    retrofit2.Call<Object> getReplyForum(@retrofit2.http.Field("user_id") String user_id,
                                         @retrofit2.http.Field("role_id") String role_id,
                                         @retrofit2.http.Field("forum_id") String forum_id,
                                         @retrofit2.http.Field("comment") String comment);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateNotifications")
    retrofit2.Call<Object> getUpdateNotifications(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id,
                                                  @retrofit2.http.Field("sound") String sound,
                                                  @retrofit2.http.Field("vibrate") String vibrate,
                                                  @retrofit2.http.Field("dnd") String dnd);

    @FormUrlEncoded
    @retrofit2.http.POST("getSendFeedback")
    retrofit2.Call<Object> getSendFeedback(@retrofit2.http.Field("user_id") String user_id,
                                           @retrofit2.http.Field("role_id") String role_id,
                                           @retrofit2.http.Field("feedback") String feedback);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessNotification")
    retrofit2.Call<Object> getAccessNotification(@retrofit2.http.Field("user_id") String user_id,
                                                 @retrofit2.http.Field("role_id") String role_id,
                                                 @retrofit2.http.Field("last_id") String last_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getRemoveNotification")
    retrofit2.Call<Object> getRemoveNotification(@retrofit2.http.Field("user_id") String user_id,
                                                 @retrofit2.http.Field("role_id") String role_id,
                                                 @retrofit2.http.Field("id") String id);

    @FormUrlEncoded
    @retrofit2.http.POST("getReadNotification")
    retrofit2.Call<Object> getReadNotification(@retrofit2.http.Field("user_id") String user_id,
                                               @retrofit2.http.Field("role_id") String role_id,
                                               @retrofit2.http.Field("id") String id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessParentDetails")
    retrofit2.Call<Object> getAccessParentDetails(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessAllergyList")
    retrofit2.Call<Object> getAccessAllergyList(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("student_id") String student_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getAddAllergyList")
    retrofit2.Call<Object> getAddAllergyList(@retrofit2.http.Field("user_id") String user_id,
                                             @retrofit2.http.Field("role_id") String role_id,
                                             @retrofit2.http.Field("student_id") String student_id,
                                             @retrofit2.http.Field("name") String name,
                                             @retrofit2.http.Field("details") String details);

    @FormUrlEncoded
    @retrofit2.http.POST("getRemoveAllergyList")
    retrofit2.Call<Object> getRemoveAllergyList(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id,
                                                @retrofit2.http.Field("student_id") String student_id,
                                                @retrofit2.http.Field("id") String id);

    @FormUrlEncoded
    @retrofit2.http.POST("getRemoveGroup")
    retrofit2.Call<Object> getRemoveGroup(@retrofit2.http.Field("user_id") String user_id,
                                          @retrofit2.http.Field("role_id") String role_id,
                                          @retrofit2.http.Field("group_id") String group_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getCreateGroup")
    retrofit2.Call<Object> getCreateGroup(@retrofit2.http.Field("user_id") String user_id,
                                          @retrofit2.http.Field("role_id") String role_id,
                                          @retrofit2.http.Field("classId") String classId,
                                          @retrofit2.http.Field("sectionId") String sectionId,
                                          @retrofit2.http.Field("subjectId") String subjectId,
                                          @retrofit2.http.Field("group_name") String group_name,
                                          @retrofit2.http.Field("students") String students);

    @FormUrlEncoded
    @retrofit2.http.POST("getAccessMessageList")
    retrofit2.Call<Object> getAccessMessageList(@retrofit2.http.Field("user_id") String user_id,
                                                @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getSendMessage")
    retrofit2.Call<Object> getSendMessage(@retrofit2.http.Field("user_id") String user_id,
                                          @retrofit2.http.Field("role_id") String role_id,
                                          @retrofit2.http.Field("to") String to,
                                          @retrofit2.http.Field("sender") String sender,
                                          @retrofit2.http.Field("receiver") String receiver,
                                          @retrofit2.http.Field("message") String message);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdateClassLayout")
    retrofit2.Call<Object> getUpdateMyClassLayout(@retrofit2.http.Field("user_id") String user_id,
                                                  @retrofit2.http.Field("role_id") String role_id,
                                                  @retrofit2.http.Field("class_id") String class_id,
                                                  @retrofit2.http.Field("section_id") String section_id,
                                                  @retrofit2.http.Field("place") String place);

    @FormUrlEncoded
    @retrofit2.http.POST("getParentLogin")
    retrofit2.Call<Object> getParentLogin(@retrofit2.http.Field("name") String name,
                                          @retrofit2.http.Field("password") String password,
                                          @retrofit2.http.Field("source") String source,
                                          @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getTeacherLogin")
    retrofit2.Call<Object> getTeacherLogin(@retrofit2.http.Field("name") String name,
                                           @retrofit2.http.Field("password") String password,
                                           @retrofit2.http.Field("source") String source,
                                           @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getUpdatePassword")
    retrofit2.Call<Object> getUpdatePassword(@retrofit2.http.Field("user_id") String user_id,
                                             @retrofit2.http.Field("role_id") String role_id,
                                             @retrofit2.http.Field("old_pass") String old_pass,
                                             @retrofit2.http.Field("new_pass") String new_pass);

    @Multipart
    @retrofit2.http.POST("getUpdateProfile")
    retrofit2.Call<Object> getUpdateProfile(@retrofit2.http.Part("user_id") RequestBody user_id,
                                            @retrofit2.http.Part("role_id") RequestBody role_id,
                                            @retrofit2.http.Part("name") RequestBody name,
                                            @retrofit2.http.Part("phone") RequestBody phone_number,
                                            @retrofit2.http.Part("email") RequestBody email,
            /* @retrofit2.http.Part("password") RequestBody password,*/
                                            @retrofit2.http.Part MultipartBody.Part image);


    @FormUrlEncoded
    @retrofit2.http.POST("getForgetPassword")
    retrofit2.Call<Object> getForgetPassword(@retrofit2.http.Field("name") String name,
                                             @retrofit2.http.Field("source") String source,
                                             @retrofit2.http.Field("role_id") String role_id);

    @FormUrlEncoded
    @retrofit2.http.POST("getResetPassword")
    retrofit2.Call<Object> getResetPassword(@retrofit2.http.Field("user_id") String user_id,
                                            @retrofit2.http.Field("role_id") String role_id,
                                            @retrofit2.http.Field("password") String new_pass);



}
