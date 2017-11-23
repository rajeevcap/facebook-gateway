package com.capillary.social.utils;

import java.util.Arrays;

/**
 * Created by rajeev on 23/11/17.
 */
public class SocialParameter {

    static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

}
