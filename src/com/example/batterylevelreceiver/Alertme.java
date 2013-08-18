package com.example.batterylevelreceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Alertme extends BroadcastReceiver{

	public Boolean low=false;
	private String imei= null;
	Context ctx;
	public Alertme(Context c) {
		ctx=c;
		// TODO Auto-generated constructor stub
		
	}
        @Override
        public void onReceive(Context context, Intent intent) {
        	
             /*   Intent i = new Intent(context, MainActivity.class);  
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(i);  */
        	TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
    		imei=telephonyManager.getDeviceId();
        	sendPostRequest("low",imei); 
        }
        
    	// Send data to server http post request
        private void sendPostRequest(String lon, String lat) {

    		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

    			@Override
    			protected String doInBackground(String ...params) {

    				String lon = params[0];
    				String lat = params[1];
    			

    				//System.out.println("*** doInBackground ** lon " + lon + " lat :" + lat);

    				HttpClient httpClient = new DefaultHttpClient();

    				// In a POST request, we don't pass the values in the URL.
    				//Therefore we use only the web page URL as the parameter of the HttpPost argument
    				HttpPost httpPost = new HttpPost("http://sms2share.herobo.com/android2php.php");

    				// Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
    				//uniquely separate by the other end.
    				//To achieve that we use BasicNameValuePair				
    				//Things we need to pass with the POST request
    				BasicNameValuePair lonBasicNameValuePair = new BasicNameValuePair("lon", lon);
    				BasicNameValuePair latBasicNameValuePair = new BasicNameValuePair("lat", lat);
    				

    				// We add the content that we want to pass with the POST request to as name-value pairs
    				//Now we put those sending details to an ArrayList with type safe of NameValuePair
    				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
    				nameValuePairList.add(lonBasicNameValuePair);
    				nameValuePairList.add(latBasicNameValuePair);
    				

    				try {
    					// UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs. 
    					//This is typically useful while sending an HTTP POST request. 
    					UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

    					// setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
    					httpPost.setEntity(urlEncodedFormEntity);

    					try {
    						// HttpResponse is an interface just like HttpPost.
    						//Therefore we can't initialize them
    						HttpResponse httpResponse = httpClient.execute(httpPost);

    						// According to the JAVA API, InputStream constructor do nothing. 
    						//So we can't initialize InputStream although it is not an interface
    						InputStream inputStream = httpResponse.getEntity().getContent();

    						InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

    						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    						StringBuilder stringBuilder = new StringBuilder();

    						String bufferedStrChunk = null;

    						while((bufferedStrChunk = bufferedReader.readLine()) != null){
    							stringBuilder.append(bufferedStrChunk);
    						}

    						return stringBuilder.toString();

    					} catch (ClientProtocolException cpe) {
    						System.out.println("First Exception caz of HttpResponese :" + cpe);
    						cpe.printStackTrace();
    					} catch (IOException ioe) {
    						System.out.println("Second Exception caz of HttpResponse :" + ioe);
    						ioe.printStackTrace();
    					}

    				} catch (UnsupportedEncodingException uee) {
    					System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
    					uee.printStackTrace();
    				}

    				return null;
    			}

    			@Override
    			protected void onPostExecute(String result) {
    				super.onPostExecute(result);

    				if(result.equals("working")){
    					Toast.makeText(ctx.getApplicationContext(), "HTTP POST is working...", Toast.LENGTH_LONG).show();
    				}else{
    					//Toast.makeText(getApplicationContext(), "Invalid POST req...", Toast.LENGTH_LONG).show();
    				}
    			}

    						
    		}

    		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
    		sendPostReqAsyncTask.execute(lon, lat);		
    	}      
}