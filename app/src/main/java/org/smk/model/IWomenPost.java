package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IWomenPost implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("audioFile")
    @Expose
    private String audioFile;
    @SerializedName("authorId")
    @Expose
    private String authorId;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("content_mm")
    @Expose
    private String contentMm;
    @SerializedName("credit_link")
    @Expose
    private String creditLink;
    @SerializedName("credit_link_mm")
    @Expose
    private String creditLinkMm;
    @SerializedName("credit_logo_url")
    @Expose
    private String creditLogoUrl;
    @SerializedName("credit_name")
    @Expose
    private String creditName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("isAllow")
    @Expose
    private Boolean isAllow;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("postUploadName")
    @Expose
    private String postUploadName;
    @SerializedName("postUploadNameMM")
    @Expose
    private String postUploadNameMM;

    @SerializedName("postUploadPersonImg")
    @Expose
    private String postUploadPersonImg;
    @SerializedName("postUploadUserImgPath")
    @Expose
    private String postUploadUserImgPath;
    @SerializedName("postUploadedDate")
    @Expose
    private String postUploadedDate;
    @SerializedName("post_author_role")
    @Expose
    private String postAuthorRole;
    @SerializedName("post_author_role_mm")
    @Expose
    private String postAuthorRoleMm;
    @SerializedName("share_count")
    @Expose
    private Integer shareCount;
    @SerializedName("suggest_section_eng")
    @Expose
    private String suggestSectionEng;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("titleMm")
    @Expose
    private String titleMm;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

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
     * @return The audioFile
     */
    public String getAudioFile() {
        return audioFile;
    }

    /**
     * @param audioFile The audioFile
     */
    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    /**
     * @return The authorId
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId The authorId
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * @return The commentCount
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount The comment_count
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return The contentMm
     */
    public String getContentMm() {
        return contentMm;
    }

    /**
     * @param contentMm The content_mm
     */
    public void setContentMm(String contentMm) {
        this.contentMm = contentMm;
    }

    /**
     * @return The creditLink
     */
    public String getCreditLink() {
        return creditLink;
    }

    /**
     * @param creditLink The credit_link
     */
    public void setCreditLink(String creditLink) {
        this.creditLink = creditLink;
    }

    /**
     * @return The creditLinkMm
     */
    public String getCreditLinkMm() {
        return creditLinkMm;
    }

    /**
     * @param creditLinkMm The credit_link_mm
     */
    public void setCreditLinkMm(String creditLinkMm) {
        this.creditLinkMm = creditLinkMm;
    }

    /**
     * @return The creditLogoUrl
     */
    public String getCreditLogoUrl() {
        return creditLogoUrl;
    }

    /**
     * @param creditLogoUrl The credit_logo_url
     */
    public void setCreditLogoUrl(String creditLogoUrl) {
        this.creditLogoUrl = creditLogoUrl;
    }

    /**
     * @return The creditName
     */
    public String getCreditName() {
        return creditName;
    }

    /**
     * @param creditName The credit_name
     */
    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The isAllow
     */
    public Boolean getIsAllow() {
        return isAllow;
    }

    /**
     * @param isAllow The isAllow
     */
    public void setIsAllow(Boolean isAllow) {
        this.isAllow = isAllow;
    }

    /**
     * @return The likes
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * @param likes The likes
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * @return The postUploadName
     */
    public String getPostUploadName() {
        return postUploadName;
    }

    /**
     * @param postUploadName The postUploadName
     */
    public void setPostUploadName(String postUploadName) {
        this.postUploadName = postUploadName;
    }
    /**
     *
     * @return
     * The postUploadNameMM
     */
    public String getPostUploadNameMM() {
        return postUploadNameMM;
    }

    /**
     *
     * @param postUploadNameMM
     * The postUploadNameMM
     */
    public void setPostUploadNameMM(String postUploadNameMM) {
        this.postUploadNameMM = postUploadNameMM;
    }

    /**
     * @return The postUploadPersonImg
     */
    public String getPostUploadPersonImg() {
        return postUploadPersonImg;
    }

    /**
     * @param postUploadPersonImg The postUploadPersonImg
     */
    public void setPostUploadPersonImg(String postUploadPersonImg) {
        this.postUploadPersonImg = postUploadPersonImg;
    }

    /**
     * @return The postUploadUserImgPath
     */
    public String getPostUploadUserImgPath() {
        return postUploadUserImgPath;
    }

    /**
     * @param postUploadUserImgPath The postUploadUserImgPath
     */
    public void setPostUploadUserImgPath(String postUploadUserImgPath) {
        this.postUploadUserImgPath = postUploadUserImgPath;
    }

    /**
     * @return The postUploadedDate
     */
    public String getPostUploadedDate() {
        return postUploadedDate;
    }

    /**
     * @param postUploadedDate The postUploadedDate
     */
    public void setPostUploadedDate(String postUploadedDate) {
        this.postUploadedDate = postUploadedDate;
    }

    /**
     * @return The postAuthorRole
     */
    public String getPostAuthorRole() {
        return postAuthorRole;
    }

    /**
     * @param postAuthorRole The post_author_role
     */
    public void setPostAuthorRole(String postAuthorRole) {
        this.postAuthorRole = postAuthorRole;
    }

    /**
     * @return The postAuthorRoleMm
     */
    public String getPostAuthorRoleMm() {
        return postAuthorRoleMm;
    }

    /**
     * @param postAuthorRoleMm The post_author_role_mm
     */
    public void setPostAuthorRoleMm(String postAuthorRoleMm) {
        this.postAuthorRoleMm = postAuthorRoleMm;
    }

    /**
     * @return The shareCount
     */
    public Integer getShareCount() {
        return shareCount;
    }

    /**
     * @param shareCount The share_count
     */
    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    /**
     * @return The suggestSectionEng
     */
    public String getSuggestSectionEng() {
        return suggestSectionEng;
    }

    /**
     * @param suggestSectionEng The suggest_section_eng
     */
    public void setSuggestSectionEng(String suggestSectionEng) {
        this.suggestSectionEng = suggestSectionEng;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The titleMm
     */
    public String getTitleMm() {
        return titleMm;
    }

    /**
     * @param titleMm The titleMm
     */
    public void setTitleMm(String titleMm) {
        this.titleMm = titleMm;
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
     * @return The videoId
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * @param videoId The videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
    public String getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt The deleted_at
     */
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

}
