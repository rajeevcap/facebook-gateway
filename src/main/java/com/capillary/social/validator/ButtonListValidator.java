package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.BUTTON_LIST_SIZE_LIMIT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;

public class ButtonListValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonListValidator.class);

    private List<Button> buttonList;

    public ButtonListValidator(List<Button> buttonList) {
        this.buttonList = buttonList;
    }

    public boolean validate() {
        boolean isValid = true;
        if (buttonList.size() > BUTTON_LIST_SIZE_LIMIT) {
            logger.debug("button list count provided is greater the limit");
            isValid = false;
        } else if (buttonList.isEmpty() || buttonList == null) {
            logger.debug("button list is empty in button message");
            isValid = false;
        }
        for (Button button : buttonList) {
            isValid = isValid && new ButtonMessageTitleValidator(button.title).validate();
            if (button.type != null) {
                switch (button.type) {
                    case web_url:
                        isValid &= new ButtonMessageUrlValidator(button.data.get(ButtonField.url)).validate();
                        break;
                    case postback:
                    case phone_number:
                        isValid &= new ButtonMessagePayloadValidator(button.data.get(ButtonField.payload)).validate();
                        break;
                    default:
                        isValid = false;
                }
            } else {
                logger.debug("button list type is null");
                isValid = false;
            }
        }
        return isValid;
    }

}
