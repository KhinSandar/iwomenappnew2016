package org.undp_iwomen.iwomen.model.retrofit_api;

import org.smk.model.CalendarEvent;
import org.smk.model.IWomenPost;
import org.smk.model.PhotoUpload;
import org.smk.model.Sticker;
import org.smk.model.SubResourceItem;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.data.AuthorItem;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
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

            @Field("postId") String postId,
            @Field("userId") String userId,
            @Field("user_name") String user_name,
            @Field("sticker_img_path") String sticker_img_path,
            @Field("user_img_path") String user_img_path,
            @Field("comment_contents") String comment_contents,
            Callback<CalendarEvent> callback);

    @FormUrlEncoded
    @POST(CommonConfig.GET_COMMENT_LIST_BY_POST_ID_URL)
    void postCommentTestByPostID(

            @Field("postId") String postId,
            @Field("userId") String userId,
            @Field("user_name") String user_name,
            @Field("user_img_path") String user_img_path,
            @Field("comment_contents") String comment_contents,
            Callback<CalendarEvent> callback);


    @GET(CommonConfig.GET_SUB_RESOURCE_LIST_BY_RESOURCID_URL)
    void getSubResourceByResourceIDByPagination(
            @Query("page") int page,
            @Query("resource_id") String resourceId,
            Callback<List<SubResourceItem>> callback);

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
}
