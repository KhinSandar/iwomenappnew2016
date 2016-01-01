package org.undp_iwomen.iwomen.data;

/**
 * Created by khinsandar on 8/13/15.
 */
public class CommentItem {

    private String _profile_picture;
    private String _user_name;
    private String _comment_message;
    private String _timestamp;

    public CommentItem(String profile_picture, String _user_name, String comment_message, String timestamp){
        this._profile_picture = profile_picture;
        this._user_name = _user_name;
        this._comment_message = comment_message;
        this._timestamp = timestamp;
    }

    public String get_profile_picture() {
        return _profile_picture;
    }

    public void set_profile_picture(String _profile_picture) {
        this._profile_picture = _profile_picture;
    }

    public String get_user_name() {
        return _user_name;
    }

    public void set_user_name(String _user_name) {
        this._user_name = _user_name;
    }

    public String get_comment_message() {
        return _comment_message;
    }

    public void set_comment_message(String _comment_message) {
        this._comment_message = _comment_message;
    }

    public String get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(String _timestamp) {
        this._timestamp = _timestamp;
    }
}
