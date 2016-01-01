package org.undp_iwomen.iwomen.model.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by khinsandar on 4/17/15.
 */
@ParseClassName("AppVersion")
public class AppVersion extends ParseObject {

    public void setVersionName(String description) {
        put("VersionName",description);
    }

    public String getVersionName() {
        return getString("VersionName");
    }

    public static ParseQuery<AppVersion> getQuery(){
        return ParseQuery.getQuery(AppVersion.class);
    }
}
