package com.lzb.rock.demo.config;

import java.util.Arrays;

import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShardingConfig {

	@Bean(name ="ds_0" )
	public MasterSlaveRuleConfiguration getMasterSlaveRuleConfiguration0() {
		return new MasterSlaveRuleConfiguration("ds_0", "m0", Arrays.asList("m0s0", "m0s1"));
	}

	@Bean(name ="ds_1" )
	public MasterSlaveRuleConfiguration getMasterSlaveRuleConfiguration1() {
		return new MasterSlaveRuleConfiguration("ds_1", "m1", Arrays.asList("m1s0", "m1s1"));
	}

	@Bean(name ="ds_2" )
	public MasterSlaveRuleConfiguration getMasterSlaveRuleConfiguration2() {
		return new MasterSlaveRuleConfiguration("ds_2", "m2", Arrays.asList("m2s0", "m2s1"));
	}

}
