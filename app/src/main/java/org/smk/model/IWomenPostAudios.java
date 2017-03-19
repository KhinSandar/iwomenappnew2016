package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IWomenPostAudios implements Serializable {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("post_id")
@Expose
private String postId;
@SerializedName("name")
@Expose
private String name;
@SerializedName("name_mm")
@Expose
private String nameMm;
@SerializedName("links_path")
@Expose
private String linksPath;
@SerializedName("uploaded_date")
@Expose
private String uploadedDate;
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

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getPostId() {
return postId;
}

public void setPostId(String postId) {
this.postId = postId;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getNameMm() {
return nameMm;
}

public void setNameMm(String nameMm) {
this.nameMm = nameMm;
}

public String getLinksPath() {
return linksPath;
}

public void setLinksPath(String linksPath) {
this.linksPath = linksPath;
}

public String getUploadedDate() {
return uploadedDate;
}

public void setUploadedDate(String uploadedDate) {
this.uploadedDate = uploadedDate;
}

public Boolean getIsAllow() {
return isAllow;
}

public void setIsAllow(Boolean isAllow) {
this.isAllow = isAllow;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public Object getDeletedAt() {
return deletedAt;
}

public void setDeletedAt(Object deletedAt) {
this.deletedAt = deletedAt;
}

}