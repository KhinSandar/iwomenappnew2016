package org.smk.model;

/**
 * Created by SMK on 5/9/2016.
 */
public class UserIntro {
    private int drawable;
    private String description;

    public UserIntro(int drawable, String description) {
        this.drawable = drawable;
        this.description = description;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
