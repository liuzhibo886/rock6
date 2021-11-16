package com.lzb.rock.netty.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lzb.rock.netty.handler.MyNettyServer;
import com.lzb.rock.netty.properties.MyNettyProperties;
import com.lzb.rock.netty.threads.DelayThread;

import lombok.extern.slf4j.Slf4j;

/**
 * netty 启动
 * 
 * @author liuzhibo
 *
 */
@Component
@Async
@Slf4j
public class NettyStartRunner implements CommandLineRunner {

	@Autowired
	MyNettyProperties myNettyProperties;

	@Override
	public void run(String... args) throws Exception {
		// 启动服务端
		MyNettyServer nettyServer = new MyNettyServer();
		nettyServer.start();
	}

}
