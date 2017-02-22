package org.undp_iwomen.iwomen.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportPosts implements Serializable {

@SerializedName("postId")
@Expose
private Integer postId;
@SerializedName("userId")
@Expose
private Integer userId;
@SerializedName("point")
@Expose
private Integer point;
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

public Integer getPostId() {
return postId;
}

public void setPostId(Integer postId) {
this.postId = postId;
}

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public Integer getPoint() {
return point;
}

public void setPoint(Integer point) {
this.point = point;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}