package org.undp_iwomen.iwomen.model.retrofit_api;

//import com.google.android.gms.internal.ne;


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


public class CommentAPI {
    private static CommentAPI mInstance;
    private CommentService mService;


    public CommentAPI(){
        /*ApiRequestInterceptor request_Interceptor = new ApiRequestInterceptor();
        User user = new User();
        user.setUser_apikey("4a321A0415PK7E50G2720R9591SwH6bX");
        user.setUseremail("api@myanmadeals.com");

        request_Interceptor.setUser(user);// I pass the user from my model*/
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
                        //Log.e("//////////////Lost Report API////////////////////////", msg);
                    }
                })
                .setConverter(new StringConverter()) //Reply String result
                .build();

        //.setLog(new AndroidLog("YOUR_LOG_TAG"))

        //RestAdapter restAdapterII = new RestAdapter.Builder().setClient(new OkClient(okHttpClient)).build();

        /*new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
            .setEndpoint(Config.BASE_URL)
            .build();*/
        //.setClient(new OkClient(okHttpClient))



        /*final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setConverter(new StringConverter())
                .setEndpoint(Config.PRODUCTS_BASE_URL).build();
        */
        mService = restAdapter.create(CommentService.class);



    }




    //.setRequestInterceptor(requestInterceptor)


    public static CommentAPI getInstance(){
        if(mInstance == null){
            mInstance = new CommentAPI();
        }
        return  mInstance;
    }
    public CommentService getService(){
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
