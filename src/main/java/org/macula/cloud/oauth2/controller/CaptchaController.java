package org.macula.cloud.oauth2.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CaptchaController {
	@Autowired
	private DefaultKaptcha captchaProducer;

	@RequestMapping(value = { "/imageCode", "/captcha", "/public/captcha" }, method = RequestMethod.GET)
	public void createCaptcha(HttpServletRequest request, HttpServletResponse response) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		ServletOutputStream out = null;
		try {
			String capText = captchaProducer.createText();
			HttpSession session = request.getSession();
			session.setAttribute("captchaCode", capText);
			BufferedImage bi = captchaProducer.createImage(capText);
			out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch (Exception e) {
			log.info("create captcha fail: {}", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					log.info("captcha output close fail: {}", e);
				}
			}
		}
	}
}
