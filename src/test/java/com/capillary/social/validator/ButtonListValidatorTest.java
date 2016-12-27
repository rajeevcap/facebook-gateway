package com.capillary.social.validator;

import static com.capillary.social.services.impl.FacebookConstants.BUTTON_LIST_SIZE_LIMIT;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.Button;
import com.capillary.social.FacebookEntityGenerator;

public class ButtonListValidatorTest {

    @Test
    public void shouldBeInvalidWhenButtonCountExceedsLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = new Button();
        for (int i = 0; i < BUTTON_LIST_SIZE_LIMIT + 1; i++) {
            buttonList.add(button);
        }
        Assert.assertEquals(false, new ButtonListValidator(buttonList).validate());
    }

    @Test
    public void shouldBeValidWhenButtonCountIsWithinLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = FacebookEntityGenerator.generateButton();
        buttonList.add(button);
        Assert.assertEquals(true, new ButtonListValidator(buttonList).validate());
    }

    @Test
    public void shouldBeInvalidWhenNoButtonsAdded() {
        List<Button> buttonList = new ArrayList<Button>();
        Assert.assertEquals(false, new ButtonListValidator(buttonList).validate());
    }

}
