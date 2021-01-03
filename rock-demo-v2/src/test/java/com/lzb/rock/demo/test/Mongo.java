package com.lzb.rock.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.lzb.rock.base.util.UtilDate;
import com.lzb.rock.demo.DemoApplication;
import com.lzb.rock.demo.entity.TestMongodb;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class Mongo {

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	public void run() throws InterruptedException {
		TestMongodb entity = new TestMongodb();
		entity.setTestName("demo momgodb测试名称" + UtilDate.getFomtTimeByDateString());

		mongoTemplate.insert(entity);
		log.info("demo momgodb:{};TestId:{}", entity,entity.getTestId());
	}

}
