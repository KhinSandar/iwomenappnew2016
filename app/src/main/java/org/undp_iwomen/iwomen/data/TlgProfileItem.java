package org.undp_iwomen.iwomen.data;

import java.io.Serializable;

/***************************************
 * Created by khinsandar on 8/13/15.
 *****************************************/
public class TlgProfileItem implements Serializable {

    private String _objectId;
    private String _tlg_group_name;
    private String _tlg_group_name_mm;

    private String _tlg_group_address;
    private String _tlg_group_address_mm;

    private String _tlg_group_lat_address;
    private String _tlg_group_lng_address;

    private String tlgLeaderFbLink;

    private String tlgLogoImg;

    private String tlgLeaderImg;

    private String tlgLeaderName;
    private String tlgLeaderNameMm;

    private String tlgLeaderPh;
    private String tlgLeaderRole;

    private String tlgGroupMemberNo;
    private String tlgGroupOtherMemberNo;

    private String tlgGroupKeyActivity;
    private String tlgGroupKeyActivityMm;
    private String tlgGroupKeySkills;
    private String tlgGroupKeySkillsMm;

    private String created_at;
    private String updated_at;


    public TlgProfileItem(){

    }


    public TlgProfileItem(String objectId, String tlg_group_name, String tlg_group_address, String tlg_group_lat_address, String tlg_group_lng_address){
        this._objectId = objectId;
        this._tlg_group_name = tlg_group_name;
        this._tlg_group_address = tlg_group_address;
        this._tlg_group_lat_address = tlg_group_lat_address;
        this._tlg_group_lng_address = tlg_group_lng_address;
    }

    public String get_objectId() {
        return _objectId;
    }

    public void set_objectId(String _objectId) {
        this._objectId = _objectId;
    }

    public String get_tlg_group_name() {
        return _tlg_group_name;
    }

    public void set_tlg_group_name(String _tlg_group_name) {
        this._tlg_group_name = _tlg_group_name;
    }

    public String get_tlg_group_name_mm() {
        return _tlg_group_name_mm;
    }

    public void set_tlg_group_name_mm(String _tlg_group_name_mm) {
        this._tlg_group_name_mm = _tlg_group_name_mm;
    }

    public String get_tlg_group_address_mm() {
        return _tlg_group_address_mm;
    }

    public void set_tlg_group_address_mm(String _tlg_group_address_mm) {
        this._tlg_group_address_mm = _tlg_group_address_mm;
    }

    public String getTlgLeaderFbLink() {
        return tlgLeaderFbLink;
    }

    public void setTlgLeaderFbLink(String tlgLeaderFbLink) {
        this.tlgLeaderFbLink = tlgLeaderFbLink;
    }

    public String getTlgLogoImg() {
        return tlgLogoImg;
    }

    public void setTlgLogoImg(String tlgLogoImg) {
        this.tlgLogoImg = tlgLogoImg;
    }

    public String getTlgLeaderImg() {
        return tlgLeaderImg;
    }

    public void setTlgLeaderImg(String tlgLeaderImg) {
        this.tlgLeaderImg = tlgLeaderImg;
    }

    public String getTlgLeaderName() {
        return tlgLeaderName;
    }

    public void setTlgLeaderName(String tlgLeaderName) {
        this.tlgLeaderName = tlgLeaderName;
    }

    public String getTlgLeaderNameMm() {
        return tlgLeaderNameMm;
    }

    public void setTlgLeaderNameMm(String tlgLeaderNameMm) {
        this.tlgLeaderNameMm = tlgLeaderNameMm;
    }

    public String getTlgLeaderPh() {
        return tlgLeaderPh;
    }

    public void setTlgLeaderPh(String tlgLeaderPh) {
        this.tlgLeaderPh = tlgLeaderPh;
    }

    public String getTlgLeaderRole() {
        return tlgLeaderRole;
    }

    public void setTlgLeaderRole(String tlgLeaderRole) {
        this.tlgLeaderRole = tlgLeaderRole;
    }

    public String getTlgGroupMemberNo() {
        return tlgGroupMemberNo;
    }

    public void setTlgGroupMemberNo(String tlgGroupMemberNo) {
        this.tlgGroupMemberNo = tlgGroupMemberNo;
    }

    public String getTlgGroupOtherMemberNo() {
        return tlgGroupOtherMemberNo;
    }

    public void setTlgGroupOtherMemberNo(String tlgGroupOtherMemberNo) {
        this.tlgGroupOtherMemberNo = tlgGroupOtherMemberNo;
    }

    public String getTlgGroupKeyActivity() {
        return tlgGroupKeyActivity;
    }

    public void setTlgGroupKeyActivity(String tlgGroupKeyActivity) {
        this.tlgGroupKeyActivity = tlgGroupKeyActivity;
    }

    public String getTlgGroupKeyActivityMm() {
        return tlgGroupKeyActivityMm;
    }

    public void setTlgGroupKeyActivityMm(String tlgGroupKeyActivityMm) {
        this.tlgGroupKeyActivityMm = tlgGroupKeyActivityMm;
    }

    public String getTlgGroupKeySkills() {
        return tlgGroupKeySkills;
    }

    public void setTlgGroupKeySkills(String tlgGroupKeySkills) {
        this.tlgGroupKeySkills = tlgGroupKeySkills;
    }

    public String getTlgGroupKeySkillsMm() {
        return tlgGroupKeySkillsMm;
    }

    public void setTlgGroupKeySkillsMm(String tlgGroupKeySkillsMm) {
        this.tlgGroupKeySkillsMm = tlgGroupKeySkillsMm;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String get_tlg_group_address() {
        return _tlg_group_address;
    }

    public void set_tlg_group_address(String _tlg_group_address) {
        this._tlg_group_address = _tlg_group_address;
    }

    public String get_tlg_group_lat_address() {
        return _tlg_group_lat_address;
    }

    public void set_tlg_group_lat_address(String _tlg_group_lat_address) {
        this._tlg_group_lat_address = _tlg_group_lat_address;
    }

    public String get_tlg_group_lng_address() {
        return _tlg_group_lng_address;
    }

    public void set_tlg_group_lng_address(String _tlg_group_lng_address) {
        this._tlg_group_lng_address = _tlg_group_lng_address;
    }
}
