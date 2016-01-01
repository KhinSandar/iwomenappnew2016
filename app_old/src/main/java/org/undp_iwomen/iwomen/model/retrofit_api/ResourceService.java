package org.undp_iwomen.iwomen.model.retrofit_api;

import org.undp_iwomen.iwomen.CommonConfig;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface ResourceService {


    @GET(CommonConfig.RESOURCE_URL)
    public void getResourceList( @Query("order") String s,@Query("limit") int limit,@Query("skip") int skiplimit,@Query("where") String sWhere, Callback<String> callback);



}
