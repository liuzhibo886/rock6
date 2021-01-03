package com.lzb.rock.redis.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lzb.rock.base.facade.ICacheMapper;
import com.lzb.rock.base.util.UtilClass;
import com.lzb.rock.base.util.UtilString;

@Component
public class RedisMapper implements ICacheMapper {

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	/**
	 * redis set缓存
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 */
	@Override
	public void set(String key, String value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * redis set缓存 失效时间默认600秒
	 * 
	 * @param key
	 * @param value
	 */
	@Override
	public void set(String key, String value) {
		set(key, value, 600);
	}

	/**
	 * 
	 * @param key
	 * @param obj 只能为对象
	 */
	@Override
	public void set(String key, Object obj) {
		set(key, JSON.toJSONString(obj), 600);
	}

	/**
	 * 
	 * @param key
	 * @param obj     只能为对象
	 * @param timeout
	 */
	@Override
	public void set(String key, Object obj, long timeout) {
		set(key, JSON.toJSONString(obj), timeout);
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 获取过期时间
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Long getExpire(String key) {
		// 根据key获取过期时间
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	@Override
	public void del(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <T> T get(String key, Class<T> clazz) {
		String str = redisTemplate.opsForValue().get(key);
		if (UtilString.isNotBlank(str)) {
			return (T) UtilClass.getJavaBeanByString(str, clazz);
		}
		return null;
	}

	public <T> List<T> getList(String key, Class<T> clazz) {
		String str = redisTemplate.opsForValue().get(key);
		if (UtilString.isNotBlank(str)) {
			return UtilClass.getJavaListByString(str, clazz);
		}
		return null;
	}

	/**
	 * 对缓存数加减
	 * 
	 * @param key
	 * @param delta
	 * @return
	 */
	@Override
	public Long increment(String key, long delta) {

		return redisTemplate.boundValueOps(key).increment(delta);
	}

	/**
	 * 对缓存数加减
	 * 
	 * @param key
	 * @param delta
	 * @return
	 */
	@Override
	public Double increment(String key, Double delta) {

		return redisTemplate.boundValueOps(key).increment(delta);
	}

	/**
	 * 判断锁是否存在
	 */
	@Override
	public boolean islock(String key) {
		key = getLockKey(key);
		return redisTemplate.hasKey(key);
	}

	/**
	 * 加锁 默认有效时间5分钟
	 * 
	 * @param key 锁唯一id
	 * @return
	 */
	@Override
	public boolean lock(String key) {
		key = getLockKey(key);
		return lock(key, 60 * 5);
	}

	/**
	 * 重置key时间
	 * 
	 * @date 2020年8月14日上午11:33:40
	 * @param key
	 * @param timeout
	 */
	@Override
	public void expire(String key, long timeout) {
		if (redisTemplate.hasKey(key)) {
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 重新设置锁的失效时间
	 * 
	 * @param key
	 * @param timeout
	 */
	@Override
	public void lockExpire(String key, long timeout) {
		key = getLockKey(key);
		expire(key, timeout);
	}

	/**
	 * 加锁
	 * 
	 * @param key     锁唯一id
	 * @param timeout 超时时间，单位秒
	 * @return true成功， false失败
	 */
	@Override
	public Boolean lock(String key, long timeout) {
		key = getLockKey(key);
		Boolean isLock = false;
		String uuid = UUID.randomUUID().toString();
		// 若不在，则设置值
		Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, uuid);
		if (flag && redisTemplate.hasKey(key)) {
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);

			String newUuid = redisTemplate.opsForValue().get(key);

			if (uuid.equals(newUuid)) {
				isLock = true;
			}
		}
		return isLock;
	}

	/**
	 * 加锁添加内容
	 * 
	 * @date 2020年8月5日下午2:03:53
	 * @param key
	 * @param timeout
	 * @param value
	 * @return
	 */
	@Override
	public Boolean lock(String key, long timeout, String value) {
		key = getLockKey(key);
		Boolean isLock = false;
		// 若不在，则设置值
		Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, value);
		if (flag && redisTemplate.hasKey(key)) {
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			String redisValue = redisTemplate.opsForValue().get(key);
			if (value.equals(redisValue)) {
				isLock = true;
			}
		}
		return isLock;
	}

	/**
	 * 解锁
	 */
	@Override
	public void unlock(String key) {
		key = getLockKey(key);
		redisTemplate.delete(key);
	}

	/**
	 * 生成分布式锁的key
	 * 
	 * @return
	 */
	@Override
	public String getLockKey(String key) {
		if (!key.startsWith("LOCK_")) {
			key = "LOCK_" + key;
		}
		return key;
	}

	@Override
	public Set<String> keys(String pattern) {

		return redisTemplate.keys(pattern);
	}
}
