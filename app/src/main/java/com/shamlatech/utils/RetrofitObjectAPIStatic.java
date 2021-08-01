package com.shamlatech.utils;

/**
 * Created by  Dharmalingam Sekar  on 11/17/2016.
 */

public interface RetrofitObjectAPIStatic {


    @retrofit2.http.POST("lookups.json")
    retrofit2.Call<Object> getAccessLookups(@retrofit2.http.Query("teacher_id") String teacher_id);

    @retrofit2.http.POST("user_info.json")
    retrofit2.Call<Object> getAccessUserInfo(@retrofit2.http.Query("user_id") String user_id);

    @retrofit2.http.POST("check_activation.json")
    retrofit2.Call<Object> getCheckActivation(@retrofit2.http.Query("phone_number") String phone_number,
                                              @retrofit2.http.Query("role_id") String role_id);

    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getActivateUser(@retrofit2.http.Query("phone_number") String phone_number,
                                           @retrofit2.http.Query("password") String password,
                                           @retrofit2.http.Query("role_id") String role_id);


    @retrofit2.http.POST("user_info.json")
    retrofit2.Call<Object> getLogin(@retrofit2.http.Query("name") String name,
                                    @retrofit2.http.Query("password") String password,
                                    @retrofit2.http.Query("source") String source,
                                    @retrofit2.http.Query("role_id") String role_id);

    @retrofit2.http.POST("teacher_class_details.json")
    retrofit2.Call<Object> getAccessTeachersDetails(@retrofit2.http.Query("teacher_id") String teacher_id);

    @retrofit2.http.POST("group_list.json")
    retrofit2.Call<Object> getAccessTeachersGroup(@retrofit2.http.Query("teacher_id") String teacher_id);

    @retrofit2.http.POST("student_info.json")
    retrofit2.Call<Object> getAccessStudentInfo(@retrofit2.http.Query("stud_id") String stud_id);


    @retrofit2.http.POST("annoncement_list.json")
    retrofit2.Call<Object> getAccessAnnouncement(@retrofit2.http.Query("user_id") String user_id,
                                                 @retrofit2.http.Query("role_id") String role_id,
                                                 @retrofit2.http.Query("status") String status,
                                                 @retrofit2.http.Query("last_id") String last_id,
                                                 @retrofit2.http.Query("date_from") String date_from,
                                                 @retrofit2.http.Query("date_to") String date_to);

    @retrofit2.http.POST("annoncement_list.json")
    retrofit2.Call<Object> getAccessSingleAnnouncement(@retrofit2.http.Query("user_id") String user_id,
                                                       @retrofit2.http.Query("role_id") String role_id,
                                                       @retrofit2.http.Query("announcement_id") String announcement_id);


    @retrofit2.http.POST("student_teachers.json")
    retrofit2.Call<Object> getAccessStudentTeacher(@retrofit2.http.Query("user_id") String user_id,
                                                   @retrofit2.http.Query("role_id") String role_id,
                                                   @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("student_education.json")
    retrofit2.Call<Object> getAccessStudentEducation(@retrofit2.http.Query("user_id") String user_id,
                                                     @retrofit2.http.Query("role_id") String role_id,
                                                     @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("student_behaviour.json")
    retrofit2.Call<Object> getAccessStudentBehaviour(@retrofit2.http.Query("user_id") String user_id,
                                                     @retrofit2.http.Query("role_id") String role_id,
                                                     @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("student_health.json")
    retrofit2.Call<Object> getAccessStudentHealth(@retrofit2.http.Query("user_id") String user_id,
                                                  @retrofit2.http.Query("role_id") String role_id,
                                                  @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("student_attendance.json")
    retrofit2.Call<Object> getAccessStudentAttendance(@retrofit2.http.Query("user_id") String user_id,
                                                      @retrofit2.http.Query("role_id") String role_id,
                                                      @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("student_fees.json")
    retrofit2.Call<Object> getAccessStudentFees(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id,
                                                @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("assignment_list.json")
    retrofit2.Call<Object> getAccessStudentAssignmentList(@retrofit2.http.Query("user_id") String user_id,
                                                          @retrofit2.http.Query("role_id") String role_id,
                                                          @retrofit2.http.Query("stud_id") String stud_id);

    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getUpdateStudentMark(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id,
                                                @retrofit2.http.Query("stud_id") String stud_id,
                                                @retrofit2.http.Query("exam_id") String exam_id,
                                                @retrofit2.http.Query("subject_id") String subject_id,
                                                @retrofit2.http.Query("marks") String marks,
                                                @retrofit2.http.Query("comments") String comments);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getAddUpdateAssignment(@retrofit2.http.Query("user_id") String user_id,
                                                  @retrofit2.http.Query("role_id") String role_id,
                                                  @retrofit2.http.Query("stud_id") String stud_id,
                                                  @retrofit2.http.Query("subject_id") String subject_id,
                                                  @retrofit2.http.Query("assignment_id") String assignment_id,
                                                  @retrofit2.http.Query("assignment_name") String assignment_name,
                                                  @retrofit2.http.Query("assignment_details") String assignment_details,
                                                  @retrofit2.http.Query("given_date") String given_date,
                                                  @retrofit2.http.Query("due_date") String due_date);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getSubmitAssignment(@retrofit2.http.Query("user_id") String user_id,
                                               @retrofit2.http.Query("role_id") String role_id,
                                               @retrofit2.http.Query("stud_id") String stud_id,
                                               @retrofit2.http.Query("assignment_id") String assignment_id,
                                               @retrofit2.http.Query("date") String date);

    @retrofit2.http.POST("student_behaviour.json")
    retrofit2.Call<Object> getUpdateBehaviour(@retrofit2.http.Query("user_id") String user_id,
                                              @retrofit2.http.Query("role_id") String role_id,
                                              @retrofit2.http.Query("stud_id") String stud_id,
                                              @retrofit2.http.Query("behaviour") String behaviour);

    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getUpdateAttendance(@retrofit2.http.Query("user_id") String user_id,
                                               @retrofit2.http.Query("role_id") String role_id,
                                               @retrofit2.http.Query("stud_id") String stud_id,
                                               @retrofit2.http.Query("date") String date,
                                               @retrofit2.http.Query("period") String period,
                                               @retrofit2.http.Query("subject_id") String subject_id,
                                               @retrofit2.http.Query("attendance") String attendance,
                                               @retrofit2.http.Query("reason") String reason);


    @retrofit2.http.POST("school_event.json")
    retrofit2.Call<Object> getAccessSchoolEvents(@retrofit2.http.Query("user_id") String user_id,
                                                 @retrofit2.http.Query("role_id") String role_id);


    @retrofit2.http.POST("document_list.json")
    retrofit2.Call<Object> getAccessDocuments(@retrofit2.http.Query("user_id") String user_id,
                                              @retrofit2.http.Query("role_id") String role_id,
                                              @retrofit2.http.Query("last_id") String last_id);


    @retrofit2.http.POST("photo_list.json")
    retrofit2.Call<Object> getAccessPhotos(@retrofit2.http.Query("user_id") String user_id,
                                           @retrofit2.http.Query("role_id") String role_id,
                                           @retrofit2.http.Query("last_id") String last_id);


    @retrofit2.http.POST("video_list.json")
    retrofit2.Call<Object> getAccessVideos(@retrofit2.http.Query("user_id") String user_id,
                                           @retrofit2.http.Query("role_id") String role_id,
                                           @retrofit2.http.Query("last_id") String last_id);

    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getRemoveDocument(@retrofit2.http.Query("user_id") String user_id,
                                             @retrofit2.http.Query("role_id") String role_id,
                                             @retrofit2.http.Query("id") String id);


    @retrofit2.http.POST("forum_list.json")
    retrofit2.Call<Object> getAccessForumsList(@retrofit2.http.Query("user_id") String user_id,
                                               @retrofit2.http.Query("role_id") String role_id,
                                               @retrofit2.http.Query("type") String type,
                                               @retrofit2.http.Query("stud_id") String stud_id,
                                               @retrofit2.http.Query("status") String status,
                                               @retrofit2.http.Query("last_id") String last_id);


    @retrofit2.http.POST("forum_detaiils.json")
    retrofit2.Call<Object> getCreateForumsList(@retrofit2.http.Query("user_id") String user_id,
                                               @retrofit2.http.Query("role_id") String role_id,
                                               @retrofit2.http.Query("topic") String topic,
                                               @retrofit2.http.Query("from") String from,
                                               @retrofit2.http.Query("to") String to,
                                               @retrofit2.http.Query("type") String type,
                                               @retrofit2.http.Query("class_id") String class_id,
                                               @retrofit2.http.Query("section_id") String section_id,
                                               @retrofit2.http.Query("stud_id") String stud_id);


    @retrofit2.http.POST("forum_detaiils.json")
    retrofit2.Call<Object> getAccessSingleForum(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id,
                                                @retrofit2.http.Query("forum_id") String forum_id);

    @retrofit2.http.POST("forum_detaiils.json")
    retrofit2.Call<Object> getUpdateFavForum(@retrofit2.http.Query("user_id") String user_id,
                                             @retrofit2.http.Query("role_id") String role_id,
                                             @retrofit2.http.Query("forum_id") String forum_id,
                                             @retrofit2.http.Query("fav") String fav);


    @retrofit2.http.POST("forum_detaiils.json")
    retrofit2.Call<Object> getUpdateOpenForum(@retrofit2.http.Query("user_id") String user_id,
                                              @retrofit2.http.Query("role_id") String role_id,
                                              @retrofit2.http.Query("forum_id") String forum_id,
                                              @retrofit2.http.Query("open") String open);


    @retrofit2.http.POST("forum_detaiils.json")
    retrofit2.Call<Object> getReplyForum(@retrofit2.http.Query("user_id") String user_id,
                                         @retrofit2.http.Query("role_id") String role_id,
                                         @retrofit2.http.Query("forum_id") String forum_id,
                                         @retrofit2.http.Query("comment") String comment);

    @retrofit2.http.POST("user_info.json")
    retrofit2.Call<Object> getUpdateNotifications(@retrofit2.http.Query("user_id") String user_id,
                                                  @retrofit2.http.Query("role_id") String role_id,
                                                  @retrofit2.http.Query("sound") String sound,
                                                  @retrofit2.http.Query("vibrate") String vibrate,
                                                  @retrofit2.http.Query("dnd") String dnd);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getSendFeedback(@retrofit2.http.Query("user_id") String user_id,
                                           @retrofit2.http.Query("role_id") String role_id,
                                           @retrofit2.http.Query("feedback") String feedback);

    @retrofit2.http.POST("notification_list.json")
    retrofit2.Call<Object> getAccessNotification(@retrofit2.http.Query("user_id") String user_id,
                                                 @retrofit2.http.Query("role_id") String role_id,
                                                 @retrofit2.http.Query("last_id") String last_id);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getRemoveNotification(@retrofit2.http.Query("user_id") String user_id,
                                                 @retrofit2.http.Query("role_id") String role_id,
                                                 @retrofit2.http.Query("id") String id);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getReadNotification(@retrofit2.http.Query("user_id") String user_id,
                                               @retrofit2.http.Query("role_id") String role_id,
                                               @retrofit2.http.Query("id") String id);

    @retrofit2.http.POST("parent_student_details.json")
    retrofit2.Call<Object> getAccessParentDetails(@retrofit2.http.Query("parent_id") String parent_id);


    @retrofit2.http.POST("list_allergies.json")
    retrofit2.Call<Object> getAccessAllergyList(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id,
                                                @retrofit2.http.Query("stud_id") String stud_id);


    @retrofit2.http.POST("list_allergies.json")
    retrofit2.Call<Object> getAddAllergyList(@retrofit2.http.Query("user_id") String user_id,
                                             @retrofit2.http.Query("role_id") String role_id,
                                             @retrofit2.http.Query("stud_id") String stud_id,
                                             @retrofit2.http.Query("name") String name,
                                             @retrofit2.http.Query("details") String details);

    @retrofit2.http.POST("list_allergies.json")
    retrofit2.Call<Object> getRemoveAllergyList(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id,
                                                @retrofit2.http.Query("stud_id") String stud_id,
                                                @retrofit2.http.Query("id") String id);


    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getRemoveGroup(@retrofit2.http.Query("user_id") String user_id,
                                          @retrofit2.http.Query("role_id") String role_id,
                                          @retrofit2.http.Query("group_id") String group_id);

    @retrofit2.http.POST("group_list.json")
    retrofit2.Call<Object> getCreateGroup(@retrofit2.http.Query("teacher_id") String teacher_id,
                                          @retrofit2.http.Query("classId") String classId,
                                          @retrofit2.http.Query("sectionId") String sectionId,
                                          @retrofit2.http.Query("subjectId") String subjectId,
                                          @retrofit2.http.Query("group_name") String group_name,
                                          @retrofit2.http.Query("students") String students);


    @retrofit2.http.POST("message_list.json")
    retrofit2.Call<Object> getAccessMessageList(@retrofit2.http.Query("user_id") String user_id,
                                                @retrofit2.http.Query("role_id") String role_id);

    @retrofit2.http.POST("common_success.json")
    retrofit2.Call<Object> getSendMessage(@retrofit2.http.Query("user_id") String user_id,
                                          @retrofit2.http.Query("role_id") String role_id,
                                          @retrofit2.http.Query("to") String to,
                                          @retrofit2.http.Query("message") String message);

    @retrofit2.http.POST("myclass.json")
    retrofit2.Call<Object> getUpdateMyClassLayout(@retrofit2.http.Query("user_id") String user_id,
                                                  @retrofit2.http.Query("role_id") String role_id,
                                                  @retrofit2.http.Query("class_id") String class_id,
                                                  @retrofit2.http.Query("section_id") String section_id,
                                                  @retrofit2.http.Query("place") String place);


    @retrofit2.http.POST("user_info_parent.json")
    retrofit2.Call<Object> getParentLogin(@retrofit2.http.Query("name") String name,
                                          @retrofit2.http.Query("password") String password,
                                          @retrofit2.http.Query("source") String source,
                                          @retrofit2.http.Query("role_id") String role_id);

    @retrofit2.http.POST("user_info_teacher.json")
    retrofit2.Call<Object> getTeacherLogin(@retrofit2.http.Query("name") String name,
                                           @retrofit2.http.Query("password") String password,
                                           @retrofit2.http.Query("source") String source,
                                           @retrofit2.http.Query("role_id") String role_id);

}
