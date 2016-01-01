package org.undp_iwomen.iwomen.data;

import java.io.Serializable;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourceItem implements Serializable {

    private String resourceText;
    private String resourceName;
    private String resourceNameMM;
    private String resourceImgPath;
    private int resourceImg;
    private String resourceId;


    public ResourceItem(){

        super();
    }
    public ResourceItem(String id,String resourceName ,String resoueMM ,  String imgPath){
        super();
        //this.resourceText = text;
        //this.resourceImg = i;
        this.resourceId = id;
        this.resourceName = resourceName;
        this.resourceNameMM = resoueMM;
        this.resourceImgPath = imgPath;

    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceText() {
        return resourceText;
    }

    public String getResourceNameMM() {
        return resourceNameMM;
    }

    public void setResourceNameMM(String resourceNameMM) {
        this.resourceNameMM = resourceNameMM;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResourceText(String resourceText) {
        this.resourceText = resourceText;
    }

    public String getResourceImgPath() {
        return resourceImgPath;
    }

    public void setResourceImgPath(String resourceImgPath) {
        this.resourceImgPath = resourceImgPath;
    }

    public int getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }
}
