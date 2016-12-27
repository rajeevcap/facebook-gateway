package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.ELEMENT_LIST_SIZE_LIMIT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.Button;
import com.capillary.social.ButtonField;
import com.capillary.social.ButtonType;
import com.capillary.social.Element;
import com.google.common.base.Strings;

public class ElementListValidator {

    private static Logger logger = LoggerFactory.getLogger(ElementListValidator.class);

    private List<Element> elementList;

    public ElementListValidator(List<Element> elementList) {
        this.elementList = elementList;
    }

    private boolean isNotNull(Object obj) {
        return obj != null;
    }

    public boolean validate() {
        boolean isValid = true;
        if (elementList.size() > ELEMENT_LIST_SIZE_LIMIT) {
            logger.debug("number of elements provided exceeds the limit");
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
                isValid &= new ButtonListValidator(element.buttonList).validate();
            }
        }
        return isValid;
    }

}
