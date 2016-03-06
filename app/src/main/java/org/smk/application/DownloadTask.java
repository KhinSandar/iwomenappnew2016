package org.smk.application;

import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.smk.clientapi.MySSLSocketFactory;
import org.smk.model.Download;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;


public class DownloadTask{
    private final static int BUFFER_SIZE = 1024 * 8;
	private String url;
	private String filePath = Environment.getExternalStorageDirectory() +"/iwomen/SKStorage/";
	private String apkFile = Environment.getExternalStorageDirectory() +"/iwomen/SKStorage/";
	private int responseCode;
	private long availableStorage;
	private HttpURLConnection connection;
	private OkHttpClient client;
	private boolean isDownloadingStop = false;
	private int totalSize;
	private int finishedSize;
	private int donwloadPercent;
	public DownloadTask(String url, String fileName) {
		availableStorage = StorageUtils.getAvailableStorage();
		IfExistFileDir(filePath);
		apkFile += fileName;
		this.url = url;
	}
	
	@SuppressWarnings("deprecation")
	public void startDownload(){
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);
	        
	        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			client = new OkHttpClient();
			client.setSslSocketFactory(sf.getSSLContext().getSocketFactory());
			URL http_url = new URL(url);
			connection = new OkUrlFactory(client).open(http_url);
			connection.setRequestMethod("GET");
			File resumeFile = new File(apkFile);
			if(resumeFile.exists()){
	            connection.setRequestProperty("Range", "bytes="+(resumeFile.length())+"-");
			}
			connection.setDoInput(true);
		    connection.setDoOutput(true);
			connection.connect();
			responseCode = connection.getResponseCode();
			Log.i("","Hello Response Code : "+responseCode);
			if(responseCode == 404){
				Log.e("","Download File Not Found.");
				return;
			}else if(responseCode == 422){
				Log.e("","Download File Error.");
				return;
			}else{
				Log.i("","Hello Downloaded File Size : "+resumeFile.length()+" Remain File Size : "+connection.getContentLength());
				if(resumeFile.length() == connection.getContentLength()){
					Log.e(null, "Output file already exists. Skipping download.");
					// TODO install for APK
					EventBus.getDefault().post(new Download(true, connection.getContentLength(), connection.getContentLength(), 100, apkFile));
					
				}else{
					createDir(new File(filePath));
					long lenghtOfFile = connection.getContentLength();
					RandomAccessFile output = new RandomAccessFile(apkFile, "rw");
					byte[] buffer = new byte[BUFFER_SIZE];

			        BufferedInputStream input = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);

			        int count = 0, n = 0;
					try {
						output.seek(output.length());
						while (!isDownloadingStop) {
			                n = input.read(buffer, 0, BUFFER_SIZE);
			                if (n == -1) {
			                    break;
			                }
			                totalSize = (int) lenghtOfFile;
			                finishedSize = (int) ((int) count / lenghtOfFile);
			                donwloadPercent = (int)((count*100)/lenghtOfFile);
			                EventBus.getDefault().post(new Download(false, totalSize, finishedSize, donwloadPercent, apkFile));
			                output.write(buffer, 0, n);
			                count += n;
						}

					} catch (Exception e) {
						// TODO: handle exception
					}finally{
						connection.disconnect();
						output.close();
						input.close();
						/*
				         * check memory
				         */
				        if(connection.getContentLength() > availableStorage){
				        	Log.e("","Out of Memory.");
				        }else{
				        	if(!isDownloadingStop){
				        		// TODO install for APK
				        		EventBus.getDefault().post(new Download(true, totalSize, finishedSize, donwloadPercent, apkFile));
				        		
				        	}
				        }
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void cancelDownload(){
		Log.i("","Hello Downloading Task Cancel...");
		isDownloadingStop = true;
	}
		
	/*private void unZipFile(String zipFile, String unZipFile){
		File archive = new File(zipFile);
		try {
	      	
	    	ZipFile zipfile = new ZipFile(archive);
	        for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
	        	ZipEntry entry = (ZipEntry) e.nextElement();
	            unzipEntry(zipfile, entry, unzipFile);
	        }
	        UnzipUtil d = new UnzipUtil(zipFile, unzipFile); 
	        d.unzip();
	          
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    finally{
	    	File zipFileLocation = new File(zipFile);
	        if(zipFileLocation.exists()){
	        	zipFileLocation.delete();
	        	EventBus.getDefault().post(true);
	        }
	    }
	}
	
	private void unzipEntry(ZipFile zipfile, ZipEntry entry,String outputDir) throws IOException {

	      if (entry.isDirectory()) {
	          createDir(new File(outputDir, entry.getName()));
	          return;
	      }

	      File outputFile = new File(outputDir, entry.getName());
	      if (!outputFile.getParentFile().exists()) {
	          createDir(outputFile.getParentFile());
	      }

	     // Log.v("", "Extracting: " + entry);
	      BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
	      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

	      try {

	      } finally {
	      	outputStream.flush();
	      	outputStream.close();
	      	inputStream.close();
	      }
	  }*/

	  private void createDir(File dir) {
	      if (dir.exists()) {
	          return;
	      }
	      if (!dir.mkdirs()) {
				throw new RuntimeException("Can not create dir " + dir);
	      }
	  }

	public Boolean IfExistFileDir(String filePath){
		File fileDir = new File(filePath);
		if(!fileDir.exists()){
			try {
				fileDir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
		return true;
	}

}
