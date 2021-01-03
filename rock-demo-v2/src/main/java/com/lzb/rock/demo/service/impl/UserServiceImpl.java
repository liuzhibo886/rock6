package com.lzb.rock.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.lzb.rock.base.enums.RedisKey;
import com.lzb.rock.base.exception.BusException;
import com.lzb.rock.base.model.Header;
import com.lzb.rock.base.model.UrlPattern;
import com.lzb.rock.base.util.UtilAES;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.demo.entity.User;
import com.lzb.rock.demo.enums.DemoEnum;
import com.lzb.rock.demo.repository.IUserRepository;
import com.lzb.rock.demo.service.IUserService;
import com.lzb.rock.redis.mapper.RedisMapper;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	RedisMapper redisMapper;

	@Value("${token.aes.encodeRules}")
	String encodeRules;

	@Override
	public User findUserByUserId(Long userId, Long tenantId) {

		User user = new User();
		user.setUserId(userId);
		user.setTenantId(tenantId);
		Example<User> example = Example.of(user);
		Optional<User> optional = userRepository.findOne(example);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteByUserId(Long userId, Long tenantId) {
		User user = new User();
		user.setUserId(userId);
		user.setTenantId(tenantId);
		userRepository.delete(user);
	}

	@Override
	public String logon(String userAccount, String userPassword) {
		// 查询用户信息
		User user = userRepository.findByUserAccount(userAccount);
		if (user == null) {
			throw new BusException(DemoEnum.USER_ACCOUNT_ERR);
		}

		String token = UUID.randomUUID().toString().replace("-", "");
		Header header = new Header();
		header.setUserId(user.getUserId() + "");
		header.setTenantId(user.getTenantId() + "");
		header.setToken(token);

//		user.setToken(token);
		// token 保存到数据库

//		User userNew = new User();
//		userNew.setUserId(user.getUserId());
//		userNew.setToken(token);
//		userRepository.save(userNew);

		userRepository.updateByUserIdAndTenantId(token, user.getUserId(), user.getTenantId());

		String aesToken = UtilAES.Base64AESEncode(encodeRules, UtilJson.getStr(header));

		// 查询可访问权限的url，先写死
		List<UrlPattern> urls = new ArrayList<UrlPattern>();
		urls.add(UrlPattern.builder().pattern("/**").method(HttpMethod.GET).build());
		urls.add(UrlPattern.builder().pattern("/user").method(HttpMethod.POST).build());
		redisMapper.set(RedisKey.TOKEN + token, urls, 60 * 60 * 2);

		return aesToken;
	}

}
