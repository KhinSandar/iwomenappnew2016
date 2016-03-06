package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.undp_iwomen.iwomen.data.AuthorItem;

import java.io.Serializable;

public class SubResourceItem implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("authorName")
    @Expose
    private String authorName;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("author_img_url")
    @Expose
    private String authorImgUrl;
    @SerializedName("isAllow")
    @Expose
    private Boolean isAllow;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("posted_date")
    @Expose
    private String postedDate;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @SerializedName("sub_res_icon_img_url")
    @Expose
    private String subResIconImgUrl;
    @SerializedName("sub_resouce_content_eng")
    @Expose
    private String subResouceContentEng;
    @SerializedName("sub_resouce_content_mm")
    @Expose
    private String subResouceContentMm;
    @SerializedName("sub_resource_title_eng")
    @Expose
    private String subResourceTitleEng;
    @SerializedName("sub_resource_title_mm")
    @Expose
    private String subResourceTitleMm;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("author")
    @Expose
    private AuthorItem author;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     *
     * @param authorName
     * The authorName
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     *
     * @return
     * The authorId
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     *
     * @param authorId
     * The author_id
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     *
     * @return
     * The authorImgUrl
     */
    public String getAuthorImgUrl() {
        return authorImgUrl;
    }

    /**
     *
     * @param authorImgUrl
     * The author_img_url
     */
    public void setAuthorImgUrl(String authorImgUrl) {
        this.authorImgUrl = authorImgUrl;
    }

    /**
     *
     * @return
     * The isAllow
     */
    public Boolean getIsAllow() {
        return isAllow;
    }

    /**
     *
     * @param isAllow
     * The isAllow
     */
    public void setIsAllow(Boolean isAllow) {
        this.isAllow = isAllow;
    }

    /**
     *
     * @return
     * The objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     *
     * @param objectId
     * The objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     *
     * @return
     * The postedDate
     */
    public String getPostedDate() {
        return postedDate;
    }

    /**
     *
     * @param postedDate
     * The posted_date
     */
    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    /**
     *
     * @return
     * The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     *
     * @param resourceId
     * The resource_id
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     *
     * @return
     * The subResIconImgUrl
     */
    public String getSubResIconImgUrl() {
        return subResIconImgUrl;
    }

    /**
     *
     * @param subResIconImgUrl
     * The sub_res_icon_img_url
     */
    public void setSubResIconImgUrl(String subResIconImgUrl) {
        this.subResIconImgUrl = subResIconImgUrl;
    }

    /**
     *
     * @return
     * The subResouceContentEng
     */
    public String getSubResouceContentEng() {
        return subResouceContentEng;
    }

    /**
     *
     * @param subResouceContentEng
     * The sub_resouce_content_eng
     */
    public void setSubResouceContentEng(String subResouceContentEng) {
        this.subResouceContentEng = subResouceContentEng;
    }

    /**
     *
     * @return
     * The subResouceContentMm
     */
    public String getSubResouceContentMm() {
        return subResouceContentMm;
    }

    /**
     *
     * @param subResouceContentMm
     * The sub_resouce_content_mm
     */
    public void setSubResouceContentMm(String subResouceContentMm) {
        this.subResouceContentMm = subResouceContentMm;
    }

    /**
     *
     * @return
     * The subResourceTitleEng
     */
    public String getSubResourceTitleEng() {
        return subResourceTitleEng;
    }

    /**
     *
     * @param subResourceTitleEng
     * The sub_resource_title_eng
     */
    public void setSubResourceTitleEng(String subResourceTitleEng) {
        this.subResourceTitleEng = subResourceTitleEng;
    }

    /**
     *
     * @return
     * The subResourceTitleMm
     */
    public String getSubResourceTitleMm() {
        return subResourceTitleMm;
    }

    /**
     *
     * @param subResourceTitleMm
     * The sub_resource_title_mm
     */
    public void setSubResourceTitleMm(String subResourceTitleMm) {
        this.subResourceTitleMm = subResourceTitleMm;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     *
     * @return
     * The author
     */
    public AuthorItem getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(AuthorItem author) {
        this.author = author;
    }

}

