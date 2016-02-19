package org.undp_iwomen.iwomen.model.retrofit_api;

//import com.google.android.gms.internal.ne;


import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import org.undp_iwomen.iwomen.CommonConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;


public class TlgProfileAPI {
    private static TlgProfileAPI mInstance;
    private TlgProfileService mService;


    public TlgProfileAPI(){

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Parse-Application-Id","WyU802fB70eNhyF9uSoj1SgKVKGLYYAZ0kX96xhr");

                request.addHeader("X-Parse-REST-API-Key", "02De951aEWCJ367IhEwqhFJ1tVjQZ3y6RoQ8c2Xi");
            }
        };


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(7000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(7000, TimeUnit.MILLISECONDS);

        final RestAdapter restAdapter = new
                RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(CommonConfig.BASE_URL)
                .setRequestInterceptor(requestInterceptor)


                .setClient(new OkClient(okHttpClient))

                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                       // Log.e("//////////////TLG  Profile API////////////////////////", msg);
                    }
                })
                .setConverter(new StringConverter()) //Reply String result
                .build();


        mService = restAdapter.create(TlgProfileService.class);



    }




    //.setRequestInterceptor(requestInterceptor)


    public static TlgProfileAPI getInstance(){
        if(mInstance == null){
            mInstance = new TlgProfileAPI();
        }
        return  mInstance;
    }
    public TlgProfileService getService(){
        return mService;
    }

    public static class StringConverter implements Converter {


        @Override
        public Object fromBody(TypedInput body, java.lang.reflect.Type type) throws ConversionException {
            String text = null;
            try {
                text = fromStream(body.in());
            } catch (IOException ignored) {/*NOP*/ }

            return text;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return null;
        }

        public static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }

    public static class MockClient implements Client {
        @Override
        public Response execute(Request request) throws IOException {
            URI uri = URI.create(request.getUrl());
            String responseString = "";

            if (uri.getPath().equals("/api/products")) {
                responseString = "{result:\"ok\"}";
            } else {
                responseString = "{result:\"error\"}";
            }

            return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST,
                    new TypedByteArray("application/json", responseString.getBytes()));
        }
    }

}
