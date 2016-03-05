package org.undp_iwomen.iwomen.model.retrofit_api;

import org.smk.model.PhotoUpload;
import org.undp_iwomen.iwomen.CommonConfig;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.mime.MultipartTypedOutput;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface SMKSeverService {


    @POST(CommonConfig.CREATE_USER_PHOTO_URL)
    void postUserPhoto(
            @Body MultipartTypedOutput attachments,
            Callback<PhotoUpload> callback);


}
