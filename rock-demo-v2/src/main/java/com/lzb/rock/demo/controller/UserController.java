package com.lzb.rock.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lzb.rock.base.model.Result;
import com.lzb.rock.demo.entity.User;
import com.lzb.rock.demo.service.IUserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping
	public Result<User> findUserByUserId(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "tenantId") Long tenantId) {
		User user = userService.findUserByUserId(userId, tenantId);
		return new Result<User>(user);

	}

	@PostMapping
	public Result<User> save(@RequestBody User user) {
		userService.save(user);
		return new Result<User>(user);
	}

	@DeleteMapping
	public Result<Void> deleteByUserId(@RequestParam Long userId, @RequestParam(required = false) Long tenantId) {
		userService.deleteByUserId(userId, tenantId);
		return new Result<Void>();
	}

	@GetMapping("/logon")
	public Result<String> logon(@RequestParam("account") String userAccount,
			@RequestParam("password") String userPassword) {
		String token = userService.logon(userAccount, userPassword);
		return new Result<String>(token);
	}

}
