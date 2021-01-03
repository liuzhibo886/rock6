package com.lzb.rock.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lzb
 * @Date 2019年7月31日 下午5:39:18
 */
@ComponentScan(basePackages = { "com.lzb.rock" })
@EnableAutoConfiguration
@Slf4j
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		System.out.println("==================启动成功==========================");
	}
}
