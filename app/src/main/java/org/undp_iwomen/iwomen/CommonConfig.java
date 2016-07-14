package org.undp_iwomen.iwomen;

/**
 * Created by khinsandar on 3/23/15.
 */
public class CommonConfig {
    public static final String AUTHORITY = "org.undp_iwomen.iwomen";

    public static final String SHARE_PREFERENCE_USER_INFO = "org.undp_iwomen.iwomen.user.info";
    public static final String IS_LOGIN = "hasLogin";
    public static final String USER_OBJ_ID = "objectId";
    public static final String USER_ID = "user_Id";
    public static final String USER_NAME = "username";
    public static final String USER_PH = "ph";
    public static final String USER_EMAIL= "email";
    public static final String USER_FBID_INCLUDE = "FbId_Include";
    public static final String USER_FBID = "FbId";
    public static final String REGISTER_MSG = "register_msg";

    public static final String USER_PWD = "password";
    public static final String USER_CON_PWD = "confirmpwd";
    public static final String USER_STATE = "state";
    public static final String USER_COUNTRY = "country";
    public static final String USER_TLG_ID = "tlg_id";
    public static final String USER_TLG_NAME = "tlg_name";

    public static final String USER_ISWORTH = "Isworth";


    public static final String USER_UPLOAD_IMG_NAME = "img_name";
    public static final String USER_UPLOAD_IMG_URL= "user_img_url";

    public static final String USER_ROLE = "user_role";

    public static final String USER_CODE = "user_code";
    public static final String USER_POINTS = "user_points";
    public static final String USER_SHARE_STATUS = "user_share_status";


    public static final String USER_IMAGE_PATH = "ImagePath";

    public static final String USER_GENDER= "user_gender";

    public static final String DUPLICATE_ERROR= "duplicate_err";


    //https://api.parse.com/1/classes/City?X-Parse-Application-Id=OUN2VvuU6SN6DcRIDq3bT0ovJPXAk630qHVNJ9Gk&X-Parse-REST-API-Key=wxZB1WZBLzHEwfkUMToL0ykTLaiWY7Z1NuzfADLO
    public static final String BASE_SMK_URL = "http://api.iwomenapp.org";
    public static final String BASE_URL = "https://api.parse.com/1";

    public static final String COMMENT_URL = "/classes/Comment";

    public static final String TLGPROFILE_URL = "/classes/TlgProfile";

    public static final String TLGPROFILE_DETAILBYID_URL = "/classes/TlgProfile/{id}";



    public static final String USER_POST_URL = "/classes/Post";

    public static final String USER_POST_DETAIL_BYID_URL = "/classes/Post/{id}";

    public static final String IWOMEN_POST_URL = "/classes/IwomenPost";

    public static final String IWOMEN_POST_DETAIL_BYID_URL = "/classes/IwomenPost/{id}";

    public static final String RESOURCE_URL = "/classes/Resources";


    public static final String SISTERAPP_URL = "/classes/SisterDownloadApp";

    public static final String SUB_RESOURCE_URL = "/classes/SubResourceDetail";

    public static final String AUTHOR_DETAIL_BYID_URL = "/classes/Author/{id}";


    public static final String STICKERS_URL = "/classes/StickerStore";


    public static final String SHARE_URL ="https://www.facebook.com/iwomenApp";

    public static final String GET_CALENDAR_EVENT = "/api/v1/calendars";

    public static final String GET_IWOMEN_POST_BY_SEARCH = "/api/v1/searchIwomenPost";

    public static final String GET_POST_BY_SEARCH = "/api/v1/searchPosts";

    public int getPostMaxCharacterCount () {
        int value = 140;
        return value;
    }

    //http://shopyface.com/api/v1/usersUpload
    public static final String CREATE_USER_PHOTO_URL ="/api/v1/usersUpload";

    public static final String CREATE_POST_PHOTO_URL ="/api/v1/file/imageUpload";

    //http://shopyface.com/api/v1/file/imageUpload
    public static final String CREATE_IWOMEN_POST_PHOTO_URL ="api/v1/file/imageUpload";

    public static final String CREATE_TLG_PROFILE_URL ="/api/v1/tlgProfiles";

    public static final String CREATE_LOGIN_URL ="/api/v1/login";

    public static final String CREATE_REGISTER_URL = "/api/v1/users";

    public static final String UPDATE_USER_URL = "/api/v1/users/{id}";

    public static final String GET_IWOMEN_POST_BY_DATE_URL = "/api/v1/iwomenPosts";

    public static final String GET_RESOURCE_URL = "/api/v1/resources";

    public static final String GET_SISTER_APP_LIST_URL = "/api/v1/sisterDownloadApps";

    public static final String GET_COMMENT_LIST_BY_POST_ID_URL = "/api/v1/comments";

    public static final String GET_IWOMEN_POST_BY_POST_ID_URL = "/api/v1/iwomenPosts/{id}";

    public static final String GET_CATEGORIES_LIST__URL = "/api/v1/categories";

    public static final String CREATE_CALENDAR_EVENT_URL= "/api/v1/calendars";

    public static final String GET_CALENDAR_EVENT_BY_ID_URL = "/api/v1/calendars/{id}";

    public static final String GET_USER_POST_COUNT_BY_OBJ_ID = "/api/v1/postCounts/{id}";

    public static final String GET_STICKER_LIST_URL = "/api/v1/stickerStores";

    public static final String GET_SUB_RESOURCE_LIST_BY_RESOURCID_URL="/api/v1/subResourceDetails";

    public static final String GET_AUTHOR_BY_ID_URL = "/api/v1/authors/{id}";

    public static final String GET_POST_BY_DATE_URL = "/api/v1/posts";

    public static final String GET_CALENDDARE_EVENT_LIST_BY_DATE_URL = "/api/v1/events";

    public static final String POST_IWOMEN_POST_LIKE = "/api/v1/iwomenPostLikes";

    public static final String POST_POSTS_LIKE = "/api/v1/postLikes";

    public static final String CREATE_NEW_POST = "/api/v1/posts";

    public static final String CREATE_NEW_WOMENPOST = "/api/v1/iwomenPosts";

    public static final String GET_ALL_AVATOR = "/api/v1/avators";


    public static final String CREATE_SHARE_FREIND_CODE = "/api/v1/shareUsers";

    public static final String GET_USER_BY_ID = "/api/v1/users/{id}";

    public static final String GET_PRIZE_POINT= "/api/v1/pointPrices";


}
