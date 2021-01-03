package com.lzb.rock.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.lzb.rock.base.BaseApplication;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author lzb
 * @Date 2020-12-27 12:02:11
 *
 */
@ComponentScan(basePackages = { "com.lzb.rock" })
@EntityScan(basePackages = "com.lzb.rock.**.entity")
@EnableAutoConfiguration
@EnableScheduling
@Slf4j
@EnableAsync
@EnableSwagger2
public class DemoApplication extends BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("==================启动成功==========================");
		log.info("==================启动成功==========================");
	}
}
