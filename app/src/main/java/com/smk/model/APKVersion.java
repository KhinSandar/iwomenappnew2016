package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APKVersion {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("version_id")
@Expose
private Integer versionId;
@SerializedName("version_name")
@Expose
private String versionName;
@SerializedName("created_at")
@Expose
private String createdAt;

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
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The versionId
*/
public Integer getVersionId() {
return versionId;
}

/**
* 
* @param versionId
* The version_id
*/
public void setVersionId(Integer versionId) {
this.versionId = versionId;
}

/**
* 
* @return
* The versionName
*/
public String getVersionName() {
return versionName;
}

/**
* 
* @param versionName
* The version_name
*/
public void setVersionName(String versionName) {
this.versionName = versionName;
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

}