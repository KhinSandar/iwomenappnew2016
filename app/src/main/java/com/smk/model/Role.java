package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {

@SerializedName("id")
@Expose
private String id;
@SerializedName("slug")
@Expose
private String slug;
@SerializedName("name")
@Expose
private String name;
@SerializedName("permissions")
@Expose
private Permissions permissions;
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
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The slug
*/
public String getSlug() {
return slug;
}

/**
* 
* @param slug
* The slug
*/
public void setSlug(String slug) {
this.slug = slug;
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
* The permissions
*/
public Permissions getPermissions() {
return permissions;
}

/**
* 
* @param permissions
* The permissions
*/
public void setPermissions(Permissions permissions) {
this.permissions = permissions;
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
