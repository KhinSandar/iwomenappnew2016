package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextQuestion {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("question")
@Expose
private String question;
@SerializedName("question_mm")
@Expose
private String questionMm;
@SerializedName("description")
@Expose
private String description;
@SerializedName("description_mm")
@Expose
private String descriptionMm;
@SerializedName("start_date")
@Expose
private String startDate;
@SerializedName("end_date")
@Expose
private String endDate;
@SerializedName("status")
@Expose
private String status;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

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
* The question
*/
public String getQuestion() {
return question;
}

/**
* 
* @param question
* The question
*/
public void setQuestion(String question) {
this.question = question;
}

/**
* 
* @return
* The questionMm
*/
public String getQuestionMm() {
return questionMm;
}

/**
* 
* @param questionMm
* The question_mm
*/
public void setQuestionMm(String questionMm) {
this.questionMm = questionMm;
}

/**
* 
* @return
* The description
*/
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The descriptionMm
*/
public String getDescriptionMm() {
return descriptionMm;
}

/**
* 
* @param descriptionMm
* The description_mm
*/
public void setDescriptionMm(String descriptionMm) {
this.descriptionMm = descriptionMm;
}

/**
* 
* @return
* The startDate
*/
public String getStartDate() {
return startDate;
}

/**
* 
* @param startDate
* The start_date
*/
public void setStartDate(String startDate) {
this.startDate = startDate;
}

/**
* 
* @return
* The endDate
*/
public String getEndDate() {
return endDate;
}

/**
* 
* @param endDate
* The end_date
*/
public void setEndDate(String endDate) {
this.endDate = endDate;
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

}