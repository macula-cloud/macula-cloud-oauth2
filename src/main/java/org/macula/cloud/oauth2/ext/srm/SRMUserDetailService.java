package org.macula.cloud.oauth2.ext.srm;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.macula.cloud.core.utils.SignatureUtils;
import org.macula.cloud.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SRMUserDetailService {

	private SRMProperties properties;

	private static String MSGTYP = "MSGTYP";

	/**
	 * MSGTYP 返回值 如果成功，MSGTYP=”S”，如果失败，MSGTYP=”E”
	 */
	private static String MSGTYP_TRUE = "S";

	private static String SYSTEMNAME = "SRM";

	public SRMUserDetailService(SRMProperties properties) {
		this.properties = properties;
	}

	@Autowired(required = false)
	private RestTemplate requestTemplate;

	public boolean loadUserByUsername(String username, String password) {
		String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
		String uuid = UUID.randomUUID().toString();
		String sign = encryptSRMRequestParams(username, password, currentTime, uuid);
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(properties.getUri())
					.queryParam("username", username).queryParam("password", password)
					.queryParam("systemname", SYSTEMNAME).queryParam("sap-client", properties.getSapClient()).encode()
					.build();
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			requestHeaders.add("content-md5", sign);
			requestHeaders.add("x-sap-appid", properties.getMyappid());
			requestHeaders.add("x-sap-timestamp", currentTime);
			requestHeaders.add("x-sap-signature-nonce", uuid);
			requestHeaders.add("x-sap-signature-method", "MD5");
			requestHeaders.add("x-sap-signature-version", "1.0");
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
			ResponseEntity<String> response = getRequestTemplate().exchange(uriComponents.toUri(), HttpMethod.GET,
					requestEntity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				String responseBody = response.getBody();
				JSONObject validObj = JSON.parseObject(responseBody);
				String msgtyp = validObj.getString(MSGTYP);
				return MSGTYP_TRUE.equals(msgtyp);
			}
		} catch (Exception e) {
			log.error("调用SRM验证接口失败", e);
		}

		return false;
	}

	private RestTemplate getRequestTemplate() {
		if (requestTemplate == null) {
			requestTemplate = new RestTemplate();
		}
		return requestTemplate;
	};

	private String encryptSRMRequestParams(String username, String password, String timestamp, String uuid) {
		List<String> params = new ArrayList<String>();
		params.add("password=".concat(password));
		params.add("sap-client=".concat(properties.getSapClient()));
		params.add("systemname=".concat(SYSTEMNAME));
		params.add("username=".concat(username));
		params.add("appid=".concat(properties.getMyappid()));
		params.add("securitykey=".concat(properties.getSecuritykey()));
		params.add("x-sap-timestamp=".concat(timestamp));
		params.add("x-sap-signature-nonce=".concat(uuid));
		String forSign = StringUtils.join(params, '&');
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return SignatureUtils.byte2hex(md.digest(forSign.getBytes("utf-8")));
		} catch (Exception e) {
			throw new RuntimeException("md5 sign error !", e);
		}
	}

}
