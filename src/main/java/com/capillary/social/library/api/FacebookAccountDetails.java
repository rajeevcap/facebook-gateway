package com.capillary.social.library.api;

import in.capillary.ifaces.Shopbook.ShopbookService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.common.crypto.AuthenticationKey;
import com.capillary.commons.thrift.external.RPCService;
import com.capillary.servicediscovery.ServiceDiscovery;
import com.capillary.servicediscovery.model.KnownService;
import com.capillary.servicediscovery.services.ThriftService;
import com.google.common.collect.Maps;

public class FacebookAccountDetails {

	private static Logger logger = LoggerFactory
			.getLogger(FacebookAccountDetails.class);

	public String getAccountDetails(Integer orgId,
			String pageId) {

		String accountDetails = "";

		try {
			ThriftService service = (ThriftService) ServiceDiscovery
					.getInstance().get(KnownService.SHOPBOOK_THRIFT_SERVICE);
			if (service.isStale()) {
				logger.warn("Service {} fetched is stale",
						KnownService.SHOPBOOK_THRIFT_SERVICE.name());
			}

			int readTimeout = 5000;
			int connectTimeout = 5000;
			ShopbookService.Iface httpClient = RPCService.httpClient(
					ShopbookService.Iface.class, service.getURI()
							+ "/shopbook_service.php", connectTimeout,
					readTimeout);
			Map<String, String> headers = Maps.newHashMap();
			headers.put(
					"X-CAP-SERVICE-AUTH-KEY",
					AuthenticationKey
							.generateKey(com.capillary.common.crypto.AuthenticationKey.Module.CAMPAIGN_SHARD));
			RPCService.setHttpHeaders(httpClient, headers);

			accountDetails = httpClient.getAccountDetailsByChannel(orgId,
					pageId, "FACEBOOK");

		} catch (Exception e) {
			logger.error("Unable to connect to the shopbook thrift service", e);
		}

		logger.info("Account Details: {}", accountDetails);

		return accountDetails;
	}

}
