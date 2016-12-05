package com.capillary.social.validator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.capillary.social.Element;

public class ElementListValidatorTest {

    @Test
    public void elementListCountExceedValidator() {
        List<Element> elementList = new ArrayList<Element>();
        Element element = new Element();
        for (int i = 0; i < 11; i++) {
            elementList.add(element);
        }
        Assert.assertEquals(false, new ElementListValidator(elementList).validate());
    }

}
