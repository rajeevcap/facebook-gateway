package com.capillary.social.validator;

import static com.capillary.social.FacebookEntityGenerator.generateButton;
import static com.capillary.social.services.impl.FacebookConstants.BUTTON_LIST_SIZE_LIMIT;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.Button;
import com.capillary.social.MessageType;

public class ButtonListValidatorTest {

    @Test
    public void shouldBeInvalidWhenButtonCountExceedsLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = new Button();
        for (int i = 0; i < BUTTON_LIST_SIZE_LIMIT + 1; i++) {
            buttonList.add(button);
        }
        Assert.assertEquals(false, new ButtonListValidator(buttonList, MessageType.buttonMessage).validate());
    }

    @Test
    public void shouldBeValidWhenButtonCountIsWithinLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = generateButton();
        buttonList.add(button);
        Assert.assertEquals(false, new ButtonListValidator(buttonList, MessageType.buttonMessage).validate());
    }

    @Test
    public void shouldBeInvalidWhenNoButtonsAdded() {
        List<Button> buttonList = new ArrayList<Button>();
        Assert.assertEquals(false, new ButtonListValidator(buttonList, MessageType.buttonMessage).validate());
    }

}
