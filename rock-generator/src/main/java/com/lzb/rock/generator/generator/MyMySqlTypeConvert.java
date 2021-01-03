package com.lzb.rock.generator.generator;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

public class MyMySqlTypeConvert extends MySqlTypeConvert {
	@Override
	public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
		IColumnType columnType = super.processTypeConvert(globalConfig, fieldType);
		if (columnType.getType().equals(DbColumnType.LOCAL_DATE_TIME.getType())) {
			columnType = DbColumnType.DATE;
		}
		return columnType;
	}
}
