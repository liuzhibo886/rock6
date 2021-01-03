package com.lzb.rock.demo.service;

import com.lzb.rock.demo.entity.User;

/**
 * 用户
 * 
 * @author lzb
 *
 *         2020年12月16日 上午9:41:25
 *
 */
public interface IUserService {

	/**
	 * 根据ID获取数据
	 * 
	 * @param testId
	 * @return
	 */
	public User findUserByUserId(Long userId, Long tenantId);

	/**
	 * 保存数据
	 * 
	 * @param Path
	 * @return
	 */
	public User save(User user);

	/**
	 * 删除数据
	 * 
	 * @param Path
	 * @return
	 */
	public void deleteByUserId(Long userId, Long tenantId);

	public String logon(String userAccount, String userPassword);

}
