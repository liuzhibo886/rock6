package com.lzb.rock.netty.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 
 * @author lzb
 *
 *         2020 23:31:03
 */
@Data
@ConfigurationProperties(prefix = "rock.netty")
@Configuration
public class MyNettyProperties {

	/**
	 * 读取超时时间
	 */
	Integer readerIdleTimeSeconds = 5;

	/**
	 * 写超时时间
	 */
	Integer writerIdleTimeSeconds = 5;

	/**
	 * 读写超时时间
	 */
	Integer allIdleTimeSeconds = 15;
	/**
	 * AES加密秘钥
	 */
	String aesEncodeRules;

	/**
	 * netty端口号
	 */
	Integer port = 19066;

	String webSocketPath = "/websocket";

	@Value("${spring.profiles.active}")
	String active;
}
