package org.undp_iwomen.iwomen.data;



import java.io.Serializable;

/**
 * Created by khinsandar on 10/1/15.
 */
public class AuthorItem implements Serializable {


    private String authorImg;

    private String authorInfoEng;

    private String authorInfoMM;

    private String authorName;

    private String authorTitleEng;

    private String authorTitleMM;

    private String createdAt;

    private String objectId;

    private String updatedAt;

    public AuthorItem(){

    }

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public String getAuthorInfoEng() {
        return authorInfoEng;
    }

    public void setAuthorInfoEng(String authorInfoEng) {
        this.authorInfoEng = authorInfoEng;
    }

    public String getAuthorInfoMM() {
        return authorInfoMM;
    }

    public void setAuthorInfoMM(String authorInfoMM) {
        this.authorInfoMM = authorInfoMM;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorTitleEng() {
        return authorTitleEng;
    }

    public void setAuthorTitleEng(String authorTitleEng) {
        this.authorTitleEng = authorTitleEng;
    }

    public String getAuthorTitleMM() {
        return authorTitleMM;
    }

    public void setAuthorTitleMM(String authorTitleMM) {
        this.authorTitleMM = authorTitleMM;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
