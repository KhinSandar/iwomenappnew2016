package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResourceItem implements Serializable  {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("objectId")
@Expose
private String objectId;
@SerializedName("author_img_path")
@Expose
private String authorImgPath;
@SerializedName("isAllow")
@Expose
private Boolean isAllow;
@SerializedName("resource_author_id")
@Expose
private String resourceAuthorId;
@SerializedName("resource_author_name")
@Expose
private String resourceAuthorName;
@SerializedName("resource_icon_img")
@Expose
private String resourceIconImg;
@SerializedName("resource_title_eng")
@Expose
private String resourceTitleEng;
@SerializedName("resource_title_mm")
@Expose
private String resourceTitleMm;
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
* The authorImgPath
*/
public String getAuthorImgPath() {
return authorImgPath;
}

/**
* 
* @param authorImgPath
* The author_img_path
*/
public void setAuthorImgPath(String authorImgPath) {
this.authorImgPath = authorImgPath;
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
* The resourceAuthorId
*/
public String getResourceAuthorId() {
return resourceAuthorId;
}

/**
* 
* @param resourceAuthorId
* The resource_author_id
*/
public void setResourceAuthorId(String resourceAuthorId) {
this.resourceAuthorId = resourceAuthorId;
}

/**
* 
* @return
* The resourceAuthorName
*/
public String getResourceAuthorName() {
return resourceAuthorName;
}

/**
* 
* @param resourceAuthorName
* The resource_author_name
*/
public void setResourceAuthorName(String resourceAuthorName) {
this.resourceAuthorName = resourceAuthorName;
}

/**
* 
* @return
* The resourceIconImg
*/
public String getResourceIconImg() {
return resourceIconImg;
}

/**
* 
* @param resourceIconImg
* The resource_icon_img
*/
public void setResourceIconImg(String resourceIconImg) {
this.resourceIconImg = resourceIconImg;
}

/**
* 
* @return
* The resourceTitleEng
*/
public String getResourceTitleEng() {
return resourceTitleEng;
}

/**
* 
* @param resourceTitleEng
* The resource_title_eng
*/
public void setResourceTitleEng(String resourceTitleEng) {
this.resourceTitleEng = resourceTitleEng;
}

/**
* 
* @return
* The resourceTitleMm
*/
public String getResourceTitleMm() {
return resourceTitleMm;
}

/**
* 
* @param resourceTitleMm
* The resource_title_mm
*/
public void setResourceTitleMm(String resourceTitleMm) {
this.resourceTitleMm = resourceTitleMm;
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

}
