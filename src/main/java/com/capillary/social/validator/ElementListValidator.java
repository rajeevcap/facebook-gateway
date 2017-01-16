package com.capillary.social.validator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.ButtonType;
import com.capillary.social.Element;
import com.capillary.social.MessageType;
import com.google.common.base.Strings;

public class ElementListValidator {

    private static Logger logger = LoggerFactory.getLogger(ElementListValidator.class);

    private List<Element> elementList;
    
    private MessageType messageType;

    public ElementListValidator(List<Element> elementList, MessageType messageType) {
        this.elementList = elementList;
        this.messageType = messageType;
    }
    
    public boolean isNotNull(Object obj) {
        return obj != null;
    }

    public boolean validate() {
        boolean isValid = true;
        if(new ElementListCountValidator(elementList.size(), messageType).validate()) {
            logger.error("element list count " + elementList.size() + " is invalid for : " + messageType);
            isValid = false;
        }
        for (Element element : elementList) {
            isValid &= new GenericMessageTitleValidator(element.title).validate();
            if (!Strings.isNullOrEmpty(element.subtitle)) {
                isValid &= new GenericMessageSubtitleValidator(element.subtitle).validate();
            }
            if (isNotNull(element.defaultAction)) {
                Button webUrlButton = element.defaultAction;
                isValid &= webUrlButton.type == ButtonType.web_url;
                isValid &= new ButtonMessageUrlValidator(webUrlButton.data.get(ButtonField.url)).validate();
            }
            if (isNotNull(element.buttonList)) {
                isValid &= new ButtonListValidator(element.buttonList, messageType).validate();
            }
        }
        return isValid;
    }

}
