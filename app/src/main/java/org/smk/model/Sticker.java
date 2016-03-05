package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sticker implements Serializable {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("objectId")
@Expose
private String objectId;
@SerializedName("stickerImg")
@Expose
private String stickerImg;
@SerializedName("stickerImgPath")
@Expose
private String stickerImgPath;
@SerializedName("stickerName")
@Expose
private String stickerName;
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
* The stickerImg
*/
public String getStickerImg() {
return stickerImg;
}

/**
* 
* @param stickerImg
* The stickerImg
*/
public void setStickerImg(String stickerImg) {
this.stickerImg = stickerImg;
}

/**
* 
* @return
* The stickerImgPath
*/
public String getStickerImgPath() {
return stickerImgPath;
}

/**
* 
* @param stickerImgPath
* The stickerImgPath
*/
public void setStickerImgPath(String stickerImgPath) {
this.stickerImgPath = stickerImgPath;
}

/**
* 
* @return
* The stickerName
*/
public String getStickerName() {
return stickerName;
}

/**
* 
* @param stickerName
* The stickerName
*/
public void setStickerName(String stickerName) {
this.stickerName = stickerName;
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
