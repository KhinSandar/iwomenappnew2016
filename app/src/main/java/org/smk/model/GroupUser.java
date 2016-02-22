package org.smk.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupUser {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("group_city")
@Expose
private String groupCity;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("username")
@Expose
private String username;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("profile_img")
@Expose
private String profileImg;
@SerializedName("competition_question_id")
@Expose
private String competitionQuestionId;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("image_url")
@Expose
private String imageUrl;
@SerializedName("total_has_answer")
@Expose
private Integer totalHasAnswer;
@SerializedName("current_has_answer")
@Expose
private Integer currentHasAnswer;
@SerializedName("init_answer")
@Expose
private Answer initAnswer;
@SerializedName("answer")
@Expose
private List<Answer> answer = new ArrayList<Answer>();

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
* The groupName
*/
public String getGroupName() {
return groupName;
}

/**
* 
* @param groupName
* The group_name
*/
public void setGroupName(String groupName) {
this.groupName = groupName;
}

/**
* 
* @return
* The groupCity
*/
public String getGroupCity() {
return groupCity;
}

/**
* 
* @param groupCity
* The group_city
*/
public void setGroupCity(String groupCity) {
this.groupCity = groupCity;
}

/**
* 
* @return
* The userId
*/
public String getUserId() {
return userId;
}

/**
* 
* @param userId
* The user_id
*/
public void setUserId(String userId) {
this.userId = userId;
}

/**
* 
* @return
* The username
*/
public String getUsername() {
return username;
}

/**
* 
* @param username
* The username
*/
public void setUsername(String username) {
this.username = username;
}

/**
* 
* @return
* The phone
*/
public String getPhone() {
return phone;
}

/**
* 
* @param phone
* The phone
*/
public void setPhone(String phone) {
this.phone = phone;
}

/**
* 
* @return
* The profileImg
*/
public String getProfileImg() {
return profileImg;
}

/**
* 
* @param profileImg
* The profile_img
*/
public void setProfileImg(String profileImg) {
this.profileImg = profileImg;
}

/**
* 
* @return
* The competitionQuestionId
*/
public String getCompetitionQuestionId() {
return competitionQuestionId;
}

/**
* 
* @param competitionQuestionId
* The competition_question_id
*/
public void setCompetitionQuestionId(String competitionQuestionId) {
this.competitionQuestionId = competitionQuestionId;
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
* The imageUrl
*/
public String getImageUrl() {
return imageUrl;
}

/**
* 
* @param imageUrl
* The image_url
*/
public void setImageUrl(String imageUrl) {
this.imageUrl = imageUrl;
}

/**
* 
* @return
* The totalHasAnswer
*/
public Integer getTotalHasAnswer() {
return totalHasAnswer;
}

/**
* 
* @param totalHasAnswer
* The total_has_answer
*/
public void setTotalHasAnswer(Integer totalHasAnswer) {
this.totalHasAnswer = totalHasAnswer;
}

/**
* 
* @return
* The currentHasAnswer
*/
public Integer getCurrentHasAnswer() {
return currentHasAnswer;
}

/**
* 
* @param currentHasAnswer
* The current_has_answer
*/
public void setCurrentHasAnswer(Integer currentHasAnswer) {
this.currentHasAnswer = currentHasAnswer;
}

/**
* 
* @return
* The initAnswer
*/
public Answer getInitAnswer() {
return initAnswer;
}

/**
* 
* @param initAnswer
* The init_answer
*/
public void setInitAnswer(Answer initAnswer) {
this.initAnswer = initAnswer;
}


/**
* 
* @return
* The answer
*/
public List<Answer> getAnswer() {
return answer;
}

/**
* 
* @param answer
* The answer
*/
public void setAnswer(List<Answer> answer) {
this.answer = answer;
}

}