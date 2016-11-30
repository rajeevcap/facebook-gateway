package com.capillary.social.external.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.social.systems.config.SystemConfig;
import com.capillary.social.FacebookException;
import com.capillary.social.FacebookService.Iface;
import com.google.gson.Gson;

public class FacebookServiceListener implements Iface {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookServiceListener.class);

	@Autowired
	private SystemConfig systemConfig;

	@Override
	public boolean isAlive() throws TException {
		// TODO Auto-generated method stub
		logger.info("Is alive called");
		return true;
	}

	@Override
	public int sendMessage(String recipientID, String messageText)
			throws FacebookException, TException {
		// TODO Auto-generated method stub
		
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
				return 20;
				//return result.toString();

			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result);
			logger.info("successfully sent the messages: {}", result);

		} catch (Exception e) {
			logger.error("exception in fetching intouch backend bulk api", e);
		}
		System.out.println("abhi");
		System.out.println(result);
		return 20;
		//return result.toString();

	}

}
