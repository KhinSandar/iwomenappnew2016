package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("facebookId")
    @Expose
    private String facebookId;
    @SerializedName("isTlgTownshipExit")
    @Expose
    private Boolean isTlgTownshipExit;
    @SerializedName("isTestAcc")
    @Expose
    private Boolean isTestAcc;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("searchName")
    @Expose
    private String searchName;
    @SerializedName("tlg_city_address")
    @Expose
    private String tlgCityAddress;
    @SerializedName("tlg_city")
    @Expose
    private String tlgCity;
    @SerializedName("tlg_country")
    @Expose
    private String tlgCountry;
    @SerializedName("user_profile_img")
    @Expose
    private String userProfileImg;
    @SerializedName("registerBODname")
    @Expose
    private String registerBODname;
    @SerializedName("userImgPath")
    @Expose
    private String userImgPath;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("role")
    @Expose
    private String role;

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
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The facebookId
     */
    public String getFacebookId() {
        return facebookId;
    }

    /**
     *
     * @param facebookId
     * The facebookId
     */
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    /**
     *
     * @return
     * The isTlgTownshipExit
     */
    public Boolean getIsTlgTownshipExit() {
        return isTlgTownshipExit;
    }

    /**
     *
     * @param isTlgTownshipExit
     * The isTlgTownshipExit
     */
    public void setIsTlgTownshipExit(Boolean isTlgTownshipExit) {
        this.isTlgTownshipExit = isTlgTownshipExit;
    }

    /**
     *
     * @return
     * The isTestAcc
     */
    public Boolean getIsTestAcc() {
        return isTestAcc;
    }

    /**
     *
     * @param isTestAcc
     * The isTestAcc
     */
    public void setIsTestAcc(Boolean isTestAcc) {
        this.isTestAcc = isTestAcc;
    }

    /**
     *
     * @return
     * The phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     *
     * @param phoneNo
     * The phoneNo
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     *
     * @return
     * The profileimage
     */
    public String getProfileimage() {
        return profileimage;
    }

    /**
     *
     * @param profileimage
     * The profileimage
     */
    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    /**
     *
     * @return
     * The searchName
     */
    public String getSearchName() {
        return searchName;
    }

    /**
     *
     * @param searchName
     * The searchName
     */
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    /**
     *
     * @return
     * The tlgCityAddress
     */
    public String getTlgCityAddress() {
        return tlgCityAddress;
    }

    /**
     *
     * @param tlgCityAddress
     * The tlg_city_address
     */
    public void setTlgCityAddress(String tlgCityAddress) {
        this.tlgCityAddress = tlgCityAddress;
    }

    /**
     *
     * @return
     * The tlgCity
     */
    public String getTlgCity() {
        return tlgCity;
    }

    /**
     *
     * @param tlgCity
     * The tlg_city
     */
    public void setTlgCity(String tlgCity) {
        this.tlgCity = tlgCity;
    }

    /**
     *
     * @return
     * The tlgCountry
     */
    public String getTlgCountry() {
        return tlgCountry;
    }

    /**
     *
     * @param tlgCountry
     * The tlg_country
     */
    public void setTlgCountry(String tlgCountry) {
        this.tlgCountry = tlgCountry;
    }

    /**
     *
     * @return
     * The userProfileImg
     */
    public String getUserProfileImg() {
        return userProfileImg;
    }

    /**
     *
     * @param userProfileImg
     * The user_profile_img
     */
    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    /**
     *
     * @return
     * The registerBODname
     */
    public String getRegisterBODname() {
        return registerBODname;
    }

    /**
     *
     * @param registerBODname
     * The registerBODname
     */
    public void setRegisterBODname(String registerBODname) {
        this.registerBODname = registerBODname;
    }

    /**
     *
     * @return
     * The userImgPath
     */
    public String getUserImgPath() {
        return userImgPath;
    }

    /**
     *
     * @param userImgPath
     * The userImgPath
     */
    public void setUserImgPath(String userImgPath) {
        this.userImgPath = userImgPath;
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

    /**
     *
     * @return
     * The role
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @param role
     * The role
     */
    public void setRole(String role) {
        this.role = role;
    }

}
