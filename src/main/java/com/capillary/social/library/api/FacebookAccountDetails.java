package com.capillary.social.library.api;

import in.capillary.ifaces.Shopbook.AccountDetails;
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

    private static Logger logger = LoggerFactory.getLogger(FacebookAccountDetails.class);

    public AccountDetails getAccountDetails(Integer orgId, String pageId) {

        AccountDetails accountDetails = null;

        try {
            ThriftService service = (ThriftService) ServiceDiscovery.getInstance().get(
                    KnownService.SHOPBOOK_THRIFT_SERVICE);
            if (service.isStale()) {
                logger.warn("Service {} fetched is stale", KnownService.SHOPBOOK_THRIFT_SERVICE.name());
            }

            logger.info("Shopbook Service: {} ", service);

            int readTimeout = 10000;
            int connectTimeout = 10000;
            ShopbookService.Iface httpClient = RPCService.httpClient(ShopbookService.Iface.class,
                    service.getURI() + "/shopbook_service.php", connectTimeout, readTimeout);

            logger.info("Shopbook Client: {} ", httpClient);

            Map<String, String> headers = Maps.newHashMap();
            headers.put("X-CAP-SERVICE-AUTH-KEY",
                    AuthenticationKey.generateKey(com.capillary.common.crypto.AuthenticationKey.Module.CAMPAIGN_SHARD));
            RPCService.setHttpHeaders(httpClient, headers);

            logger.debug("here");

            accountDetails = httpClient.getAccountDetailsByChannel(orgId, pageId, "FACEBOOK");

        } catch (Exception e) {
            logger.error("Unable to connect to the shopbook thrift service", e);
        }

        logger.info("Account Details: {}", accountDetails);

        return accountDetails;
    }
}
