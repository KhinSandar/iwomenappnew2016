package org.undp_iwomen.iwomen.model.retrofit_api;

import org.undp_iwomen.iwomen.CommonConfig;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface CommentService {


    @GET(CommonConfig.COMMENT_URL)
    public void getCommentByPostId(@Query("where") String s, @Query("order") String orderby, Callback<String> callback);



}
