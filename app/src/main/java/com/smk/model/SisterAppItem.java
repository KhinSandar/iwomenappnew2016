package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SisterAppItem implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("app_img")
    @Expose
    private String appImg;
    @SerializedName("app_link")
    @Expose
    private String appLink;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_name_mm")
    @Expose
    private String appNameMM;

    @SerializedName("app_about_mm")
    @Expose
    private String appAboutMM;

    @SerializedName("app_about")
    @Expose
    private String appAbout;

    @SerializedName("app_package_name")
    @Expose
    private String appPackageName;
    @SerializedName("isAllow")
    @Expose
    private Boolean isAllow;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
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
     * @return The appImg
     */
    public String getAppImg() {
        return appImg;
    }

    /**
     * @param appImg The app_img
     */
    public void setAppImg(String appImg) {
        this.appImg = appImg;
    }

    /**
     * @return The appLink
     */
    public String getAppLink() {
        return appLink;
    }

    /**
     * @param appLink The app_link
     */
    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    /**
     * @return The appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName The app_name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppNameMM() {
        return appNameMM;
    }

    public void setAppNameMM(String appNameMM) {
        this.appNameMM = appNameMM;
    }

    public String getAppAboutMM() {
        return appAboutMM;
    }

    public void setAppAboutMM(String appAboutMM) {
        this.appAboutMM = appAboutMM;
    }

    public String getAppAbout() {
        return appAbout;
    }

    public void setAppAbout(String appAbout) {
        this.appAbout = appAbout;
    }

    /**
     * @return The appPackageName
     */
    public String getAppPackageName() {
        return appPackageName;
    }

    /**
     * @param appPackageName The app_package_name
     */
    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
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

}
