package com.lzb.rock.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.demo.DemoApplication;
import com.lzb.rock.redis.mapper.RedisMapper;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class Redis {

	@Autowired
	RedisMapper redisMapper;

	/**
	 * 发送普通消息
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void run() throws InterruptedException {
		String key = "DEMO_REDIS_KEY_";

		/**
		 * 删除key
		 */
		redisMapper.del(key);
		
		com.lzb.rock.demo.entity.User user = new com.lzb.rock.demo.entity.User();
		user.setUserName("redis测试名称"+UtilDate.getFomtTimeByDateString());
		/**
		 * set数据
		 */
		redisMapper.set(key, user);
		
		
		com.lzb.rock.demo.entity.User test2=redisMapper.get(key,com.lzb.rock.demo.entity.User.class);
		log.info("test2:{}",test2);
		
		

	}

}
