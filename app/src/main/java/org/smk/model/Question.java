package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("question_mm")
    @Expose
    private String questionMm;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("option")
    @Expose
    private List<Option> option = new ArrayList<Option>();
    @SerializedName("answer")
    @Expose
    private String answer;

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
     * @return The questionId
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId The question_id
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question The question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return The questionMm
     */
    public String getQuestionMm() {
        return questionMm;
    }

    /**
     * @param questionMm The question_mm
     */
    public void setQuestionMm(String questionMm) {
        this.questionMm = questionMm;
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
     * @return The option
     */
    public List<Option> getOption() {
        return option;
    }

    /**
     * @param option The option
     */
    public void setOption(List<Option> option) {
        this.option = option;
    }

    /**
     * @return The answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer The answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @param answer The answer
     */
    public void appendAnswer(String answer) {
        if(this.answer != null && this.answer.length() > 0)
            this.answer += ","+answer;
        else
            this.answer = answer;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", type='" + type + '\'' +
                ", question='" + question + '\'' +
                ", questionMm='" + questionMm + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", option=" + option +
                ", answer='" + answer + '\'' +
                '}';
    }
}
