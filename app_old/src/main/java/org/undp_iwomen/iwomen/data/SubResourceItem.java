package org.undp_iwomen.iwomen.data;

import java.io.Serializable;

/**
 * Created by khinsandar on 7/29/15.
 */
public class SubResourceItem implements Serializable {




    private int resourceImg;
    private String authorName;
    private String author_id;
    private String icon_img_url;
    private String author_img_url;

    private String posted_date;
    private String sub_resouce_content_eng;
    private String sub_resouce_content_mm;
    private String sub_resource_title_eng;
    private String sub_resource_title_mm;


    public SubResourceItem(){

        super();
    }
    public SubResourceItem(String sub_resouce_title_eng,String sub_resouce_title_mm, String sub_resouce_cn_eng,String sub_resouce_cn_mm,String authorName, String authorId, String authorImgPath,String IconimgPath ,String postDate){
        super();

        this.sub_resource_title_eng = sub_resouce_title_eng;
        this.sub_resource_title_mm = sub_resouce_title_mm;
        this.sub_resouce_content_eng = sub_resouce_cn_eng;
        this.sub_resouce_content_mm = sub_resouce_cn_mm;

        this.authorName = authorName;
        this.author_id = authorId;

        this.author_img_url = authorImgPath;
        this.icon_img_url = IconimgPath;

        this.posted_date = postDate;
    }

    public String getIcon_img_url() {
        return icon_img_url;
    }

    public void setIcon_img_url(String icon_img_url) {
        this.icon_img_url = icon_img_url;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_img_url() {
        return author_img_url;
    }

    public void setAuthor_img_url(String author_img_url) {
        this.author_img_url = author_img_url;
    }

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }

    public String getSub_resouce_content_eng() {
        return sub_resouce_content_eng;
    }

    public void setSub_resouce_content_eng(String sub_resouce_content_eng) {
        this.sub_resouce_content_eng = sub_resouce_content_eng;
    }

    public String getSub_resouce_content_mm() {
        return sub_resouce_content_mm;
    }

    public void setSub_resouce_content_mm(String sub_resouce_content_mm) {
        this.sub_resouce_content_mm = sub_resouce_content_mm;
    }

    public String getSub_resource_title_eng() {
        return sub_resource_title_eng;
    }

    public void setSub_resource_title_eng(String sub_resource_title_eng) {
        this.sub_resource_title_eng = sub_resource_title_eng;
    }

    public String getSub_resource_title_mm() {
        return sub_resource_title_mm;
    }

    public void setSub_resource_title_mm(String sub_resource_title_mm) {
        this.sub_resource_title_mm = sub_resource_title_mm;
    }



    public int getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }
}
