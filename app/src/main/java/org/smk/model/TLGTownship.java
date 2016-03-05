package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TLGTownship implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("is_leader")
    @Expose
    private Boolean isLeader;
    @SerializedName("tlg_group_address")
    @Expose
    private String tlgGroupAddress;
    @SerializedName("tlg_group_address_mm")
    @Expose
    private String tlgGroupAddressMm;
    @SerializedName("tlg_group_key_activity")
    @Expose
    private String tlgGroupKeyActivity;
    @SerializedName("tlg_group_key_activity_mm")
    @Expose
    private String tlgGroupKeyActivityMm;
    @SerializedName("tlg_group_key_skills")
    @Expose
    private String tlgGroupKeySkills;
    @SerializedName("tlg_group_key_skills_mm")
    @Expose
    private String tlgGroupKeySkillsMm;
    @SerializedName("tlg_group_lat_address")
    @Expose
    private String tlgGroupLatAddress;
    @SerializedName("tlg_group_lng_address")
    @Expose
    private String tlgGroupLngAddress;
    @SerializedName("tlg_group_logo")
    @Expose
    private String tlgGroupLogo;
    @SerializedName("tlg_group_member_no")
    @Expose
    private String tlgGroupMemberNo;
    @SerializedName("tlg_group_name")
    @Expose
    private String tlgGroupName;
    @SerializedName("tlg_group_name_mm")
    @Expose
    private String tlgGroupNameMm;
    @SerializedName("tlg_group_other_member_no")
    @Expose
    private String tlgGroupOtherMemberNo;
    @SerializedName("tlg_leader_fb_link")
    @Expose
    private String tlgLeaderFbLink;
    @SerializedName("tlg_leader_img")
    @Expose
    private String tlgLeaderImg;
    @SerializedName("tlg_leader_name")
    @Expose
    private String tlgLeaderName;
    @SerializedName("tlg_leader_name_mm")
    @Expose
    private String tlgLeaderNameMm;
    @SerializedName("tlg_leader_ph")
    @Expose
    private String tlgLeaderPh;
    @SerializedName("tlg_leader_role")
    @Expose
    private String tlgLeaderRole;
    @SerializedName("tlg_member_ph")
    @Expose
    private String tlgMemberPh;
    @SerializedName("tlg_member_ph_no")
    @Expose
    private String tlgMemberPhNo;
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
     * The isLeader
     */
    public Boolean getIsLeader() {
        return isLeader;
    }

    /**
     *
     * @param isLeader
     * The is_leader
     */
    public void setIsLeader(Boolean isLeader) {
        this.isLeader = isLeader;
    }

    /**
     *
     * @return
     * The tlgGroupAddress
     */
    public String getTlgGroupAddress() {
        return tlgGroupAddress;
    }

    /**
     *
     * @param tlgGroupAddress
     * The tlg_group_address
     */
    public void setTlgGroupAddress(String tlgGroupAddress) {
        this.tlgGroupAddress = tlgGroupAddress;
    }

    /**
     *
     * @return
     * The tlgGroupAddressMm
     */
    public String getTlgGroupAddressMm() {
        return tlgGroupAddressMm;
    }

    /**
     *
     * @param tlgGroupAddressMm
     * The tlg_group_address_mm
     */
    public void setTlgGroupAddressMm(String tlgGroupAddressMm) {
        this.tlgGroupAddressMm = tlgGroupAddressMm;
    }

    /**
     *
     * @return
     * The tlgGroupKeyActivity
     */
    public String getTlgGroupKeyActivity() {
        return tlgGroupKeyActivity;
    }

    /**
     *
     * @param tlgGroupKeyActivity
     * The tlg_group_key_activity
     */
    public void setTlgGroupKeyActivity(String tlgGroupKeyActivity) {
        this.tlgGroupKeyActivity = tlgGroupKeyActivity;
    }

    /**
     *
     * @return
     * The tlgGroupKeyActivityMm
     */
    public String getTlgGroupKeyActivityMm() {
        return tlgGroupKeyActivityMm;
    }

    /**
     *
     * @param tlgGroupKeyActivityMm
     * The tlg_group_key_activity_mm
     */
    public void setTlgGroupKeyActivityMm(String tlgGroupKeyActivityMm) {
        this.tlgGroupKeyActivityMm = tlgGroupKeyActivityMm;
    }

    /**
     *
     * @return
     * The tlgGroupKeySkills
     */
    public String getTlgGroupKeySkills() {
        return tlgGroupKeySkills;
    }

    /**
     *
     * @param tlgGroupKeySkills
     * The tlg_group_key_skills
     */
    public void setTlgGroupKeySkills(String tlgGroupKeySkills) {
        this.tlgGroupKeySkills = tlgGroupKeySkills;
    }

    /**
     *
     * @return
     * The tlgGroupKeySkillsMm
     */
    public String getTlgGroupKeySkillsMm() {
        return tlgGroupKeySkillsMm;
    }

    /**
     *
     * @param tlgGroupKeySkillsMm
     * The tlg_group_key_skills_mm
     */
    public void setTlgGroupKeySkillsMm(String tlgGroupKeySkillsMm) {
        this.tlgGroupKeySkillsMm = tlgGroupKeySkillsMm;
    }

    /**
     *
     * @return
     * The tlgGroupLatAddress
     */
    public String getTlgGroupLatAddress() {
        return tlgGroupLatAddress;
    }

    /**
     *
     * @param tlgGroupLatAddress
     * The tlg_group_lat_address
     */
    public void setTlgGroupLatAddress(String tlgGroupLatAddress) {
        this.tlgGroupLatAddress = tlgGroupLatAddress;
    }

    /**
     *
     * @return
     * The tlgGroupLngAddress
     */
    public String getTlgGroupLngAddress() {
        return tlgGroupLngAddress;
    }

    /**
     *
     * @param tlgGroupLngAddress
     * The tlg_group_lng_address
     */
    public void setTlgGroupLngAddress(String tlgGroupLngAddress) {
        this.tlgGroupLngAddress = tlgGroupLngAddress;
    }

    /**
     *
     * @return
     * The tlgGroupLogo
     */
    public String getTlgGroupLogo() {
        return tlgGroupLogo;
    }

    /**
     *
     * @param tlgGroupLogo
     * The tlg_group_logo
     */
    public void setTlgGroupLogo(String tlgGroupLogo) {
        this.tlgGroupLogo = tlgGroupLogo;
    }

    /**
     *
     * @return
     * The tlgGroupMemberNo
     */
    public String getTlgGroupMemberNo() {
        return tlgGroupMemberNo;
    }

    /**
     *
     * @param tlgGroupMemberNo
     * The tlg_group_member_no
     */
    public void setTlgGroupMemberNo(String tlgGroupMemberNo) {
        this.tlgGroupMemberNo = tlgGroupMemberNo;
    }

    /**
     *
     * @return
     * The tlgGroupName
     */
    public String getTlgGroupName() {
        return tlgGroupName;
    }

    /**
     *
     * @param tlgGroupName
     * The tlg_group_name
     */
    public void setTlgGroupName(String tlgGroupName) {
        this.tlgGroupName = tlgGroupName;
    }

    /**
     *
     * @return
     * The tlgGroupNameMm
     */
    public String getTlgGroupNameMm() {
        return tlgGroupNameMm;
    }

    /**
     *
     * @param tlgGroupNameMm
     * The tlg_group_name_mm
     */
    public void setTlgGroupNameMm(String tlgGroupNameMm) {
        this.tlgGroupNameMm = tlgGroupNameMm;
    }

    /**
     *
     * @return
     * The tlgGroupOtherMemberNo
     */
    public String getTlgGroupOtherMemberNo() {
        return tlgGroupOtherMemberNo;
    }

    /**
     *
     * @param tlgGroupOtherMemberNo
     * The tlg_group_other_member_no
     */
    public void setTlgGroupOtherMemberNo(String tlgGroupOtherMemberNo) {
        this.tlgGroupOtherMemberNo = tlgGroupOtherMemberNo;
    }

    /**
     *
     * @return
     * The tlgLeaderFbLink
     */
    public String getTlgLeaderFbLink() {
        return tlgLeaderFbLink;
    }

    /**
     *
     * @param tlgLeaderFbLink
     * The tlg_leader_fb_link
     */
    public void setTlgLeaderFbLink(String tlgLeaderFbLink) {
        this.tlgLeaderFbLink = tlgLeaderFbLink;
    }

    /**
     *
     * @return
     * The tlgLeaderImg
     */
    public String getTlgLeaderImg() {
        return tlgLeaderImg;
    }

    /**
     *
     * @param tlgLeaderImg
     * The tlg_leader_img
     */
    public void setTlgLeaderImg(String tlgLeaderImg) {
        this.tlgLeaderImg = tlgLeaderImg;
    }

    /**
     *
     * @return
     * The tlgLeaderName
     */
    public String getTlgLeaderName() {
        return tlgLeaderName;
    }

    /**
     *
     * @param tlgLeaderName
     * The tlg_leader_name
     */
    public void setTlgLeaderName(String tlgLeaderName) {
        this.tlgLeaderName = tlgLeaderName;
    }

    /**
     *
     * @return
     * The tlgLeaderNameMm
     */
    public String getTlgLeaderNameMm() {
        return tlgLeaderNameMm;
    }

    /**
     *
     * @param tlgLeaderNameMm
     * The tlg_leader_name_mm
     */
    public void setTlgLeaderNameMm(String tlgLeaderNameMm) {
        this.tlgLeaderNameMm = tlgLeaderNameMm;
    }

    /**
     *
     * @return
     * The tlgLeaderPh
     */
    public String getTlgLeaderPh() {
        return tlgLeaderPh;
    }

    /**
     *
     * @param tlgLeaderPh
     * The tlg_leader_ph
     */
    public void setTlgLeaderPh(String tlgLeaderPh) {
        this.tlgLeaderPh = tlgLeaderPh;
    }

    /**
     *
     * @return
     * The tlgLeaderRole
     */
    public String getTlgLeaderRole() {
        return tlgLeaderRole;
    }

    /**
     *
     * @param tlgLeaderRole
     * The tlg_leader_role
     */
    public void setTlgLeaderRole(String tlgLeaderRole) {
        this.tlgLeaderRole = tlgLeaderRole;
    }

    /**
     *
     * @return
     * The tlgMemberPh
     */
    public String getTlgMemberPh() {
        return tlgMemberPh;
    }

    /**
     *
     * @param tlgMemberPh
     * The tlg_member_ph
     */
    public void setTlgMemberPh(String tlgMemberPh) {
        this.tlgMemberPh = tlgMemberPh;
    }

    /**
     *
     * @return
     * The tlgMemberPhNo
     */
    public String getTlgMemberPhNo() {
        return tlgMemberPhNo;
    }

    /**
     *
     * @param tlgMemberPhNo
     * The tlg_member_ph_no
     */
    public void setTlgMemberPhNo(String tlgMemberPhNo) {
        this.tlgMemberPhNo = tlgMemberPhNo;
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

