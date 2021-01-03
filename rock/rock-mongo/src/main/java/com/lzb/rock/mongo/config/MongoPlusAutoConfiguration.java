package com.lzb.rock.mongo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.lzb.rock.mongo.converter.BigDecimalToDecimal128Converter;
import com.lzb.rock.mongo.converter.Decimal128ToBigDecimalConverter;
import com.lzb.rock.mongo.properties.MongoOptionProperties;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(MongoOptionProperties.class)
@ConditionalOnMissingBean(type = "org.springframework.data.mongodb.MongoDbFactory")
public class MongoPlusAutoConfiguration {

	@Bean
	public MongoClientOptions mongoClientOptions(MongoOptionProperties mongoOptionProperties) {
		if (mongoOptionProperties == null) {
			
			return new MongoClientOptions.Builder().build();
		}

		return new MongoClientOptions.Builder().minConnectionsPerHost(mongoOptionProperties.getMinConnectionPerHost())
				.connectionsPerHost(mongoOptionProperties.getMaxConnectionPerHost())
				.threadsAllowedToBlockForConnectionMultiplier(
						mongoOptionProperties.getThreadsAllowedToBlockForConnectionMultiplier())
				.serverSelectionTimeout(mongoOptionProperties.getServerSelectionTimeout())
				.maxWaitTime(mongoOptionProperties.getMaxWaitTime())
				.maxConnectionIdleTime(mongoOptionProperties.getMaxConnectionIdleTime())
				.maxConnectionLifeTime(mongoOptionProperties.getMaxConnectionLifeTime())
				.connectTimeout(mongoOptionProperties.getConnectTimeout())
				.socketTimeout(mongoOptionProperties.getSocketTimeout())
				.sslEnabled(mongoOptionProperties.getSslEnabled())
				.sslInvalidHostNameAllowed(mongoOptionProperties.getSslInvalidHostNameAllowed())
				.heartbeatFrequency(mongoOptionProperties.getHeartbeatFrequency())
				.minConnectionsPerHost(mongoOptionProperties.getMinConnectionPerHost())
				.heartbeatConnectTimeout(mongoOptionProperties.getHeartbeatConnectTimeout())
				.heartbeatSocketTimeout(mongoOptionProperties.getSocketTimeout())
				.localThreshold(mongoOptionProperties.getLocalThreshold())
				.connectionsPerHost(mongoOptionProperties.getConnectionsPerHost()).build();
	}

	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoMappingContext mongoMappingContext,
			MongoDbFactory mongoDbFactory) throws Exception {
		DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		List<Object> list = new ArrayList<>();
		list.add(new BigDecimalToDecimal128Converter());// 自定义的类型转换器
		list.add(new Decimal128ToBigDecimalConverter());// 自定义的类型转换器
		converter.setCustomConversions(new MongoCustomConversions(list));
		return converter;
	}

}
