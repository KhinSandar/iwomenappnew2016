package org.undp_iwomen.iwomen.data;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by khinsandar on 10/1/15.
 */

public class AuthorItem implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("organization_name_mm")
    @Expose
    private String organizationNameMm;
    @SerializedName("authorImg")
    @Expose
    private String authorImg;
    @SerializedName("authorInfoEng")
    @Expose
    private String authorInfoEng;
    @SerializedName("authorInfoMM")
    @Expose
    private String authorInfoMM;
    @SerializedName("authorName")
    @Expose
    private String authorName;
    @SerializedName("authorNameMM")
    @Expose
    private String authorNameMM;
    @SerializedName("authorTitleEng")
    @Expose
    private String authorTitleEng;
    @SerializedName("authorTitleMM")
    @Expose
    private String authorTitleMM;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("message")
    @Expose
    private String message;

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
     * The organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     *
     * @param organizationName
     * The organization_name
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     *
     * @return
     * The organizationNameMm
     */
    public String getOrganizationNameMm() {
        return organizationNameMm;
    }

    /**
     *
     * @param organizationNameMm
     * The organization_name_mm
     */
    public void setOrganizationNameMm(String organizationNameMm) {
        this.organizationNameMm = organizationNameMm;
    }

    /**
     *
     * @return
     * The authorImg
     */
    public String getAuthorImg() {
        return authorImg;
    }

    /**
     *
     * @param authorImg
     * The authorImg
     */
    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    /**
     *
     * @return
     * The authorInfoEng
     */
    public String getAuthorInfoEng() {
        return authorInfoEng;
    }

    /**
     *
     * @param authorInfoEng
     * The authorInfoEng
     */
    public void setAuthorInfoEng(String authorInfoEng) {
        this.authorInfoEng = authorInfoEng;
    }

    /**
     *
     * @return
     * The authorInfoMM
     */
    public String getAuthorInfoMM() {
        return authorInfoMM;
    }

    /**
     *
     * @param authorInfoMM
     * The authorInfoMM
     */
    public void setAuthorInfoMM(String authorInfoMM) {
        this.authorInfoMM = authorInfoMM;
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
     * The authorNameMM
     */
    public String getAuthorNameMM() {
        return authorNameMM;
    }

    /**
     *
     * @param authorNameMM
     * The authorNameMM
     */
    public void setAuthorNameMM(String authorNameMM) {
        this.authorNameMM = authorNameMM;
    }

    /**
     *
     * @return
     * The authorTitleEng
     */
    public String getAuthorTitleEng() {
        return authorTitleEng;
    }

    /**
     *
     * @param authorTitleEng
     * The authorTitleEng
     */
    public void setAuthorTitleEng(String authorTitleEng) {
        this.authorTitleEng = authorTitleEng;
    }

    /**
     *
     * @return
     * The authorTitleMM
     */
    public String getAuthorTitleMM() {
        return authorTitleMM;
    }

    /**
     *
     * @param authorTitleMM
     * The authorTitleMM
     */
    public void setAuthorTitleMM(String authorTitleMM) {
        this.authorTitleMM = authorTitleMM;
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
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}

