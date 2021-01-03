package com.lzb.rock.demo.service;

import com.lzb.rock.demo.entity.Tenant;

/**
 * 租户
 * 
 * @author lzb
 *
 *         2020年12月16日 上午9:40:59
 *
 */
public interface ITenantService {

	/**
	 * 根据ID获取数据
	 * 
	 * @param testId
	 * @return
	 */
	public Tenant findTenantByTenantId(Long tenantId);

	/**
	 * 保存数据
	 * 
	 * @param Path
	 * @return
	 */
	public Tenant save(Tenant Tenant);

	/**
	 * 删除数据
	 * 
	 * @param Path
	 * @return
	 */
	public void deleteByTenantId(Long tenantId);

}
