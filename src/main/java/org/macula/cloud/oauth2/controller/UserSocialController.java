package org.macula.cloud.oauth2.controller;

import org.macula.cloud.oauth2.command.CreateSocialUserCommand;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserSocialController {

	@PutMapping("/v1/user/social")
	public String createSocialUser(@RequestBody CreateSocialUserCommand socialUser) {
		log.info("Received ", socialUser);
		return "success";
	}

}
