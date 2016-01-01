package org.undp_iwomen.iwomen.model.parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by khinsandar on 4/17/15.
 */
@ParseClassName("IwomenPost")
public class IwomenPost extends ParseObject {

    String title;
    String content;
    String title_mm;
    String content_mm;
    String likes;
    String contentTypes;


    String createdAt;
    String updatedAt;
    String userId;

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getTitleMm() {
        return getString("titleMm");
    }

    public void setTitleMm(String titlemm) {
        put("titleMm", titlemm);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public String getContentMm() {
        return getString("content_mm");
    }

    public void setContentMm(String contentmm) {
        put("content_mm", contentmm);
    }

    public int getLikes() {
        return getInt("likes");
    }

    public void setLikes(int likes) {
        put("likes", likes);
    }

    public int getCommentCount() {
        return getInt("comment_count");
    }

    public void setCommentCount(int likes) {
        put("comment_count", likes);
    }

    public int getShareCount() {
        return getInt("share_count");
    }

    public void setShareCount(int likes) {
        put("share_count", likes);
    }

    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    public void setImageFile(ParseFile file) {
        put("image", file);
    }

    public String getContentTypes() {
        return getString("contentType");
    }

    public void setContentTypes(String contentTypes) {
        put("contentType", contentTypes);
    }

    public String getPostUploadUserImgPath() {
        return getString("postUploadUserImgPath");
    }

    public void setPostUploadUserImgPath(String contentTypes) {
        put("postUploadUserImgPath", contentTypes);
    }

    public Date getPostUploadedDate() {
        return getDate("postUploadedDate");
    }

    public void setPostUploadedDate(Date postUploadedDate) {
        put("postUploadedDate", postUploadedDate);
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

    public String getUserId() {
        return getString("userId");
    }

    public ParseUser getUser() {
        return getParseUser("userId");
    }

    public void setUserId(String userId) {
        put("userId", ParseObject.createWithoutData("_User", userId));
    }

    public String getPostUploadAuthorName() {
        return getString("postUploadName");
    }

    public void setPostUploadAuthorName(String postUploadName) {
        put("postUploadName", postUploadName);
    }

    public ParseFile getPostUploadAuthorImgFile() {
        return getParseFile("PostUploadPersonImg");
    }

    public void setPostUploadAuthorImgFile(ParseFile file) {
        put("PostUploadPersonImg", file);
    }

    public String getVideoId() {
        return getString("videoId");
    }

    public void setVideoId(String videoId) {
        put("videoId", videoId);
    }

    public String getsuggest_section() {
        return getString("suggest_section");
    }

    public void setsuggest_section(String suggest_section) {
        put("suggest_section", suggest_section);
    }

    public String getsuggest_section_eng() {
        return getString("suggest_section_eng");
    }

    public void setsuggest_section_eng(String suggest_section_eng) {
        put("suggest_section_eng", suggest_section_eng);
    }

    public void setIsAllow(boolean status){
        put("isAllow",status);
    }

    public boolean getIsAllow(){
        return getBoolean("isAllow");
    }
    /*public void setBusId(String busId) {
        put("BusId", ParseObject.createWithoutData(Bus.class, busId));
    }

    public ParseObject getBusId() {
        return getParseObject("BusId");
    }
    public void setUser(String currentUserID) {
        put("AdminUser", ParseObject.createWithoutData("_User", currentUserID));

    }*/

    public static ParseQuery<IwomenPost> getQuery() {
        return ParseQuery.getQuery(IwomenPost.class);
    }
}
