package com.capillary.social.validator;

import static com.capillary.social.utils.FacebookGatewayUtils.countryCurrencyCodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class CountryCodeValidator {

    private static Logger logger = LoggerFactory.getLogger(CountryCodeValidator.class);

    private String field;

    public CountryCodeValidator(String field) {
        super();
        this.field = field;
    }

    private boolean validateNotNullOrEmpty(String field) {
        return !Strings.isNullOrEmpty(field);
    }

    public boolean validate() {
        if(!validateNotNullOrEmpty(field)) {
            logger.info("country currency code is empty!");
            return false;
        } else if(!countryCurrencyCodes.contains(field)) {
            logger.info("country currency code is unrecognized");
            return false;
        }
        return true;
    }

}
