package com.lzb.rock.demo.config;
//package com.lzb.rock.demo.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 
// * @author lzb
// *
// *         2020年12月22日 下午2:06:01
// *
// */
//@Configuration
//public class RabbitConfig {
//
//	// 队列 起名：TestDirectQueue
//	@Bean
//	public Queue test0Queue() {
//		// durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
//		// exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//		// autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
//		// return new Queue("TestDirectQueue",true,true,false);
//
//		// 一般设置一下队列的持久化就好,其余两个就是默认false
//		return new Queue("TEST_0_QUEUE", true, false, false);
//	}
//
//	public Queue test1Queue() {
//		return new Queue("TEST_1_QUEUE", true, false, false);
//	}
//
//	// Direct交换机 起名：TestDirectExchange
//	@Bean
//	DirectExchange DirectExchange0() {
//		return new DirectExchange("BZCST0", true, false);
//	}
//
//	DirectExchange DirectExchange1() {
//		return new DirectExchange("BZCST1", true, false);
//	}
//
//	// 绑定 将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
//	@Bean
//	Binding bindingTest0() {
//		return BindingBuilder.bind(test0Queue()).to(DirectExchange0()).with("TEST_DEMO");
//	}
//
//	@Bean
//	Binding bindingTest01() {
//		return BindingBuilder.bind(test1Queue()).to(DirectExchange1()).with("TEST_DEMO");
//	}
//
//}
