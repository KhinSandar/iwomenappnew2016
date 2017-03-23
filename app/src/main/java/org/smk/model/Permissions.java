package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Permissions implements Serializable {

@SerializedName("user.create")
@Expose
private boolean userCreate;
@SerializedName("user.update")
@Expose
private boolean userUpdate;
@SerializedName("user.view")
@Expose
private boolean userView;
@SerializedName("user.delete")
@Expose
private boolean userDelete;

/**
* 
* @return
* The userCreate
*/
public boolean isUserCreate() {
return userCreate;
}

/**
* 
* @param userCreate
* The user.create
*/
public void setUserCreate(boolean userCreate) {
this.userCreate = userCreate;
}

/**
* 
* @return
* The userUpdate
*/
public boolean isUserUpdate() {
return userUpdate;
}

/**
* 
* @param userUpdate
* The user.update
*/
public void setUserUpdate(boolean userUpdate) {
this.userUpdate = userUpdate;
}

/**
* 
* @return
* The userView
*/
public boolean isUserView() {
return userView;
}

/**
* 
* @param userView
* The user.view
*/
public void setUserView(boolean userView) {
this.userView = userView;
}

/**
* 
* @return
* The userDelete
*/
public boolean isUserDelete() {
return userDelete;
}

/**
* 
* @param userDelete
* The user.delete
*/
public void setUserDelete(boolean userDelete) {
this.userDelete = userDelete;
}

}
