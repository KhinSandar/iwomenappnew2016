package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeItem {

@SerializedName("postId")
@Expose
private Integer postId;
@SerializedName("userId")
@Expose
private Integer userId;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("id")
@Expose
private Integer id;
@SerializedName("message")
@Expose
private String message;

/**
* 
* @return
* The postId
*/
public Integer getPostId() {
return postId;
}

/**
* 
* @param postId
* The postId
*/
public void setPostId(Integer postId) {
this.postId = postId;
}

/**
* 
* @return
* The userId
*/
public Integer getUserId() {
return userId;
}

/**
* 
* @param userId
* The userId
*/
public void setUserId(Integer userId) {
this.userId = userId;
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
