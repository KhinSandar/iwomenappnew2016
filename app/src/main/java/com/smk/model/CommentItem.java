package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("comment_contents")
    @Expose
    private String commentContents;
    @SerializedName("comment_created_time")
    @Expose
    private String commentCreatedTime;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("sticker_img_path")
    @Expose
    private String stickerImgPath;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("user_img_path")
    @Expose
    private String userImgPath;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("human_created_at")
    @Expose
    private String humanCreatedAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * @param objectId The objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return The commentContents
     */
    public String getCommentContents() {
        return commentContents;
    }

    /**
     * @param commentContents The comment_contents
     */
    public void setCommentContents(String commentContents) {
        this.commentContents = commentContents;
    }

    /**
     * @return The commentCreatedTime
     */
    public String getCommentCreatedTime() {
        return commentCreatedTime;
    }

    /**
     * @param commentCreatedTime The comment_created_time
     */
    public void setCommentCreatedTime(String commentCreatedTime) {
        this.commentCreatedTime = commentCreatedTime;
    }

    /**
     * @return The postId
     */
    public String getPostId() {
        return postId;
    }

    /**
     * @param postId The postId
     */
    public void setPostId(String postId) {
        this.postId = postId;
    }

    /**
     * @return The stickerImgPath
     */
    public String getStickerImgPath() {
        return stickerImgPath;
    }

    /**
     * @param stickerImgPath The sticker_img_path
     */
    public void setStickerImgPath(String stickerImgPath) {
        this.stickerImgPath = stickerImgPath;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The userImgPath
     */
    public String getUserImgPath() {
        return userImgPath;
    }

    /**
     * @param userImgPath The user_img_path
     */
    public void setUserImgPath(String userImgPath) {
        this.userImgPath = userImgPath;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }


    public String getHumanCreatedAt() {
        return humanCreatedAt;
    }

    public void setHumanCreatedAt(String humanCreatedAt) {
        this.humanCreatedAt = humanCreatedAt;
    }
}
