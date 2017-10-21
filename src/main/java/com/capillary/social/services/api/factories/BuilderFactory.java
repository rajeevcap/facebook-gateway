package com.capillary.social.services.api.factories;

import com.capillary.social.SocialChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 20/9/17
 */
public abstract class BuilderFactory<T> {
	private Map<SocialChannel, T> buildersmap;
	public BuilderFactory(){
		buildersmap = this.buildersList();
	}

	protected abstract Map<SocialChannel,T> buildersList();

	public T getBulder(SocialChannel socialChannel) {
		if (!buildersmap.containsKey(socialChannel)) {
			throw new RuntimeException("could not find builder for " + socialChannel.name());
		}
		return buildersmap.get(socialChannel);
	}
}
