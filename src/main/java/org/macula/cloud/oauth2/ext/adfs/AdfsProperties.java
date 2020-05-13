package org.macula.cloud.oauth2.ext.adfs;

import com.infinitusint.emp.sdk.adfs.AdfsConfig;

public class AdfsProperties extends AdfsConfig {

	private String callbackUrl;

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
}
