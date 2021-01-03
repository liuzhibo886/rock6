/**
 * @author lzb
 *
 * 
 *2019年5月20日 下午11:05:29
 */
package com.lzb.rock.base.facade;

import java.util.Set;

/**
 * @author 缓存接口定义
 * 
 *         2019年5月20日 下午11:05:29
 */
public interface ICacheMapper {

	/**
	 * set缓存
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 */
	public void set(String key, String value, long timeout);

	/**
	 * set缓存 失效时间默认600秒
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);

	/**
	 * 
	 * @param key
	 * @param obj 只能为对象
	 */
	public void set(String key, Object obj);

	/**
	 * 
	 * @param key
	 * @param obj     只能为对象
	 * @param timeout
	 */
	public void set(String key, Object obj, long timeout);

	/**
	 * 判断缓存是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key);

	/**
	 * 获取过期时间
	 * 
	 * @param key
	 * @return
	 */
	public Long getExpire(String key);

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public void del(String key);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public <T> T get(String key, Class<T> targetClass);

	/**
	 * 对缓存数加减
	 * 
	 * @param key
	 * @param delta
	 * @return
	 */
	public Long increment(String key, long delta);

	/**
	 * 对缓存数加减
	 * 
	 * @param key
	 * @param delta
	 * @return
	 */
	public Double increment(String key, Double delta);

	/**
	 * 判断锁是否存在
	 */
	public boolean islock(String key);

	/**
	 * 加锁 默认有效时间5分钟
	 * 
	 * @param key 锁唯一id
	 * @return
	 */
	public boolean lock(String key);

	/**
	 * 重置key时间
	 * 
	 * @date 2020年8月14日上午11:33:40
	 * @param key
	 * @param timeout
	 */
	public void expire(String key, long timeout);

	/**
	 * 重新设置锁的失效时间
	 * 
	 * @param key
	 * @param timeout
	 */
	public void lockExpire(String key, long timeout);

	/**
	 * 加锁
	 * 
	 * @param key     锁唯一id
	 * @param timeout 超时时间，单位秒
	 * @return true成功， false失败
	 */
	public Boolean lock(String key, long timeout);

	/**
	 * 加锁添加内容
	 * 
	 * @date 2020年8月5日下午2:03:53
	 * @param key
	 * @param timeout
	 * @param value
	 * @return
	 */
	public Boolean lock(String key, long timeout, String value);

	/**
	 * 解锁
	 */
	public void unlock(String key);

	/**
	 * 生成分布式锁的key
	 * 
	 * @return
	 */
	public String getLockKey(String key);

	/**
	 * 获取key值
	 * 
	 * @date 2020年9月18日下午8:10:28
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern);

}
