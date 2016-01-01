package org.undp_iwomen.iwomen;

/**
 * Created by khinsandar on 3/23/15.
 */
public class CommonConfig {
    public static final String AUTHORITY = "org.undp_iwomen.iwomen";

    public static final String SHARE_PREFERENCE_USER_INFO = "org.undp_iwomen.iwomen.user.info";
    public static final String IS_LOGIN = "hasLogin";
    public static final String USER_OBJ_ID = "objectId";
    public static final String USER_NAME = "username";
    public static final String USER_PH = "ph";
    public static final String USER_EMAIL= "email";
    public static final String USER_FBID_INCLUDE = "FbId_Include";
    public static final String USER_FBID = "FbId";

    public static final String USER_IMAGE_PATH = "ImagePath";




    //https://api.parse.com/1/classes/City?X-Parse-Application-Id=OUN2VvuU6SN6DcRIDq3bT0ovJPXAk630qHVNJ9Gk&X-Parse-REST-API-Key=wxZB1WZBLzHEwfkUMToL0ykTLaiWY7Z1NuzfADLO
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
    public int getPostMaxCharacterCount () {
        int value = 140;
        return value;
    }


}
