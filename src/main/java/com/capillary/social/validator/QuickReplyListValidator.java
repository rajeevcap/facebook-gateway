package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.QUICK_REPLY_LIST_SIZE_LIMIT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.social.QuickReply;

public class QuickReplyListValidator {

    private static Logger logger = LoggerFactory.getLogger(ButtonListValidator.class);

    private List<QuickReply> quickReplyList;

    public QuickReplyListValidator(List<QuickReply> quickReplyList) {
        this.quickReplyList = quickReplyList;
    }

    public boolean validate() {
        boolean isValid = true;
        if (quickReplyList.size() > QUICK_REPLY_LIST_SIZE_LIMIT) {
            logger.debug("quick reply list count provided is greater the limit");
            isValid = false;
        } else if (quickReplyList.isEmpty() || quickReplyList == null) {
            logger.debug("quick reply list is empty in button message");
            isValid = false;
        }
        for (QuickReply quickReply : quickReplyList) {
            if (quickReply.content_type != null) {
                switch (quickReply.content_type) {
                    case text:
                        isValid &= new QuickReplyMessageTitleValidator(quickReply.title).validate()
                                   && new QuickReplyMessagepayloadValidator(quickReply.payload).validate();
                        break;
                    case location:
                        break;
                    default:
                        isValid = false;
                }
            } else {
                logger.debug("quick reply content type is empty");
                isValid = false;
            }
        }
        return isValid;
    }

}
