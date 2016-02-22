package org.smk.clientapi;


import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class NetworkEngine {
	private static final String TAG = "Retrofit";
	static INetworkEngine instance;
	public static RequestInterceptor requestInterceptor = new RequestInterceptor() {
		  public void intercept(RequestFacade request) {
			System.setProperty("http.keepAlive", "false");
			request.addHeader("Connection", "close");
			request.addHeader("Accept-Language", "en-US,en;q=0.8");
		    request.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		    
		  }
		};
	public static INetworkEngine getInstance() {
		
		if (instance==null) {
			RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint("http://api.iwomenapp.org")
			.setLogLevel(RestAdapter.LogLevel.FULL)
			.setRequestInterceptor(requestInterceptor)
			.setClient(new GzippedClient(new OkClient(new OkHttpClient())))
			.setLog(new RestAdapter.Log() {
				@Override
				public void log(String msg) {
					Log.d(TAG, "==>"+ msg);
				}
			})
			.setErrorHandler(new MyErrorHandler()).build();
			instance = adapter.create(INetworkEngine.class);
		}
		return instance;
	}
}
