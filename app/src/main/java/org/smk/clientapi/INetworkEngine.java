package org.smk.clientapi;

import com.google.gson.JsonObject;
import com.smk.model.Categories;
import com.smk.model.CommentItem;
import com.smk.model.ResourceItem;
import com.smk.model.SisterAppItem;

import org.smk.model.APKVersion;
import org.smk.model.Answer;
import org.smk.model.CalendarEvent;
import org.smk.model.CompetitionQuestion;
import org.smk.model.GroupUser;
import org.smk.model.IWomenPost;
import org.smk.model.PhotoUpload;
import org.smk.model.Rating;
import org.smk.model.Review;
import org.smk.model.TLGTownship;
import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

public interface INetworkEngine {

    @GET("/api/v1/competition")
    void getCompetitionQuestion(
            @Query("access_token") String token,
            @Query("user_id") String user_id,
            Callback<CompetitionQuestion> callback);

    @GET("/api/v1/competitiongroup")
    void getCompetitionGroupUser(
            @Query("access_token") String token,
            @Query("user_id") String user_id,
            @Query("question_id") Integer question_id,
            Callback<List<GroupUser>> callback);

    @FormUrlEncoded
    @POST("/api/v1/competitionanswer")
    void postCompetitionAnswer(
            @Field("access_token") String access_token,
            @Field("answer1_id") Integer answer1_id,
            @Field("answer1") String answer1,
            @Field("answer_mm1") String answer_mm1,
            @Field("answer2_id") Integer answer2_id,
            @Field("answer2") String answer2,
            @Field("answer_mm2") String answer_mm2,
            @Field("answer3_id") Integer answer3_id,
            @Field("answer3") String answer3,
            @Field("answer_mm3") String answer_mm3,
            @Field("question_id") Integer question_id,
            @Field("group_user_id") Integer group_user_id,
            Callback<String> callback);

    @FormUrlEncoded
    @POST("/api/v1/mutipleAnswers")
    void postCompetitionMutipleAnswer(
            @Field("answers") String answer,
            @Field("user_id") Integer user_id,
            Callback<String> callback);

    @GET("/api/v1/app")
    void getAPKVersion(
            @Query("access_token") String access_token,
            Callback<APKVersion> callback);

    @GET("/api/v1/competitionanswer/{id}")
    void getUserAnswer(
            @Path("id") Integer id,
            Callback<List<Answer>> callback);

    @GET("/api/v1/review")
    void getReview(
            @Query("function") String function,
            Callback<Rating> callback);

    @FormUrlEncoded
    @POST("/api/v1/review")
    void postReview(
            @Field("user_id") String user_id,
            @Field("ratings") Double ratings,
            @Field("function") String function,
            @Field("review") String review,
            Callback<Review> callback);

    @FormUrlEncoded
    @POST("/api/v1/gcms")
    void postRegisterNotification(
            @Field("reg_id") String regId,
            @Field("device_id") String devId,
            @Field("user_id") String user_id,
            Callback<JsonObject> callback);

    @POST("/api/v1/file/imageUpload")
    void uploadImage(
            @Body MultipartTypedOutput attachments,
            Callback<PhotoUpload> callback);

    //KSD URL
    //http://api.shopyface.com/api/v1/auth/photo
    @POST(CommonConfig.CREATE_USER_PHOTO_URL)
    void postUserPhoto(
            @Body MultipartTypedOutput attachments,
            Callback<PhotoUpload> callback);

    @FormUrlEncoded
    @POST(CommonConfig.CREATE_REGISTER_URL)
    void postCreateUser(
            @Field("username") String name,
            @Field("password") String pwd,
            @Field("phoneNo") String ph,
            @Field("profileimage") String photo,
            @Field("isTlgTownshipExit") int isTlg,
            @Field("tlg_city_address") String tlg_city_address,// Role ?

            @Field("tlg_city") String sate,
            @Field("tlg_country") String country,
            Callback<User> callback);

    @POST(CommonConfig.UPDATE_USER_URL)
    void postUpdateUser(
            @Path("id") Integer id,
            @Query("username") String name,
            @Query("password") String pwd,
            @Query("phoneNo") String ph,
            @Query("profileimage") String photo,
            @Query("isTlgTownshipExit") String isTlg,
            @Query("tlg_city_address") String tlg_city_address,// Role ?

            Callback<User> callback);

    ///api-v1/auth/login
    @FormUrlEncoded
    @POST(CommonConfig.CREATE_LOGIN_URL)
    void postLogin(
            @Field("username") String email,
            @Field("password") String pwd,
            Callback<User> callback);

    @GET(CommonConfig.CREATE_TLG_PROFILE_URL)
    void getTLGTownship(

            Callback<List<TLGTownship>> callback);

    @Multipart
    @POST(CommonConfig.CREATE_IWOMEN_POST_PHOTO_URL)
    void postiWomenPostPhoto(
            @Part("image") TypedFile image,
            Callback<String> callback);

    @GET(CommonConfig.GET_IWOMEN_POST_BY_DATE_URL)
    void getIWomenPostByDateByPagination(
            @Query("page") int page,
            @Query("sorting") String sorting,
            @Query("isAllow") int isAllow,
            Callback<List<IWomenPost>> callback);

    @GET(CommonConfig.GET_CALENDDARE_EVENT_LIST_BY_DATE_URL)
    void getEventListByDate(
            @Query("date") String date,
            Callback<List<CalendarEvent>> callback);

    @GET(CommonConfig.GET_CALENDAR_EVENT)
    void getCalendarListByDateMothEvent(
            @Query("date") String date,
            Callback<List<CalendarEvent>> callback);

    @GET(CommonConfig.GET_RESOURCE_URL)
    void getResourceByPagination(
            @Query("page") int page,
            @Query("isAllow") int isAllow,
            Callback<List<ResourceItem>> callback);
    @GET(CommonConfig.GET_SISTER_APP_LIST_URL)
    void getSisterAppByPagination(
            @Query("page") int page,
            Callback<List<SisterAppItem>> callback);

    @GET(CommonConfig.GET_COMMENT_LIST_BY_POST_ID_URL)
    void getCommentlistByPostIDByPagination(
            @Query("page") int page,@Query("post_id") String postId,
            Callback<List<CommentItem>> callback);

    @GET(CommonConfig.GET_IWOMEN_POST_BY_POST_ID_URL)
    void getIwomenPostByPostID(
            @Path("id") String id,
            Callback<IWomenPost> callback);

    @GET(CommonConfig.GET_CATEGORIES_LIST__URL)
    void getCategoriesByPagination(
            @Query("page") int page,
            Callback<List<Categories>> callback);

    @FormUrlEncoded
    @POST(CommonConfig.CREATE_CALENDAR_EVENT_URL)
    void postCreateCalendarEventMM(
            @Field("userId") String userID,
            @Field("description_mm") String description_mm,
            @Field("title_mm") String title_mm,
            @Field("location") String location,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,

            Callback<CalendarEvent> callback);

    @FormUrlEncoded
    @POST(CommonConfig.CREATE_CALENDAR_EVENT_URL)
    void postCreateCalendarEventEng(
            @Field("userId") String userID,
            @Field("description") String description,
            @Field("title") String title,
            @Field("location") String location,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,

            Callback<CalendarEvent> callback);





}

