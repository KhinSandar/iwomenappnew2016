package org.undp_iwomen.iwomen.model.retrofit_api;

import org.undp_iwomen.iwomen.CommonConfig;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface UserPostService {

    @GET(CommonConfig.USER_POST_URL)
    public void getUserPost(@Query("order") String s, @Query("limit") int limit, @Query("skip") int skiplimit, @Query("where") String sWhere, Callback<String> callback);

    @GET(CommonConfig.IWOMEN_POST_URL)
    public void getIWomenPost(@Query("order") String s, @Query("limit") int limit, @Query("skip") int skiplimit, @Query("where") String sWhere, Callback<String> callback);


    @GET(CommonConfig.USER_POST_URL)
    public void getPostCount(@Query("limit") int limit, @Query("count") int skiplimit, @Query("where") String sWhere, Callback<String> callback);

    @GET(CommonConfig.COMMENT_URL)
    public void getCommentCount(@Query("limit") int limit, @Query("count") int skiplimit, @Query("where") String sWhere, Callback<String> callback);


    @GET(CommonConfig.USER_POST_DETAIL_BYID_URL)
    public void getUserPostDetailById(@Path("id") String id, Callback<String> callback);

    @GET(CommonConfig.IWOMEN_POST_DETAIL_BYID_URL)
    public void getIwomenPostDetailById(@Path("id") String id, Callback<String> callback);

    @GET(CommonConfig.AUTHOR_DETAIL_BYID_URL)
    public void getAuhtorDetailById(@Path("id") String id, Callback<String> callback);


    @GET(CommonConfig.STICKERS_URL)
    public void getAllStickers( Callback<String> callback);

    @GET(CommonConfig.SISTERAPP_URL)
    public void getSisterAppList( @Query("order") String s,@Query("where") String sWhere, Callback<String> callback);


}
