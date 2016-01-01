package org.undp_iwomen.iwomen.provider;

import android.net.Uri;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;


/**
 * Created by thitoo on 5/14/15.
 */
public class IwomenProviderData {



    public static final String CONTENT_AUTHORITY = CommonConfig.AUTHORITY;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);


    private static final String USER_PATH = "user";
    private static final String  POST_PATH = "post";
    private static final String  USER_POST_PATH = "user_post";
    private static final String  COMMENT_PATH = "comment";
    private static final String  RESOURCE_PATH = "resource";
    private static final String  SUB_RESOURCE_PATH = "sub_resource";
    private static final String  SISTERAPP_PATH = "sisterapp";



    public static class UserProvider implements TableAndColumnsName.UserUtil{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(USER_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.user";

        public static Uri buildContentUri(String  id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

    }

    public static class PostProvider implements TableAndColumnsName.PostUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(POST_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.post";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }

    public static class CommentProvider implements TableAndColumnsName.CommentUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(COMMENT_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.comment";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }

    public static class UserPostProvider implements TableAndColumnsName.UserPostUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(USER_POST_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.user_post";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }


    public static class ResourceProvider implements TableAndColumnsName.ResourceUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(RESOURCE_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.resource";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }

    public static class SubResourceProvider implements TableAndColumnsName.SubResourceUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(SUB_RESOURCE_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.sub_resource";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }

    public static class SisterAppProvider implements TableAndColumnsName.SubResourceUtil{
        public static final Uri CONTETN_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(SISTERAPP_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.undp_iwomen.sisterapp";

        public static Uri buildContentUri(String id){
            return CONTETN_URI.buildUpon().appendPath(id).build();
        }




    }




}
