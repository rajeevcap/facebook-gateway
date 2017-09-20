package com.capillary.social.services.api.factories;

import com.capillary.social.SocialChannel;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public interface BuilderFactory<T> {
	public T getBulder(SocialChannel socialChannel);
}
