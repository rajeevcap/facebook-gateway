package com.capillary.social.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Address;
import com.google.common.base.Strings;

public class AddressValidator {

    private static Logger logger = LoggerFactory.getLogger(AddressValidator.class);

    private Address address;

    public AddressValidator(Address address) {
        this.address = address;
    }

    private boolean validateNotNullOrEmpty(String field) {
        return !Strings.isNullOrEmpty(field);
    }

    public boolean validate() {
        boolean isValid = true;
        isValid &= validateNotNullOrEmpty(address.streetOne);
        isValid &= validateNotNullOrEmpty(address.city);
        isValid &= validateNotNullOrEmpty(address.postalCode);
        isValid &= validateNotNullOrEmpty(address.state);
        isValid &= validateNotNullOrEmpty(address.country);
        if (!isValid)
            logger.debug("invalid address : " + address);
        return isValid;
    }

}
