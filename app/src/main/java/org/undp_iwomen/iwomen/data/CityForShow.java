package org.undp_iwomen.iwomen.data;

/**
 * Created by Toe Lie on 7/1/2015.
 */
public class CityForShow {
    private String mId;
    private String mNameInEnglish;
    private String mNameInMyanmar;

    public CityForShow(){}

    public CityForShow(String id, String nameInEnglish, String nameInMyanmar) {
        mId = id;
        mNameInEnglish = nameInEnglish;
        mNameInMyanmar = nameInMyanmar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNameInEnglish() {
        return mNameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        mNameInEnglish = nameInEnglish;
    }

    public String getNameInMyanmar() {
        return mNameInMyanmar;
    }

    public void setNameInMyanmar(String nameInMyanmar) {
        mNameInMyanmar = nameInMyanmar;
    }
}
