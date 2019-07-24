package com.mrtecks.school;

import androidx.annotation.NonNull;

import com.mrtecks.school.classPOJO.classBean;
import com.mrtecks.school.modulePOJO.moduleBean;
import com.mrtecks.school.regiterPOJO.registerBean;
import com.mrtecks.school.schoolPOJO.schoolBean;
import com.mrtecks.school.singleTopicPOJO.singleTopicBean;
import com.mrtecks.school.surveyPOJO.surveyBean;
import com.mrtecks.school.topicsPOJO.topicBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {

    @NonNull
    @GET("e2l/api/getSchools.php")
    Call<schoolBean> getSchools();


    @NonNull
    @Multipart
    @POST("e2l/api/getClasses.php")
    Call<classBean> getClasses(
            @Part("school_id") @NonNull String school_id
    );

    @Multipart
    @POST("e2l/api/register.php")
    Call<registerBean> register(
            @Part("name") String name,
            @Part("contact") String contact,
            @Part("school_id") String school_id,
            @Part("class") String classes,
            @Part("rollno") String rollno,
            @Part("password") String password,
            @Part MultipartBody.Part file1
    );

    @Multipart
    @POST("e2l/api/login.php")
    Call<registerBean> login(
            @Part("contact") String contact,
            @Part("password") String password
    );

    @Multipart
    @POST("e2l/api/getModules.php")
    Call<moduleBean> getModules(
            @Part("school_id") String school_id,
            @Part("class") String classid,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("e2l/api/getTopics.php")
    Call<topicBean> getTopics(
            @Part("module") String module,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("e2l/api/getTopicById.php")
    Call<singleTopicBean> getTopicById(
            @Part("qid") String qid,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("e2l/api/submitMCQ.php")
    Call<topicBean> submitMCQ(
            @Part("user_id") String user_id,
            @Part("qid") String qid,
            @Part("answer") String answer,
            @Part("module") String module
    );

    @Multipart
    @POST("e2l/api/submitMCQmodule.php")
    Call<topicBean> submitMCQmodule(
            @Part("user_id") String user_id,
            @Part("qid") String qid,
            @Part("answer") String answer,
            @Part("mid") String mid,
            @Part("school_id") String school_id,
            @Part("class") String classes
    );

    @NonNull
    @GET("e2l/api/getSurvey.php")
    Call<surveyBean> getSurvey();

    @Multipart
    @POST("e2l/api/submit_survey.php")
    Call<registerBean> submitSurvey(
            @Part("user_id") String user_id,
            @Part("qid") String qid,
            @Part("answer") String answer
    );

    @Multipart
    @POST("e2l/api/submitFile.php")
    Call<registerBean> submitFile(
            @Part("user_id") String user_id,
            @Part("qid") String qid,
            @Part("module") String module,
            @Part MultipartBody.Part file1
    );

    @Multipart
    @POST("e2l/api/submitFilemodule.php")
    Call<registerBean> submitFilemodule(
            @Part("user_id") String user_id,
            @Part("qid") String qid,
            @Part("mid") String module,
            @Part("school_id") String school_id,
            @Part("class") String classes,
            @Part MultipartBody.Part file1
    );

    /*@Multipart
    @POST("roshni/api/login.php")
    Call<verifyBean> login(
            @Part("phone") String client
    );

    @Multipart
    @POST("roshni/api/register_worker.php")
    Call<verifyBean> worker_signup(
            @Part("phone") String client,
            @Part("type") String type
    );

    @Multipart
    @POST("roshni/api/verify.php")
    Call<verifyBean> verify(
            @Part("phone") String client,
            @Part("otp") String otp
    );


    @Multipart
    @POST("roshni/api/update_worker_personal.php")
    Call<verifyBean> updateWorkerPersonal(
            @Part("user_id") String user_id,
            @Part("name") String name,
            @Part("dob") String dob,
            @Part("gender") String gender,
            @Part("cpin") String cpin,
            @Part("cstate") String cstate,
            @Part("cdistrict") String cdistrict,
            @Part("carea") String carea,
            @Part("cstreet") String cstreet,
            @Part("ppin") String ppin,
            @Part("pstate") String pstate,
            @Part("pdistrict") String pdistrict,
            @Part("parea") String parea,
            @Part("pstreet") String pstreet,
            @Part("category") String category,
            @Part("religion") String religion,
            @Part("educational") String educational,
            @Part("marital") String marital,
            @Part("children") String children,
            @Part("belowsix") String belowsix,
            @Part("sixtofourteen") String sixtofourteen,
            @Part("fifteentoeighteen") String fifteentoeighteen,
            @Part("goingtoschool") String goingtoschool,
            @Part MultipartBody.Part file1
    );

    @Multipart
    @POST("roshni/api/update_brand.php")
    Call<verifyBean> updateBrand(
            @Part("user_id") String user_id,
            @Part("name") String name,
            @Part("registration_number") String registration_number,
            @Part("contact_person") String contact_person,
            @Part("cpin") String cpin,
            @Part("cstate") String cstate,
            @Part("cdistrict") String cdistrict,
            @Part("carea") String carea,
            @Part("cstreet") String cstreet,
            @Part("ppin") String ppin,
            @Part("pstate") String pstate,
            @Part("pdistrict") String pdistrict,
            @Part("parea") String parea,
            @Part("pstreet") String pstreet,
            @Part("manufacturing_units") String manufacturing_units,
            @Part("factory_outlet") String factory_outlet,
            @Part("products") String products,
            @Part("country") String country,
            @Part("workers") String workers,
            @Part("certification") String certification,
            @Part("expiry") String expiry,
            @Part("website") String website,
            @Part("email") String email,
            @Part MultipartBody.Part file1
    );

    @Multipart
    @POST("roshni/api/post_job.php")
    Call<verifyBean> postjob(
            @Part("brand_id") String brand_id,
            @Part("title") String title,
            @Part("skills") String skills,
            @Part("preferred") String preferred,
            @Part("location") String location,
            @Part("experience") String experience,
            @Part("role") String role,
            @Part("gender") String gender,
            @Part("education") String education,
            @Part("hours") String hours,
            @Part("salary") String salary,
            @Part("stype") String stype
    );

    @Multipart
    @POST("roshni/api/apply_job.php")
    Call<verifyBean> apply_job(
            @Part("job_id") String job_id,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("roshni/api/update_worker_professional.php")
    Call<verifyBean> updateWorkerProfessional(
            @Part("user_id") String user_id,
            @Part("sector") String sector,
            @Part("skills") String skills,
            @Part("experience") String experience,
            @Part("employment") String employment,
            @Part("employer") String employer,
            @Part("home") String home,
            @Part("workers") String workers,
            @Part("looms") String looms,
            @Part("location") String location
    );

    @Multipart
    @POST("roshni/api/getJobListForWorker.php")
    Call<workerJobListBean> getJobListForWorker(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("roshni/api/getAppliedListForWorker.php")
    Call<workerJobListBean> getAppliedListForWorker(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("roshni/api/getJobDetailsForWorker.php")
    Call<workerJobDetailBean> getJobDetailForWorker(
            @Part("user_id") String user_id,
            @Part("jid") String jid
    );

    @GET("roshni/api/getSectors.php")
    Call<sectorBean> getSectors();

    @GET("roshni/api/getRoles.php")
    Call<sectorBean> getRoles();

    @GET("roshni/api/getSkills.php")
    Call<sectorBean> getSkills();

    @GET("roshni/api/getLocations.php")
    Call<sectorBean> getLocations();*/

}
