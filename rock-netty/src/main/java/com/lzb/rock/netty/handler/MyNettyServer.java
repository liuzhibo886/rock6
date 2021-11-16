package com.lzb.rock.netty.handler;

import com.lzb.rock.base.holder.SpringContextHolder;
import com.lzb.rock.netty.properties.MyNettyProperties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 标题: Netty 服务
 * 
 * @author lzb
 * @Date 2019年9月30日 上午9:27:21
 */
@Slf4j
public class MyNettyServer {

	private Channel channel;
	// 主线程组
	EventLoopGroup bossGroup = new NioEventLoopGroup(4);
	// 工作线程组
	EventLoopGroup workerGroup = new NioEventLoopGroup(20);

	/**
	 * 启动服务
	 */
	public ChannelFuture start() {

		MyNettyProperties myNettyProperties = SpringContextHolder.getBean(MyNettyProperties.class);
		/**
		 * 通道初始对象
		 */
		// TCP
		// MyNettyServerChannelInitializer<SocketChannel> bootNettyChannelInitializer =
		// new MyNettyServerChannelInitializer<>();

		MyNettyWebSocketChannelInitializer bootNettyChannelInitializer = new MyNettyWebSocketChannelInitializer();

		ChannelFuture channelFuture = null;
		try {
			/**
			 * ServerBootstrap 是一个启动NIO服务的辅助启动类
			 */
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			/**
			 * 设置group，将bossGroup， workerGroup线程组传递到ServerBootstrap
			 */
			serverBootstrap = serverBootstrap.group(bossGroup, workerGroup).option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_BACKLOG,1024*1024*10);
			/**
			 * ServerSocketChannel是以NIO的selector为基础进行实现的，用来接收新的连接，这里告诉Channel通过NioServerSocketChannel获取新的连接
			 */
			serverBootstrap = serverBootstrap.channel(NioServerSocketChannel.class);
			/**
			 * 日志登记
			 */
			serverBootstrap = serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

			/**
			 * option是设置 bossGroup，childOption是设置workerGroup netty 默认数据包传输大小为1024字节,
			 * 设置它可以自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费 最小 初始化 最大 (根据生产环境实际情况来定) 使用对象池，重用缓冲区
			 * 设置合理的值.否则会造成内存上飚
			 */
			serverBootstrap = serverBootstrap.option(ChannelOption.RCVBUF_ALLOCATOR,
					new AdaptiveRecvByteBufAllocator(64, 2048, 102400));
			// TCP会自动发送一个活动探测数据报文
			serverBootstrap = serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR,
					new AdaptiveRecvByteBufAllocator(64, 2048, 102400));
			/**
			 * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
			 */
			serverBootstrap = serverBootstrap.childHandler(bootNettyChannelInitializer);
			/**
			 * 监听端口
			 */
			serverBootstrap.localAddress(myNettyProperties.getPort());
			/**
			 * 绑定端口，同步等待成功
			 */
			// channelFuture = serverBootstrap.bind(address).sync();
			/**
			 * 不绑定网卡
			 */
			channelFuture = serverBootstrap.bind().sync();

			log.info("服务器启动开始监听端口: {}", myNettyProperties.getPort());
			/**
			 * 等待服务器监听端口关闭
			 */
			channelFuture.channel().closeFuture().sync();

		} catch (Exception e) {
			log.error("Netty 启动失败:", e);
		} finally {
			if (channelFuture != null && channelFuture.isSuccess()) {
				log.info("启动成功，端口号:{}", myNettyProperties.getPort());
			} else {
				log.error("启动失败,端口号：{}", myNettyProperties.getPort());
			}
			// 关闭主线程
			if (bossGroup.isShutdown()) {
				bossGroup.shutdownGracefully();
			}
			// 关闭工作组
			if (workerGroup.isShutdown()) {
				workerGroup.shutdownGracefully();
			}
		}

		return channelFuture;
	}

	public void destroy() {
		log.info("Shutdown Netty Server...");
		if (channel != null) {
			channel.close();
		}
		// 关闭主线程组
		bossGroup.shutdownGracefully();
		// 关闭工作线程组
		workerGroup.shutdownGracefully();
		log.info("Shutdown Netty Server Success!");
	}

}
