package org.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GcmMessage implements Serializable {

    @SerializedName("_token")
    @Expose
    private String Token;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("postId")
    @Expose
    private Integer postId;

    //01-23 11:13:29.204 29014-29269/? I/iWomen: Gcm Message: {"message":"\u1031\u1014\u102c\u1000\u1039\u1021\u101e\u1005\u1039\u1011\u103c\u1000\u1039\u1019\u100a\u1039\u1037\u1017\u102c\u1038\u101b\u103d\u1004\u1039\u1038\u1010\u103c\u1004\u1039 \u1017\u101f\u102f\u101e\u102f\u1010\u101b\u101b\u1014\u1039\u1010\u103c\u1004\u1039 \u1031\u1000\u102c\u1019\u1014\u1039\u1094\u1031\u1015\u1038\u101c\u102d\u102f\u1094\u101b\u1015\u102b\u1031\u1010\u102c\u1037\u1019\u100a\u1039","title":"Q&A","postId":2198}
    /**
     * @return The Token
     */
    public String getToken() {
        return Token;
    }

    /**
     * @param Token The _token
     */
    public void setToken(String Token) {
        this.Token = Token;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

}

