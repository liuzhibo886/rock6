package com.lzb.rock.demo.config;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.exception.BusException;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义分片
 * 
 * @author lzb
 *
 *         2020年12月23日 下午1:33:06
 *
 */
@Slf4j
public class UserTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {

		Long value =shardingValue.getValue();
		if (value < 5) {
			for (String targetNames : availableTargetNames) {
				if (targetNames.endsWith("0")) {
					return targetNames;
				}
			}
		} else {
			for (String targetNames : availableTargetNames) {
				if (targetNames.endsWith("1")) {
					return targetNames;
				}
			}
		}
		log.info("availableTargetNames:{};shardingValue:{}", availableTargetNames, shardingValue);
		throw new BusException(RockEnum.PAEAM_ERR, "没有匹配合适的数据源");
	}

}
