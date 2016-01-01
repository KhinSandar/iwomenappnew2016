package org.undp_iwomen.iwomen.data;

import java.io.Serializable;

/**
 * Created by khinsandar on 8/28/15.
 */
public class SisterAppItem implements Serializable{

    private String _app_name;
    private String _app_package_name;
    private String _app_img;
    private String _app_down_link;
    String _object_id;
    public SisterAppItem(String id,String app_name,String app_package_name, String app_down_link, String img_app){
        this._object_id = id;
        this._app_name = app_name;
        this._app_down_link = app_down_link;
        this._app_img = img_app;
        this._app_package_name = app_package_name;
    }

    public String get_object_id() {
        return _object_id;
    }

    public void set_object_id(String _object_id) {
        this._object_id = _object_id;
    }

    public String get_app_package_name() {
        return _app_package_name;
    }

    public void set_app_package_name(String _app_package_name) {
        this._app_package_name = _app_package_name;
    }

    public String get_app_name() {
        return _app_name;
    }

    public void set_app_name(String _app_name) {
        this._app_name = _app_name;
    }

    public String get_app_img() {
        return _app_img;
    }

    public void set_app_img(String _app_img) {
        this._app_img = _app_img;
    }

    public String get_app_down_link() {
        return _app_down_link;
    }

    public void set_app_down_link(String _app_down_link) {
        this._app_down_link = _app_down_link;
    }
}
