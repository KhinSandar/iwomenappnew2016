package org.undp_iwomen.iwomen.database;

import android.provider.BaseColumns;

/**
 * Created by KSD on 5/14/15.
 */
public class TableAndColumnsName {


    public interface TableNames {


        String USER = "user";
        String POST = "post";
        String USER_POST = "user_post";
        String COMMENT= "comment";

        String RESOURCE= "resource";
        String SUBRESOURCE= "sub_resource";
        String SISTERAPP= "sisterapp";

        String SUBRESOURCEDETAIL= "sub_resource_detail";

        String PROJECT_USER = "project_user";
        String FORM = "form";
    }

    public interface UserUtil{
        String USER_OBJ_ID ="user_obj_id";
        String USER_ROLE ="role";
        String USER_NAME = "user_name";
        String USER_PH = "user_ph";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_USER_TABLE = "Create Table If Not Exists " + TableNames.USER + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                USER_OBJ_ID + " Text Not Null," +
                USER_ROLE + " Text Not Null," +
                USER_NAME + " Text Not Null," +
                USER_PH + " Text Not Null," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Date Not Null," +
                UPDATED_DATE + " Date Not Null);";

    }

    public interface SubResourceDetailUtil{
        String SUB_RESOURCE_ID ="sub_resource_id";
        String USER_ID ="user_id";
        String LIKE_COUNT = "like_count";
        String STATUS = "status";



        String CREATE_SUB_RESOURCE_DETAIL = "Create Table If Not Exists " + TableNames.SUBRESOURCEDETAIL + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                SUB_RESOURCE_ID + " Text Not Null," +
                USER_ID + " Text Not Null," +
                LIKE_COUNT + " Text Not Null," +
                STATUS + " Text Not Null);" ;


    }

    public interface PostUtil{

        String POST_OBJ_ID ="post_obj_id";
        String POST_TITLE ="post_title";
        String POST_CONTENT ="content";
        String POST_CONTENT_MM ="content_mm";
        String POST_LIKES = "post_likes";
        String POST_IMG_PATH = "post_img_path";
        String POST_CONTENT_TYPES = "post_content_type";

        String POST_CONTENT_USER_ID = "post_content_user_id";
        String POST_CONTENT_USER_NAME = "post_content_user_name";
        String POST_CONTENT_USER_IMG_PATH = "post_content_user_img_path";

        String POST_CONTENT_AUTHOR_ID = "post_content_author_id";
        String POST_CONTENT_AUTHOR_ROLE = "post_content_author_role";



        //TODO TableColumnUpdate 1
        String POST_CONTENT_VIDEO_ID = "video_id";
        String POST_CONTENT_SUGGEST_TEXT= "post_content_suggest_text";
        String POST_CONTENT_TITLE_MM = "post_title_mm";
        String LIKE_STATUS = "like_status";

        String POST_CONTENT_AUTHOR_ROLE_MM = "post_content_author_role_mm";

        String CREDIT_LINK_ENG = "credit_link";
        String CREDIT_LINK_MM = "credit_link_mm";
        String CREDIT_LOGO_URL = "credit_logo_url";
        String CREDIT_NAME = "credit_name";

        String COMMENT_COUNT = "comment_count";
        String SHARE_COUNT = "share_count";


        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_POST_TABLE = "Create Table If Not Exists " + TableNames.POST + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                POST_OBJ_ID + " Text Not Null," +
                POST_TITLE + " Text Not Null," +
                POST_CONTENT + " Text Not Null," +
                POST_CONTENT_MM+ " Text Not Null," +
                POST_LIKES + " INTEGER Not Null," +
                POST_IMG_PATH + " Text Not Null," +
                POST_CONTENT_TYPES + " Text Not Null," +

                POST_CONTENT_USER_ID + " Text Not Null," +
                POST_CONTENT_USER_NAME + " Text Not Null," +
                POST_CONTENT_USER_IMG_PATH + " Text Not Null," +

                POST_CONTENT_AUTHOR_ID + " Text Not Null," +
                POST_CONTENT_AUTHOR_ROLE + " Text Not Null," +

                POST_CONTENT_VIDEO_ID + " Text Not Null," +
                POST_CONTENT_SUGGEST_TEXT + " Text Not Null," +
                POST_CONTENT_TITLE_MM + " Text Not Null," +

                LIKE_STATUS + " Text Not Null," +

                POST_CONTENT_AUTHOR_ROLE_MM + " Text Not Null," +
                CREDIT_LINK_ENG + " Text Not Null," +
                CREDIT_LINK_MM + " Text Not Null," +
                CREDIT_LOGO_URL + " Text Not Null," +
                CREDIT_NAME + " Text Not Null," +
                COMMENT_COUNT + " INTEGER Not Null," +
                SHARE_COUNT + " INTEGER Not Null," +



                STATUS + " Text Not Null," +
                CREATED_DATE + " DateTime Not Null," +
                UPDATED_DATE + " DateTime Not Null,"+
                " UNIQUE ("+ POST_OBJ_ID+") On Conflict Ignore);";

    }
    public interface UserPostUtil{

        String POST_OBJ_ID ="post_obj_id";
        String POST_TITLE ="post_title";
        String POST_CONTENT ="content";
        String POST_CONTENT_MM ="content_mm";
        String POST_LIKES = "post_likes";
        String POST_IMG_PATH = "post_img_path";
        String POST_CONTENT_TYPES = "post_content_type";

        String POST_CONTENT_USER_ID = "post_content_user_id";
        String POST_CONTENT_USER_NAME = "post_content_user_name";
        String POST_CONTENT_USER_IMG_PATH = "post_content_user_img_path";


        String POST_CONTENT_VIDEO_ID = "video_id";
        String POST_CONTENT_SUGGEST_TEXT= "post_content_suggest_text";
        String POST_CONTENT_TITLE_MM = "post_title_mm";
        String LIKE_STATUS = "like_status";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_USER_POST_TABLE = "Create Table If Not Exists " + TableNames.USER_POST + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                POST_OBJ_ID + " Text Not Null," +
                POST_TITLE + " Text Not Null," +
                POST_CONTENT + " Text Not Null," +
                POST_CONTENT_MM+ " Text Not Null," +
                POST_LIKES + " INTEGER Not Null," +
                POST_IMG_PATH + " Text Not Null," +
                POST_CONTENT_TYPES + " Text Not Null," +

                POST_CONTENT_USER_ID + " Text Not Null," +
                POST_CONTENT_USER_NAME + " Text Not Null," +
                POST_CONTENT_USER_IMG_PATH + " Text Not Null," +

                POST_CONTENT_VIDEO_ID + " Text Not Null," +
                POST_CONTENT_SUGGEST_TEXT + " Text Not Null," +
                POST_CONTENT_TITLE_MM + " Text Not Null," +

                LIKE_STATUS + " Text Not Null," +
                STATUS + " Text Not Null," +
                CREATED_DATE + " DateTime Not Null," +
                UPDATED_DATE + " DateTime Not Null,"+
                " UNIQUE ("+ POST_OBJ_ID+") On Conflict Ignore);";

    }
    public interface CommentUtil{
        String COMMENT_OBJ_ID ="comment_obj_id";
        String TITLE ="role";
        String CONTENTS = "user_name";

        String COMMENT_CONTENT_USER_ID = "comment_content_user_id";
        String COMMENT_CONTENT_USER_NAME = "comment_content_user_name";
        String COMMENT_CONTENT_USER_IMG_PATH = "comment_content_user_img_path";



        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_COMMENT_TABLE = "Create Table If Not Exists " + TableNames.COMMENT + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                COMMENT_OBJ_ID + " Text Not Null," +
                PostUtil.POST_OBJ_ID + " Text Not Null," +
                TITLE + " Text Not Null," +
                CONTENTS + " Text Not Null," +

                COMMENT_CONTENT_USER_ID + " Text Not Null," +
                COMMENT_CONTENT_USER_NAME + " Text Not Null," +
                COMMENT_CONTENT_USER_IMG_PATH + " Text Not Null," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Text Not Null," +
                UPDATED_DATE + " Text Not Null);";

    }

    public interface ResourceUtil{
        String RESOURCE_OBJ_ID ="resource_obj_id";
        String RESOURCE_TITLE_MM ="resource_title_mm";
        String RESOURCE_TITLE_ENG = "resource_title_eng";
        String RESOURCE_LOGO_IMG_PATH = "resource_icon_img";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_RESOURCE_TABLE = "Create Table If Not Exists " + TableNames.RESOURCE + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                RESOURCE_OBJ_ID + " Text Not Null," +
                RESOURCE_TITLE_MM + " Text Not Null," +
                RESOURCE_TITLE_ENG + " Text Not Null," +
                RESOURCE_LOGO_IMG_PATH + " Text Not Null," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Date Not Null," +
                UPDATED_DATE + " DateTime Not Null,"+
                " UNIQUE ("+ RESOURCE_OBJ_ID+") On Conflict Ignore);";

    }

    public interface SubResourceUtil{
        String SUB_RESOURCE_OBJ_ID ="sub_resource_obj_id";

        String SUB_RESOURCE_AUTHOR_NAME ="authorName";
        String SUB_RESOURCE_AUTHOR_ID ="author_id";
        String SUB_RESOURCE_AUTHOR_IMG_PATH ="author_img_url";
        String SUB_RESOURCE_TITLE_MM ="sub_resource_title_mm";
        String SUB_RESOURCE_TITLE_ENG = "sub_resource_title_eng";
        String SUB_RESOURCE_CONTENT_MM ="sub_resouce_content_mm";
        String SUB_RESOURCE_CONTENT_ENG ="sub_resouce_content_eng";
        String SUB_RESOURCE_LOGO_IMG_PATH = "sub_res_icon_img_url";
        String SUB_RESOURCE_POSTED_DATE = "sub_res_posted_date";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_SUB_RESOURCE_TABLE = "Create Table If Not Exists " + TableNames.SUBRESOURCE + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                SUB_RESOURCE_OBJ_ID + " Text Not Null," +
                SUB_RESOURCE_AUTHOR_NAME+ " Text Not Null," +
                SUB_RESOURCE_AUTHOR_ID+ " Text Not Null," +
                SUB_RESOURCE_AUTHOR_IMG_PATH+ " Text Not Null," +

                SUB_RESOURCE_CONTENT_MM+ " Text Not Null," +
                SUB_RESOURCE_CONTENT_ENG+ " Text Not Null," +

                SUB_RESOURCE_TITLE_MM + " Text Not Null," +
                SUB_RESOURCE_TITLE_ENG + " Text Not Null," +
                SUB_RESOURCE_LOGO_IMG_PATH + " Text Not Null," +

                SUB_RESOURCE_POSTED_DATE+ " Date Not Null," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Date Not Null," +
                UPDATED_DATE + " DateTime Not Null,"+
                " UNIQUE ("+ SUB_RESOURCE_OBJ_ID+") On Conflict Ignore);";

    }

    public interface SisterAppUtil{
        String SISTER_APP_OBJ_ID ="sister_app_obj_id";
        String SISTER_APP_NAME ="app_name";
        String SISTER_APP_LINK = "app_link";
        String SISTER_APP_PACKAGE_NAME = "app_package_name";
        String SISTER_APP_LOGO_IMG_PATH = "app_img";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_SISTERAPP_TABLE = "Create Table If Not Exists " + TableNames.SISTERAPP + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +

                SISTER_APP_OBJ_ID + " Text Not Null," +
                SISTER_APP_NAME + " Text Not Null," +
                SISTER_APP_LINK + " Text Not Null," +
                SISTER_APP_PACKAGE_NAME + " Text Not Null," +
                SISTER_APP_LOGO_IMG_PATH + " Text Not Null," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Date Not Null," +
                UPDATED_DATE + " DateTime Not Null,"+
                " UNIQUE ("+ SISTER_APP_OBJ_ID+") On Conflict Ignore);";

    }




    public interface ParseUtil {
        String PARSE_ID = "parseId";
    }

    /*public interface ProjectUserTableUtil {
        String PROJECT_USER_ID = "project_user_id";

        String STATUS = "status";
        String CREATED_DATE ="created_at";
        String UPDATED_DATE = "updated_at";

        String CREATE_CITY_TABLE = "Create Table If Not Exists " + TableNames.PROJECT_USER + "(" +
                BaseColumns._ID + " Integer Primary Key Autoincrement," +
                ProjectUtil.PROJECT_ID + " Text Not Null ," +
                UserUtil.USER_ID + " Text Not Null ," +

                STATUS + " Text Not Null," +
                CREATED_DATE + " Text Not Null," +
                UPDATED_DATE + " Text Not Null);";

    }*/



}
