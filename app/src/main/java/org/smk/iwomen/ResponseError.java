package org.smk.iwomen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseError {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_mm")
    @Expose
    private String errorMm;

    /**
     * @return The error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error The error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return The errorMm
     */
    public String getErrorMm() {
        return errorMm;
    }

    /**
     * @param errorMm The error_mm
     */
    public void setErrorMm(String errorMm) {
        this.errorMm = errorMm;
    }
}