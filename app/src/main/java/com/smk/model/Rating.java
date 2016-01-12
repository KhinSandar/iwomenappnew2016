package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("total_ratings")
    @Expose
    private Double totalRatings;
    @SerializedName("total_users")
    @Expose
    private Integer totalUsers;

    /**
     *
     * @return
     * The totalRatings
     */
    public Double getTotalRatings() {
        return totalRatings;
    }

    /**
     *
     * @param totalRatings
     * The total_ratings
     */
    public void setTotalRatings(Double totalRatings) {
        this.totalRatings = totalRatings;
    }

    /**
     *
     * @return
     * The totalUsers
     */
    public Integer getTotalUsers() {
        return totalUsers;
    }

    /**
     *
     * @param totalUsers
     * The total_users
     */
    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

}