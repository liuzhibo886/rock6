package com.lzb.rock.netty.handler;

import java.util.concurrent.TimeUnit;

import com.lzb.rock.base.holder.SpringContextHolder;
import com.lzb.rock.netty.properties.MyNettyProperties;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 通道初始化
 */
public class MyNettyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));// 设置log监听器，并且日志级别为debug，方便观察运行流程
		// websocket协议本身是基于http协议的，所以这边也要使用http解编码器
		// pipeline.addLast("http-codec", new HttpServerCodec());// 设置解码器
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("decoder", new HttpRequestDecoder());
		// netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
		pipeline.addLast("aggregator", new HttpObjectAggregator(1024 * 1024));// 聚合器，使用websocket会用到
		// 以块的方式来写的处理器
		pipeline.addLast("http-chunked", new ChunkedWriteHandler());// 用于大数据的分区传输
		// websocket定义了传递数据的6中frame类型
		/**
		 * 设置心跳
		 */
		MyNettyProperties myNettyProperties = SpringContextHolder.getBean(MyNettyProperties.class);
		pipeline.addLast(new MyIdleStateHandler(myNettyProperties.getReaderIdleTimeSeconds(),
				myNettyProperties.getWriterIdleTimeSeconds(), myNettyProperties.getAllIdleTimeSeconds(),
				TimeUnit.SECONDS));
		/**
		 * 自定义业务
		 */
		pipeline.addLast("handler", new MyNettyWebSocketChannelInboundHandlerAdapter());// 自定义的业务handler

	}
}
