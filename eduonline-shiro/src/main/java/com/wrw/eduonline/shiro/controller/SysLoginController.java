package com.wrw.eduonline.shiro.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.wrw.eduonline.common.utils.R;
import com.wrw.eduonline.shiro.utils.ShiroUtils;


/**   
 *    
 * 项目名称：eduonline-shiro   
 * 类名称：SysLoginController   
 * 类描述：loginController  
 * 创建人：wrw   
 * 创建时间：2017年9月18日 下午8:17:30   
 * @version        
 */
@Controller
public class SysLoginController {

	/**
	 * 验证码相关的
	 */
	@Autowired
	private Producer producer;
	
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		
		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * login
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String userName, String passWord, String captcha)throws IOException {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if (!captcha.equalsIgnoreCase(kaptcha)) {
			return R.error("验证码不正确");
		}
		
		try {
			Subject subject = ShiroUtils.getSubject();
			//sha265加密
			passWord = new Sha256Hash(passWord).toHex();
			UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
			subject.login(token);
		} catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return R.error(e.getMessage());
		} catch (LockedAccountException e) {
			return R.error(e.getMessage());
		} catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
		
		return R.ok();
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logou() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}