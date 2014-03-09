package de.htw.mada.cookit.utils;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpConnectionManager{

	private static HttpConnectionManager httpConnectionManager;

	public static HttpConnectionManager getInstance(){
		if(httpConnectionManager == null) {
			httpConnectionManager = new HttpConnectionManager();
		}
		return httpConnectionManager;
	}

	private String parseCommand(short command) {
		String url = "";
		switch (command) {
		case Constant.GET_A_RECIPE:
			url = Constant.IP_ADDRESS + "/android_projekt/fetch.php?";
			break;

		case Constant.GET_ALL_RECIPEES:
			url = Constant.IP_ADDRESS + "/android_projekt/fetchall.php?";
			break;

		case Constant.INSERT_RECIPE :
			url = Constant.IP_ADDRESS + "/android_projekt/insert.php?";
			break;

		case Constant.UPLOAD_PICTURE :
			url = Constant.IP_ADDRESS + "/android_projekt/fileupload.php";
			break;
		}
		return url;
	}

	public KochBuchResponseHandler request(short command, String[] input, int id){
		String result = "";
		KochBuchResponseHandler kochBuchResponseHandler = null;
		try {
			String url = parseCommand(command);
			HttpClient httpClient = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			HttpPost httpPost = new HttpPost(url);

			switch (command) {
			case Constant.GET_A_RECIPE:

				nameValuePairs.add(new BasicNameValuePair(Constant.ID, "" + id));


				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				result = httpClient.execute(httpPost,responseHandler);
				break;

			case Constant.GET_ALL_RECIPEES:

				result = httpClient.execute(httpPost,responseHandler);
				break;

			case Constant.INSERT_RECIPE:

				nameValuePairs.add(new BasicNameValuePair("dish_name",input[0]));
				nameValuePairs.add(new BasicNameValuePair("author",input[1]));
				nameValuePairs.add(new BasicNameValuePair("comment",input[2]));
				nameValuePairs.add(new BasicNameValuePair("type",input[3]));
				nameValuePairs.add(new BasicNameValuePair("difficulty",input[4]));
				nameValuePairs.add(new BasicNameValuePair("duration",input[5]));
				nameValuePairs.add(new BasicNameValuePair("ingredients",input[6]));
				nameValuePairs.add(new BasicNameValuePair("directions",input[7]));
				nameValuePairs.add(new BasicNameValuePair("notes",input[8]));
				if(input[10] != null) {
					nameValuePairs.add(new BasicNameValuePair("image_name","/android_projekt/upload/" + input[10]));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				result = httpClient.execute(httpPost, responseHandler);
				break;

			case Constant.UPLOAD_PICTURE:

				nameValuePairs.add(new BasicNameValuePair("image", input[9]));
				nameValuePairs.add(new BasicNameValuePair("image_name", input[10]));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				result = httpClient.execute(httpPost, responseHandler);
				break;
			}


		} catch (Exception e) {
		}
		
		kochBuchResponseHandler = KochBuchResponseHandler.parseResponse(result);
		
		return kochBuchResponseHandler;
	}

}
