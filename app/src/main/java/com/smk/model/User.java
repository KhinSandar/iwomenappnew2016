package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("id")
@Expose
private String id;
@SerializedName("username")
@Expose
private String username;
@SerializedName("email")
@Expose
private String email;
@SerializedName("first_name")
@Expose
private String firstName;
@SerializedName("last_name")
@Expose
private String lastName;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("address")
@Expose
private Object address;
@SerializedName("photo")
@Expose
private Object photo;
@SerializedName("remember_token")
@Expose
private String rememberToken;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("role")
@Expose
private String role;



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
* The firstName
*/
public String getFirstName() {
return firstName;
}

/**
* 
* @param firstName
* The first_name
*/
public void setFirstName(String firstName) {
this.firstName = firstName;
}

/**
* 
* @return
* The lastName
*/
public String getLastName() {
return lastName;
}

/**
* 
* @param lastName
* The last_name
*/
public void setLastName(String lastName) {
this.lastName = lastName;
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
* The address
*/
public Object getAddress() {
return address;
}

/**
* 
* @param address
* The address
*/
public void setAddress(Object address) {
this.address = address;
}

/**
* 
* @return
* The photo
*/
public Object getPhoto() {
return photo;
}

/**
* 
* @param photo
* The photo
*/
public void setPhoto(Object photo) {
this.photo = photo;
}

/**
* 
* @return
* The rememberToken
*/
public String getRememberToken() {
return rememberToken;
}

/**
* 
* @param rememberToken
* The remember_token
*/
public void setRememberToken(String rememberToken) {
this.rememberToken = rememberToken;
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
* The role
*/
  public void setRole(String role) {
        this.role = role;
    }

/**
*
 */
public String getRole() {
    return this.role;
}


}
