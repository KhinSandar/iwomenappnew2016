package com.smk.clientapi;

import com.smk.model.APKVersion;
import com.smk.model.Answer;
import com.smk.model.CompetitionQuestion;
import com.smk.model.GroupUser;
import com.smk.model.Rating;
import com.smk.model.Review;
import com.smk.model.TLGTownship;
import com.smk.model.User;

import org.undp_iwomen.iwomen.CommonConfig;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface INetworkEngine {

    @GET("/api-v1/competition")
    void getCompetitionQuestion(
            @Query("access_token") String token,
            @Query("user_id") String user_id,
            Callback<CompetitionQuestion> callback);

    @GET("/api-v1/competitiongroup")
    void getCompetitionGroupUser(
            @Query("access_token") String token,
            @Query("user_id") String user_id,
            @Query("question_id") Integer question_id,
            Callback<List<GroupUser>> callback);

    @FormUrlEncoded
    @POST("/api-v1/competitionanswer")
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

    @GET("/api-v1/app")
    void getAPKVersion(
            @Query("access_token") String access_token,
            Callback<APKVersion> callback);

    @GET("/api-v1/competitionanswer/{id}")
    void getUserAnswer(
            @Path("id") Integer id,
            Callback<List<Answer>> callback);

    @GET("/api-v1/review")
    void getReview(
            @Query("function") String function,
            Callback<Rating> callback);

    @FormUrlEncoded
    @POST("/api-v1/review")
    void postReview(
            @Field("user_id") String user_id,
            @Field("ratings") Double ratings,
            @Field("function") String function,
            @Field("review") String review,
            Callback<Review> callback);

    //KSD URL
    //http://api.shopyface.com/api-v1/auth/photo
    @Multipart
    @POST(CommonConfig.CREATE_USER_PHOTO_URL)
    void postUserPhoto(
            @Part("image") TypedFile image,
            Callback<String> callback);

    @FormUrlEncoded
    @POST("/api/v1/users")
    void postCreateUser(
            @Field("username") String name,
            @Field("password") String pwd,
            @Field("phoneNo") String ph,
            @Field("profileimage") String add,
            @Field("isTlgTownshipExit") String photo,
            @Field("tlg_city_address") String role,// Role ?

            Callback<User> callback);


    ///api-v1/auth/login
    @FormUrlEncoded
    @POST("/api/v1/login")
    void postLogin(
            @Field("username") String email,
            @Field("password") String pwd,
            Callback<User> callback);

    @GET("/api/v1/tlgProfiles")
    void getTLGTownship(

            Callback<List<TLGTownship>> callback);

}
