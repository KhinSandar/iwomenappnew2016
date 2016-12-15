package org.undp_iwomen.iwomen.model.retrofit_api;

import com.smk.model.CommentItem;
import com.smk.model.ResourceItem;

import org.smk.model.CalendarEvent;
import org.smk.model.IWomenPost;
import org.smk.model.LikeItem;
import org.smk.model.PhotoUpload;
import org.smk.model.Sticker;
import org.smk.model.SubResourceItem;
import org.smk.model.TLGTownship;
import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.data.AuthorItem;
import org.undp_iwomen.iwomen.data.PrizePointsItem;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface SMKSeverService {


    @POST(CommonConfig.CREATE_USER_PHOTO_URL)
    void postUserPhoto(
            @Body MultipartTypedOutput attachments,
            Callback<PhotoUpload> callback);

    @GET(CommonConfig.GET_CALENDAR_EVENT_BY_ID_URL)
    void getCalendarEventDetailByID(
            @Path("id") String id,
            Callback<CalendarEvent> callback);

    @GET(CommonConfig.GET_USER_POST_COUNT_BY_OBJ_ID)
    void getUserPostCountByObjID(
            @Path("id") String id,
            Callback<String> callback);

    @GET(CommonConfig.GET_STICKER_LIST_URL)
    void getStickersByPagination(
            @Query("page") int page,
            Callback<List<Sticker>> callback);

    @FormUrlEncoded
    @POST(CommonConfig.GET_COMMENT_LIST_BY_POST_ID_URL)
    void postCommentByPostID(
            @Field("postType") String postType,
            @Field("postId") String postId,
            @Field("userId") String userId,
            @Field("user_name") String user_name,
            @Field("sticker_img_path") String sticker_img_path,
            @Field("user_img_path") String user_img_path,
            @Field("comment_contents") String comment_contents,
            Callback<CommentItem> callback);

    @FormUrlEncoded
    @POST(CommonConfig.GET_COMMENT_LIST_BY_POST_ID_URL)
    void postCommentTestByPostID(
            @Field("postType") String postType,
            @Field("postId") String postId,
            @Field("userId") String userId,
            @Field("user_name") String user_name,
            @Field("user_img_path") String user_img_path,
            @Field("comment_contents") String comment_contents,
            Callback<CommentItem> callback);
//Linn for BeKnowledgable Comment
    @FormUrlEncoded
    @POST(CommonConfig.POST_COMMENT_BEKNOWLEDGABLE)
    void postCommentBeKnowledgableByPostID(
            @Field("postType") String postType,
            @Field("postId") String postId,
            @Field("userId") String userId,
            @Field("user_name") String user_name,
            @Field("user_img_path") String user_img_path,
            @Field("comment_contents") String comment_contents,
            Callback<CommentItem> callback);


    @GET(CommonConfig.GET_SUB_RESOURCE_LIST_BY_RESOURCID_URL)
    void getSubResourceByResourceIDByPagination(
            @Query("page") int page,
            @Query("resource_id") String resourceId,
            @Query("isAllow") int isAllow,
            Callback<List<SubResourceItem>> callback);

    @GET(CommonConfig.CREATE_TLG_PROFILE_URL)
    void getTLGTownshipByPagination(
            @Query("page") int page,
            Callback<List<TLGTownship>> callback);

    @GET(CommonConfig.GET_CALENDAR_EVENT)
    void getCalendarEventDetailByDate(
            @Query("date") int page,
            Callback<CalendarEvent> callback);

    @GET(CommonConfig.GET_CALENDAR_EVENT)
    void getCalendarListByDateEvent(
            @Query("date") int page,
            Callback<List<CalendarEvent>> callback);

    @GET(CommonConfig.GET_AUTHOR_BY_ID_URL)
    void getAuthorDetailByID(
            @Path("id") String id,
            Callback<AuthorItem> callback);

    @GET(CommonConfig.GET_POST_BY_DATE_URL)
    void getPostByDateByPagination(
            @Query("page") int page,
            @Query("isAllow") int isAllow,
            @Query("category") String category,
            //@Query("sorting") String sorting,
            //@Query("isAllow") Boolean isAllow,
            Callback<List<IWomenPost>> callback);

    @GET(CommonConfig.GET_POST_BY_SEARCH)
    void getPostBySearch(
            @Query("page") int page,
            @Query("isAllow") int isAllow,
            @Query("category") String category,
            @Query("keywords") String keywords,
            Callback<List<IWomenPost>> callback);


    ///api-v1/auth/login
    @FormUrlEncoded
    @POST(CommonConfig.POST_IWOMEN_POST_LIKE)
    void postIWomenPostLike(
            @Field("postId") String postId,
            @Field("userId") String userId,
            Callback<LikeItem> callback);

    @FormUrlEncoded
    @POST(CommonConfig.POST_IWOMEN_SUBRESOURCE_DETAIL_LIKES)
    void postiWomenSubResourceDetailLikes(
            @Field("postId") String postId,
            @Field("userId") String userId,
            Callback<String> callback);

    @FormUrlEncoded
    @POST(CommonConfig.POST_POSTS_LIKE)
    void postPostsLike(
            @Field("postId") String postId,
            @Field("userId") String userId,
            Callback<LikeItem> callback);

    @GET(CommonConfig.GET_ALL_AVATOR)
    void getAllAvator(
            Callback<String> callback);

    @FormUrlEncoded
    @POST(CommonConfig.CREATE_SHARE_FREIND_CODE)
    void postShareUserObject(

            @Field("user_id") int user_id,
            @Field("share_objectId") String obj_id,
            Callback<String> callback);

    @GET(CommonConfig.GET_USER_BY_ID)
    void getUserPoinsByID(
            @Path("id") String id,
            Callback<User> callback);

    @GET(CommonConfig.GET_PRIZE_POINT)
    void getPrizePoints(

            Callback<List<PrizePointsItem>> callback);

    @GET(CommonConfig.GET_WEEKLY_CONTENT_IWOMENPOST)
    void getWeeklyContentiWomenPost(
            Callback<IWomenPost> iWomenPostCallback
    );

    @GET(CommonConfig.GET_WEEKLY_CONTENT_RESOURCE)
    void getWeeklyContentResource(
            Callback<ResourceItem> iWomenPostCallback
    );
    @FormUrlEncoded
    @POST(CommonConfig.GET_CHECK_IWOMENPOST_LIKE)
    void postCheckiWomenPostLike(
            @Field("postId") String postId,
            @Field("userId") String userId,
            Callback<String> callback);
    @FormUrlEncoded
    @POST(CommonConfig.GET_CHECK_POST_LIKE)
    void postCheckPostLike(
            @Field("postId") String postId,
            @Field("userId") String userId,
            Callback<String> callback);

    @FormUrlEncoded
    @PUT(CommonConfig.UPDATE_USER_PHONE_URL)
    void postUserPhone(
            @Path("id") String id,
            @Field("phoneNo") String phoneNo,
            Callback<User> callback);

}
