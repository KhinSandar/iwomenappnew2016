package org.undp_iwomen.iwomen.data;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by dharmaone on 14/08/2014.
 */
public class CategoriesDataModel implements Serializable {

    public String category_id;
    public String parent_id;
    public String id_path;
    public String category;
    public String position;
    public String status;
    public String product_count;
    public String seo_name;
    public String seo_path;

    public JSONObject main_pair;

    public String image_path;

    public CategoriesDataModel(){
        super();
    }
    public CategoriesDataModel(String cat_id, String cat_name, String img_path){
        this.category_id = cat_id;
        this.category = cat_name;
        this.image_path = img_path;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    //public ArrayList<String> main_pair;
    //public JSONArray main_pair;
    //public JSONObject detailed;
}
