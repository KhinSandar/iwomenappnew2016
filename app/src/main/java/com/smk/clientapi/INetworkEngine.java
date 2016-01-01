package com.smk.clientapi;

import com.smk.model.APKVersion;
import com.smk.model.Answer;
import com.smk.model.CompetitionQuestion;
import com.smk.model.GroupUser;
import com.smk.model.Review;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

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
			Callback<Integer> callback);

	@FormUrlEncoded
	@POST("/api-v1/review")
	void postReview(
			@Field("user_id") String user_id,
			@Field("ratings") Double ratings,
			@Field("function") String function,
			Callback<Review> callback);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
