package org.undp_iwomen.iwomen.data;

public class FeedItem {


    private String post_obj_id;
    String post_title;
    String post_content;
    int post_like;
    String post_img_path;
    String post_content_type;
    String post_content_user_id;
    String post_content_user_name;
    String post_content_user_img_path;

    //TODO TableColumnUpdate 4
    String post_content_author_id;
    String post_content_author_role;

    String post_content_video_id;
    String post_content_suggest_text;
    String post_content_mm;
    String post_title_mm;

    String post_like_status;
    String status;
    String created_at;
    String updated_at;

    String author_role_mm;
    String credit_name;
    String credit_logo_link;
    String credit_link_mm;
    String credit_link_eng;
    int post_comment_count = 0;
    int post_share_count = 0;

    public FeedItem() {
    }


    public FeedItem(String post_obj_id, String post_title, String post_content, int post_like,
                    String post_img_path, String post_content_type, String post_content_user_id,
                    String post_content_user_name, String post_content_user_img_path,
                    String post_content_mm,
                    String post_video_id, String post_suggest_text, String post_title_mm,
                    String post_like_status,
                    String status, String created_at, String updated_at) {
        super();
        this.post_obj_id = post_obj_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_like = post_like;
        this.post_img_path = post_img_path;
        this.post_content_type = post_content_type;
        this.post_content_user_id = post_content_user_id;
        this.post_content_user_name = post_content_user_name;
        this.post_content_user_img_path = post_content_user_img_path;

        this.post_content_video_id = post_video_id;
        this.post_content_suggest_text = post_suggest_text;
        this.post_content_mm = post_content_mm;
        this.post_title_mm = post_title_mm;
        this.post_like_status = post_like_status;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getPost_content_mm() {
        return post_content_mm;
    }

    public void setPost_content_mm(String post_content_mm) {
        this.post_content_mm = post_content_mm;
    }

    public String getPost_obj_id() {
        return post_obj_id;
    }

    public void setPost_obj_id(String post_obj_id) {
        this.post_obj_id = post_obj_id;
    }

    public int getPost_like() {
        return post_like;
    }

    public void setPost_like(int post_like) {
        this.post_like = post_like;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }


    public String getPost_img_path() {
        return post_img_path;
    }

    public void setPost_img_path(String post_img_path) {
        this.post_img_path = post_img_path;
    }

    public String getPost_content_type() {
        return post_content_type;
    }

    public void setPost_content_type(String post_content_type) {
        this.post_content_type = post_content_type;
    }

    public String getPost_content_user_id() {
        return post_content_user_id;
    }

    public void setPost_content_user_id(String post_content_user_id) {
        this.post_content_user_id = post_content_user_id;
    }

    public String getPost_content_user_name() {
        return post_content_user_name;
    }

    public void setPost_content_user_name(String post_content_user_name) {
        this.post_content_user_name = post_content_user_name;
    }

    public String getPost_content_user_img_path() {
        return post_content_user_img_path;
    }

    public void setPost_content_user_img_path(String post_content_user_img_path) {
        this.post_content_user_img_path = post_content_user_img_path;
    }

    public String getPost_content_video_id() {
        return post_content_video_id;
    }

    public void setPost_content_video_id(String post_content_video_id) {
        this.post_content_video_id = post_content_video_id;
    }

    public String getPost_content_suggest_text() {
        return post_content_suggest_text;
    }

    public void setPost_content_suggest_text(String post_content_suggest_text) {
        this.post_content_suggest_text = post_content_suggest_text;
    }

    public String getPost_content_author_id() {
        return post_content_author_id;
    }

    public void setPost_content_author_id(String post_content_author_id) {
        this.post_content_author_id = post_content_author_id;
    }

    public String getPost_content_author_role() {
        return post_content_author_role;
    }

    public void setPost_content_author_role(String post_content_author_role) {
        this.post_content_author_role = post_content_author_role;
    }

    public String getPost_title_mm() {
        return post_title_mm;
    }

    public void setPost_title_mm(String post_title_mm) {
        this.post_title_mm = post_title_mm;
    }

    public String getPost_like_status() {
        return post_like_status;
    }

    public void setPost_like_status(String post_like_status) {
        this.post_like_status = post_like_status;
    }


    public String getAuthor_role_mm() {
        return author_role_mm;
    }

    public void setAuthor_role_mm(String author_role_mm) {
        this.author_role_mm = author_role_mm;
    }

    public String getCredit_name() {
        return credit_name;
    }

    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    public String getCredit_logo_link() {
        return credit_logo_link;
    }

    public void setCredit_logo_link(String credit_logo_link) {
        this.credit_logo_link = credit_logo_link;
    }

    public String getCredit_link_mm() {
        return credit_link_mm;
    }

    public void setCredit_link_mm(String credit_link_mm) {
        this.credit_link_mm = credit_link_mm;
    }

    public String getCredit_link_eng() {
        return credit_link_eng;
    }

    public void setCredit_link_eng(String credit_link_eng) {
        this.credit_link_eng = credit_link_eng;
    }

    public int getPost_comment_count() {
        return post_comment_count;
    }

    public void setPost_comment_count(int post_comment_count) {
        this.post_comment_count = post_comment_count;
    }

    public int getPost_share_count() {
        return post_share_count;
    }

    public void setPost_share_count(int post_share_count) {
        this.post_share_count = post_share_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
