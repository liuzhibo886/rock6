package com.lzb.rock.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.lzb.rock.base.BaseApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.lzb" })
@EnableFeignClients(basePackages = { "com.lzb" })
public class NettyApplication extends BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyApplication.class, args);
		log.info("==================启动成功==========================");
	}
}
