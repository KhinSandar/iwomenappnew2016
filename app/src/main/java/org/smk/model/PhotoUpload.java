package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoUpload implements Serializable {

@SerializedName("__type")
@Expose
private String Type;
@SerializedName("name")
@Expose
private String name;
@SerializedName("url")
@Expose
private String url;
@SerializedName("resize_url")
@Expose
private List<String> resizeUrl = new ArrayList<String>();

/**
* 
* @return
* The Type
*/
public String getType() {
return Type;
}

/**
* 
* @param Type
* The __type
*/
public void setType(String Type) {
this.Type = Type;
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
* The url
*/
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
public void setUrl(String url) {
this.url = url;
}

/**
* 
* @return
* The resizeUrl
*/
public List<String> getResizeUrl() {
return resizeUrl;
}

/**
* 
* @param resizeUrl
* The resize_url
*/
public void setResizeUrl(List<String> resizeUrl) {
this.resizeUrl = resizeUrl;
}

}
