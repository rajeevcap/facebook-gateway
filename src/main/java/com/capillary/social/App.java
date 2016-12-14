package com.capillary.social;

/**
 * Hello world!
 *
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.servicediscovery.Service;
import com.capillary.servicediscovery.ServiceDiscovery;
import com.capillary.servicediscovery.model.KnownService;
import com.google.gson.Gson;

public class App {
	private final String USER_AGENT = "Mozilla/5.0";

	private static Logger logger = LoggerFactory.getLogger(App.class);

	// HTTP POST request
	public String sendPost() throws Exception {
		String url = "";
		StringBuffer result = new StringBuffer();

		try {
			url = "https://graph.facebook.com/v2.6/me/messages?"
					+ "access_token=EAAKPVQEsKeEBAEu7gLAnSYK"
					+ "Y3bAdfVTZBcQMWZBiN3kZAJKiUH7fnK5R0SJ6FbXJ5RJdSZBhifgKY2BC7gPxiaCZCzw1S9g2q0ZCeRS3HW67rZA8TiOKNSAZA3PyZAEn29scb9rtttwVfRdA02ZCZCfPl4uv9tFV99lG2flY4RShD2PKwZDZD";
			logger.info("trying to get intouch bulk api call: {}", url);

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// adding headers and requesting for authentication for :
			// campaign.api.users
			
			post.setHeader("Content-Type", "application/json");
			String str = "{ \"recipient\": { \"id\": \"806245566145063\" },\"message\": { \"text\": \"wassup?\", \"metadata\": \"DEVELOPER_DEFINED_METADATA\" } }";
			Gson gson = new Gson();
			
			post.setEntity(new StringEntity(str));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.info(
						"Recieved Error code while doing a post request: errorCode : {}, response: {}",
						response.getStatusLine().getStatusCode(), response);

				System.out.println("error");
				return result.toString();

			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result);
			logger.info("successfully recieved the wechatopenIds: {}", result);

		} catch (Exception e) {
			logger.error("exception in fetching intouch backend bulk api", e);
		}
		System.out.println("abhi");
		System.out.println(result);
		return result.toString();
	}

	public static void main(String[] args) {

//		System.out.println("Hello World!");
//		System.out.println("Abhinav");
//		App a = new App();
//		Gson gson = new Gson();
//		String str = "{ \"recipient\": { \"id\": \"806245566145063\" },\"message\": { \"text\": \"wassup\", \"metadata\": \"DEVELOPER_DEFINED_METADATA\" } }";
//		String postBody = gson.toJson(str);
//		System.out.println(postBody);
//		try {
//			a.sendPost();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String s = "\"fg\"hj\"";
//		System.out.println(s);
//		s = s.replaceAll("^\"|\"$", "");
//		System.out.println(s);
		A a = new A();
		a.tel();
	}
}
