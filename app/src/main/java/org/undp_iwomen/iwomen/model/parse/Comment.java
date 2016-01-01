package org.undp_iwomen.iwomen.model.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by khinsandar on 4/17/15.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {


    String createdAt;
    String updatedAt;
    String userId;

    public String getcomment_contents() {
        return getString("comment_contents");
    }

    public void setcomment_contents(String comment_contents) {
        put("comment_contents", comment_contents);
    }

    public Date getcomment_created_time() {
        return getDate("comment_created_time");
    }

    public void setcomment_created_time(Date comment_created_time) {
        put("comment_created_time", comment_created_time);
    }


    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public void setCreatedAt(Date createdAt) {
        put("createdAt", createdAt);
    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public void setUpdatedAt(Date updatedAt) {
        put("updatedAt", updatedAt);
    }

    public void setpostId(String postId) {
        put("postId", ParseObject.createWithoutData(Post.class, postId));
    }

    public ParseObject getpostId() {
        return getParseObject("postId");
    }

    public String getUserId() {
        return getString("userId");
    }

    public ParseUser getUser() {
        return getParseUser("userId");
    }

    public String getUserName() {
        return getString("user_name");
    }

    public void setUserName(String user_name) {
        put("user_name", user_name);
    }

    public String getUserImgPathName() {
        return getString("user_img_path");
    }

    public void setUserImgPathName(String user_img) {
        put("user_img_path", user_img);
    }

    public void setUserId(String userId) {
        put("userId", ParseObject.createWithoutData("_User", userId));
    }


    public static ParseQuery<Comment> getQuery() {
        return ParseQuery.getQuery(Comment.class);
    }
}
