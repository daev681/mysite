package com.douzone.mysite.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.security.AuthUser;
import com.douzone.mysite.service.UserServcie;
import com.douzone.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserServcie userService;

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "/user/join";
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo , BindingResult result , Model model) {
		if(result.hasErrors()) {
			/*
			 * List<ObjectError> list = result.getAllErrors(); for(ObjectError error : list)
			 * { System.out.println(error); }
			 */
			// model.addAttribute("UserVo", vo);
			model.addAllAttributes(result.getModel());
			return "user/join";
		}
		userVo.setRole("ADMIN");
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping(value = "/joinsuccess")
	public String joinsuccess() {
		return "/user/joinsuccess";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "/user/login";
	}
	/*
	 * @RequestMapping(value = "/login", method = RequestMethod.POST) public String
	  login(HttpSession seesion,
	  
	  @RequestParam(value = "email", required = true, defaultValue = "") String
	  email,
	  
	  @RequestParam(value = "password", required = true, defaultValue = "") String
	 password, Model model) {
	  
	  UserVo authUser = userService.getUser(email, password); if (authUser == null)
	  { model.addAttribute("result", "fail"); model.addAttribute("email", email);
	  return "user/login"; }
	  
	 // 로그인 처리 seesion.setAttribute("authUser", authUser); return "redirect:/"; }
	 

	/*
	 * @RequestMapping("/logout") public String logout(HttpSession session) { UserVo
	 * authUser = (UserVo) session.getAttribute("authUser");
	 * 
	 * if (authUser == null) { return "redirect:/"; }
	 * 
	 * // 로그아웃 처리
	 * 
	 * session.removeAttribute("authUser"); session.invalidate();
	 * 
	 * return "redirect:/"; }
	 */

	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@AuthUser UserVo authUser, Model model) {
		UserVo userVo = userService.getUser(authUser.getNo());
		model.addAttribute("user", userVo);
		return "user/update";
	}

	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@AuthUser UserVo authUser, UserVo userVo) {
		userVo.setNo(authUser.getNo());
		userService.updateUser(userVo);
		authUser.setName(userVo.getName());

		return "redirect:/user/update";
	}
}
