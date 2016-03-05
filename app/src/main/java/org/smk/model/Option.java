package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Option implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mutiple_question_id")
    @Expose
    private Integer mutipleQuestionId;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("option_mm")
    @Expose
    private String optionMm;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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
     * @return The mutipleQuestionId
     */
    public Integer getMutipleQuestionId() {
        return mutipleQuestionId;
    }

    /**
     * @param mutipleQuestionId The mutiple_question_id
     */
    public void setMutipleQuestionId(Integer mutipleQuestionId) {
        this.mutipleQuestionId = mutipleQuestionId;
    }

    /**
     * @return The option
     */
    public String getOption() {
        return option;
    }

    /**
     * @param option The option
     */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * @return The optionMm
     */
    public String getOptionMm() {
        return optionMm;
    }

    /**
     * @param optionMm The option_mm
     */
    public void setOptionMm(String optionMm) {
        this.optionMm = optionMm;
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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", mutipleQuestionId=" + mutipleQuestionId +
                ", option='" + option + '\'' +
                ", optionMm='" + optionMm + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
