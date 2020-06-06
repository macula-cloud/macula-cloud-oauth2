package org.macula.cloud.oauth2.controller;

import org.macula.cloud.core.command.CreateSocialUserCommand;
import org.macula.cloud.oauth2.service.UserSocialService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/social")
@Slf4j
public class UserSocialController {

	private final UserSocialService ucService;

	public UserSocialController(UserSocialService ucService) {
		this.ucService = ucService;
	}

	@PutMapping("/create")
	public String createSocialUser(@RequestBody CreateSocialUserCommand command) {
		log.info("Received ", command);
		return ucService.createSocialUser(command);
	}

}
