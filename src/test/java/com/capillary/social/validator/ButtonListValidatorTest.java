package com.capillary.social.validator;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.Button;

public class ButtonListValidatorTest {

    @Test
    public void shouldBeInvalidWhenButtonCountExceedsLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = new Button();
        for (int i = 0; i < 4; i++) {
            buttonList.add(button);
        }
        assertEquals(false, new ButtonListValidator(buttonList).validate());
    }

    @Test
    public void shouldBeValidWhenButtonCountIsWithinLimit() {
        List<Button> buttonList = new ArrayList<Button>();
        Button button = new Button();
        for (int i = 0; i < 3; i++) {
            buttonList.add(button);
        }
        assertEquals(true, new ButtonListValidator(buttonList).validate());
    }

    @Test
    public void shouldBeValidWhenNoButtonsAdded() {
        List<Button> buttonList = new ArrayList<Button>();
        assertEquals(true, new ButtonListValidator(buttonList).validate());
    }

}
