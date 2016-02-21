package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CorrectAnswer {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("question_id")
@Expose
private Integer questionId;
@SerializedName("competition_group_user_id")
@Expose
private Integer competitionGroupUserId;
@SerializedName("answer")
@Expose
private String answer;
@SerializedName("answer_mm")
@Expose
private String answerMm;
@SerializedName("status")
@Expose
private String status;
@SerializedName("correct")
@Expose
private Boolean correct;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("competitiongroupuser")
@Expose
private GroupUser groupUser;
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
* The questionId
*/
public Integer getQuestionId() {
return questionId;
}

/**
* 
* @param questionId
* The question_id
*/
public void setQuestionId(Integer questionId) {
this.questionId = questionId;
}

/**
* 
* @return
* The competitionGroupUserId
*/
public Integer getCompetitionGroupUserId() {
return competitionGroupUserId;
}

/**
* 
* @param competitionGroupUserId
* The competition_group_user_id
*/
public void setCompetitionGroupUserId(Integer competitionGroupUserId) {
this.competitionGroupUserId = competitionGroupUserId;
}

/**
* 
* @return
* The answer
*/
public String getAnswer() {
return answer;
}

/**
* 
* @param answer
* The answer
*/
public void setAnswer(String answer) {
this.answer = answer;
}

/**
* 
* @return
* The answerMm
*/
public String getAnswerMm() {
return answerMm;
}

/**
* 
* @param answerMm
* The answer_mm
*/
public void setAnswerMm(String answerMm) {
this.answerMm = answerMm;
}

/**
* 
* @return
* The status
*/
public String getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
public void setStatus(String status) {
this.status = status;
}

/**
* 
* @return
* The correct
*/
public Boolean getCorrect() {
return correct;
}

/**
* 
* @param correct
* The correct
*/
public void setCorrect(Boolean correct) {
this.correct = correct;
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

public GroupUser getGroupUser() {
    return groupUser;
}

public void setGroupUser(GroupUser groupUser) {
    this.groupUser = groupUser;
}
}